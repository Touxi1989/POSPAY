package com.telpo.solab.iso8583.codecs;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import com.telpo.solab.iso8583.CustomBinaryField;
import com.telpo.solab.iso8583.CustomField;
import com.telpo.solab.iso8583.IsoType;
import com.telpo.solab.iso8583.IsoValue;
import com.telpo.solab.iso8583.parse.FieldParseInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * A codec to manage subfields inside a field of a certain type.
 * 
 * @author Enrique Zamudio Date: 25/11/13 11:25
 */
public class CompositeField implements CustomBinaryField<CompositeField> {

	// private static final Logger log =
	// LoggerFactory.getLogger(CompositeField.class);
	/** Stores the subfields. */
	@SuppressWarnings("rawtypes")
	private List<IsoValue> values;
	/** Stores the parsers for the subfields. */
	private List<FieldParseInfo> parsers;

	@SuppressWarnings("rawtypes")
	public void setValues(List<IsoValue> values) {
		this.values = values;
	}

	@SuppressWarnings("rawtypes")
	public List<IsoValue> getValues() {
		return values;
	}

	@SuppressWarnings("rawtypes")
	public CompositeField addValue(IsoValue<?> v) {
		if (values == null) {
			values = new ArrayList<IsoValue>(4);
		}
		values.add(v);
		return this;
	}

	public <T> CompositeField addValue(T val, CustomField<T> encoder,
									   IsoType t, int length) {
		return addValue(t.needsLength() ? new IsoValue<T>(t, val, length,
				encoder) : new IsoValue<T>(t, val, encoder));
	}

	@SuppressWarnings("unchecked")
	public <T> IsoValue<T> getField(int idx) {
		if (idx < 0 || idx > values.size())
			return null;
		return values.get(idx);
	}

	public <T> T getObjectValue(int idx) {
		IsoValue<T> v = getField(idx);
		return v == null ? null : v.getValue();
	}

	public void setParsers(List<FieldParseInfo> fpis) {
		parsers = fpis;
	}

	public List<FieldParseInfo> getParsers() {
		return parsers;
	}

	public CompositeField addParser(FieldParseInfo fpi) {
		if (parsers == null) {
			parsers = new ArrayList<FieldParseInfo>(4);
		}
		parsers.add(fpi);
		return this;
	}

	@Override
	public CompositeField decodeBinaryField(byte[] buf, int offset, int length) {
		@SuppressWarnings("rawtypes")
		List<IsoValue> vals = new ArrayList<IsoValue>(parsers.size());
		int pos = 0;
		try {
			for (FieldParseInfo fpi : parsers) {
				IsoValue<?> v = fpi.parseBinary(0, buf, pos, null);
				if (v != null) {
					if (v.getType() == IsoType.NUMERIC
							|| v.getType() == IsoType.DATE10
							|| v.getType() == IsoType.DATE4
							|| v.getType() == IsoType.DATE_EXP
							|| v.getType() == IsoType.AMOUNT
							|| v.getType() == IsoType.TIME) {
						pos += (v.getLength() / 2) + (v.getLength() % 2);
					} else {
						pos += v.getLength();
					}
					if (v.getType() == IsoType.LLVAR
							|| v.getType() == IsoType.LLBIN) {
						pos++;
					} else if (v.getType() == IsoType.LLLVAR
							|| v.getType() == IsoType.LLLBIN) {
						pos += 2;
					}
					vals.add(v);
				}
			}
			final CompositeField f = new CompositeField();
			f.setValues(vals);
			return f;
		} catch (ParseException ex) {
			// log.error("Decoding binary CompositeField", ex);
			return null;
		} catch (UnsupportedEncodingException ex) {
			// log.error("Decoding binary CompositeField", ex);
			return null;
		}
	}

	@Override
	public CompositeField decodeField(String value) {
		@SuppressWarnings("rawtypes")
		List<IsoValue> vals = new ArrayList<IsoValue>(parsers.size());
		byte[] buf = value.getBytes();
		int pos = 0;
		try {
			for (FieldParseInfo fpi : parsers) {
				IsoValue<?> v = fpi.parse(0, buf, pos, null);
				if (v != null) {
					pos += v.toString().getBytes(fpi.getCharacterEncoding()).length;
					if (v.getType() == IsoType.LLVAR
							|| v.getType() == IsoType.LLBIN) {
						pos += 2;
					} else if (v.getType() == IsoType.LLLVAR
							|| v.getType() == IsoType.LLLBIN) {
						pos += 3;
					}
					vals.add(v);
				}
			}
			final CompositeField f = new CompositeField();
			f.setValues(vals);
			return f;
		} catch (ParseException ex) {
			// log.error("Decoding CompositeField", ex);
			return null;
		} catch (UnsupportedEncodingException ex) {
			// log.error("Decoding CompositeField", ex);
			return null;
		}
	}

	@Override
	public byte[] encodeBinaryField(CompositeField value) {
		final ByteArrayOutputStream bout = new ByteArrayOutputStream();
		try {
			for (IsoValue<?> v : value.getValues()) {
				v.write(bout, true, true);
			}
		} catch (IOException ex) {
			// log.error("Encoding binary CompositeField", ex);
			// shouldn't happen
		}

		return bout.toByteArray();
	}

	@Override
	public String encodeField(CompositeField value) {
		final ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte[] buf = null;
		try {
			String encoding = null;
			for (IsoValue<?> v : value.getValues()) {
				v.write(bout, false, true);
				if (encoding == null)
					encoding = v.getCharacterEncoding();
			}
			buf = bout.toByteArray();
			return new String(buf, encoding == null ? "UTF-8" : encoding);
		} catch (UnsupportedEncodingException ex) {
			// log.error("Encoding text CompositeField", ex);
		} catch (IOException ex) {
			// log.error("Encoding text CompositeField", ex);
			// shouldn't happen
		}
		return new String(buf);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("CompositeField[");
		if (values != null) {
			boolean first = true;
			for (IsoValue<?> v : values) {
				if (first)
					first = false;
				else
					sb.append(',');
				sb.append(v.getType());
			}
		}
		return sb.append(']').toString();
	}
}

package com.telpo.solab.iso8583.util;

import com.telpo.solab.iso8583.IsoType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class IsoTyptFormatUtil {

	public static String format(final Date value, final TimeZone tz,
			final Object dataType) {
		final SimpleDateFormat sdf;
		if (dataType == IsoType.DATE10) {
			sdf = new SimpleDateFormat("MMddHHmmss");
		} else if (dataType == IsoType.DATE4) {
			sdf = new SimpleDateFormat("MMdd");
		} else if (dataType == IsoType.DATE_EXP) {
			sdf = new SimpleDateFormat("yyMM");
		} else if (dataType == IsoType.TIME) {
			sdf = new SimpleDateFormat("HHmmss");
		} else {
			throw new IllegalArgumentException("Cannot format date as "
					+ dataType);
		}
		if (tz != null) {
			sdf.setTimeZone(tz);
		}
		return sdf.format(value);
	}
}

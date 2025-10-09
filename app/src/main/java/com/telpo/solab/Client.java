package com.telpo.solab;

import com.telpo.solab.iso8583.IsoMessage;
import com.telpo.solab.iso8583.IsoType;
import com.telpo.solab.iso8583.MessageFactory;
import com.telpo.solab.iso8583.impl.SimpleTraceGenerator;
import com.telpo.solab.iso8583.parse.ConfigParser;

//        import org.apache.commons.logging.Log;
//        import org.apache.commons.logging.LogFactory;


/**
 * Implements a very simple TCP client application that connects to a server and
 * sends some requests, displaying the response codes and confirmations.
 * 
 * @author Enrique Zamudio
 */
public class Client extends Thread {

	// private static final Log log = LogFactory.getLog(Client.class);

	private static final String[] data = new String[] { "1234567890",
			"5432198765", "1020304050", "abcdefghij", "AbCdEfGhIj",
			"1a2b3c4d5e", "A1B2C3D4E5", "a0c0d0f0e0", "j5k4m3nh45" };
	private static final java.math.BigDecimal[] amounts = new java.math.BigDecimal[] {
			new java.math.BigDecimal("10"), new java.math.BigDecimal("20.50"),
			new java.math.BigDecimal("37.44") };
	private static MessageFactory mfact;
	/**
	 * This map holds the messages that are still awaiting response. Keys are
	 * traces, values are IsoMessages.
	 */
	private static java.util.Hashtable pending = new java.util.Hashtable();

	private java.net.Socket sock;

	public Client(java.net.Socket socket) {
		sock = socket;
	}

	public void run() {
		byte[] lenbuf = new byte[2];
		try {
			// For high volume apps you will be better off only reading the
			// stream in one thread
			// and then using another thread to parse the buffers and process
			// the responses
			// Otherwise the network buffer might fill up and you can miss a
			// message.
			while (sock != null && sock.isConnected() && !isInterrupted()) {
				sock.getInputStream().read(lenbuf);
				int size = ((lenbuf[0] & 0xff) << 8) | (lenbuf[1] & 0xff);
				byte[] buf = new byte[size];
				// We're not expecting ETX in this case
				if (sock.getInputStream().read(buf) == size) {
					try {
						// We'll use this header length as a reference.
						// In practice, ISO headers for any message type are the
						// same length.
						String respHeader = mfact.getIsoHeader(0x200);
						IsoMessage resp = mfact.parseMessage(buf,
								respHeader == null ? 12 : respHeader.length());
						System.out.println("Read response " + resp.getField(11)
								+ " conf " + resp.getField(38) + ": "
								+ new String(buf));
						pending.remove(resp.getField(11).toString());
					} catch (java.text.ParseException ex) {
						System.out.println("Parsing response" + ex);
					}
				} else {
					pending.clear();
					return;
				}
			}
		} catch (java.io.IOException ex) {
			System.out.println("Reading responses" + ex);
		} finally {
			try {
				sock.close();
			} catch (java.io.IOException ex) {
			}
			;
		}
	}

	public static void main(String[] args) throws Exception {
		java.util.Random rng = new java.util.Random(System.currentTimeMillis());
		System.out.println("Reading config");
		mfact = ConfigParser
				.createFromClasspathConfig("j8583/example/config.xml");
		mfact.setAssignDate(true);
		mfact.setTraceNumberGenerator(new SimpleTraceGenerator((int) (System
				.currentTimeMillis() % 10000)));
		System.out.println("Connecting to server");
		java.net.Socket sock = new java.net.Socket("192.168.3.93", 22005);
		// Send 10 messages, then wait for the responses
		com.telpo.solab.Client reader = new com.telpo.solab.Client(sock);
		reader.start();
		// for (int i = 0; i < 10; i++) {
		IsoMessage req = mfact.newMessage(0x0820);
		req.setValue(4, amounts[rng.nextInt(amounts.length)], IsoType.AMOUNT, 0);
		req.setValue(12, req.getObjectValue(7), IsoType.TIME, 0);
		req.setValue(13, req.getObjectValue(7), IsoType.DATE4, 0);
		req.setValue(15, req.getObjectValue(7), IsoType.DATE4, 0);
		req.setValue(17, req.getObjectValue(7), IsoType.DATE4, 0);
		req.setValue(37, new Long(System.currentTimeMillis() % 1000000),
				IsoType.NUMERIC, 12);
		req.setValue(41, data[rng.nextInt(data.length)], IsoType.ALPHA, 16);
		req.setValue(48, data[rng.nextInt(data.length)], IsoType.LLLVAR, 0);
		pending.put(req.getField(11).toString(), req);
		System.out.println("Sending request " + req.getField(11));
		req.write(sock.getOutputStream(), 2);
		// }
		System.out.println("Waiting for responses");
		while (pending.size() > 0 && sock.isConnected()) {
			sleep(500);
		}
		reader.interrupt();
		sock.close();
		System.out.println("DONE.");
	}

}
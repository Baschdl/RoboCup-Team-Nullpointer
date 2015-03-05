package de.null_pointer.communication_pi;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import lejos.pc.comm.NXTConnector;

import org.apache.log4j.Logger;

/**
 * @author sebastian
 * 
 *         Mit dieser Klasse koennen Strings zum Brick gesendet sowie empfangen
 *         und die Verbindung am Schluss geschlossen werden.
 * 
 */
public class RealCommunicationPi extends CommunicationPi {
	private DataInputStream dataFromBrick = null;
	private DataOutputStream dataToBrick = null;
	private NXTConnector conn = null;

	private Writer writer = null;

	private static Logger logger = Logger.getLogger(RealCommunicationPi.class);

	private RealCommunicationPi() {
	}

	public RealCommunicationPi(InputStream inputStream,
			OutputStream outputStream, NXTConnector pConn) {
		dataFromBrick = new DataInputStream(inputStream);
		dataToBrick = new DataOutputStream(outputStream);
		conn = pConn;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("sensordata.log"), "utf-8"));
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Empfaengt einen String von einem Brick.
	 * 
	 * @return Gibt den aus dem DataInpuString gelesenen String zurueck, bei
	 *         einer Exception den String "ENDE".
	 */
	@Override
	public synchronized String receiveString() {
		try {

			String data = dataFromBrick.readUTF();
			try {
				writer.write(data);
			} catch (IOException ex) {
				// report
				ex.printStackTrace();
			}
			return data;

		} catch (IOException e) {
			logger.error("Fehler beim Lesen des DataInputStream");
			return "ENDE";

		}

	}

	/**
	 * Sendet den uebergebenen String an einen Brick.
	 * 
	 * @param Benoetigt
	 *            den String, welcher gesendet werden soll.
	 */
	@Override
	public synchronized void sendString(String data) {
		try {
			logger.debug("writes data to DataOutputStream");
			dataToBrick.writeUTF(data);
			logger.debug("flushes data");
			dataToBrick.flush();
			logger.debug("transmission finished");

		} catch (IOException e) {

			logger.error("IO Exception writing bytes");

		} catch (NullPointerException npe) {
			logger.error("NullPointerException writing data");
		}
	}

	/**
	 * Schliesst den DataInputStream, DataOutputStream und die Verbindung zum
	 * Brick ueber den NXTConnector.
	 */
	@Override
	public void closeConnection() {
		try {
			dataFromBrick.close();
			dataToBrick.close();
			logger.info("Closed data streams");
			writer.close();

		} catch (IOException ioe) {
			logger.error("IO Exception Closing connection");
		}

		try {

			conn.close();
			logger.info("Closed connection to brick");

		} catch (IOException ioe) {
			logger.error("IO Exception Closing connection to brick");
		}

	}

}

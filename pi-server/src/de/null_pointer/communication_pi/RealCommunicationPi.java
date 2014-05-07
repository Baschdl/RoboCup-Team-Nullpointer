package de.null_pointer.communication_pi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import lejos.pc.comm.NXTConnector;

import org.apache.log4j.Logger;

public class RealCommunicationPi extends CommunicationPi {
	DataInputStream dataFromBrick;
	DataOutputStream dataToBrick;
	NXTConnector conn;

	private static Logger logger = Logger.getLogger(RealCommunicationPi.class);

	private RealCommunicationPi() {
	}

	public RealCommunicationPi(InputStream inputStream,
			OutputStream outputStream, NXTConnector pConn) {
		dataFromBrick = new DataInputStream(inputStream);
		dataToBrick = new DataOutputStream(outputStream);
		conn = pConn;
	}

	@Override
	public synchronized String receiveString() {
		try {

			String data = dataFromBrick.readUTF();

			return data;

		} catch (IOException e) {
			logger.error("Fehler beim Lesen des DataInputStream");
			return "ENDE";

		}

	}

	@Override
	public synchronized void sendString(String data) {
		try {

			dataToBrick.writeUTF(data);

			dataToBrick.flush();

		} catch (IOException e) {

			logger.error("IO Exception writing bytes");

		} catch (NullPointerException npe) {

			logger.error("NullPointerException writing data");

		}

	}

	@Override
	public void closeConnection() {
		try {

			dataFromBrick.close();

			dataToBrick.close();

			logger.info("Closed data streams");

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

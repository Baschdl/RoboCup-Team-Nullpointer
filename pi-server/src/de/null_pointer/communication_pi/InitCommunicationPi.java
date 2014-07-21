package de.null_pointer.communication_pi;

import lejos.pc.comm.NXTConnector;

import org.apache.log4j.Logger;

public class InitCommunicationPi {

	private static Logger logger = Logger.getLogger(InitCommunicationPi.class);
	private NXTConnector conn = new NXTConnector();

	public CommunicationPi initConnection(String brick_id) {

		try {

			if (!conn.connectTo("usb://" + brick_id)) {

				logger.warn("no NXT found using USB - simulation mode brick id "
						+ brick_id);
				return new VirtualCommunicationPi();

			} else {
				return new RealCommunicationPi(conn.getInputStream(),
						conn.getOutputStream(), conn);
			}

		} catch (Exception e) {

			logger.warn("PC: error connection to NXT: " + brick_id);
			System.exit(0);

			return null;

		}

	}

}
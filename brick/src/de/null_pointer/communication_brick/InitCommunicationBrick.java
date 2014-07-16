package de.null_pointer.communication_brick;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import lejos.nxt.comm.USB;
import lejos.nxt.comm.USBConnection;

public class InitCommunicationBrick {

	private USBConnection con;

	private DataInputStream dataFromPi;
	private DataOutputStream dataToPi;

	public CommunicationBrick initConnection() {

		try {

			con = USB.waitForConnection();
			dataFromPi = con.openDataInputStream();
			dataToPi = con.openDataOutputStream();

			return new RealCommunicationBrick(dataFromPi, dataToPi);

		} catch (Exception e) {

			System.out.println("error connection to PC");
			System.exit(0);

			return null;

		}

	}

}

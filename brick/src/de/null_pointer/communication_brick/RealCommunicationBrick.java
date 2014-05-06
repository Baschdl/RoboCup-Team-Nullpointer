package de.null_pointer.communication_brick;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class RealCommunicationBrick extends CommunicationBrick {
	DataInputStream dataFromPi;
	DataOutputStream dataToPi;

	private RealCommunicationBrick() {
	}

	public RealCommunicationBrick(InputStream inputStream,
			OutputStream outputStream) {
		dataFromPi = new DataInputStream(inputStream);
		dataToPi = new DataOutputStream(outputStream);
	}

	@Override
	public String receiveString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendString(String data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeConnection() {
		// TODO Auto-generated method stub

	}

}

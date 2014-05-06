package de.null_pointer.communication_pi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class RealCommunicationPi extends Communication_Pi {
	DataInputStream dataFromBrick;
	DataOutputStream dataToBrick;

	private RealCommunicationPi() {
	}

	public RealCommunicationPi(InputStream inputStream,
			OutputStream outputStream) {
		dataFromBrick = new DataInputStream(inputStream);
		dataToBrick = new DataOutputStream(outputStream);
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

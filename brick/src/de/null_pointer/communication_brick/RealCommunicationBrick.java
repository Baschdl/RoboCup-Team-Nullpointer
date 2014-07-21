package de.null_pointer.communication_brick;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
		String string;
		try {
			// System.out.println("readUTF: Lesen...");
			string = dataFromPi.readUTF();
			// System.out.println("readUTF: Fertig!");
			return string;
		} catch (IOException e) {
			System.err.println("IO Exception reading data");
			return null;
		}
	}

	@Override
	public void sendString(String data) {
		try {
			// System.out.println("Senden...");
			dataToPi.writeUTF(data);
			dataToPi.flush();
			// System.out.println("Gesendet!");

		} catch (IOException e) {
			System.err.println("IO Exception writing data");
		}

	}

	@Override
	public void closeConnection() {
		try {
			dataFromPi.close();
			dataToPi.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

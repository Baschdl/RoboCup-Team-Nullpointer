package de.null_pointer.communication_pi;

import java.util.ArrayList;


public class VirtualCommunicationPi extends CommunicationPi {
	private boolean closed = false;
	private ArrayList<String> dataArrayList = new ArrayList<String>();

	public boolean isClosed() {
		return closed;
	}

	@Override
	public String receiveString() {
		return dataArrayList.remove(0);
	}

	@Override
	public void sendString(String data) {
		dataArrayList.add(data);
		
	}

	@Override
	public void closeConnection() {
		closed = true;
		dataArrayList = null;
	}

}

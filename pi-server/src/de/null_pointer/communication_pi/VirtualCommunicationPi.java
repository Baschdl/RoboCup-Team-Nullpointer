package de.null_pointer.communication_pi;

import java.util.LinkedList;


public class VirtualCommunicationPi extends CommunicationPi {
	private boolean closed = false;
	private LinkedList<String> dataArrayList = new LinkedList<String>();

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

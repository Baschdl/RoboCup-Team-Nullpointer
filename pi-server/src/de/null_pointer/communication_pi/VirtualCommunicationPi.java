package de.null_pointer.communication_pi;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;

public class VirtualCommunicationPi extends CommunicationPi {
	private static Logger logger = Logger.getLogger(CommunicationPi.class);

	private boolean closed = false;
	private LinkedList<String> outgoing = new LinkedList<String>();
	private LinkedList<String> incoming = new LinkedList<String>();

	public boolean isClosed() {
		return closed;
	}

	@Override
	public String receiveString() {
		try {
			return incoming.getFirst();
		} catch (NoSuchElementException e) {
			try {
				wait();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return "*0;0;0;0#";
		}
	}

	@Override
	public void sendString(String data) {
		outgoing.add(data);

	}

	@Override
	public void closeConnection() {
		closed = true;
		outgoing = null;
		incoming = null;
	}

	public void insertIncomingString(String data) {
		incoming.addLast(data);
		notifyAll();
	}

	public String[] getAllOutgoingString() {
		String[] outgoingData = new String[outgoing.size()];
		int i = 0;
		for (String data : outgoing) {
			outgoingData[i] = data;
			i++;
		}
		return outgoingData;
	}

	public String getOutgoingString(int index) {
		if (index < outgoing.size()) {
			return outgoing.get(index);
		} else {
			logger.warn("Object doesn't exist (getOutgoingString(" + index
					+ ")");
			return "";
		}
	}

}

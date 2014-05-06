package de.null_pointer.communication_brick;

public class VirtualCommunicationBrick extends CommunicationBrick {
	private boolean closed = false;

	public boolean isClosed() {
		return closed;
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
		closed = true;

	}

}

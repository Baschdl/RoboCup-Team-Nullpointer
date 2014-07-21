package de.null_pointer.communication_brick;

public abstract class CommunicationBrick {

	public abstract String receiveString();

	public abstract void sendString(String data);

	public abstract void closeConnection();

}
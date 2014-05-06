package de.null_pointer.communication_pi;

public abstract class CommunicationPi {

	public abstract String receiveString();
	public abstract void sendString(String data);
	public abstract void closeConnection();
}
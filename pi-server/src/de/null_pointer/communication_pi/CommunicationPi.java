package de.null_pointer.communication_pi;


/**
 * @author sebastian
 * 
 * Oberklasse fuer die reale und die virtuelle Kommunikationsklasse.
 * Letztere dient dem jUnit-Test.
 *
 */
public abstract class CommunicationPi {

	public abstract String receiveString();
	public abstract void sendString(String data);
	public abstract void closeConnection();
}
package de.null_pointer.brick;

import lejos.nxt.Button;
import lejos.util.Delay;
import de.null_pointer.communication_brick.BrickControlBrick;
import de.null_pointer.communication_brick.InitCommunicationBrick;

public class Brick {

	public static void main(String[] args) {
		try {
			System.out.println("Startet Kommunikation...");
			InitCommunicationBrick initCom = new InitCommunicationBrick();
			BrickControlBrick comBrick = new BrickControlBrick(
					initCom.initConnection());
			System.out.println("Kommunikation gestartet");

			comBrick.start();
			comBrick.setDaemon(true);
			System.out.println("Vorbereitungen beendet");
			while (!Button.ESCAPE.isDown()) {
				Delay.msDelay(500);
			}
		} catch (Exception e) {
			System.exit(1);
		}
	}
}
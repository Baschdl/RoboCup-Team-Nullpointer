package de.null_pointer.sensorprocessing_pi;

public class LSAProcessingPi {

	private int black = -1;
	private int white = -1;
	private int[] lsa = new int[] { -1, -1, -1, -1, -1, -1, -1, -1 };
	/**
	 * true if black, false if not
	 */
	private boolean[] blackWhite = new boolean[8];

	private final Object lockBlack = new Object();
	private final Object lockWhite = new Object();
	private final Object lockLSA = new Object();

	public LSAProcessingPi() {
	}

	public void setBlack(int black) {
		synchronized (lockBlack) {
			this.black = black;
		}
	}

	public int getBlack() {
		synchronized (lockBlack) {
			return black;
		}
	}

	public void setWhite(int white) {
		synchronized (lockWhite) {
			this.white = white;
		}
	}

	public int getWhite() {
		synchronized (lockWhite) {
			return white;
		}
	}

	public void setLSA(int index, int value) {
		synchronized (lockLSA) {
			lsa[index] = value;
			if (value <= black) {
				blackWhite[index] = true;
			} else {
				blackWhite[index] = false;
			}
		}
	}

	public int getLSA(int index) {
		synchronized (lockLSA) {
			return lsa[index];
		}
	}

	public int[] getLSA() {
		synchronized (lockLSA) {
			return lsa;
		}
	}

	public void setTestLSA(int lsa[]){
		
	}
}

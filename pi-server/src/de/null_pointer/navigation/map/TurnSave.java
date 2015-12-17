package de.null_pointer.navigation.map;

public class TurnSave {

	private TurnSave nextTurn = null;

	private int moveDirection = -1;
	private int[] tremauxCounter = { -1, -1, -1, -1 };
	private boolean[] victimsFound = { false, false, false, false };

	public TurnSave(int moveDirection, int[] tremauxCounter,
			boolean[] victimsFound) {
		this.moveDirection = moveDirection;
		this.tremauxCounter = tremauxCounter.clone();
		this.victimsFound = victimsFound.clone();
	}

	public TurnSave getNextTurn() {
		return nextTurn;
	}

	public void setNextTurn(TurnSave nextTurn) {
		this.nextTurn = nextTurn;
	}

	public int getMoveDirection() {
		return moveDirection;
	}

	public int[] getTremauxCounter() {
		return tremauxCounter;
	}

	public boolean[] getVictimFound() {
		return victimsFound;
	}
}

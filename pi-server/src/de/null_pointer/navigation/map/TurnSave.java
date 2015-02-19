package de.null_pointer.navigation.map;

public class TurnSave {

	private TurnSave nextTurn = null;

	private int moveDirection = -1;
	private int[] tremauxCounter = { -1, -1, -1, -1 };

	public TurnSave(int moveDirection, int[] tremauxCounter) {
		this.moveDirection = moveDirection;
		this.tremauxCounter = tremauxCounter;
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
}

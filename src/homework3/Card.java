/**
 * Samier Trabilsy
 * Student ID: 109839226
 * Homework #3
 * Thursday: R04
 * Gustavo Poscidonio
 * Mahsa Torkaman
 * @author Samier
 */
package homework3;
/**
 * The Card class class contains basic information about a playing card
 */
public class Card {
	private int suit;
	private int value;
	private boolean isFaceUp;
	public static final String[] VALUES = {" ","A","2","3","4","5","6","7","8","9","10","J","Q","K"};
    public static final char[] SUITS    = {' ', '\u2666', '\u2663','\u2665', '\u2660'};   // {' ', 'DIAMOND', 'CLUB', 'HEART', 'SPADE'};
	
    /**
     * Creates a new face-up Card with a blank value and suit
     */
	public Card() {
		suit = 0;
		value = 0;
		isFaceUp = true;
	}
	/**
	 * Creates a new Card with a specified suit and value and that is specified to be face-up or not
	 * @param suit
	 * @param value
	 * @param isFaceUp
	 */
	public Card(int suit, int value, boolean isFaceUp) {
		this.suit = suit;
		this.value = value;
		this.isFaceUp = isFaceUp;
	}
	
	public int getSuit() {
		return suit;
	}
	
	public void setSuit(int suit) {
		this.suit = suit;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	public boolean isFaceUp() {
		return isFaceUp;
	}
	
	public void setFaceUp(boolean faceUp) {
		isFaceUp = faceUp;
	}

	/**
	 * Tests if the card is red or not
	 * @return True if the suit is hearts or diamonds; false otherwise
	 */
	public boolean isRed() {
		return (suit % 2 == 1);
	}
	
	/**
	 * Gets the String representation of this Card, which is a neatly formatted string 
	 * containing its value and suit
	 * 
	 * @return The String representation of this Card.
	 */
	public String toString() {
		String s = "[" + VALUES[value]+SUITS[suit] + "]";
		if (!isFaceUp())
			s = "[XX]";
		return s;
	}
	
}

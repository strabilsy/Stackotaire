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

import java.util.Stack;
/**
 * The CardStack class represents a stack of cards in the game. 
 * The following stack types are: 's' - stock, 'w' - waste, 'f' - foundations, and 't' - tableau.
 */
public class CardStack extends Stack<Card> {
	private int size;
	private char type;
	private static Card blank = new Card();; //to indicate an empty stack
	private int fIndex; //foundation index (used to display empty Foundation stacks)

	/**
	 * Creates an empty CardStack with the specified type
	 * @param type the stack type
	 * <dt><b>Postcondition:</b><dd> This CardStack has been initialized to an empty stack of Cards
	 */
	public CardStack(char type) {
		size = 0;
		this.type = type;
		if (type == 'f') {
			fIndex = 0;
		}
	}
	/**
	 * Adds a new Card on top of the Stack.
	 * @param newCard the new Card to be added to the stack
	 * <dt><b>Precondition:</b><dd> This CardStack has been instantiated.
	 * 
	 * <dt><b>Postcondition:</b><dd> The new Card is now at the top of the CardStack. size has incremented by 1.
	 */
	public Card push(Card newCard) {
		size++;
		return (Card)super.push(newCard);
	}
	/**
	 * Removes the Card that is on top of the stack
	 * @return the card that was on top of the stack
	 * <dt><b>Precondition:</b><dd> This CardStack has been instantiated
	 * 
	 * <dt><b>Postcondition:</b><dd> The card at the top of the CardStack has been removed from the stack. size has decremented by 1.
	 */
	public Card pop() {
		/*if (isEmpty())
			throw new EmptyStackException();*/
		size--;
		return (Card)super.pop();
	}
	/**
	 * Looks at the Card at the top of this stack without removing it from the stack.
	 * @return the card that is on top of the stack
	 * <dt><b>Precondition:</b><dd> This CardStack has been instantiated
	 */
	public Card peek() {
		return (Card)super.peek();
	}
	
	/**
	 * Tests if this stack is empty.
	 * @return True if and only if this stack contains no items; false otherwise.
	 */
	public boolean isEmpty() {
		return (size == 0);
	}
	
	public int getSize() {
		return size;
	}
	
	public char getType() {
		return type;
	}
	/**
	 * Sets the fIndex of a foundation stack in order for it to be specifically accessed
	 * @param fIndex the number of the foundation stack
	 */
	public void setfIndex(int fIndex) {
		this.fIndex = fIndex;
	}
	/**
	 * Returns a blank Card (indicates the stack is empty)
	 * @return the blank card
	 */
	public static Card getBlank() {
		return blank;
	}

	/**
	 * Renders the stack visually, depending on the type of the stack. If the stack is empty, display a blank card
	 * or, for a foundation stack, its foundation index - [F1] <br><br>
	 * Type 's': Prints the top card of the stack facing down followed by the size of the stack - [XX] <br>
	 * Type 'w': Prints the top card of the stack facing up - [9SPADE] <br>
	 * Type 't': Prints the whole stack on a line - [XX][XX][XX][4SPADE][3HEART] <br>
	 * Type 'f': Prints the top card of the stack facing up - [AHEART] <br>
	 */
	public void printStack() {
		switch(type) {
		case's': //stock: [XX] 24
			if (isEmpty())
				System.out.print(getBlank() + " " + size);
			else
				System.out.print(peek() + " " + size);
			break;
		case'w': //waste: W1: [  ]
			System.out.print("W1: ");
			if (isEmpty())
				System.out.print(getBlank());
			else
				System.out.print(peek() + " ");
			break;
		case't': //tableau: T2 [XX] [3HEART]
			if (isEmpty())
				System.out.print(getBlank());
			else {
				for (int i = 0; i < size; i++) //print each Card using Vector method
					System.out.print((Card)super.get(i) + " ");
			}
			break;
		case'f': //foundation: [F1] [F2] [3HEART] [F4]
			if (isEmpty())
				System.out.print("[F" + fIndex + "] ");
			else
				System.out.print(peek() + " ");
		}
	}
}

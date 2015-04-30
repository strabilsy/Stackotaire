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

import java.util.Collections;
import java.util.Scanner;
import java.util.Stack;
/**
 * The Stackotaire class contains the game logic and plays the game Stackotaire
 */
public class Stackotaire {
	private static final int FOUNDATIONS = 4;
	private static final int TABLEAUS = 7;
	private static CardStack deck;
	private static CardStack[] tableaus;
	private static CardStack stock;
	private static CardStack[] foundations;
	private static CardStack waste;
	private static CardStack copiedStack;
	private static Stack<String[]> commandStack;//for undo command
	private static boolean override;
	
	/**
	 * Initializes the game by distributing all cards into the proper stacks. 
	 */
	public static void initializeGame() {
		//create a deck with 52 distinct face-down cards
		deck = new CardStack('s'); //not displayed to user
		for (int i = 1; i < Card.SUITS.length; i++) {
			for (int j = 1; j < Card.VALUES.length; j++) {
				deck.push(new Card(i, j, false));
			}
		}
		Collections.shuffle(deck);
		
		copiedStack = new CardStack('s');
		commandStack = new Stack<String[]>();
		override = false;
		
		tableaus = new CardStack[TABLEAUS+1]; 
		for (int i = 1; i < tableaus.length; i++) {
			tableaus[i] = new CardStack('t'); //{null, T1, T2, T3, T4, T5, T6, T7};
			for (int j = 1; j <= i; j++) {
				tableaus[i].push(deck.pop());
			}
			tableaus[i].peek().setFaceUp(true); //make the top of each pile face-up
			copiedStack.addAll(tableaus[i]); //copy of the tableaus used to check if the game is won
		}
		stock = new CardStack('s');
		while (!deck.isEmpty()){ //distributes remaining cards to the deck
			stock.push(deck.pop());
		}
		
		foundations = new CardStack[FOUNDATIONS+1];
		for (int i = 1; i < foundations.length; i++) {
			foundations[i] = new CardStack('f'); //{null, F1, F2, F3, F4}
			foundations[i].setfIndex(i);
		}
		waste = new CardStack('w');
	}
	
	/**
	 * Renders all stacks to produce an image of the game board.
	 */
	public static void displayGame() {
		for (int i = 1; i < foundations.length; i++) {
			foundations[i].printStack();
		}
		System.out.print("     ");
		waste.printStack();
		System.out.print("     ");
		stock.printStack();
		System.out.println();
		System.out.println();
		for (int i = tableaus.length - 1; i > 0; i--) {
			System.out.print("T" + (i) + " ");
			tableaus[i].printStack();
			System.out.println();
		}
	}
	/**
	 * The main method presents a menu that allows the user interact with the game UI. 
	 * This is a simple command line like UI where the player specifies the moves to play.
	 * When prompted for a move, the player can supply the move in any one of the following formats:<br><br>
		draw <br>
			Removes the top card from the stock pile and places it face up in the waste pile. <br><br>
		move W1 T2 <br>
			Removes the top card from the waste pile, and places it on top of tableau pile #2. 
			The third argument of this command can range from T1-T7, and F1-F4.<br><br>
		move F1 T5 <br>
			Removes the top card from foundation pile #1, and places it on top of tableau pile #5. 
			The second argument of this command can range from F1-14. 
			The third argument of this command can range from T1-T7.<br><br>
		move T1 F1 <br>
			Removes the top card from the tableau pile #1, and places it on top of foundation pile #1. 
			The second argument of this command can range from T1-T7. 
			The third argument of this command can range from F1-F4.<br><br>
		moveN T3 T2 3 <br>
			Removes the top n cards, where n is the value of the fourth argument, from the tableau pile #3, 
			and places them on top of tableau pile #2. The second and third arguments of this command can range from T1-T7.<br><br>
		restart <br>
			Prompts the player to end the game and start a new one. If the player chooses yes, initialize a 
			new game board, else, continue with the current.<br><br>
		quit <br>
			Print a loss message and terminate the program.<br><br>
	 * @param args
	 */
	public static void main(String[] args) {
		initializeGame();
		displayGame();
		System.out.println("\nMain menu:\n\ndraw\nmove SRC DEST\nmoveN SRC DEST N\nundo\nrestart\nquit\n");
		Scanner input;
		String[] command;
		String move;

		while(true){
			input = new Scanner(System.in);
			System.out.print("Enter a command: ");
			command = input.nextLine().split(" ");
			move = command[0].toLowerCase();
			doMove(move, command);
		}
	}

	/**
	 * Performs the move specified from user input and updates the game board
	 * @param move the move from user input
	 * @param command the array of arguments from user input
	 */
	public static void doMove(String move, String[] command) {
		Scanner input = new Scanner(System.in);
		char srcType, destType;
		int srcNum, destNum, n;
		CardStack src = null, dest = null;
		Card newCard, topCard;
		
		switch(move) {
		
		case"draw": //draw
			if (stock.isEmpty()) {
				while(!waste.isEmpty()) {
					stock.push(waste.pop());
					stock.peek().setFaceUp(false);
				}
			}
			waste.push(stock.pop());
			waste.peek().setFaceUp(true);
			commandStack.push(command);
			break;
			
		case"move": //move SRC DEST
			if (command.length != 3) {
				System.out.println("\nmove expects 2 arguments\n");
				break;
			}
			//get Card to be moved
			srcType = Character.toUpperCase(command[1].charAt(0));
			srcNum = Character.getNumericValue(((command[1].charAt(1))));
			
			switch(srcType) {
			
			case'W':
				src = waste;
				break;
				
			case'F':
				src = foundations[srcNum];
				break;
				
			case'T':
				src = tableaus[srcNum];
				break;
			}
			
			if (src.isEmpty()) {
				System.out.println("\nThat pile is empty!\n");
				break;
			}
			else
				newCard = src.peek();
			
			//get Card to place newCard on top of
			destType = Character.toUpperCase(command[2].charAt(0));
			destNum = Character.getNumericValue(((command[2].charAt(1))));
			
			switch(destType) {
			
			case'F':
				dest = foundations[destNum];
				break;
			
			case'T':
				dest = tableaus[destNum];
				break;
			
			case'W':
				if (override) {
					dest = waste;
					break;
				}
			default:
				System.out.println("\nThis move is not allowed.");
				break;
			}
			
			if (dest.isEmpty())
				topCard = CardStack.getBlank();
			else
				topCard = dest.peek();
			
			if (isValidMove(newCard, topCard, dest.getType()) || override) {
				if (!override)
					commandStack.push(command);
				else {// undo the move (flip what was face-up back down)
					if (!dest.isEmpty() && destType == 'T') { //only tableau decks would have to set cards face-down
						if(dest.getSize() == 1) //we can't check if that one card was already face up before the card on top of it was popped
							dest.peek().setFaceUp(false);
						else
							if (!dest.elementAt(dest.indexOf(dest.peek())-1).isFaceUp()) //need to check this separately to avoid IndexOutOfBoundsException
								dest.peek().setFaceUp(false);
					}
				}
				dest.push(src.pop());
				if (!src.isEmpty())
					src.peek().setFaceUp(true);
			}
			else
				System.out.println("\nThis move is not allowed.");
			break;
			
		case"moven": //moveN SRC DEST N
			if (command.length != 4) {
				System.out.println("\nmoveN expects 3 arguments\n");
				break;
			}
			srcType = Character.toUpperCase(command[1].charAt(0));
			srcNum = Character.getNumericValue(command[1].charAt(1));
			n = Character.getNumericValue(command[3].charAt(0));
			
			switch(srcType) {
			
			default:
				System.out.println("\nThis move is not allowed.");
				break;
				
			case'T':
				src = tableaus[srcNum];
				break;
			}
			
			if (src.getSize() - n < 0 || !src.elementAt(src.indexOf(src.peek())-n+1).isFaceUp()) {
				System.out.println("\nThis move is not allowed\n");
				break;
			}
			else {
				newCard = (Card)src.elementAt(src.indexOf(src.peek())-n+1); //the bottom of this part of the stack
			}
			
			//get Card to place newCard on top of
			destType = Character.toUpperCase(command[2].charAt(0));
			destNum = Character.getNumericValue(((command[2].charAt(1))));
			
			switch(destType) {
			
			case'T':
				dest = tableaus[destNum];
				break;
			
			default:
				System.out.println("\nThis move is not allowed.");
				break;
			}
			
			if (dest.isEmpty())
				topCard = CardStack.getBlank();
			else
				topCard = dest.peek();
			
			if (isValidMove(newCard, topCard, dest.getType()) || override) {
				if (!override)
					commandStack.push(command);
				else
					if (!dest.isEmpty()) { // undo the move (flip what was face-up back down)
						if(dest.getSize() == 1) //we can't check if that one card was already face up before the card on top of it was popped
							dest.peek().setFaceUp(false);
						else
							if (!dest.elementAt(dest.indexOf(dest.peek())-1).isFaceUp()) //need to check this separately to avoid IndexOutOfBoundsException
								dest.peek().setFaceUp(false);
					}
				
				CardStack temp = new CardStack('t'); //we have to pop the elements into another CardStack to preserve the order
				for (int i = 1; i <=n; i++)
					temp.push(src.pop());
				while(!temp.isEmpty())
				{
					dest.push(temp.pop());
				}
				if (!src.isEmpty())
					src.peek().setFaceUp(true);
			}
			else
				System.out.println("\nThis move is not allowed.");
			break;
			
		case"restart": //restart
			System.out.print("Do you want to start a new game? (Y/N): ");
			if (Character.toUpperCase(input.next().charAt(0)) == 'Y') {
				System.out.println("\nSorry, you lose. Starting a new game.\n");
				initializeGame();
			}
			break;
		
		case"undo": //undo
			if (commandStack.isEmpty())
				System.out.println("\nThere was no previous move.\n");
			else {
				override = true;
				command = commandStack.pop();
				move = command[0].toLowerCase();
				if (move.equals("draw")) { //undo draw
					if (waste.isEmpty()) {
						while(!stock.isEmpty()) {
							waste.push(stock.pop());
							waste.peek().setFaceUp(true);
						}
					}

					stock.push(waste.pop());
					stock.peek().setFaceUp(false);
				}
				else {
					String oldSrc = command[1]; //switch src and dest to undo last move
					command[1] = command[2];
					command[2] = oldSrc;
					
					doMove(move, command); //redo the move backwards
				}
				override = false;
			}
			break;
			

		case"quit": //quit
			System.out.print("Do you want to quit? (Y/N): ");
			if (Character.toUpperCase(input.next().charAt(0)) == 'Y') {
				input.close();
				System.out.println("\nSorry, you lose.\nProgram terminating...");
				System.exit(0);
			}		
		}
		if (isGameWon()) { //check if game is won and if so, display a win message, and prompt the user to play again.
			System.out.println("\nCongrats, you won! PLay again? (Y/N): ");
			if (Character.toUpperCase(input.next().charAt(0)) == 'Y') {
				System.out.println("\nStarting a new game.\n");
				initializeGame();
			}
			if (Character.toUpperCase(input.next().charAt(0)) == 'N') {
				System.out.println("\nProgram terminating...");
				System.exit(0);
			}
		}
		if (!override)
			displayGame(); //updates game board
	}
	
	/**
	 * Checks for winning board
	 * @return True if all cards are face-up; false otherwise
	 */
	public static boolean isGameWon() {
		for (Card card: copiedStack) {
			if (!card.isFaceUp())
				return false;
		}
		return true;
	}

	/**
	 * Checks to see if a card can be placed on top of the top card of a specific stack
	 * @param newCard the card being moved
	 * @param topCard the top of the stack that the newCard is trying to be placed onto
	 * @param type the stack type
	 * @return True if the move is valid; false otherwise
	 */
	public static boolean isValidMove(Card newCard, Card topCard, char type) {
		if (type == 'f') {
			if (newCard.getValue() == 1 && topCard.getValue() == 0) //check if the card to be moved is an Ace and if the foundation is empty
				return true;
			if (newCard.getValue() == topCard.getValue()+1 && newCard.getSuit() == topCard.getSuit())
				return true;
		}
		if (type == 't') {
			if (newCard.getValue() == 13 && topCard.getValue() == 0)
				return true;
			if (newCard.getValue() == topCard.getValue()-1 && newCard.isRed() != topCard.isRed())
				return true;
		}
		return false;
	}

}

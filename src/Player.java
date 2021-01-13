public abstract class Player {
	protected int score;
	/* boolean variables that apply to the subclasses of Player and help in determining winner/loser
	   isWinner can be updated according to which party has a Blackjack or scored more */
	protected boolean isBlackjack, isWinner; 
	protected Card[] hand; //Each sublclass of player has its own array of cards
	
	public abstract void calculateScore();
	
	public int getScore(){ //Returns the score of a child party
		return score;
	}
	
	public abstract void drawCard(DeckOfCards d , int x); //d is the deck to draw from, x specifies which card is being drawn
	
	public void checkBlackjack(){
		//Looks at the first two cards in the child class' "hand" and decides whether they make a Blackjack or not 
		if ((hand[0].getValue() == 14 && hand[1].getValue() >= 10 && hand[1].getValue() <= 13) || 
			 hand[0].getValue() >= 10 && hand[0].getValue() <= 13 && hand[1].getValue() == 14){
			isBlackjack = true;
		}
	}
	
	public boolean getBlackjack(){
		return isBlackjack;
	}
	
	public Card getCard(int index){ //Returns a Card object for the driver class to work with.
		return hand[index];
	}
	
	public void setWinner(boolean x){ //Sets the winner stats of a party to the specified predicate value
		isWinner = x;
	}
	
	public boolean getWinner(){ //Tells whether a party is a winner or not
		return isWinner;
	}
}

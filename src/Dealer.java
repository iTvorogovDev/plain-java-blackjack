
public class Dealer extends Player{
	public Dealer(){ //A default constructor for Dealer that makes all his stats equal to 0 (useful for resetting the game in the driver class)
			score = 0;
			isBlackjack = false;
			isWinner = false;
			hand = new Card[5];
			for (int n = 0; n < hand.length; n++){
				hand[n] = new Card();
			}
	}
	
	public void drawCard(DeckOfCards d , int x){ //Places a Card object into the array with the help of DeckOfCards class
		hand[x] = d.dealCard();
		calculateScore();
	}
	
	public void calculateScore(){
		score = 0;
		for (int n = 0; n < hand.length; n++){
			/* Program Dealer to decide which value he will use for Ace when he draws one. 
			 * If the Dealer's score is 0, 1 or 11, his Ace is worth 10
			   Otherwise, the Dealer selects 1 for Ace worth */
			if ((hand[n].getValue() >= 10 && hand[n].getValue() <= 13) || (hand[n].getValue() == 14 && (score == 11 || score == 0 || score == 1)))
				score += 10;
			else if (hand[n].getValue() <= 9 && hand[n].getValue() >= 2)
				score += hand[n].getValue();
			else if (hand[n].getValue() == 14)
				score += 1;
		}
	}
}

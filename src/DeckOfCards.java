import java.util.Random;
import javax.swing.ImageIcon;

public class DeckOfCards {

	private Card[] deck = new Card[52]; //Array of Card objects that will store the "deck"
	private String[] path = {"2C", "2D", "2H", "2S", "3C", "3D", "3H", "3S", "4C", "4D", "4H", "4S",
			"5C", "5D", "5H", "5S", "6C", "6D", "6H", "6S", "7C", "7D","7H", "7S", "8C", "8D", "8H", "8S",
			"9C", "9D", "9H", "9S", "10C", "10D", "10H", "10S", "11C", "11D", "11H", "11S",
			"12C", "12D", "12H", "12S"}; //An array of paths to the Card icons
	private Card dealtCard, backOfCard; //dealtCard can be used to remember the last Card dealt. backOfCard is the "down" Card
	//numCardsDealt and cardsRemaining is used to prevent programs from drawing cards when the deck is empty.
	private int numCardsDealt, cardsRemaining; 
	private Random rnd; //Used when shuffling the card deck
	public static final int TOTAL_CARDS = 52;
	
	public DeckOfCards(){ //Resets card counting variables and fills an array of Card objects.
		rnd = new Random();
		numCardsDealt = 0;
		cardsRemaining = TOTAL_CARDS;
		dealtCard = null;
		backOfCard = new Card();
		generateDeck();
	}
	//Resets card counting variables, fills an array of Card objects, then also shuffles the array
	public DeckOfCards(boolean shuffleDeck){
		rnd = new Random();
		numCardsDealt = 0;
		cardsRemaining = TOTAL_CARDS;
		dealtCard = null;
		backOfCard = new Card();
		generateDeck();
		if (shuffleDeck)
			shuffleDeck();
	}
	
	public int getCardsRemaining(){ //Returns the number of remaining cards
		return cardsRemaining;
	}
	//generateDeck creates Card objects one by one, placing Cards in order of increasing values and grouping them by suits.
	public void generateDeck(){
		int count = 0;
		//Initialize Card objects of Clubs suit, then do the same for Diamonds suit, etc.
		for (int i = 2; i < 15; i++){ //The image file names are programmed to be acessed with the help of for loops
			deck[count] = new Card(new ImageIcon("images\\" + i + "C.gif"), i, "C");
			count++;
		}
		for (int i = 2; i < 15; i++){
			deck[count] = new Card(new ImageIcon("images\\" + i + "D.gif"), i, "D");
			count++;
		}
		for (int i = 2; i < 15; i++){
			deck[count] = new Card(new ImageIcon("images\\" + i + "H.gif"), i, "H");
			count++;
		}
		for (int i = 2; i < 15; i++){
			deck[count] = new Card(new ImageIcon("images\\" + i + "S.gif"), i, "S");
			count++;
		}
	}
	
	public void shuffleDeck(){
		for (int i = 0; i < 52; i++){
			int rand = rnd.nextInt(51); //Generate a random index to place the card in
			Card a = deck[i]; //Record the card to be moved in a temporary object
			deck[i] = deck[rand]; //Replace the card to be moved with the card from the random index 
			deck[rand] = a; //Restore the moved card in the random index
		}
	}
	
	public Card getBackOfCard(){ //Returns a "down" card
		return new Card();
	}
	
	public int getNumCardsDealt(){ //Returns the number of cards dealt
		return numCardsDealt;
	}
	
	public Card dealCard(){ //Returns the card from the current index at the deck
		numCardsDealt++;
		cardsRemaining--;
		return deck[numCardsDealt - 1];	
	}
}

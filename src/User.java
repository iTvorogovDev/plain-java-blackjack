import java.io.*;
import javax.swing.JOptionPane;

public class User extends Player{
	
	//Declare and initialize a file that is written to and read the stats from
	private File fileStats = new File("stats.txt"); //Path to stats document
	private int balance; //The amount of User's chips 
	private int bet, insurance; //Bet is set by the driver class, insurance becomes one half of the bet rounded to whole number when applicable
	private int games, wins, losses, ties, blackjacks; //Stats variables that are used in calculations
	/* This array stores aces. 
	 * If a card at hand[x] is an Ace, integer value from the corresponding index is added to the user's score
	   This allows aces in the hand to have different values rather than all Aces having the same value. */
	private int aceVal[];
	/* If the Dealer's first card is an Ace, the user will be offered an insurance bet, 
	 * since there is a high chance that the next card may get the dealer a Blackjack */
	private boolean isInsured; 
	private double wp; //Win percentage of the user
	/* Declare an array of stats variables that will be set for User according to stats.txt
	 * stats[0][j] stores digital values of the stats
	 * stats[0][0] is User's balance
	 * stats[0][1] is number of games the User played
	 * stats[0][2] is number of User's wins
	 * stats[0][3] is number of User's losses
	 * stats[0][4] is number of User's ties
	 * stats[0][4] is number of Blackjacks the user has drawn
	 * stats[0][6] is the percentage of User's wins 
	 * stats[1][j] stores String titles of the according stats
	 * stats[i][j] is not used in calculations, 
	 * but primarily serves to optimize stats writing and updating JLabels in the driver class */
	private Object[][] stats = new Object[2][7];
	
	public User(){//A default constructor for the User party, which initializes stats, making them all equal to 0, then updates them by reading from file
		score = 0;
		insurance = 0;
		bet = 0;
		aceVal = new int[5];
		ties = 0;
		blackjacks = 0;
		isBlackjack = false;
		isInsured = false;
		isWinner = false;
		hand = new Card[5];
		for (int n = 0; n < hand.length; n++){
			hand[n] = new Card();
		}
		//Parse stats of the user
		try{
			String[] data;
			String s;
			int x = 0;
			BufferedReader in = new BufferedReader(new FileReader(fileStats));
			s = in.readLine();
			while (s != null){
				data = s.split(": ");
				stats[1][x] = data[0];
				//Record the digital value of the stat
				if (x == 6){
					stats[0][x] = Double.parseDouble(data[1]);
				}
				else
					stats[0][x] = Integer.parseInt(data[1]);
				x++;
				s = in.readLine();
			}
			in.close();
			//Set int variables to the parsed stats
			balance = (int)stats[0][0];
			games = (int)stats[0][1];
			wins = (int)stats[0][2];
			losses = (int)stats[0][3];
			ties = (int)stats[0][4];
			blackjacks = (int)stats[0][5];
			//Prevent the system from dividing by zero if the stats have been reset or the game is run for the first time.
			try{
				wp = (double)wins / (double)games * 100;
			}
			catch(ArithmeticException moo){
				wp = 0; //Set wp to 0 if the division by zero exception is caught
			}
		}
		catch(IOException moo){
			JOptionPane.showMessageDialog(null, "Error retrieving user stats: " + moo.getMessage(), "BlackJack", JOptionPane.ERROR_MESSAGE);
			//Set default stats and write them to a new file in case of an error
			balance = 500;
			games = 0;
			wins = 0;
			losses = 0;
			ties = 0;
			blackjacks = 0;
			wp = 0.0;
			writeStats();
		}
	}
	
	public void writeStats(){
		//Update the stats variables from the named int variables and write them to file.
		stats[0][0] = balance;
		stats[0][1] = games;
		stats[0][2] = wins;
		stats[0][3] = losses;
		stats[0][4] = ties;
		stats[0][5] = blackjacks;
		stats[0][6] = wp;
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter(fileStats, false));
			for (int n = 0; n < stats[0].length; n++){
				out.write((String)stats[1][n] + ": ");
				if (n == 6)
					out.write(Double.toString((Double)stats[0][n]));
				else
					out.write(Integer.toString((Integer)stats[0][n]));
				out.newLine();
			}
			out.close();
		}
		catch (IOException moo){
			JOptionPane.showMessageDialog(null, "Error writing user stats: " + moo.getMessage(), "BlackJack", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void drawCard(DeckOfCards d , int x){
		hand[x] = d.dealCard();
		//If an ace is drawn, determine which value will be set to it by default. The user will then be able to change it.
		if (score >= 12)
			aceVal[x] = 1;
		else if (score < 12)
			aceVal[x] = 10;
		else
			aceVal[x] = 0;
		calculateScore();
	}
	
	public void calculateScore(){
		score = 0;
		for (int n = 0; n < hand.length; n++){
			//If the card is an ace, add its stored value to the user's score
			if (hand[n].getValue() == 14)
				score += aceVal[n];
			else if (hand[n].getValue() >= 10 && hand[n].getValue() <= 13)
				score += 10;
			else
				score += hand[n].getValue();
		}
	}
	
	public void setAceVal(int index, int val){
		aceVal[index] = val; //The user will be able to select the aces in their hand and set their values using the driver class
	}
	
	/* Below are acessor and mutator methods for the User-related variables represented by the titles of the methods
	 * updateVariable methods add the specified x to the variable or subtract from it if x is negative. If x is 0, the variable is reset to 0
	 * setVariable methods set the value of the variable to the specified x
	   getVariable methods return the value of the variable */
	
	public void updateBalance(int x){
		balance += x;
	}
	
	public void setBalance(int x){
		balance = x;
	}
	
	public int getBalance(){
		return balance;
	}
	
	public void updateBet(int x){
		if (x == 0)
			bet = 0;
		else
			bet += x;
	}
	
	public int getBet(){
		return bet;
	}
	
	public void updateGames(int x){
		if (x == 0)
			games = 0;
		else
			games += x;
	}
	
	public void updateWins(int x){
		if (x == 0)
			wins = 0;
		else
			wins += x;
	}
	
	public void updateLosses(int x){
		if (x == 0)
			losses = 0;
		else
			losses += x;
	}
	
	public void updateTies(int x){
		if (x == 0)
			ties = 0;
		else
			ties += x;
	}
	
	public void updateBlackjacks(int x){
		if (x == 0)
			blackjacks = 0;
		else
			blackjacks += x;
	}
	
	public Object getStats(int i, int j){
		return stats[i][j];
	}
	
	public double getWP(){
		return wp;
	}
	
	public void updateWP(){
		if (games != 0)
			wp = (double)wins / (double)games * 100;
		else
			wp = 0.0;
	}
	
	public void setInsurance(){//Places an insurance bet and makes the user insured
		insurance = Math.round(bet / 2);
		balance -= insurance;
		isInsured = true;
	}
	
	public boolean getInsured(){//Tells the program if the user is insured or not
		return isInsured;
	}
	
	public int getInsurance(){//Returns the amount of insurance bet
		return insurance;
	}
}

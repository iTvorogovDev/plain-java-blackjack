import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class Game extends JFrame implements ActionListener{
	//Declare and / or initialize variables
	private JFrame frame = new JFrame(); //The main frame used in the program.
	private int numUser = 0; //Tracks how many cards the user has drawn, mainly used with the array of the user's cards.
	private static int numDealer = 0; //Same as numUser, but related to the dealer.
	private int x = 0; //Used to track the index of the Aces in the User's hand to set their values and highlight the cards.
	private int rounds = 0; //After every 5 rounds, the DeckOfCards used in the game is shuffled.
	private double phase = 0; //Based on the value of phase, "Hit" button will have different effects.
	private Font afb = new Font("Agency FB", Font.BOLD, 20); //Font mainly used for JLabels and JButtons present in the game.
	private DecimalFormat df = new DecimalFormat("0.00"); //Used to format the number representing the percentage of the user's wins.
	private User user = new User(); //Subclass of Player that will be used by the user
	private Dealer dealer = new Dealer(); //Subclass of Player that will compete against the user.
	private static DeckOfCards deck = new DeckOfCards(true); //A DeckOfCards object that will simulate a card deck in the game.
	private JLayeredPane panel = new JLayeredPane(); //JLayeredPane panel will be used as the main panel for the frame contents.
	private JPanel cardPanel = new JPanel(); //panel that will contain the visual information of dealer's and user's scores and their cards.
	private JLayeredPane dealerPanel = new JLayeredPane(); //panel that will contain cards in the Dealer's hand. It is a part of cardPanel.
	private JLayeredPane userPanel = new JLayeredPane(); //Same as dealerPanel, but relates to the user.
	private JPanel statsPanel = new JPanel(); //panel which will display the stats of the game.
	private JPanel btnPanel = new JPanel(); //panel which will house the action buttons in the game (hit, stand, etc.).
	//Declare an array of labels that will display the user's stats
	private JLabel[] lblStats = {new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel()};
	private JLabel lblBet = new JLabel("Bet: $0"); //JLabel that will be used to display how much the user has bet.
	private JLabel lblInsurance = new JLabel(); //JLabel that will display how much in insurance the user has bet if applicable.
	private JLabel lblMessage = new JLabel("Place a bet!", JLabel.LEFT); //JLabel that will display prompts and messages to the user.
	private JLabel lblTip = new JLabel("Click chips to place a bet!");
	/* lblHand will display cards in the parties' hands
	 * lblHand[0][index] represents dealer's cards, lblhand[1][index] represents the user's cards. */
	private JLabel[][] lblHand = new JLabel[2][5];
	private JLabel lblDealer = new JLabel("Dealer: " + dealer.score, JLabel.CENTER); //label that displays the dealer's score
	private JLabel lblUser = new JLabel("Player: " + user.score, JLabel.CENTER); //same as lblDealer, but relates to user.
	private JButton[] btnBet = new JButton[7]; //An array of buttons that the user will use to place bets.
	private JButton btnExit = new JButton("Exit"); //A button that the user will click to exit the program.
	private JButton btnReset = new JButton("Reset Stats"); //A button that resets all the stats of the user and writes them to file.
	private JButton btnRules = new JButton("Read the Rules");
	/* Buttons that the user clicks to play the game.
	 * btnAction[0] is Hit
	 * btnAction[1] is Stand
	 * btnAction[2] is Double Down
	 * btnAction[3] is Insurance */
	private JButton[] btnAction = {new JButton("Hit"), new JButton("Stand"), new JButton("Double Down"), new JButton("Insurance")};
	
	public static void main(String[] args){
		new Game();		
	}
	
	public Game(){
		//Set up the game background
		ImageIcon table = new ImageIcon("images\\table.png");
		JLabel lblTable = new JLabel();
		lblTable.setIcon(table);
		lblTable.setBounds(0, 0, table.getIconWidth(), table.getIconHeight());
		panel.add(lblTable, new Integer(0));
		/* Set up the bet buttons
		 * The buttons are invisible and overlap 
		 * with the chips seen on the image of the table */
		//Initialize the button objects
		for (int n = 0; n < btnBet.length; n++){
			btnBet[n] = new JButton();
			btnBet[n].addActionListener(this);
			btnBet[n].setFocusable(false);
		}
		/* Since there is no a clear pattern for the button location,
		 * set bounds for the buttons manually. */
		btnBet[0].setBounds(11, 282, 37, 33);
		btnBet[1].setBounds(48, 315, 36, 36);
		btnBet[2].setBounds(89, 345, 38, 38);
		btnBet[3].setBounds(135, 368, 40, 36);
		btnBet[4].setBounds(184, 387, 42, 36);
		btnBet[5].setBounds(241, 402, 40, 38);
		btnBet[6].setBounds(0, 400, 100, 30);
		btnBet[6].setFont(afb);
		btnBet[6].setForeground(Color.YELLOW);
		btnBet[6].setBackground(Color.RED);
		btnBet[6].setText("Reset Bet");
		//Add the bet buttons to the panel and make them invisible
		for (int n = 0; n < btnBet.length; n++){
			if (n != 6){
				btnBet[n].setMargin(new Insets(0, 0, 0, 0));
				btnBet[n].setBorder(BorderFactory.createEmptyBorder());
				btnBet[n].setContentAreaFilled(false);
				btnBet[n].setToolTipText("Click me to place a bet!");
			}
			btnBet[n].setFocusable(false);
			panel.add(btnBet[n], new Integer(1));
		}
		//Set up the exit button
		btnExit.addActionListener(this);
		btnExit.setForeground(Color.YELLOW);
		btnExit.setFont(afb);
		btnExit.setBackground(Color.RED);
		btnExit.setBounds(0, 400 + btnBet[6].getHeight(), btnBet[6].getWidth(), btnBet[6].getHeight());
		btnExit.setFocusable(false);
		panel.add(btnExit, new Integer(1));
		//Set up the buttons panel
		btnPanel.setLayout(new GridLayout(4, 1, 0, 5));
		for (int n = 0; n < btnAction.length; n++){
			btnAction[n].setForeground(Color.YELLOW);
			btnAction[n].setBackground(Color.RED);
			btnAction[n].addActionListener(this);
			btnAction[n].setFont(afb);
			btnAction[n].setEnabled(false);
			btnAction[n].setFocusable(false);
			btnPanel.add(btnAction[n]);
		}
		btnPanel.setOpaque(false);
		btnPanel.setBounds(305, 380, 130, 99);
		panel.add(btnPanel, new Integer(1));
		//Set up the reset stats button
		btnReset.setForeground(Color.YELLOW);
		btnReset.setBackground(Color.RED);
		btnReset.setFont(afb);
		btnReset.addActionListener(this);
		btnReset.setBounds(540, 215, 150, 20);
		btnReset.setFocusable(false);
		panel.add(btnReset, new Integer(1));
		//Set up the rules button
		btnRules.setForeground(Color.YELLOW);
		btnRules.setBackground(Color.RED);
		btnRules.setFont(afb);
		btnRules.setFocusable(false);
		btnRules.addActionListener(this);
		btnRules.setBounds(540, 180, 150, 22);
		panel.add(btnRules, new Integer(1));
		//Set up the statsPanel
		statsPanel.setLayout(new GridLayout(7, 1, -40, 0));
		for (int i = 0; i < lblStats.length; i++){
				statsPanel.add(lblStats[i]);
				lblStats[i].setHorizontalAlignment(JLabel.RIGHT);
				lblStats[i].setFont(afb);
				lblStats[i].setForeground(Color.YELLOW);
		}
		statsPanel.setBorder(BorderFactory.createTitledBorder(null, "Stats", TitledBorder.RIGHT, 0, new Font("Monotype Corsiva", Font.PLAIN, 22), Color.YELLOW));
		statsPanel.setOpaque(false);
		statsPanel.setBounds(529, 235, 170, 245);
		statsPanel.setFocusable(false);
		panel.add(statsPanel, new Integer(1));
		//Set up the "tip" label
		lblTip.setBounds(120, 450, 170, 20);
		lblTip.setFont(afb);
		lblTip.setForeground(Color.WHITE);
		lblTip.setFocusable(false);
		panel.add(lblTip, new Integer(1));
		//Set up the message label
		lblMessage.setForeground(Color.WHITE);
		lblMessage.setFont(afb);
		lblMessage.setBounds(0, 100, 300, 30);
		lblMessage.setFocusable(false);
		panel.add(lblMessage, new Integer(1));
		//Set up the insurance label
		lblInsurance.setBounds(0, 50, 300, 30);
		lblInsurance.setForeground(Color.YELLOW);
		lblInsurance.setFont(afb);
		panel.add(lblInsurance, new Integer(1));
		//Set up the bet label
		lblBet.setBounds(0, 0, 310, 30);
		lblBet.setForeground(Color.YELLOW);
		lblBet.setFont(new Font("Rockwell Extra Bold", Font.PLAIN, 20));
		panel.add(lblBet, new Integer(1));
		//Set up cardPanel
		cardPanel.setBounds(235, -5, 255, 390);
		cardPanel.setLayout(new GridLayout(4, 1, 0, -10));
		cardPanel.setOpaque(false);
		lblDealer.setForeground(Color.YELLOW);
		lblDealer.setFont(afb);
		lblDealer.setFocusable(false);
		lblUser.setForeground(Color.YELLOW);
		lblUser.setFont(afb);
		lblUser.setFocusable(false);
		cardPanel.add(lblDealer);
		cardPanel.add(dealerPanel);
		cardPanel.add(lblUser);
		cardPanel.add(userPanel);
		panel.add(cardPanel, new Integer(1));
		//Set up user and dealer's hands labels array
		//Use null layout in order to be able to make the cards look stacked in layers
		dealerPanel.setLayout(null);
		userPanel.setLayout(null);
		Integer layer = 0;
		int xPos = 30; //xPos is relative to the dealerPanel and userPanel, not the frame
		for (Integer n = 0; n < 5; n++){
			//Initialize card labels
			lblHand[0][n] = new JLabel();
			lblHand[1][n] = new JLabel();
			lblHand[0][n].setFocusable(false);
			lblHand[1][n].setFocusable(false);
			lblHand[0][n].setBounds(xPos, dealerPanel.getY(), 73, 97);
			lblHand[1][n].setBounds(xPos, userPanel.getY(), 73, 97);
			if(n < 2){
				lblHand[0][n].setIcon(dealer.getCard(n).getImage());
				lblHand[1][n].setIcon(user.getCard(n).getImage());
			}
			dealerPanel.add(lblHand[0][n], layer);
			userPanel.add(lblHand[1][n], layer);
			layer++; //Add each card on top of the other
			xPos += 30;
		}
		updateLabels();
		//Set up the frame
		frame.addKeyListener(new AceListener());
		frame.setContentPane(panel);
		frame.setTitle("BlackJack");
		frame.setSize(table.getIconWidth(), table.getIconHeight());
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public void determineWinner(){
		for (int n = 1; n < btnAction.length; n++){
			btnAction[n].setEnabled(false);
		}
		lblHand[0][1].setIcon(dealer.getCard(1).getImage()); //Reveal the Dealer's second card if it has not already been done.
		//Conclude who won the game based on the values of isWinner
		if (user.getWinner() && dealer.getWinner()){
			tie();
		}
		else if (user.getWinner() && dealer.getWinner() == false){
			rewardUser();
		}
		else if (user.getWinner() == false && dealer.getWinner()){
			punishUser();
		}
		user.updateGames(1); //Update games count
		user.updateWP(); //Update win percentage count
		user.writeStats(); //Save the stats of the user
		updateLabels();
		if (user.getBlackjack())
			lblUser.setText("User: Blackjack!");
		if (dealer.getBlackjack())
			lblDealer.setText("Dealer: Blackjack!");
		btnAction[0].setText("Play again");
		btnReset.setEnabled(true); //Enable the button that resets all stats
		phase = 2; //btnAction[0] will reset the game
	}
	
	public void dealerTurn(){
		dealer.drawCard(deck, numDealer);
		numDealer++; //Update count of the dealer's cards
		updateHand();
	}
	
	public void dealerPhase(){
		//Reveal dealer's second card before proceeding
		lblHand[0][1].setIcon(dealer.getCard(1).getImage());
		numDealer = 2;
		dealer.calculateScore();
		updateLabels();
		while (dealer.getScore() < 17 && dealer.getScore() <= user.getScore() 
				&& numDealer < 5){ //The dealer draws till he reaches 17 (just like it says on the table) or beats the user
			dealerTurn();
		}
		//Check if the dealer busted
		if (dealer.getScore() > 21){
			updateMessage("Dealer busted!");
			user.setWinner(true);
		}
		//If the dealer did not bust, compare his score with the user's score
		else if (dealer.getScore() == user.getScore()){
			updateMessage("You break even");
			user.setWinner(true);
			dealer.setWinner(true);
		}
		else if (dealer.getScore() > user.getScore()){
			updateMessage("Dealer wins!");
			dealer.setWinner(true);
		}
		else if (user.getScore() > dealer.getScore()){
			updateMessage("You win!");
			user.setWinner(true);
		}
		determineWinner();
	}
	
	/* endPhase0 method is invoked after first two cards are dealt to each party. It checks the hands for a Blackjack
	 * If one or both parties get a Blackjack, the game ends with the applicable party being victorious.
	   Otherwise, the game continues with the dealer trying to beat the user */
	public void endPhase0(){
		user.checkBlackjack();
		dealer.checkBlackjack();
		phase = 1;
		btnAction[0].setText("Hit");
		btnAction[1].setEnabled(true);
		btnAction[3].setEnabled(false);
		lblMessage.setText("Hit a card or stand!");
		if (user.getBlackjack() || dealer.getBlackjack()){
			//If one of the parties has a Blackjack, set a winner stats for both, and call determineWinner() to reward or punish the User
			if (user.getBlackjack() && dealer.getBlackjack()){
				updateMessage("You both got a Blackjack, it's a tie!");
				lblHand[0][1].setIcon(dealer.getCard(1).getImage());
				user.setWinner(true);
				dealer.setWinner(true);
				determineWinner();
			}
			else if (user.getBlackjack()){
				updateMessage("You got a Blackjack!");
				user.setWinner(true);
			}
			else if (dealer.getBlackjack()){
				//Reveal the dealer's card that got him a Blackjack
				lblHand[0][1].setIcon(dealer.getCard(1).getImage());
				updateMessage("Dealer got a Blackjack!");
				dealer.setWinner(true);
			}
			determineWinner();
		}
		else
			btnAction[2].setEnabled(true);
	}
	
	public void placeBet(int x /*x depends on the btnBet that was clicked*/){
		btnReset.setEnabled(false); //Prevent the user from cheating by placing bet, resetting, placing more, etc.
		//Prevent the user from betting more than they have
		if (x > user.getBalance()){
			JOptionPane.showMessageDialog(null, "You don't have that much!", "Hmm...", JOptionPane.ERROR_MESSAGE);
		}
		else{
			user.updateBet(x);
			user.updateBalance(-x);
			updateLabels();
			updateMessage("Press Hit to begin!");
			btnAction[0].setEnabled(true);
		}
	}
	
	public void punishUser(){
		//Add to the user's losses count and return their insurance bet if they lost because the dealer got a Blackjack
		user.updateLosses(1);
		if (user.getInsured() && dealer.getBlackjack()){
			user.updateBalance((int)Math.round(user.getInsurance() * 1.5));
		}
	}
	
	public void rewardUser(){
		//Update the user's wins count
		user.updateWins(1);
		//If the user won because of getting a Blackjack, payout is 3 to 1
		if (user.getBlackjack()){
			user.updateBalance(user.getBet() * 3);
			user.updateBlackjacks(1);
		}
		//Otherwise, payout is 2 to 1
		else
			user.updateBalance(user.getBet() * 2);
	}
	
	public void resetGame(){
		frame.addKeyListener(new AceListener());
		user.updateBet(0); //Reset the user's bet
		updateMessage("Place a bet!");
		btnAction[0].setText("Hit");
		user = new User();
		dealer = new Dealer();
		for (int n = 1; n < btnAction.length; n++){
			btnAction[0].setEnabled(false);		
		}
		for (int n = 0; n < btnBet.length; n++){
			btnBet[n].setEnabled(true);
		}
		for (int n = 0; n < lblHand[0].length; n++){
			lblHand[0][n].setBorder(null);
			lblHand[1][n].setBorder(null);
			if (n <= 1){
				lblHand[0][n].setIcon(dealer.hand[n].getImage());
				lblHand[1][n].setIcon(user.hand[n].getImage());
			}
			else{
				lblHand[0][n].setIcon(null);
				lblHand[1][n].setIcon(null);
			}
		}
		lblInsurance.setText(null);
		//Reset the card counts and phase
		phase = 0;
		numUser = 0;
		numDealer = 0;
		rounds++;
		if (rounds == 5){ //Shuffle deck every 5 rounds
			deck = new DeckOfCards(true);
			rounds = 0;
		}
		btnReset.setEnabled(false);
		updateLabels();
	}
	
	public void tie(){ //User neither gains nor loses chips
		user.updateBalance(user.getBet());
		user.updateTies(1);
	}
	
	public void updateLabels(){ //Call this method when needing to update two or more labels at once, to optimize number of lines of code.
		lblBet.setText("Bet: $" + user.getBet());
		lblStats[0].setText("Balance: $" + user.getBalance());
		for (int n = 1; n <= 5; n++){
			lblStats[n].setText(user.getStats(1, n) + ": " + user.getStats(0, n));
		}
		lblStats[6].setText("Win Percentage: %" + df.format(user.getWP()));
		lblUser.setText("Player: " + user.getScore());
		lblDealer.setText("Dealer: " + dealer.getScore());
	}
	
	public void updateMessage(String msg){
		//Update message label with the text specified
		lblMessage.setText(msg);
	}
	
	public void updateHand(){
		//Set the icons to the corresponding cards in the user's and dealer's hands
		for (int n = 0; n < numDealer; n++){
			lblHand[0][n].setIcon(dealer.getCard(n).getImage());
		}
		for (int n = 0; n < numUser; n++){
			lblHand[1][n].setIcon(user.getCard(n).getImage());
		}
	}
	
	public void userTurn(){
		user.drawCard(deck, numUser); //Draw the card for User
		//If the drawn card is an Ace, use x to remember the card number, highlight the card and determine the Ace's default value.
		if (user.getCard(numUser).getValue() == 14){
			lblHand[1][x].setBorder(null); //Stop highlighting the previously drawn Ace
			x = numUser;
			lblHand[1][x].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
		}
		numUser++;
		updateHand();
		lblUser.setText("Player: " + user.score);
	}
	
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == btnAction[0]){
			for (int n = 0; n < btnBet.length; n++){ //Disable bet buttons
				btnBet[n].setEnabled(false);
			}
			btnReset.setEnabled(false);
			if (phase == 0){ //Draw first two cards for the user
				for (int n = 0; n < 2; n++){
					userTurn();
				}
				user.checkBlackjack(); //Check if the user's first two cards get him a Blackjack
				dealerTurn(); //Draw and reveal the dealer's first card
				updateLabels();
				dealer.drawCard(deck, numDealer); //Draw a second card for the dealer, but not reveal it
//Offer insurance to the user, if the dealer's upward card is an Ace, and the user did not draw a Blackjack, and has enough money to make insurance bet
				if (dealer.getCard(numDealer - 1).getValue() == 14 && user.getBlackjack() == false 
						&& user.getBalance() >= Math.round(user.getBet() / 2)){
					updateMessage("Would you like to get insurance?");
					btnAction[0].setText("No, continue");
					btnAction[3].setEnabled(true);
					phase = 0.5;
				}
				else{ //Otherwise, carry on with the game
					endPhase0();
				}
			}
			else if (phase == 0.5){ //User decides not to make an insurance bet when offered
				lblHand[0][1].setIcon(dealer.getCard(1).getImage());
				lblDealer.setText("Dealer: " + dealer.getScore());
				endPhase0();
			}
			else if (phase == 1){
				btnAction[2].setEnabled(false); //Disable double down button
				userTurn(); //Draw and reveal a card for the user
				if (user.getScore() > 21){ //Check whether the user busted
					dealer.setWinner(true);
					updateMessage("You busted!");
					determineWinner();
				}
				else if (numUser == 5){ //If the user draws 5 cards without going over 21 points, they automatically win.
					user.setWinner(true);
					updateMessage("You drew 5 cards, you win!");
					determineWinner();
				}
				else if (user.getScore() == 21 && numUser != 5) //If the user drew to 21 points, give the dealer a chance to tie the game
					dealerPhase();
			}
			else if (phase == 2){
				resetGame();
			}
		}
		else if (e.getSource() == btnAction[1]){ //The user stands, the dealer starts playing
			dealerPhase();
		}
		else if (e.getSource() == btnAction[2]){ //If the user doubles down, they only get one more card and increase their bet by two
			if (user.getBalance() >= user.getBet() * 2){ //Check if the user has enough chips to double their bet
				user.updateBalance(-user.getBet());
				user.updateBet(user.getBet());
				userTurn();
				if (user.score > 21){ //Check if the user's third card busted them
					updateMessage("You busted!");
					dealer.setWinner(true);
					determineWinner();
				}
				else
					dealerPhase();
				}
			else //The user can only double down if they have enough money
				JOptionPane.showMessageDialog(null, "You can't double down, you don't have that much!", "BlackJack", JOptionPane.ERROR_MESSAGE);			
			}
		else if (e.getSource() == btnAction[3]){ //The user decides to make an insurance bet		
			lblHand[0][1].setIcon(dealer.getCard(1).getImage());
			lblDealer.setText("Dealer: " + dealer.getScore());
			user.setInsurance();
			lblInsurance.setText("Insurance: $" + user.getInsurance());
			updateLabels();
			endPhase0();
		}
		//When the button overlapping with one of the chips is clicked, place as much money as shown on the corresponding chip.
		else if (e.getSource() == btnBet[0]){ 
			placeBet(100);
		}
		else if (e.getSource() == btnBet[1]){
			placeBet(50);
		}
		else if (e.getSource() == btnBet[2]){
			placeBet(25);
		}
		else if (e.getSource() == btnBet[3]){
			placeBet(10);
		}
		else if (e.getSource() == btnBet[4]){
			placeBet(5);
		}
		else if (e.getSource() == btnBet[5]){
			placeBet(1);
		}
		//btnBet[6] resets the bet, but does not allow the user to continue
		else if (e.getSource() == btnBet[6]){
			user.updateBalance(user.getBet());
			user.updateBet(0);
			updateMessage("Place a bet!");
			btnAction[0].setEnabled(false);
			updateLabels();
			btnReset.setEnabled(true);
		}
		else if (e.getSource() == btnExit){ //Close program
			System.exit(0);
		}
		else if (e.getSource() == btnReset){ //Reset the user's stats and save them in the file
			user.setBalance(500);
			user.updateGames(0);
			user.updateWins(0);
			user.updateLosses(0);
			user.updateTies(0);
			user.updateBlackjacks(0);
			user.updateWP();
			user.writeStats();
			updateLabels();
		}
		else if (e.getSource() == btnRules){
			new Rules();
		}
	}
	
	class AceListener extends KeyAdapter{
		/* The user can control the value of a selected Ace in the hand by pressing UP and DOWN buttons
		 * The user can select an Ace in their hand by pressing LEFT and RIGHT buttons */
		public void keyPressed(KeyEvent e){
			//phase != 2 condition in if statements prevents the player from changing Ace values after the round has ended
			if (e.getKeyCode() == KeyEvent.VK_UP && phase != 2){
				if (user.getScore() <= 12) //Ace will only go from 1 to 10 in value if it does not bust the user
					user.setAceVal(x, 10);
			}
			else if (e.getKeyCode() == KeyEvent.VK_DOWN && phase != 2){ //The Ace goes from 10 to 1 in value
				user.setAceVal(x, 1);
			}			
			else if (e.getKeyCode() == KeyEvent.VK_LEFT && x >= 0 && phase != 2){ 
				boolean isAce = false;
				int n = x;
				n--;
				while(n >= 0 && isAce == false){ //Run through the hand until an Ace is found or all cards have been checked
					if (user.getCard(n).getValue() == 14){
						lblHand[1][x].setBorder(null);
						x = n; //Record the index of the Ace we are looking at
						lblHand[1][x].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2)); //Highilight the selected Ace
						isAce = true; //Break the loop if an Ace is found
					}
					else //Otherwise, keep searching
						n--;
				}
			}
			else if (e.getKeyCode() == KeyEvent.VK_RIGHT && x <= 4 && phase != 2){ //Same as when LEFT arrow is pressed, but searches from left to right
				boolean isAce = false;
				int n = x;
				n++;
				while(n <= 4 && isAce == false){
					if (user.getCard(n).getValue() == 14){
						lblHand[1][x].setBorder(null);
						x = n;
						lblHand[1][x].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
						isAce = true;
					}
					else
						n++;
				}
			}
			if (phase != 2){
				user.calculateScore(); //After an Ace value is updated, the player's score is recalculated with corresponding Ace values
				lblUser.setText("Player: " + user.getScore());
			}
		}
	}
}

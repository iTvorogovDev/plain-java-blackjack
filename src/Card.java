import javax.swing.ImageIcon;


public class Card {
	private int value; //value determines the card: 2 to 10 will be respective cards , 11-14 will be Jack-Ace
	
	private int width, height; //width and height represent the width and height of the card's icon
	private String suit, name; //String representations of the card names and suit
	private ImageIcon imgCard; //Icon that represents the card in GUI
	
	public Card(){ //Constructor that creates a "face down" card
		this.imgCard = new ImageIcon("images\\back.png");
		this.value = 0;
		this.name = "";
		this.suit = "";
		this.width = imgCard.getIconWidth();
		this.height = imgCard.getIconHeight();
	}
	
	public Card(ImageIcon imgCard, int value, String suit){ //Constructor that creates a Card with specified suit name, value and icon
		this.imgCard = imgCard;
		this.value = value;
		this.name = Integer.toString(value);
		this.suit = suit;
		this.width = imgCard.getIconWidth();
		this.height = imgCard.getIconHeight();
	}
	
	public int getHeight(){ //Get height of the icon of the Card. Useful for programming UI in card games
		return height;
	}
	
	public int getWidth(){ //Same as getHeight, but returns width of the Card icon
		return width;
	}
	public String getName(){ //Returns the name of the card
		if (value == 11)
			return "Jack";
		else if (value == 12)
			return "Queen";
		else if (value == 13)
			return "King";
		else if (value == 14)
			return "Ace";
		else
			return name;
	}
	public String getSuit(){ //Returns the first letter of the name of the card suit
		if (suit.equals("S"))
			return "Spades";
		else if (suit.equals("H"))
			return "Hearts";
		else if (suit.equals("D"))
			return "Diamonds";
		else if (suit.equals("C"))
			return "Clubs";
		else
			return suit;
	}
	public int getValue(){ //Returns the order value of the card (2 is 2, 10 is 10, Jack is 11, etc.)
		return value;
	}
	
	public ImageIcon getImage(){ //Returns the icon set to the card
		return imgCard;
	}
	
	public void setHeight(int h){ //Sets the height of the card to be used
		this.height = h;
	}
	
	public void setWidth(int w){ //Same as setHeight, but mutates width
		this.width = w;
	}
	
	public void setImage(ImageIcon img){ //Sets the icon to be used with the card
		this.imgCard = img;
	}
	
	public void setName(String n){ //Sets the name of the card
		this.name = n;
	}
	
	public void setSuit(String s){ //Sets the name of the suit of the card
		this.suit = s;
	}
	
	public void setValue(int v){ //Sets the value of the card
		this.value = v;
	}
}

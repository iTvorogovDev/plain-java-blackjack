import javax.swing.*;

import java.awt.Font;
import java.io.*;

public class Rules extends JFrame{
	
	private JPanel p = new JPanel(); //The panel that will hold JTextArea that the rules will be output to.
	private JTextArea rules = new JTextArea(25, 50); //This is where the rules will be output

	public static void main(String[] args) {
		new Rules();
	}
	
	public Rules(){
		File f = new File("rules.txt"); //path to rules text document
		rules.setEditable(false);
		rules.setFont(new Font("Calibri", Font.PLAIN, 18));
		p.add(rules);
		try{ //Read text from the document and output it to the JTextArea
			String s;
			BufferedReader in = new BufferedReader(new FileReader(f));
			s = in.readLine();
			while (s != null){
				rules.append(s + "\n");
				s = in.readLine();
			}
			in.close();
		}
		catch(IOException moo){
			JOptionPane.showMessageDialog(null, "Error retrieving rules text file: " + moo.getMessage(), "Rules", JOptionPane.ERROR_MESSAGE);
		}
		//Set up the properties of the frame
		setContentPane(p);
		setTitle("Rules");
		setSize(770, 800);
		setResizable(false);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}

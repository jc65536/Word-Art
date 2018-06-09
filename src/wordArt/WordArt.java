package wordArt;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedWriter;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WordArt extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JLabel instructions = new JLabel(
			"<html>Enter a word in the textbox below to convert to ASCII art.<br/>Only alphanumeric characters and space are supported</html>",
			JLabel.CENTER);
	JTextField textInput = new JTextField("Hello world", 30);
	JButton submitInput = new JButton("Draw");
	JEditorPane asciiOutput = new JEditorPane();
	JButton saveToFile = new JButton("Save to file");
	JPanel wrapper = new JPanel();
	JPanel innerWrapper = new JPanel();

	public WordArt() {
		Container cp = getContentPane();
		cp.setLayout(new GridLayout(2, 1));
		cp.add(wrapper);
		wrapper.setLayout(new GridLayout(2, 1));
		wrapper.add(instructions);
		wrapper.add(innerWrapper);
		innerWrapper.add(textInput);
		innerWrapper.add(submitInput);
		innerWrapper.add(saveToFile);
		cp.add(asciiOutput);
		asciiOutput.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		submitInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
					String asciiString = convertToAscii(textInput.getText());
					asciiOutput.setText(asciiString);
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "IO Exception", JOptionPane.ERROR_MESSAGE,
							null);
					ex.printStackTrace();
				}
			}
		});
		saveToFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
					String asciiString = convertToAscii(textInput.getText());
					asciiOutput.setText(asciiString);
					String fileName = JOptionPane.showInputDialog("Please specify file name", "ASCII_Art-"
							+ textInput.getText().toUpperCase().substring(0, Math.min(11, textInput.getText().length()))
							+ ".txt");
					BufferedWriter w = new BufferedWriter(new FileWriter(fileName));
					w.write(asciiOutput.getText());
					w.newLine();
					w.close();
					JOptionPane.showMessageDialog(null, "Saved to " + fileName);
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "IO Exception", JOptionPane.ERROR_MESSAGE,
							null);
				}
			}
		});
	}

	public String convertToAscii(String text) throws IOException {
		char[] inputChars = text.toUpperCase().trim().toCharArray();
		String outputString = "";
		try {
			for (int a = 0; a < 6; a++) {
				for (char c : inputChars) {
					int lineNumber = (c - 'A' + 1) * 6 + a;
					if (Character.isWhitespace(c)) {
						lineNumber = a;
					}
					if (Character.isDigit(c)) {
						lineNumber = (27 + c - '0') * 6 + a;
					}
					String strAtLine = Files.readAllLines(Paths.get("CharArt")).get(lineNumber);
					outputString += strAtLine;
				}
				outputString += "\n";
			}
			return outputString;
		} catch (IndexOutOfBoundsException e) {
			JOptionPane.showMessageDialog(null,
					"Alphanumeric characters and spaces only!\nAlso, do not tamper with the CharArt file!",
					"Input not recognized", JOptionPane.WARNING_MESSAGE);
			return "";
		}
	}

	public static void main(String[] args) {
		WordArt wordArt = new WordArt();
		wordArt.setSize(800, 400);
		wordArt.setVisible(true);
		wordArt.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}

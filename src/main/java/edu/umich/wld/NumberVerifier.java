package edu.umich.wld;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class NumberVerifier extends InputVerifier {
		String lastGood;
		String message;
		
		public NumberVerifier(JTextField textField, String description) {
			lastGood = textField.getText();
			message = description + " must be a number.";
		}
		
		public boolean shouldYieldFocus(JComponent input) {
			JTextField textField = (JTextField) input;
			if (verify(input)) {
				lastGood = textField.getText();
				return true;
			}
			
			JOptionPane.showMessageDialog(null, message, "Invalid Input", JOptionPane.ERROR_MESSAGE);
			textField.setText(lastGood);
			return false;
		}

		public boolean verify(JComponent input) {
			JTextField textField = (JTextField) input;			
			return BinnerNumUtils.toDouble(textField.getText()) != null;
		}
	}
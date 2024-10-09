package HelpSystem;

import java.time.LocalDateTime;

public class OneTimePassword {
	private char[] oneTimePassword;
	private LocalDateTime expirationDate;
	
	// constructor to create password with 30 minute timer
	public OneTimePassword() {
		this.oneTimePassword = generatePassword();
		this.expirationDate = LocalDateTime.now().plusMinutes(30);
	}
	
	// generates a 5 digit random number
	private char[] generatePassword() {
		int randomNumber = 10000 + (int)(Math.random() * 90000);
		
		String passwordString = String.valueOf(randomNumber);
		
		// updates password
		return passwordString.toCharArray();
	}
	
	// checks if password is expired
	public boolean isExpired() {
		if(LocalDateTime.now().isAfter(expirationDate)) {
			return true;
		}
		
		return false; 
	}
	
	// returns password
	public String returnPassword() {
		String pw = new String(oneTimePassword);
		
		return pw; 
	}
	
}

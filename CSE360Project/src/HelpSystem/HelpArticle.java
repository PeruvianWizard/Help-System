package HelpSystem;

import java.util.Random;

public class HelpArticle {
	private long identifier; // unique 10 digit code
	private int level; // stores the "difficulty" level of the article from 0 to 3
	private String group; // string that acts as identifier specifically to group articles (example any articles about java)
	private boolean isPrivate;
	
	private String title;
	private String description;
	private String body;
	
	// constructor
	public HelpArticle(boolean isPrivate, int level, String title, String description, String body, String group) {
		Random r = new Random();
		this.identifier = 1000000000L + (long)(r.nextDouble() * 9000000000L);
		
		
		this.level = level;
		this.group = group;
		this.isPrivate = isPrivate;
		this.title = title; 
		this.description = description; 
		this.body = body;
	}
	
	// getter methods
	public long getIdentifier() {
		return identifier;
	}
	
	public int getLevel() {
		return level;
	}
	
	public String group() {
		return group;
	}
	
	public boolean isPrivate() {
		return isPrivate;
	}
	
	public String getTitle() {
		return title; 
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getBody() {
		return body; 
	}
}

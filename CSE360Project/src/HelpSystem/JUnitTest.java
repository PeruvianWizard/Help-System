package HelpSystem;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

public class JUnitTest {
	/*
	@Test
	public void TestingMessage1() throws SQLException {
		assertEquals(true, Testing.Message_Test("This is a Test", "studnt"));
	}
	*/
	
	/*
	@Test
	public void TestingMessage2() throws SQLException {
		assertEquals(false, Testing.Message_Test("This is a second Test", "studnt"));
	}
	*/
	
	
	/*
	@Test
	public void TestingAllLevel1() throws SQLException {
		assertEquals(true, Testing.All_Level_Test());
	}
	*/
	
	
	@Test
	public void TestingAllLevel2() throws SQLException {
		assertEquals(false, Testing.All_Level_Test());
	}
	
}

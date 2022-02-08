package org.cryptomator.ui.common;

import com.google.common.base.Strings;
import org.cryptomator.common.Environment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.util.ResourceBundle;

public class PasswordStrengthUtilTest {

	@Test
	public void testLongPasswords() {
		PasswordStrengthUtil util = new PasswordStrengthUtil(Mockito.mock(ResourceBundle.class), Mockito.mock(Environment.class));
		String longPw = Strings.repeat("x", 10_000);
		Assertions.assertTimeout(Duration.ofSeconds(5), () -> {
			util.computeRate(longPw);
		});
	}

	@Test
	public void testIssue979() {
		PasswordStrengthUtil util = new PasswordStrengthUtil(Mockito.mock(ResourceBundle.class), Mockito.mock(Environment.class));
		int result1 = util.computeRate("backedderrickbucklingmountainsgloveclientproceduresdesiredestinationswordhiddenram");
		Assertions.assertEquals(4, result1);
	}


	// New Test Cases for Part 2

	@Test
	public void testForWeak() {
		PasswordStrengthUtil util = new PasswordStrengthUtil(Mockito.mock(ResourceBundle.class), Mockito.mock(Environment.class));
		int result1 = util.computeRate("uci");
		Assertions.assertEquals(0, result1);
	}

	@Test
	public void testForWeak2() {
		PasswordStrengthUtil util = new PasswordStrengthUtil(Mockito.mock(ResourceBundle.class), Mockito.mock(Environment.class));
		int result1 = util.computeRate("irvine");
		Assertions.assertEquals(1, result1);
	}

	@Test
	public void testForMedium() {
		PasswordStrengthUtil util = new PasswordStrengthUtil(Mockito.mock(ResourceBundle.class), Mockito.mock(Environment.class));
		int result1 = util.computeRate("mswe2022");
		Assertions.assertEquals(2, result1);
	}

	@Test
	public void testForMedium2() {
		PasswordStrengthUtil util = new PasswordStrengthUtil(Mockito.mock(ResourceBundle.class), Mockito.mock(Environment.class));
		int result1 = util.computeRate("Irvine-2022");
		Assertions.assertEquals(3, result1);
	}

	@Test
	public void testForStrong() {
		PasswordStrengthUtil util = new PasswordStrengthUtil(Mockito.mock(ResourceBundle.class), Mockito.mock(Environment.class));
		int result1 = util.computeRate("MsweIrvine-2022!");
		Assertions.assertEquals(4, result1);
	}


	// END OF NEW TEST CASES
}

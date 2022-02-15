/*******************************************************************************
 * Copyright (c) 2016, 2017 Sebastian Stenzel and others.
 * All rights reserved.
 * This program and the accompanying materials are made available under the terms of the accompanying LICENSE file.
 *
 * Contributors:
 *     Sebastian Stenzel - initial API and implementation
 *******************************************************************************/
package org.cryptomator.common.settings;

import com.google.common.base.Strings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VaultSettingsTest {

	@ParameterizedTest
	@CsvSource({"a\u000Fa,a_a", ": \\,_ _", "汉语,汉语", "..,_", "a\ta,a\u0020a", "\t\n\r,_"})
	public void testNormalize(String test, String expected) {
		VaultSettings settings = new VaultSettings("id");
		settings.displayName().setValue(test);
		assertEquals(expected, settings.normalizeDisplayName());
	}

	// New Test Cases for Structural (Coverage) - Section 3

	@Test
	public void testGenerateRandomID(){
		// Test method invoking of subsequent methods within the call
		VaultSettings.withRandomId();
	}

	@Test
	public void testIdAndPath(){
		VaultSettings settings = new VaultSettings("test_id");
		settings.path();
		Assertions.assertEquals("test_id", settings.getId());
	}

	@Test
	public void testMountFlags(){
		VaultSettings settings = new VaultSettings("test_id");
		Assertions.assertEquals(new SimpleStringProperty("").toString() , settings.mountFlags().toString());
	}

	@Test
	public void testRevealAfterMount(){
		VaultSettings settings = new VaultSettings("test_id");
		Assertions.assertEquals(new SimpleBooleanProperty(true).toString(), settings.revealAfterMount().toString());
	}

	@Test
	public void testCustomMount(){
		VaultSettings settings = new VaultSettings("test_id");
		settings.mountName();
		Assertions.assertEquals(new SimpleStringProperty(null).toString(), settings.customMountPath().toString());
	}

	@Test
	public void testGetCustomMount(){
		VaultSettings settings = new VaultSettings("test_id");
		Assertions.assertEquals(Optional.empty(), settings.getCustomMountPath());
	}

	@Test
	public void testUsingCustomMountMath(){
		VaultSettings settings = new VaultSettings("test_id");
		Assertions.assertEquals(new SimpleBooleanProperty(false).toString(), settings.useCustomMountPath().toString());
	}

	@Test
	public void testAutoLockAndReadOnly(){
		VaultSettings settings = new VaultSettings("test_id");
		Assertions.assertEquals(new SimpleBooleanProperty(false).toString(), settings.autoLockWhenIdle().toString());
		Assertions.assertEquals(new SimpleBooleanProperty(false).toString(), settings.usesReadOnlyMode().toString());
	}

	@Test
	public void testMaxClearTextFilenameLength(){
		VaultSettings settings = new VaultSettings("test_id");
		IntegerProperty maxCleartextFilenameLength = new SimpleIntegerProperty(-1);
		Assertions.assertEquals(maxCleartextFilenameLength.toString(), settings.maxCleartextFilenameLength().toString());
	}

	@Test
	public void testWinDriveLetter(){
		VaultSettings settings = new VaultSettings("test_id");
		settings.winDriveLetter();
		Assertions.assertEquals(Optional.empty(), settings.getWinDriveLetter());
	}

	@Test
	public void testEquals(){
		VaultSettings settings = new VaultSettings("test_id");
		Assertions.assertEquals(true, settings.equals(settings));
		Assertions.assertEquals(false, settings.equals(null));
	}

	@Test
	public void testUnlockAfterStartup(){
		VaultSettings settings = new VaultSettings("test_id");
		Assertions.assertEquals(new SimpleBooleanProperty().toString(), settings.unlockAfterStartup().toString());
	}

	@Test
	public void testAutoLockIdle(){
		VaultSettings settings = new VaultSettings("test_id");
		IntegerProperty autoLockIdleSeconds = new SimpleIntegerProperty(30*60);
		Assertions.assertEquals(autoLockIdleSeconds.toString(), settings.autoLockIdleSeconds().toString());
	}

	// End of Structural Testing (Coverage)
}

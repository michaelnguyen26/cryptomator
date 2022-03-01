package org.cryptomator.common.keychain;


import org.cryptomator.integrations.keychain.KeychainAccessException;
import org.cryptomator.integrations.keychain.KeychainAccessProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;


public class KeychainManagerTest{


	//KeychainManager keychainManagerMock;

	@Test
	public void testStoreAndLoad() throws KeychainAccessException {
		KeychainManager keychainManager = new KeychainManager(new SimpleObjectProperty<>(new MapKeychainAccess()));
		keychainManager.storePassphrase("test", "asd");
		Assertions.assertArrayEquals("asd".toCharArray(), keychainManager.loadPassphrase("test"));
	}

	@Nested
	public static class WhenObservingProperties {

		@BeforeAll
		public static void startup() throws InterruptedException {
			CountDownLatch latch = new CountDownLatch(1);
			Platform.startup(latch::countDown);
			latch.await(5, TimeUnit.SECONDS);
		}

		@Test
		public void testPropertyChangesWhenStoringPassword() throws KeychainAccessException, InterruptedException {
			KeychainManager keychainManager = new KeychainManager(new SimpleObjectProperty<>(new MapKeychainAccess()));
			ReadOnlyBooleanProperty property = keychainManager.getPassphraseStoredProperty("test");
			Assertions.assertEquals(false, property.get());

			keychainManager.storePassphrase("test", "bar");

			AtomicBoolean result = new AtomicBoolean(false);
			CountDownLatch latch = new CountDownLatch(1);
			Platform.runLater(() -> {
				result.set(property.get());
				latch.countDown();
			});
			latch.await(1, TimeUnit.SECONDS);
			Assertions.assertEquals(true, result.get());
		}
	}
	// New Test Cases for Structural (Coverage) - Section 3

	@Test
	public void testCreateStoredPassphraseProperty() throws KeychainAccessException {
		KeychainManager keychainManager = new KeychainManager(new SimpleObjectProperty<>(new MapKeychainAccess()));
		keychainManager.storePassphrase("irvine", "asd");

		// delete the added passphrase
		keychainManager.deletePassphrase("irvine");
		keychainManager.displayName();

		// check if passphrase is stored now
		Assertions.assertEquals(false, keychainManager.isPassphraseStored("test"));
	}

	@Test
	public void testChangePassphrase() throws KeychainAccessException {
		KeychainManager keychainManager = new KeychainManager(new SimpleObjectProperty<>(new MapKeychainAccess()));
		keychainManager.storePassphrase("irvineMSWE", "remove");

		// change the added passphrase
		keychainManager.changePassphrase("irvineMSWE", "test_phrase");
		keychainManager.changePassphrase("irvineMSWE", keychainManager.displayName(), "test_phrase");

		// check if passphrase is still stored (and changed)
		Assertions.assertEquals(true, keychainManager.isPassphraseStored("irvineMSWE"));
	}

	@Test
	public void testStorePassphraseWithMockito() throws KeychainAccessException {
		KeychainManager keychainManagerMock;
		keychainManagerMock = mock(KeychainManager.class);
		keychainManagerMock.storePassphrase("irvineMSWE", "create");

		when(keychainManagerMock.isPassphraseStored("irvineMSWE")).thenReturn(true);

		Assertions.assertEquals(true, keychainManagerMock.isPassphraseStored("irvineMSWE"));

		InOrder inOrder = inOrder(keychainManagerMock);
		inOrder.verify(keychainManagerMock).storePassphrase(anyString(), anyString());
		inOrder.verify(keychainManagerMock).isPassphraseStored(anyString());

	}


	@Test
	public void testIsSupportAndIsLock() throws KeychainAccessException {
		KeychainManager keychainManager = new KeychainManager(new SimpleObjectProperty<>(new MapKeychainAccess()));

		Assertions.assertEquals(false, keychainManager.isLocked());
		Assertions.assertEquals(true, keychainManager.isSupported());
	}

	// End of Structural Testing (Coverage)



	// Section 5: Testable Design

	@Test
	public void shouldReturnPasswordPropertyPublic() throws KeychainAccessException{
		KeychainManager keychainManager = new KeychainManager(new SimpleObjectProperty<>(new MapKeychainAccess()));
		keychainManager.storePassphrase("MSWE", "test");

		// This is Test Case 1: MSWE is stored --> true
		BooleanProperty bool = new SimpleBooleanProperty(true);
		Assertions.assertEquals(bool.toString(), keychainManager.createStoredPassphrasePropertyPublic("MSWE").toString());

		// This is Test Case 2: Irvine is not stored --> false
		BooleanProperty bool2 = new SimpleBooleanProperty(false);
		Assertions.assertEquals(bool2.toString(), keychainManager.createStoredPassphrasePropertyPublic("Irvine").toString());
	}

	// End of Section 5: Testable Design
}
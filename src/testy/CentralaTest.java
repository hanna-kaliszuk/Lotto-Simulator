package testy;

import centrala.Centrala;
import finanse.Kwota;
import kolektura.Kolektura;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class CentralaTest {

    @BeforeEach
    void setUp() {
        // Reset the singleton instance before each test
        resetCentralaInstance();

        // Initialize Centrala with some initial funds
        Centrala.inicjalizujCentralę(new Kwota(1_000_000, 0));
    }

    @Test
    @DisplayName("Test zarejestrujKolekturę - should increment counter")
    void testZarejestrujKolekture() throws Exception {
        // Given
        Centrala centrala = Centrala.getInstancja();

        // Get initial counter value using reflection (since field is private)
        Field nrKolekturyField = Centrala.class.getDeclaredField("nrOstatniejZarejestrowanejKolektury");
        nrKolekturyField.setAccessible(true);
        int initialCounter = (int) nrKolekturyField.get(null); // static field

        // When
        Kolektura kolektura = new Kolektura(1);

        // Then
        int finalCounter = (int) nrKolekturyField.get(null);
        assertEquals(initialCounter + 1, finalCounter,
                "Counter should be incremented by 1 after registering kolektura");
    }

    @Test
    @DisplayName("Test zarejestrujKolekturę - should handle multiple registrations")
    void testMultipleRegistrations() throws Exception {
        // Given
        Centrala centrala = Centrala.getInstancja();

        Field nrKolekturyField = Centrala.class.getDeclaredField("nrOstatniejZarejestrowanejKolektury");
        nrKolekturyField.setAccessible(true);
        int initialCounter = (int) nrKolekturyField.get(null);

        // When
        Kolektura kolektura1 = new Kolektura(1);
        Kolektura kolektura2 = new Kolektura(2);
        Kolektura kolektura3 = new Kolektura(3);

        // Then
        int finalCounter = (int) nrKolekturyField.get(null);
        assertEquals(initialCounter + 3, finalCounter,
                "Counter should be incremented by 3 after registering 3 kolekturas");
    }

    @Test
    @DisplayName("Test zarejestrujKolekturę - direct method call")
    void testDirectMethodCall() throws Exception {
        // Given
        Centrala centrala = Centrala.getInstancja();
        Kolektura kolektura = new Kolektura(1);

        Field nrKolekturyField = Centrala.class.getDeclaredField("nrOstatniejZarejestrowanejKolektury");
        nrKolekturyField.setAccessible(true);
        int counterAfterConstructor = (int) nrKolekturyField.get(null);

        // When - direct method call
        centrala.zarejestrujKolekturę(kolektura);

        // Then
        int finalCounter = (int) nrKolekturyField.get(null);
        assertEquals(counterAfterConstructor + 1, finalCounter,
                "Counter should be incremented by 1 after direct method call");
    }

    @Test
    @DisplayName("Test zarejestrujKolekturę - should handle null parameter")
    void testWithNullParameter() throws Exception {
        // Given
        Centrala centrala = Centrala.getInstancja();

        Field nrKolekturyField = Centrala.class.getDeclaredField("nrOstatniejZarejestrowanejKolektury");
        nrKolekturyField.setAccessible(true);
        int initialCounter = (int) nrKolekturyField.get(null);

        // When & Then
        assertDoesNotThrow(() -> centrala.zarejestrujKolekturę(null),
                "Method should not throw exception when called with null");

        // Counter should still be incremented
        int finalCounter = (int) nrKolekturyField.get(null);
        assertEquals(initialCounter + 1, finalCounter,
                "Counter should still be incremented even with null parameter");
    }

    @Test
    @DisplayName("Test counter starts at zero after reset")
    void testCounterInitialValue() throws Exception {
        // Given - fresh reset
        resetCentralaInstance();
        Centrala.inicjalizujCentralę(new Kwota(1_000_000, 0));

        Field nrKolekturyField = Centrala.class.getDeclaredField("nrOstatniejZarejestrowanejKolektury");
        nrKolekturyField.setAccessible(true);

        // Then
        int counter = (int) nrKolekturyField.get(null);
        assertEquals(0, counter, "Counter should start at 0 after reset");
    }

    /**
     * Reset the singleton instance using reflection for testing purposes
     */
    private void resetCentralaInstance() {
        try {
            Field instanceField = Centrala.class.getDeclaredField("instancja");
            instanceField.setAccessible(true);
            instanceField.set(null, null);

            Field counterField = Centrala.class.getDeclaredField("nrOstatniejZarejestrowanejKolektury");
            counterField.setAccessible(true);
            counterField.set(null, 0);
        } catch (Exception e) {
            throw new RuntimeException("Failed to reset Centrala instance", e);
        }
    }
}
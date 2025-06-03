package testy;

import centrala.Centrala;
import finanse.Kwota;
import kolektura.Kolektura;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestZarejestrujKolekture {

    @BeforeEach
    void setUp() {
        Centrala.resetInstancji();
        Centrala.inicjalizujCentralę(new Kwota(1_000_000, 0));
    }

    @Test
    void testSingleKolekturaRegistration() throws Exception {
        // Given
        Centrala centrala = Centrala.getInstancja();

        Field counterField = Centrala.class.getDeclaredField("nrOstatniejZarejestrowanejKolektury");
        counterField.setAccessible(true);
        int initialCounter = (int) counterField.get(null);

        Field listField = Centrala.class.getDeclaredField("listaKolektur");
        listField.setAccessible(true);
        List<Kolektura> lista = (List<Kolektura>) listField.get(centrala);
        int initialSize = lista.size();

        // When
        Kolektura kolektura = new Kolektura();

        // Then
        int finalCounter = (int) counterField.get(null);
        int finalSize = lista.size();

        assertEquals(initialCounter + 1, finalCounter, "Counter should increment by 1");
        assertEquals(initialSize + 1, finalSize, "List should contain 1 more kolektura");
        assertTrue(lista.contains(kolektura), "List should contain the registered kolektura");
    }

    @Test
    void testMultipleKolekturaRegistrations() throws Exception {
        // Given
        Centrala centrala = Centrala.getInstancja();
    }
}
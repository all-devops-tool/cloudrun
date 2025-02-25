package com.cybage.winepark.domain;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import static org.junit.jupiter.api.Assertions.*;

class WineTest {

    @InjectMocks
    private Wine wine;



    @Test
    void testNoArgsConstructor() {
        Wine newWine = new Wine();
        assertNotNull(newWine);
    }

    @Test
    void testAllArgsConstructor() {
        Wine newWine = new Wine(1, 10, "Red Wine", "Merlot", 25.99);
        assertNotNull(newWine);
        assertEquals(1, newWine.getWineId());
        assertEquals(10, newWine.getQuantity());
        assertEquals("Red Wine", newWine.getCategory());
        assertEquals("Merlot", newWine.getName());
        assertEquals(25.99, newWine.getPrice(), 0.001);
    }


}

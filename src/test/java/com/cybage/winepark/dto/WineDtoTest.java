package com.cybage.winepark.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WineDtoTest {

    @Test
    public void testGettersAndSetters() {
        // Arrange
        WineDto wineDto = new WineDto();

        // Act
        wineDto.setWineId(1);
        wineDto.setQuantity(5);
        wineDto.setCategory("Red Wine");
        wineDto.setName("Merlot");
        wineDto.setPrice(25.99);

        // Assert
        assertEquals(1, wineDto.getWineId());
        assertEquals(5, wineDto.getQuantity());
        assertEquals("Red Wine", wineDto.getCategory());
        assertEquals("Merlot", wineDto.getName());
        assertEquals(25.99, wineDto.getPrice());
    }

    
    @Test
    public void testAllArgsConstructor() {
        // Arrange
        WineDto wineDto = new WineDto(1, 5, "Red Wine", "Merlot", 25.99);

        // Assert
        assertEquals(1, wineDto.getWineId());
        assertEquals(5, wineDto.getQuantity());
        assertEquals("Red Wine", wineDto.getCategory());
        assertEquals("Merlot", wineDto.getName());
        assertEquals(25.99, wineDto.getPrice());
    }

    @Test
    public void testNoArgsConstructor() {
        // Arrange
        WineDto wineDto = new WineDto();

        // Assert
        assertNull(wineDto.getWineId());
        assertNull(wineDto.getQuantity());
        assertNull(wineDto.getCategory());
        assertNull(wineDto.getName());
        assertNull(wineDto.getPrice());
    }

    @Test
    public void testToString() {
        // Arrange
        WineDto wineDto = new WineDto(1, 5, "Red Wine", "Merlot", 25.99);

        // Assert
        String expectedToString = "WineDto(wineId=1, quantity=5, category=Red Wine, name=Merlot, price=25.99)";
        assertEquals(expectedToString, wineDto.toString());
    }
}

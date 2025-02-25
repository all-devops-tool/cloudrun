package com.cybage.winepark.service;

import com.cybage.winepark.domain.Wine;
import com.cybage.winepark.repository.WineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WineServiceTest {

    @Mock
    private WineRepository wineRepositoryMock;

    @InjectMocks
    private WineServiceImpl wineService;

    @BeforeEach
    public void setUp() {
        // If you have any setup logic, you can put it here
    }

    @Test
    public void testGetAllWines() {
        // Arrange
        List<Wine> expectedWines = new ArrayList<>();
        when(wineRepositoryMock.findAll()).thenReturn(expectedWines);

        // Act
        List<Wine> result = wineService.getAllWines();

        // Assert
        assertEquals(expectedWines, result);
    }

    @Test
    public void testAddWine() {
        // Arrange
        Wine wineToAdd = new Wine();

        // Act
        wineService.addWine(wineToAdd);

        // Assert
        verify(wineRepositoryMock, times(1)).save(wineToAdd);
    }

    @Test
    public void testUpdateWine() {
        // Arrange
        Wine existingWine = new Wine();
        when(wineRepositoryMock.save(any(Wine.class))).thenReturn(existingWine);

        // Act
        wineService.updateWine(existingWine);

        // Assert
        verify(wineRepositoryMock, times(1)).save(existingWine);
    }

    @Test
    public void testDeleteWine() {
        // Arrange
        Integer wineIdToDelete = 1;

        // Act
        wineService.deleteWine(wineIdToDelete);

        // Assert
        verify(wineRepositoryMock, times(1)).deleteById(wineIdToDelete);
    }
}

package com.cybage.winepark.service;

import com.cybage.winepark.domain.Wine;
import com.cybage.winepark.repository.WineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import com.cybage.winepark.dto.WineDto;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class WineServiceImplTest {

    @Mock
    private WineRepository wineRepository;

    @InjectMocks
    private WineServiceImpl wineService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllWines_shouldReturnListOfWines() {
        // Arrange
        List<Wine> expectedWines = Arrays.asList(new Wine(), new Wine());
        when(wineRepository.findAll()).thenReturn(expectedWines);

        // Act
        List<Wine> result = wineService.getAllWines();

        // Assert
        assertEquals(expectedWines, result);
    }

    @Test
    void getWineById_shouldReturnWine() {
        // Arrange
        Integer wineId = 1;
        Wine expectedWine = new Wine();
        when(wineRepository.findByWineId(wineId)).thenReturn(expectedWine);

        // Act
        Wine result = wineService.getWineById(wineId);

        // Assert
        assertEquals(expectedWine, result);
    }

    @Test
    void addWine_shouldCallRepositorySave() {
        // Arrange
        Wine wineToAdd = new Wine();

        // Act
        wineService.addWine(wineToAdd);

        // Assert
        verify(wineRepository, times(1)).save(wineToAdd);
    }

    @Test
    void updateWine_shouldCallRepositorySave() {
        // Arrange
        Wine wineToUpdate = new Wine();

        // Act
        wineService.updateWine(wineToUpdate);

        // Assert
        verify(wineRepository, times(1)).save(wineToUpdate);
    }

    @Test
    void deleteWine_shouldCallRepositoryDeleteById() {
        // Arrange
        Integer wineId = 1;

        // Act
        wineService.deleteWine(wineId);

        // Assert
        verify(wineRepository, times(1)).deleteById(wineId);
    }

    @Test
    void wineDtoToWine_shouldConvertDtoToEntity() {
        // Arrange
        WineDto wineDto = new WineDto();
        wineDto.setName("Test Wine");

        // Act
        Wine result = wineService.wineDtoToWine(wineDto);

        // Assert
        assertEquals(wineDto.getName(), result.getName());
    }

    @Test
    void wineToWineDto_shouldConvertEntityToDto() {
        // Arrange
        Wine wine = new Wine();
        wine.setName("Test Wine");

        // Act
        WineDto result = wineService.wineToWineDto(wine);

        // Assert
        assertEquals(wine.getName(), result.getName());
    }

    @Test
    void getOperatingSystemInfo_shouldReturnHostInfo() throws UnknownHostException {
        // Arrange
        String expectedOsInfo = "Host OS: " + System.getProperty("os.name")
                + ", Host Name: " + InetAddress.getLocalHost().getHostName()
                + ", Host IP: " + InetAddress.getLocalHost().getHostAddress();

        // Act
        String result = wineService.getOperatingSystemInfo();

        // Assert
        assertEquals(expectedOsInfo, result);
    }
}

package org.example.project_5_safetynet.Services;

import org.example.project_5_safetynet.DAO.DataDAO;
import org.example.project_5_safetynet.Models.FireStation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class FireStationServiceTest {


    @BeforeAll
    public static void setUp() throws IOException {
        String dataPath = "src/test/resources/data.json";
        Files.copy(Paths.get("src/test/resources/data_source.json"),Paths.get(dataPath), StandardCopyOption.REPLACE_EXISTING);
        DataDAO.initWithFilePath(dataPath);
    }

    @Test
    void getAllPersonsCoveredByStationTest() {
        Map<String, Object> result = FireStationService.getAllPersonsCoveredByStation("128");
        assertNotNull(result);
        assertThat(result.get("adults count")).isEqualTo(1);
        assertThat(result.get("children count")).isEqualTo(1);
        assertNotNull(result.get("persons"));
    }

    @Test
    void getAllPhoneNumbersFromStationTest() {
        String[] result = FireStationService.getAllPhoneNumbersFromStation("20");
        String[] expectedResult = {"111-000-1111","222-000-2222"};
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void getAllHouseholdsFromStationsList() {
        List<Map<String, Object>> result = FireStationService.getAllHouseholdsFromStationsList("1,2");
        assertNotNull(result);
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.toString()).contains("644 Gershwin Cir");
        assertThat(result.toString()).contains("908 73rd St");
        assertThat(result.toString()).contains("947 E. Rose Dr");
        assertThat(result.toString()).contains("29 15th St");
        assertThat(result.toString()).contains("892 Downing Ct");
        assertThat(result.toString()).contains("951 LoneTree Rd");
    }

    @Test
    void createNewFireStationTest() throws IOException {
        FireStation newFireStation = new FireStation();
        newFireStation.setStation("911");
        newFireStation.setAddress("2001 Washington View");
        FireStationService.createNewFireStation(newFireStation);

        List<FireStation> results = DataDAO.getFireStationsFromStationNumber("911");

        assertNotNull(results);
        assertThat(results.getFirst().getAddress()).isEqualTo("2001 Washington View");
    }

    @Test
    void updateFireStationTest() throws IOException {
        String modifiedAddress = "0 modified Rd";
        String modifiedStationNumber = "711";
        FireStation modifiedFireStation = DataDAO.getFireStationFromAddress("951 LoneTree Rd");
        modifiedFireStation.setAddress(modifiedAddress);
        modifiedFireStation.setStation(modifiedStationNumber);
        FireStationService.updateFireStation(modifiedFireStation);

        FireStation result = DataDAO.getFireStationFromAddress("0 modified Rd");

        assertNotNull(result);
        assertThat(result.getStation()).isEqualTo("711");
    }

    @Test
    void deleteFireStationTest() throws IOException {
        FireStation targetedFireStation = new FireStation();
        targetedFireStation.setAddress("748 Townings Dr");
        targetedFireStation.setStation("3");
        boolean checker = FireStationService.deleteFireStation(targetedFireStation);

        FireStation result = DataDAO.getFireStation(targetedFireStation);
        assertThat(result).isNull();
        assertThat(checker).isEqualTo(true);
    }
}

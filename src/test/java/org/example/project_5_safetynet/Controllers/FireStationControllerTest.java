package org.example.project_5_safetynet.Controllers;

import org.example.project_5_safetynet.DAO.DataDAO;
import org.example.project_5_safetynet.Models.FireStation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class FireStationControllerTest {

    private final FireStationController fireStationController = new FireStationController();

    @BeforeAll
    public static void setUp() throws IOException {
        String dataPath = "src/test/resources/data.json";
        Files.copy(Paths.get("src/test/resources/data_source.json"),Paths.get(dataPath), StandardCopyOption.REPLACE_EXISTING);
        DataDAO.initWithFilePath(dataPath);
    }

    @Test
    void stationNumberTest() {
        ResponseEntity<Map<String, Object>> response = fireStationController.stationNumber("128");

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(Objects.requireNonNull(response.getBody()).get("children count")).isEqualTo(1);
        assertThat(response.getBody().get("adults count")).isEqualTo(1);
        assertThat(response.getBody().get("persons")).isNotNull();
    }

    @Test
    void fireStationTest() {
        ResponseEntity<String[]> response = fireStationController.firestation("1");

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(new String[]{"841-874-6512", "841-874-8547", "841-874-7462", "841-874-7784"});
    }

    @Test
    void stationsTest() {
        ResponseEntity<List<Map<String, Object>>> response = fireStationController.stations("128,129");

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody().size()).isEqualTo(2);
        assertThat(response.getBody().toString()).contains("1 addrPack");
        assertThat(response.getBody().toString()).contains("2 addrPack");
        assertThat(response.getBody().toString()).contains("3 addrPack");

    }

    @Test
    void Post_FireStationTest() throws IOException {
        FireStation newFiresStation = new FireStation();
        newFiresStation.setStation("64");
        newFiresStation.setAddress("Radeon Rd.");

        ResponseEntity<String> response = fireStationController.Post_FireStation(newFiresStation);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("New FireStation Mapping Successfully created");
    }

    @Test
    void Put_FireStationTest() throws IOException {
        FireStation newFiresStation = new FireStation();
        newFiresStation.setStation("91");
        newFiresStation.setAddress("Ctrl Modif Rd.");

        ResponseEntity<String> response = fireStationController.Put_FireStation(newFiresStation);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("FireStation Mapping Successfully updated");
    }

    @Test
    void Delete_FireStationTest() throws IOException {
        FireStation newFiresStation = new FireStation();
        newFiresStation.setStation("92");
        newFiresStation.setAddress("Intel Rd.");

        ResponseEntity<String> response = fireStationController.Delete_FireStation(newFiresStation);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("FireStation Mapping Successfully Deleted");
    }

}

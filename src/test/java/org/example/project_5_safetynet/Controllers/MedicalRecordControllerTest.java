package org.example.project_5_safetynet.Controllers;

import org.example.project_5_safetynet.DAO.DataDAO;
import org.example.project_5_safetynet.Models.MedicalRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordControllerTest {

    private final MedicalRecordController medicalRecordController = new MedicalRecordController();

    @BeforeAll
    public static void setUp() throws IOException {
        String dataPath = "src/test/resources/data.json";
        Files.copy(Paths.get("src/test/resources/data_source.json"),Paths.get(dataPath), StandardCopyOption.REPLACE_EXISTING);
        DataDAO.initWithFilePath(dataPath);
    }

    @Test
    void Post_MedicalRecordTest() throws IOException {
        MedicalRecord newMedicalRecord = new MedicalRecord();
        newMedicalRecord.setMedications(Arrays.asList("medicA:1mg", "medicB:1mg"));
        newMedicalRecord.setAllergies(Arrays.asList("allergyA", "allergyB", "allergyC"));
        newMedicalRecord.setBirthdate("01/02/2003");
        newMedicalRecord.setFirstName("Controller");
        newMedicalRecord.setLastName("TestPost");

        ResponseEntity<String> response = medicalRecordController.Post_MedicalRecord(newMedicalRecord);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("New MedicalRecord Successfully created");
    }

    @Test
    void Put_MedicalRecordTest() throws IOException {
        MedicalRecord medicalRecord = DataDAO.getMedicalRecordWithFullName("Modifier CtrTester");
        medicalRecord.setBirthdate("01/11/1991");
        medicalRecord.setMedications(Arrays.asList("new:1mg", "new:2mg"));
        medicalRecord.setAllergies(List.of("allergy1"));

        ResponseEntity<String> response = medicalRecordController.Put_MedicalRecord(medicalRecord);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("MedicalRecord Successfully updated");
    }

    @Test
    void Delete_MedicalRecordTest() throws IOException {
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("Eraser");
        medicalRecord.setLastName("CtrTester");

        ResponseEntity<String> response = medicalRecordController.Delete_MedicalRecord(medicalRecord);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("MedicalRecord Successfully Deleted");
    }
}

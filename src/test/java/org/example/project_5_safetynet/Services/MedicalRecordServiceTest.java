package org.example.project_5_safetynet.Services;

import org.example.project_5_safetynet.DAO.DataDAO;
import org.example.project_5_safetynet.Models.MedicalRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordServiceTest {

    @BeforeAll
    public static void setUp() throws IOException {
        String dataPath = "src/test/resources/data.json";
        Files.copy(Paths.get("src/test/resources/data_source.json"),Paths.get(dataPath), StandardCopyOption.REPLACE_EXISTING);
        DataDAO.initWithFilePath(dataPath);
    }

    @Test
    void createNewMedicalRecordTest() throws IOException {
        MedicalRecord newMedicalRecord = new MedicalRecord();
        newMedicalRecord.setMedications(Arrays.asList("medicA:1mg", "medicB:1mg"));
        newMedicalRecord.setAllergies(Arrays.asList("allergyA", "allergyB", "allergyC"));
        newMedicalRecord.setBirthdate("01/02/2001");
        newMedicalRecord.setFirstName("Kate");
        newMedicalRecord.setLastName("Swift");

        MedicalRecordService.createNewMedicalRecord(newMedicalRecord);

        MedicalRecord result = DataDAO.getMedicalRecordWithFullName("Kate Swift");

        assertNotNull(result);
        assertThat(result.getFirstName()).isEqualTo("Kate");
        assertThat(result.getLastName()).isEqualTo("Swift");
        assertThat(result.getBirthdate()).isEqualTo("01/02/2001");
        assertThat(result.getMedications().size()).isEqualTo(2);
        assertThat(result.getAllergies().size()).isEqualTo(3);
    }

    @Test
    void updateMedicalRecordTest() throws IOException {
        MedicalRecord medicalRecord = DataDAO.getMedicalRecordWithFullName("Shawna Stelzer");
        medicalRecord.setBirthdate("01/11/1991");
        medicalRecord.setMedications(Arrays.asList("new:1mg", "new:2mg"));
        medicalRecord.setAllergies(List.of("allergy1"));

        MedicalRecordService.updateMedicalRecord(medicalRecord);

        MedicalRecord result = DataDAO.getMedicalRecordWithFullName("Shawna Stelzer");
        assertThat(result.getBirthdate()).isEqualTo("01/11/1991");
        assertThat(result.getMedications().get(0)).isEqualTo("new:1mg");
        assertThat(result.getMedications().get(1)).isEqualTo("new:2mg");
        assertThat(result.getAllergies().getFirst()).isEqualTo("allergy1");
    }

    @Test
    void deleteMedicalRecordTest() throws IOException {
        MedicalRecord medicalRecord = DataDAO.getMedicalRecordWithFullName("Shawna Stelzer");
        MedicalRecordService.deleteMedicalRecord(medicalRecord);

        MedicalRecord result = DataDAO.getMedicalRecordWithFullName("Shawna Stelzer");

        assertThat(result).isNull();
    }
}

package org.example.project_5_safetynet.DAO;

import org.example.project_5_safetynet.Models.FireStation;
import org.example.project_5_safetynet.Models.MedicalRecord;
import org.example.project_5_safetynet.Models.Person;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DataDAOTest {
    @BeforeAll
    public static void setUp() throws IOException {
        String dataPath = "src/test/resources/data.json";
        Files.copy(Paths.get("src/test/resources/data_source.json"),Paths.get(dataPath),StandardCopyOption.REPLACE_EXISTING);
        DataDAO.initWithFilePath(dataPath);
    }

    @Test
    void readDataFromDiskTest() throws IOException {
        assertNotNull(DataDAO.getPersons());
        assertNotNull(DataDAO.getFireStations());
        assertNotNull(DataDAO.getMedicalRecords());
    }

    @Test
    void getPersonsTest() {
        assertNotNull(DataDAO.getPersons());
    }
    @Test
    void getFireStationsTest() {
        assertNotNull(DataDAO.getFireStations());
    }
    @Test
    void getMedicalRecordsTest() {
        assertNotNull(DataDAO.getMedicalRecords());
    }

    @Test
    void getFireStationsFromStationNumberTest() {
        String stationNumber = "20";
        List<FireStation> result = DataDAO.getFireStationsFromStationNumber(stationNumber);
        int count = result.size();

        assertEquals(2, count);
        for (FireStation fireStation:result) {
            assertThat(fireStation.getStation()).isEqualTo(stationNumber);
        }
    }

    @Test
    void getFireStationFromAddressTest() {
        String address = "1509 Culver St";
        FireStation result = DataDAO.getFireStationFromAddress(address);

        assertNotNull(result);
        assertThat(result.getStation()).isEqualTo("3");
        assertThat(result.getAddress()).isEqualTo("1509 Culver St");
    }

    @Test
    void getFireStationTest() {
        FireStation fireStation = new FireStation();
        fireStation.setAddress("1509 Culver St");
        fireStation.setStation("3");
        FireStation result = DataDAO.getFireStation(fireStation);

        assertNotNull(result);
        assertThat(result.getStation()).isEqualTo("3");
        assertThat(result.getAddress()).isEqualTo("1509 Culver St");
    }

    @Test
    void getAllAddressesFromStationNumberTest(){
        List<String> result = DataDAO.getAllAddressesFromStationNumber("1");
        int count = result.size();

        assertEquals(3, count);
        assertThat(result).contains("908 73rd St");
        assertThat(result).contains("947 E. Rose Dr");
        assertThat(result).contains("644 Gershwin Cir");
    }

    @Test 
    void getAllPersonsFromAddressTest() {
        String address = "112 Steppes Pl";
        List<Person> result = DataDAO.getAllPersonsFromAddress(address);

        int count = result.size();
        assertEquals(3, count);
        assertThat(result.get(0).getAddress()).isEqualTo(address);
        assertThat(result.get(1).getAddress()).isEqualTo(address);
        assertThat(result.get(2).getAddress()).isEqualTo(address);
    }

    @Test
    void getAllPersonsFromStationNumberTest() {
        String stationNumber = "20";
        List<Person> result = DataDAO.getAllPersonsFromStationNumber(stationNumber);
        int count = result.size();
        assertEquals(2, count);
    }

    @Test
    void getPersonsWithLastNameTest() {
        String lastName = "Peters";
        List<Person> result = DataDAO.getPersonsWithLastName(lastName);

        int count = result.size();
        assertEquals(2, count);
        for (Person person: result) {
            assertThat(person.getLastName()).isEqualTo(lastName);
        }
    }

    @Test
    void getPersonWithFullNameTest() {
        String fullName = "Jamie Peters";
        Person result = DataDAO.getPersonWithFullName(fullName);

        assertNotNull(result);
        assertThat(result.getFullName()).isEqualTo(fullName);
        assertThat(result.getEmail()).isEqualTo("jpeter@email.com");
    }

    @Test
    void getMedicalRecordForPerson() {
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Boyd");

        MedicalRecord result = DataDAO.getMedicalRecordForPerson(person);

        assertThat(result.getBirthdate()).isEqualTo("03/06/1984");
        assertThat(result.getFullName()).isEqualTo(person.getFullName());
        assertNotNull(result.getMedications());
        assertNotNull(result.getAllergies());
    }

    @Test
    void getMedicalRecordWithFullNameTest() {
        String fullName = "John Boyd";

        MedicalRecord result = DataDAO.getMedicalRecordWithFullName(fullName);

        assertThat(result.getBirthdate()).isEqualTo("03/06/1984");
        assertThat(result.getFullName()).isEqualTo(fullName);
        assertNotNull(result.getMedications());
        assertNotNull(result.getAllergies());
    }

    @Test
    void getAllChildrenFromAddressTest() {
        String address = "1509 Culver St";

        List<Person> result = DataDAO.getAllChildrenFromAddress(address);
        int count = result.size();

        assertEquals(2, count);
        for (Person person:result) {
            assertThat(DataDAO.getMedicalRecordForPerson(person).getAge()).isLessThanOrEqualTo(17);
        }

    }

    @Test
    void getAllAdultsFromAddressTest() {
        String address = "1509 Culver St";

        List<Person> result = DataDAO.getAllAdultsFromAddress(address);
        int count = result.size();

        assertEquals(3, count);
        for (Person person:result) {
            assertThat(DataDAO.getMedicalRecordForPerson(person).getAge()).isGreaterThanOrEqualTo(18);
        }
    }

    @Test
    void createNewPersonTest() throws IOException {
        String firstName = "Jane";
        String lastName = "Doe";
        Person newPerson = new Person();
        newPerson.setFirstName(firstName);
        newPerson.setLastName(lastName);
        newPerson.setAddress("2025 Moon base");
        newPerson.setCity("Abysses");
        DataDAO.createNewPerson(newPerson);

        Person result = DataDAO.getPersonWithFullName(firstName+' '+lastName);

        assertNotNull(result);
        assertThat(result.getFirstName()).isEqualTo(firstName);
        assertThat(result.getLastName()).isEqualTo(lastName);
    }

    @Test
    void updatePersonTest() throws IOException {
        String fullName = "John Boyd";
        String newEmail = "updated@Email.com";
        String newPhone = "010-010-0101";

        Person source = DataDAO.getPersonWithFullName(fullName);
        source.setEmail(newEmail);
        source.setPhone(newPhone);
        DataDAO.updatePerson(source);

        Person result = DataDAO.getPersonWithFullName(fullName);
        assertThat(result.getEmail()).isEqualTo(newEmail);
        assertThat(result.getPhone()).isEqualTo(newPhone);
    }

    @Test
    void deletePersonTest() throws IOException {
        String fullName = "Delete Test";
        Person person = DataDAO.getPersonWithFullName(fullName);

        DataDAO.deletePerson(person);

        Person result = DataDAO.getPersonWithFullName(fullName);
        assertThat(result).isNull();
    }

    @Test
    void createNewFireStationTest() throws IOException {
        String station = "6";
        String address = "32 bits st";
        FireStation newFireStation = new FireStation();
        newFireStation.setStation(station);
        newFireStation.setAddress(address);
        DataDAO.createNewFireStation(newFireStation);

        FireStation result = DataDAO.getFireStationFromAddress(address);

        assertNotNull(result);
        assertThat(result.getStation()).isEqualTo(station);
    }

    @Test
    void updateFireStationTest() throws IOException {
        String address = "112 Steppes Pl";
        String stationNewValue = "120";

        FireStation source = DataDAO.getFireStationFromAddress(address);
        source.setStation(stationNewValue);
        DataDAO.updateFireStation(source);

        FireStation result = DataDAO.getFireStationFromAddress(address);
        assertThat(result.getStation()).isEqualTo(stationNewValue);
    }

    @Test
    void deleteFireStationTest() throws IOException {
        String address = "1 Delete Test St";
        FireStation fireStation = DataDAO.getFireStationFromAddress(address);

        DataDAO.deleteFireStation(fireStation);

        FireStation result = DataDAO.getFireStationFromAddress(address);
        assertThat(result).isNull();
    }

    @Test
    void createNewMedicalRecordTest() throws IOException {
        String firstName = "Jane";
        String lastName = "Doe";
        String birthDate = "01/01/1972";
        List<String> allergies = new ArrayList<>(Arrays.asList("histamine", "latex"));
        List<String> medications = new ArrayList<>(Arrays.asList("Lisinopril: 100mg", "Atorvastatin: 50mg"));

        MedicalRecord newMedicalRecord = new MedicalRecord();
        newMedicalRecord.setFirstName(firstName);
        newMedicalRecord.setLastName(lastName);
        newMedicalRecord.setBirthdate(birthDate);
        newMedicalRecord.setMedications(medications);
        newMedicalRecord.setAllergies(allergies);
        DataDAO.createNewMedicalRecord(newMedicalRecord);

        MedicalRecord result = DataDAO.getMedicalRecordWithFullName("Jane Doe");

        assertNotNull(result);
        assertThat(result.getFirstName()).isEqualTo(firstName);
        assertThat(result.getLastName()).isEqualTo(lastName);
        assertThat(result.getBirthdate()).isEqualTo(birthDate);
        assertThat(result.getMedications()).isEqualTo(medications);
        assertThat(result.getAllergies()).isEqualTo(allergies);
    }

    @Test
    void updateMedicalRecordTest() throws IOException {
        String fullName = "Clive Ferguson";
        String newBirthDate = "05/05/1985";
        List<String> newAllergies = new ArrayList<>(List.of("peanut", "ibuprofen"));
        List<String> newMedications = new ArrayList<>(List.of("atorvastatin: 128mg"));

        MedicalRecord source = DataDAO.getMedicalRecordWithFullName(fullName);
        source.setBirthdate(newBirthDate);
        source.setMedications(newMedications);
        source.setAllergies(newAllergies);
        DataDAO.updateMedicalRecord(source);

        MedicalRecord result = DataDAO.getMedicalRecordWithFullName(fullName);
        assertThat(result.getBirthdate()).isEqualTo(newBirthDate);
        assertThat(result.getMedications()).isEqualTo(newMedications);
        assertThat(result.getAllergies()).isEqualTo(newAllergies);
    }

    @Test
    void deleteMedicalRecordTest() throws IOException {
        String fullName = "Delete Test";
        MedicalRecord medicalRecord = DataDAO.getMedicalRecordWithFullName(fullName);

        DataDAO.deleteMedicalRecord(medicalRecord);

        MedicalRecord result = DataDAO.getMedicalRecordWithFullName(fullName);
        assertThat(result).isNull();
    }


}
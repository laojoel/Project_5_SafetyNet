package org.example.project_5_safetynet.DAO;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.project_5_safetynet.Models.Data;
import org.example.project_5_safetynet.Models.FireStation;
import org.example.project_5_safetynet.Models.MedicalRecord;
import org.example.project_5_safetynet.Models.Person;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static org.example.project_5_safetynet.Project5SafetyNetApplication.logger;

public class DataDAO {
    static Data data;
    static String dataPath;

    public static void initWithFilePath(String filePath) throws IOException {
        if (dataPath==null){
            dataPath = filePath;
            data = new ObjectMapper().readValue(new File(dataPath), Data.class);
        }
        else {
            logger.info("# Critical | DataDAO | initWithFilePath | data json file not found");
        }
    }

    public static void writeDataToDisk() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.writeValue(new File(dataPath), data);
    }

    public static List<Person> getPersons() {
        return data.getPersons();
    }
    public static List<FireStation> getFireStations() {
        return data.getFirestations();
    }
    public static List<MedicalRecord> getMedicalRecords() {
        return data.getMedicalrecords();
    }

    //
    public static List<FireStation> getFireStationsFromStationNumber(String stationNumber) {
        return data.getFirestations().stream()
                .filter(fireStation -> fireStation.getStation().equals(stationNumber))
                .collect(Collectors.toList());
    }
    public static FireStation getFireStationFromAddress(String address) {
        return data.getFirestations().stream()
                .filter(fireStation -> (fireStation.getAddress()).equals(address))
                .findFirst().orElse(null);
    }
    public static FireStation getFireStation(FireStation lookup) {
        return data.getFirestations().stream()
                .filter(fireStation -> (fireStation.getAddress()).equals(lookup.getAddress()))
                .filter(fireStation -> (fireStation.getStation()).equals(lookup.getStation()))
                .findFirst().orElse(null);
    }
    public static List<String> getAllAddressesFromStationNumber(String stationNumber) {
        List<FireStation> addresses = getFireStationsFromStationNumber(stationNumber);
        return addresses.stream()
                .map(FireStation::getAddress)
                .collect(Collectors.toList());
    }
    public static List<Person> getAllPersonsFromAddress(String address) {
        return data.getPersons().stream()
                .filter(person -> person.getAddress().equals(address))
                .collect(Collectors.toList());
    }

    public static List<Person> getAllPersonsFromStationNumber(String stationNumber) {
        List<String> addresses = getAllAddressesFromStationNumber(stationNumber);
        return data.getPersons().stream()
                .filter(Person -> addresses.contains(Person.getAddress()))
                .collect(Collectors.toList());
    }

    public static List<Person> getPersonsWithLastName (String lastName) {
        return getPersons().stream()
                .filter(person -> person.lastName.equals(lastName))
                .collect(Collectors.toList());
    }
    public static Person getPersonWithFullName (String fullName) {
        return data.getPersons().stream()
                .filter(person -> person.getFullName().equals(fullName))
                .findFirst().orElse(null);
    }

    public static MedicalRecord getMedicalRecordForPerson(Person person) {
        return data.getMedicalrecords().stream()
                .filter(medicalRecord -> (medicalRecord.getFullName()).equals(person.getFullName()))
                .findFirst().orElse(null);
    }
    public static MedicalRecord getMedicalRecordWithFullName(String fullName) {
        return data.getMedicalrecords().stream()
                .filter(medicalRecord -> (medicalRecord.getFullName()).equals(fullName))
                .findFirst().orElse(null);
    }

    public static List<Person> getAllChildrenFromAddress(String address) {

        List<Person> persons = getAllPersonsFromAddress(address);
        List<Person> children = new ArrayList<>();
        MedicalRecord medicalRecord;
        for (Person person: persons) {
            medicalRecord = getMedicalRecordForPerson(person);
            if (medicalRecord!=null) {
                if (medicalRecord.getAge() < 18) {children.add(person);}
            }
        }
        return children;
    }
    public static List<Person> getAllAdultsFromAddress(String address) {
        List<Person> persons = getAllPersonsFromAddress(address);
        List<Person> adults = new ArrayList<>();
        MedicalRecord medicalRecord;
        for (Person person : persons) {
            medicalRecord = getMedicalRecordForPerson(person);
            if (medicalRecord != null) {
                if (getMedicalRecordForPerson(person).getAge() >= 18) {
                    adults.add(person);
                }
            }
        }
        return adults;
    }

    public static void createNewPerson(Person person) throws IOException {
        data.getPersons().add(person);
        writeDataToDisk();
        logger.info("DataDAO | createNewPerson | Person with full name " + person.getFullName() + " created");
    }
    public static boolean updatePerson(Person person) throws IOException {
        Person recordedPerson = getPersonWithFullName(person.getFullName());
        if (recordedPerson != null) {
            if (person.getPhone() != null) {recordedPerson.setPhone(person.getPhone());}
            if (person.getEmail() != null) {recordedPerson.setEmail(person.getEmail());}
            if (person.getCity() != null) {recordedPerson.setCity(person.getCity());}
            if (person.getAddress() != null) {recordedPerson.setAddress(person.getAddress());}
            if (person.getZip() != null) {recordedPerson.setZip(person.getZip());}
            writeDataToDisk();
            logger.info("DataDAO | updatePerson | Person with full name " + person.getFullName() + " updated");
            return true;
        }
        else {
            logger.error("DataDAO | updatePerson | Person with full name " + person.getFullName() + " not found");
            return false;
        }
    }

    public static boolean deletePerson(Person person) throws IOException {
        Person recordedPerson = getPersonWithFullName(person.getFullName());
        if (recordedPerson != null) {
            data.getPersons().remove(recordedPerson);
            writeDataToDisk();
            logger.info("DataDAO | deletePerson | Person with full name " + person.getFullName() + " deleted");
            return true;
        }
        else {
            logger.error("DataDAO | deletePerson | Person with full name " + person.getFullName() + " not found");
            return false;
        }
    }

    public static void createNewFireStation(FireStation fireStation) throws IOException {
        data.getFirestations().add(fireStation);
        writeDataToDisk();
    }
    public static boolean updateFireStation(FireStation fireStation) throws IOException {
        FireStation recordedFireStation = getFireStationFromAddress(fireStation.getAddress());
        if (recordedFireStation != null) {
            if (fireStation.getStation() != null) {recordedFireStation.setStation(fireStation.getStation());}
            writeDataToDisk();
            logger.info("DataDAO | updateFireStation | FireStation address " + fireStation.getAddress() + "updated");
            return true;
        }
        else {
            logger.error("DataDAO | updateFireStation | FireStation address " + fireStation.getAddress() + " not found");
            return false;
        }
    }
    public static boolean deleteFireStation(FireStation lookup) throws IOException {
        FireStation recordedFireStation = getFireStation(lookup);
        if (recordedFireStation != null) {
            data.getFirestations().removeIf(fireStation -> fireStation.address.equals(recordedFireStation.getAddress())
                    && fireStation.getStation().equals(recordedFireStation.getStation()));
            writeDataToDisk();
            logger.info("DataDAO | deleteFireStation | FireStation address " + lookup.getAddress() + " deleted");
            return true;
        }
        else {
            logger.error("DataDAO | deleteFireStation | FireStation address " + lookup.getAddress() + " not found");
            return false;
        }
    }

    public static void createNewMedicalRecord(MedicalRecord medicalRecord) throws IOException {
        data.getMedicalrecords().add(medicalRecord);
        writeDataToDisk();
    }
    public static boolean updateMedicalRecord(MedicalRecord medicalRecord) throws IOException {
        MedicalRecord recordedMedicalRecord = getMedicalRecordWithFullName(medicalRecord.getFullName());
        if (recordedMedicalRecord != null) {
            if (medicalRecord.getBirthdate() != null) {recordedMedicalRecord.setBirthdate(medicalRecord.getBirthdate());}
            if (medicalRecord.getMedications() != null) {recordedMedicalRecord.setMedications(medicalRecord.getMedications());}
            if (medicalRecord.getAllergies() != null) {recordedMedicalRecord.setAllergies(medicalRecord.getAllergies());}
            writeDataToDisk();
            logger.info("DataDAO | updateMedicalRecord | Full name " + medicalRecord.getFullName() + " Updated");
            return true;
        }
        else {
            logger.error("DataDAO | updateMedicalRecord | Full name " + medicalRecord.getFullName() + " not found");
            return false;
        }
    }
    public static boolean deleteMedicalRecord(MedicalRecord medicalRecord) throws IOException {
        MedicalRecord recordedMedicalRecord = getMedicalRecordWithFullName(medicalRecord.getFullName());
        if (recordedMedicalRecord != null) {
            data.getMedicalrecords().remove(recordedMedicalRecord);
            writeDataToDisk();
            logger.info("DataDAO | deleteMedicalRecord | Full name " + medicalRecord.getFullName() + " deleted");
            return true;
        }
        else {
            logger.error("DataDAO | deleteMedicalRecord | Full name " + medicalRecord.getFullName() + " not found");
            return false;
        }
    }

}

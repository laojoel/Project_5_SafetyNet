package org.example.project_5_safetynet.Services;

import org.example.project_5_safetynet.DAO.DataDAO;
import org.example.project_5_safetynet.Models.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PersonService {
    public static Map<String, Object> getChildrenAtAddress(String address) {
        Map<String, Object> map = new HashMap<>();
        map.put("children", DataDAO.getAllChildrenFromAddress(address).stream()
                .map(person -> new Child(person.getFirstName(), person.getLastName(), DataDAO.getMedicalRecordForPerson(person).getAge())));
        map.put("other residents", DataDAO.getAllAdultsFromAddress(address).stream()
                .map(Person::getFullName)
                .collect(Collectors.toList()));
        return map;
    }

    public static Map<String, Object> getPersonsMedicalsAndFireStationNumberAtAddress(String address) {
        List<Person> persons = DataDAO.getAllPersonsFromAddress(address);
        Map<String, Object> map = new HashMap<>();
        map.put("stationNumber",DataDAO.getFireStationFromAddress(address).station);
        map.put("persons", DataDAO.getAllPersonsFromAddress(address).stream()
                .map(person -> new PersonMedical(person.getFirstName(), person.getLastName(), person.getPhone(), DataDAO.getMedicalRecordForPerson(person).getAge()
                ,DataDAO.getMedicalRecordForPerson(person).getMedications(), DataDAO.getMedicalRecordForPerson(person).getAllergies())));
        return map;
    }

    public static List<PersonInfo> getPersonInformationFromLastName(String lastName) {
        return  DataDAO.getPersonsWithLastName(lastName).stream()
                .map(person -> new PersonInfo(person.getLastName(), person.getEmail(), person.getAddress(), DataDAO.getMedicalRecordForPerson(person).getAge()
                ,DataDAO.getMedicalRecordForPerson(person).getMedications(), DataDAO.getMedicalRecordForPerson(person).getAllergies()))
                .collect(Collectors.toList());
    }

    public static List<String> getCommunityEmails(String city) {
        return DataDAO.getPersons().stream()
                .filter(person -> person.getCity().equals(city))
                .map(Person::getEmail)
                .distinct()
                .collect(Collectors.toList());
    }


    public static void createNewPerson(Person person) throws IOException {
        DataDAO.createNewPerson(person);
    }
    public static boolean updatePerson(Person person) throws IOException {
        return DataDAO.updatePerson(person);
    }
    public static boolean deletePerson(Person person) throws IOException {
        return DataDAO.deletePerson(person);
    }

}

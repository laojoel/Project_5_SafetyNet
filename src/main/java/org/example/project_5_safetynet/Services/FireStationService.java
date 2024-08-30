package org.example.project_5_safetynet.Services;

import org.example.project_5_safetynet.DAO.DataDAO;
import org.example.project_5_safetynet.Models.FireStation;
import org.example.project_5_safetynet.Models.Person;
import org.example.project_5_safetynet.Models.PersonBasicInfo;
import org.example.project_5_safetynet.Models.PersonMedical;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

@Service
public class FireStationService {
    public static Map<String, Object> getAllPersonsCoveredByStation(String stationNumber) {
        List<Person> persons =  DataDAO.getAllPersonsFromStationNumber(stationNumber);

        int adultCount=0, childrenCount=0;
        for (Person person : persons) {
            if (DataDAO.getMedicalRecordForPerson(person).getAge() >= 18) {adultCount++;}
            else {childrenCount++;}
        }

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("adults count", adultCount);
        map.put("children count", childrenCount);
        map.put("persons", persons.stream().map(person -> new PersonBasicInfo(person.getFirstName(), person.getLastName(), person.getAddress(), person.getPhone())));

        return map;
    }

    public static String[] getAllPhoneNumbersFromStation(String stationNumber) {
        return DataDAO.getAllPersonsFromStationNumber(stationNumber).stream()
                .map(Person::getPhone)
                .distinct()
                .toArray(String[]::new);
    }

    public static List<Map<String, Object>> getAllHouseholdsFromStationsList(String stationNumberList) {
        List<Map<String, Object>> fireStations = new ArrayList<>();

        String[] stationNumbers = stationNumberList.split(",");
        for (String stationNumber: stationNumbers) {
            Map<String, Object> map = new LinkedHashMap<>();
            fireStations.add(map);

            map.put("station", stationNumber);
            List<Map<String, Object>> listAddresses = new ArrayList<>();
            map.put("addresses", listAddresses);


            List<String> addresses = DataDAO.getAllAddressesFromStationNumber(stationNumber);
            for (String address: addresses) {
                Stream<PersonMedical> personMedicals = DataDAO.getAllPersonsFromAddress(address).stream()
                        .map(person -> new PersonMedical(person.getFirstName(), person.getLastName(), person.getPhone(), DataDAO.getMedicalRecordForPerson(person).getAge()
                                ,DataDAO.getMedicalRecordForPerson(person).getMedications(), DataDAO.getMedicalRecordForPerson(person).getAllergies()));

                Map<String, Object> mapAddress = new LinkedHashMap<>();
                mapAddress.put("address", address);
                mapAddress.put("persons", personMedicals);
                listAddresses.add(mapAddress);
            }
        }
        return fireStations;
    }

    public static void createNewFireStation(FireStation fireStation) throws IOException {
        DataDAO.createNewFireStation(fireStation);
    }
    public static boolean updateFireStation(FireStation fireStation) throws IOException {
        return DataDAO.updateFireStation(fireStation);
    }
    public static boolean deleteFireStation(FireStation fireStation) throws IOException {
        return DataDAO.deleteFireStation(fireStation);
    }
}

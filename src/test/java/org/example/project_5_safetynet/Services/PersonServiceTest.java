package org.example.project_5_safetynet.Services;

import org.example.project_5_safetynet.DAO.DataDAO;
import org.example.project_5_safetynet.Models.Child;
import org.example.project_5_safetynet.Models.Person;
import org.example.project_5_safetynet.Models.PersonInfo;
import org.example.project_5_safetynet.Models.PersonMedical;
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
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {
    @BeforeAll
    public static void setUp() throws IOException {
        String dataPath = "src/test/resources/data.json";
        Files.copy(Paths.get("src/test/resources/data_source.json"),Paths.get(dataPath), StandardCopyOption.REPLACE_EXISTING);
        DataDAO.initWithFilePath(dataPath);
    }

    @Test
    void getChildrenAtAddressTest() {
        Map<String, Object> result = PersonService.getChildrenAtAddress("947 E. Rose Dr");
        List<Child> children = ((Stream<Child>)result.get("children")).collect(Collectors.toList());
        List<String> adults = (List<String>) result.get("other residents");

        assertThat(children.get(0).getFirstName()).isEqualTo("Kendrik");
        assertThat(children.get(0).getLastName()).isEqualTo("Stelzer");
        assertThat(adults.get(0)).isEqualTo("Brian Stelzer");
    }

    @Test
    void getPersonsMedicalsAndFireStationNumberAtAddressTest() {
        Map<String,Object> result = PersonService.getPersonsMedicalsAndFireStationNumberAtAddress("908 73rd St");
        String stationNumber = (String)result.get("stationNumber");
        List<PersonMedical> personMedicals = ((Stream<PersonMedical>)result.get("persons")).toList();

        assertThat(stationNumber).isEqualTo("1");
        assertThat(personMedicals.get(0).getFirstName()).isEqualTo("Reginold");
        assertThat(personMedicals.get(0).getMedications()).isEqualTo(Arrays.asList("thradox:700mg"));
        assertThat(personMedicals.get(0).getAllergies()).isEqualTo(Arrays.asList("illisoxian"));
        assertThat(personMedicals.get(1).getFirstName()).isEqualTo("Jamie");
        assertThat(personMedicals.get(1).getMedications()).isEqualTo(Arrays.asList());
        assertThat(personMedicals.get(1).getAllergies()).isEqualTo(Arrays.asList());
    }

    @Test
    void getPersonInformationFromLastNameTest() {
        List<PersonInfo> result = PersonService.getPersonInformationFromLastName("Cooper");

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getLastName()).isEqualTo("Cooper");
        assertThat(result.get(0).getAddress()).isEqualTo("112 Steppes Pl");
        assertThat(result.get(0).getMedications()).isEqualTo(Arrays.asList("hydrapermazol:300mg","dodoxadin:30mg"));
        assertThat(result.get(0).getAllergies()).isEqualTo(Arrays.asList("shellfish"));

        assertThat(result.get(1).getLastName()).isEqualTo("Cooper");
        assertThat(result.get(1).getAddress()).isEqualTo("489 Manchester St");
        assertThat(result.get(1).getMedications()).isEqualTo(Arrays.asList());
        assertThat(result.get(1).getAllergies()).isEqualTo(Arrays.asList());
    }

    @Test
    void getCommunityEmailsTest() {
        List<String> result = PersonService.getCommunityEmails("Tatooine");

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0)).isEqualTo("jedi@starwars.net");
        assertThat(result.get(1)).isEqualTo("master@starwars.net");
    }

    @Test
    void createNewPersonTest() throws IOException {
        Person person = new Person();
        person.setFirstName("new");
        person.setLastName("Resident");
        person.setEmail("newComer@cool.net");
        person.setCity("bruceville");
        person.setAddress("Built Rd.");
        person.setPhone("111-222-3333");
        PersonService.createNewPerson(person);

        Person result = DataDAO.getPersonWithFullName("new Resident");

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("newComer@cool.net");
        assertThat(result.getAddress()).isEqualTo("Built Rd.");
        assertThat(result.getCity()).isEqualTo("bruceville");
        assertThat(result.getPhone()).isEqualTo("111-222-3333");
    }

    @Test
    void updatePersonTest() throws IOException {
        Person person = DataDAO.getPersonWithFullName("update Resident");
        person.setEmail("anotherEmail@super.com");
        person.setPhone("555-000-5555");
        PersonService.updatePerson(person);

        Person result = DataDAO.getPersonWithFullName("update Resident");

        assertThat(result.getPhone()).isEqualTo("555-000-5555");
        assertThat(result.getEmail()).isEqualTo("anotherEmail@super.com");
    }

    @Test
    void DeletePersonTest() throws IOException {
        Person person = DataDAO.getPersonWithFullName("delete Resident");
        PersonService.deletePerson(person);

        Person result = DataDAO.getPersonWithFullName(person.getFullName());

        assertThat(result).isNull();
    }
}

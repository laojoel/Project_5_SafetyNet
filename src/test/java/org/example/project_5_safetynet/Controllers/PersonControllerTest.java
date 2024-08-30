package org.example.project_5_safetynet.Controllers;

import org.example.project_5_safetynet.DAO.DataDAO;
import org.example.project_5_safetynet.Models.Person;
import org.example.project_5_safetynet.Services.PersonService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class PersonControllerTest {

    private final PersonController personController = new PersonController();

    @BeforeAll
    public static void setUp() throws IOException {
        String dataPath = "src/test/resources/data.json";
        Files.copy(Paths.get("src/test/resources/data_source.json"),Paths.get(dataPath), StandardCopyOption.REPLACE_EXISTING);
        DataDAO.initWithFilePath(dataPath);
    }

    @Test
    void Post_PersonTest() throws IOException {
        Person newPerson = new Person();
        newPerson.setFirstName("Create");
        newPerson.setLastName("CtrlSummon");
        newPerson.setEmail("newComer@ctrl.net");
        newPerson.setCity("Titan");
        newPerson.setAddress("Strong Rd.");
        newPerson.setPhone("123-222-3333");

        ResponseEntity<String> response = personController.Post_Person(newPerson);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("New Person: Create CtrlSummon Successfully created");
    }

    @Test
    void Put_PersonTest() throws IOException {
        Person person = DataDAO.getPersonWithFullName("Updated CtrlResident");
        person.setEmail("anotherEmail@super.com");
        person.setPhone("555-000-5555");
        PersonService.updatePerson(person);

        ResponseEntity<String> response = personController.Put_Person(person);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("Person: Updated CtrlResident Successfully updated");
    }

    @Test
    void Put_PersonNotFoundTest() throws IOException {
        Person person = new Person();
        person.setFirstName("Sega");
        person.setLastName("Genesis");
        PersonService.updatePerson(person);

        ResponseEntity<String> response = personController.Put_Person(person);

        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void Delete_PersonTest() throws IOException {
        Person person = DataDAO.getPersonWithFullName("Deleted CtrlResident");

        ResponseEntity<String> response = personController.Delete_Person(person);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("Person: Deleted CtrlResident Successfully Deleted");
    }

    @Test
    void Delete_PersonNotFoundTest() throws IOException {
        Person person = new Person();
        person.setFirstName("Sega");
        person.setLastName("Genesis");

        ResponseEntity<String> response = personController.Delete_Person(person);

        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }
}

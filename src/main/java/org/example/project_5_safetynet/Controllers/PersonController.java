package org.example.project_5_safetynet.Controllers;

import org.example.project_5_safetynet.Models.Person;
import org.example.project_5_safetynet.Models.PersonInfo;
import org.example.project_5_safetynet.Services.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.*;

@RestController
public class PersonController {

    @GetMapping("/childAlert")
    public ResponseEntity<Map<String, Object>> stationNumber(@RequestParam("address") String address) {
        return ResponseEntity.ok(PersonService.getChildrenAtAddress(address));
    }

    @GetMapping("/fire")
    public ResponseEntity<Map<String, Object>> fire(@RequestParam("address") String address) {
        return ResponseEntity.ok(PersonService.getPersonsMedicalsAndFireStationNumberAtAddress(address));
    }

    @GetMapping("/personInfoLastName")
    public ResponseEntity<List<PersonInfo>> personInfoLastName(@RequestParam("lastName") String lastName) {
        return ResponseEntity.ok(PersonService.getPersonInformationFromLastName(lastName));
    }

    @GetMapping("/communityEmail")
    public ResponseEntity<List<String>> communityEmail(@RequestParam("city") String city) {
        return ResponseEntity.ok(PersonService.getCommunityEmails(city));
    }



    @PostMapping("/person")
    public ResponseEntity<String> Post_Person(@RequestBody Person person) throws IOException {
        PersonService.createNewPerson(person);
        return ResponseEntity.ok("New Person: " + person.getFullName() + " Successfully created");
    }

    @PutMapping("/person")
    public ResponseEntity<String> Put_Person(@RequestBody Person person) throws IOException {
        if (PersonService.updatePerson(person)){return ResponseEntity.ok("Person: " + person.getFullName() + " Successfully updated");}
        else {return new ResponseEntity<>(HttpStatus.NOT_FOUND);}

    }

    @DeleteMapping("/person")
    public ResponseEntity<String> Delete_Person(@RequestBody Person person) throws IOException {
        if (PersonService.deletePerson(person)){return ResponseEntity.ok("Person: " + person.getFullName() + " Successfully Deleted");}
        else {return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
    }

}
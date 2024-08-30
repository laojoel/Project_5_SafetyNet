package org.example.project_5_safetynet.Controllers;

import org.example.project_5_safetynet.Models.FireStation;
import org.example.project_5_safetynet.Models.Person;
import org.example.project_5_safetynet.Services.FireStationService;
import org.example.project_5_safetynet.Services.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
public class FireStationController {

    @GetMapping("/firestation")
    public ResponseEntity<Map<String, Object>> stationNumber(@RequestParam("station_number") String station_number) {
        return ResponseEntity.ok(FireStationService.getAllPersonsCoveredByStation(station_number));
    }

    @GetMapping("/phoneAlert")
    public ResponseEntity<String[]> firestation(@RequestParam("firetation_number") String firestation_number) {
        return ResponseEntity.ok(FireStationService.getAllPhoneNumbersFromStation(firestation_number));
    }

    @GetMapping("/flood")
    public ResponseEntity<List<Map<String, Object>>> stations(@RequestParam("stations") String stations_numbers_list) {
        return ResponseEntity.ok(FireStationService.getAllHouseholdsFromStationsList(stations_numbers_list));
    }

    @PostMapping("/firestation")
    public ResponseEntity<String> Post_FireStation(@RequestBody FireStation fireStation) throws IOException {
        FireStationService.createNewFireStation(fireStation);
        return ResponseEntity.ok("New FireStation Mapping Successfully created");
    }

    @PutMapping("/firestation")
    public ResponseEntity<String> Put_FireStation(@RequestBody FireStation fireStation) throws IOException {
        if (FireStationService.updateFireStation(fireStation)){return ResponseEntity.ok("FireStation Mapping Successfully updated");}
        else {return new ResponseEntity<>(HttpStatus.NOT_FOUND);}

    }

    @DeleteMapping("/firestation")
    public ResponseEntity<String> Delete_FireStation(@RequestBody FireStation fireStation) throws IOException {
        if (FireStationService.deleteFireStation(fireStation)){return ResponseEntity.ok("FireStation Mapping Successfully Deleted");}
        else {return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
    }
}
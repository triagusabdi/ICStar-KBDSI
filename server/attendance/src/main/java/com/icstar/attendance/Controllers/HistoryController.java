package com.icstar.attendance.Controllers;

import com.icstar.attendance.Models.History;
import com.icstar.attendance.Repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.Duration;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api")
public class HistoryController {
    @Autowired
    HistoryRepository historyRepository;

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/clock_in")
    public ResponseEntity<History> postClockin(
            @RequestBody History postClockin) {

        LocalDateTime currentTime = LocalDateTime.now();
        postClockin.setClockIn(currentTime);
        postClockin.setElapsedTime(Duration.ZERO);
        postClockin.setStatus("Clock In");

        History history = historyRepository.save(postClockin);
        return new ResponseEntity<>(history, HttpStatus.CREATED);

    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/clock_out/{historyId}")
    public ResponseEntity<History> postClockOut(
            @PathVariable Long historyId) {
        History history = historyRepository.findById(historyId).orElse(null);

        if (history == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!"Clock In".equals(history.getStatus())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LocalDateTime currentTime = LocalDateTime.now();
        history.setClockOut(currentTime);

        Duration elapsedTime = Duration.between(history.getClockIn(), currentTime);
        history.setElapsedTime(elapsedTime);

        history.setStatus("Clock Out");

        History updatedHistory = historyRepository.save(history);
        return new ResponseEntity<>(updatedHistory, HttpStatus.OK);
    }




}

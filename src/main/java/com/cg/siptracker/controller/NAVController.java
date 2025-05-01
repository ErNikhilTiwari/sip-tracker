package com.cg.siptracker.controller;

import com.cg.siptracker.model.NAVRecord;
import com.cg.siptracker.repository.NAVRecordRepository;
import com.cg.siptracker.service.NAVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/nav")
public class NAVController {

    @Autowired
    private NAVService navService;

    @Autowired
    private NAVRecordRepository navRecordRepository;

    @GetMapping("/get")
    public List<NAVRecord> getBySchemeName(@RequestParam String fundName) {
        return navRecordRepository.findByFundNameContainingIgnoreCase(fundName);
    }

    @GetMapping("/getall")
    public NAVRecord getall(){
        return navRecordRepository.findById(1l).get();
    }

    @PostMapping("/fetch")
    public ResponseEntity<?> fetchNAVs() {
        try {
            int count = navService.fetchAndStoreNAVs();
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "recordsFetched", count
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", e.getMessage())
            );
        }
    }
}

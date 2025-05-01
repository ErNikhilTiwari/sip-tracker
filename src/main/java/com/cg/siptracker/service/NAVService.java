package com.cg.siptracker.service;

import com.cg.siptracker.model.NAVRecord;
import com.cg.siptracker.repository.NAVRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

    @Service
    public class NAVService {

        @Autowired
        private NAVRecordRepository navRecordRepository;

        private final String AMFI_URL = "https://www.amfiindia.com/spages/NAVAll.txt"; // Plain text format

        public int fetchAndStoreNAVs() throws IOException {
            URL url = new URL(AMFI_URL);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;
            int count = 0;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length > 5 && !parts[0].equals("Scheme Code")) {
                    String fundName = parts[3].trim();
                    Double nav;
                    try {
                        nav = Double.parseDouble(parts[4]);
                    } catch (NumberFormatException e) {
                        continue; // skip invalid NAVs
                    }
                    LocalDate date = LocalDate.parse(parts[5], formatter);

                    if (!navRecordRepository.existsByFundNameAndDate(fundName, date)) {
                        NAVRecord record = new NAVRecord();
                        record.setFundName(fundName);
                        record.setNavValue(nav);
                        record.setDate(date);
                        record.setTimestampFetched(LocalDateTime.now());

                        navRecordRepository.save(record);
                        count++;
                    }
                }
            }
            return count;

        }

        public List<NAVRecord> getNAVHistory(String fundName) {
            return navRecordRepository.findByFundNameContainingIgnoreCase(fundName);
        }
    }

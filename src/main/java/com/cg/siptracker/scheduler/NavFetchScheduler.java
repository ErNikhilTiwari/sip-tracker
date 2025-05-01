package com.cg.siptracker.scheduler;

import com.cg.siptracker.service.NAVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NavFetchScheduler {

    @Autowired
    private NAVService navService;

    @Scheduled(cron = "0 56 10 * * *") // 9:30 AM every day
    public void scheduleNAVFetch() {
        try {
            int count = navService.fetchAndStoreNAVs();
            System.out.println("Fetched " + count + " new NAV records.");
        } catch (Exception e) {
            System.err.println("Error fetching NAVs: " + e.getMessage());
        }
    }
}


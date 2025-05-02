package com.cg.siptracker.scheduler;

import com.cg.siptracker.dto.ResponseDTO;
import com.cg.siptracker.service.INAVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NavFetchScheduler {

    @Autowired
    private INAVService navService;

    @Scheduled(cron = "0 30 9 * * *") // 9:30 AM every day
    public void scheduleNAVFetch() {
        try {
            ResponseDTO response = navService.fetchAndStoreNAVs();
            System.out.println("NAV Scheduler " + response.getMessage() + ": " + response.getData());
        } catch (Exception e) {
            System.err.println("[NAV Scheduler Error] " + e.getMessage());
        }
    }
}

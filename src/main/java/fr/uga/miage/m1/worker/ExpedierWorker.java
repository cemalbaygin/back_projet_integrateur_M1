package fr.uga.miage.m1.worker;

import fr.uga.miage.m1.service.CommandeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log
public class ExpedierWorker {
    private final CommandeService commandeService;

    @Scheduled(cron = "0 */5 * * * *")
    public void scheduleFixedDelayTask() {
        commandeService.expedierEnAttente();
    }
}

package org.yura.services;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.logging.log4j.Logger;
import org.yura.model.AppointmentDate;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import static java.lang.Thread.sleep;

public class QueueRefresherService {
    private final MailService mailService;
    private final FetchService fetchService;
    private final Logger logger;

    public QueueRefresherService(MailService mailService, FetchService fetchService, Logger logger) {
        this.mailService = mailService;
        this.fetchService = fetchService;
        this.logger = logger;
    }

    public void run(String dozvilUrl, List<String> dozvilHosts, int dozvilTimeout, String mailRecipient) throws InterruptedException {
        while (true){
            for(var dozvilHost : dozvilHosts){
                CompletableFuture<List<AppointmentDate>> futureDates = fetchDates(dozvilUrl + dozvilHost);
                CompletableFuture<Void> futureProcessor = processDates(futureDates, dozvilHost, mailRecipient);
                futureProcessor.join();
                sleep(60000L * dozvilTimeout);
            }
        }
    }

    private CompletableFuture<Void> processDates(CompletableFuture<List<AppointmentDate>> featureDates, String dozvilHost, String mailRecipient) {
        return featureDates.thenAccept(dates -> {
            logger.info(dozvilHost + ": " + dates.toString());

            if (!dates.isEmpty()){
                mailService.sendEmail(mailRecipient, "Dozvil: " + dozvilHost, dates.toString());
            }
        });
    }

    private CompletableFuture<List<AppointmentDate>> fetchDates(String url){
        return fetchService.fetchData(url,  new TypeReference<>() {});
    }
}

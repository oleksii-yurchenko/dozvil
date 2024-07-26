package org.yura;

import org.apache.logging.log4j.LogManager;
import org.yura.config.AppConfig;
import org.yura.config.Constants;
import org.yura.services.FetchService;
import org.yura.services.MailService;
import org.yura.services.QueueRefresherService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        AppConfig config = new AppConfig();
        String dozvilUrl = config.getProperty("app.dozvil.url");
        int dozvilTimeout = Integer.parseInt(config.getProperty("app.dozvil.timeout"));
        String mailRecipient = config.getProperty("mail.recipient");
        List<String> dozvilHosts = Constants.DOZVIL_HOSTS;

        try {
            new QueueRefresherService(
                    new MailService(config),
                    new FetchService(),
                    LogManager.getLogger("Logger")
            ).run(dozvilUrl, dozvilHosts, dozvilTimeout, mailRecipient);

        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
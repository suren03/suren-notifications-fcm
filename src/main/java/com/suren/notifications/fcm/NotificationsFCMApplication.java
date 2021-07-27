package com.suren.notifications.fcm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * @author Surendra Kumar S
 */

@Slf4j
@SpringBootApplication
public class NotificationsFCMApplication {

    public static void main(String[] args) {

        log.warn("Initializing...");
        ApplicationContext applicationContext = SpringApplication.run(NotificationsFCMApplication.class, args);

        log.warn("Application Started.");

    }

}

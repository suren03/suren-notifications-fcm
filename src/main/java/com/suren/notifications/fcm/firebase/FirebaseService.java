package com.suren.notifications.fcm.firebase;

import com.google.firebase.messaging.*;
import com.suren.notifications.fcm.domain.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * @author Surendra Kumar S
 */

@Slf4j
@Service
public class FirebaseService {

    public String sendMessageToToken(NotificationRequest request) {

        String response = null;

        try {
            Message message = getPreconfiguredMessageToToken(request);
            response = sendNotificationMessage(message);

            log.info("Notification message sent. Device token - {}, Response - {} ", request.getToken(), response);

        } catch (Exception e) {
            log.error("Exception occurred while sending notification to FCM ", e);
            response = e.getMessage();
        }

        return response;
    }

    private String sendNotificationMessage(Message message) throws FirebaseMessagingException {
        return FirebaseMessaging.getInstance().send(message);
        //sendAsync(message).get();
    }

    private AndroidConfig getAndroidConfig(String topic) {
        return AndroidConfig.builder()
                .setTtl(Duration.ofMinutes(2).toMillis()).setCollapseKey(topic)
                .setPriority(AndroidConfig.Priority.HIGH)
                .setNotification(
                        AndroidNotification.builder()
                                .setSound(NotificationParameter.SOUND.getValue())
                                .setColor(NotificationParameter.COLOR.getValue())
                                .setTag(topic)
                                .build())
                .build();
    }

    private ApnsConfig getApnsConfig(String topic) {
        return ApnsConfig.builder()
                .setAps(Aps.builder()
                        .setCategory(topic)
                        .setThreadId(topic)
                        .build())
                .build();
    }

    private Message getPreconfiguredMessageToToken(NotificationRequest request) {
        return getMessageBuilder(request)
                .setToken(request.getToken())
                .build();
    }

    private Message.Builder getMessageBuilder(NotificationRequest request) {

        AndroidConfig androidConfig = getAndroidConfig(request.getTopic());
        ApnsConfig apnsConfig = getApnsConfig(request.getTopic());

        return Message.builder()
                .setApnsConfig(apnsConfig)
                .setAndroidConfig(androidConfig)
                .setNotification(
//                        new Notification(request.getTitle(), request.getMessage()));
                        Notification.builder()
                                .setTitle(request.getTitle())
                                .setBody(request.getMessage())
                                .build());
    }


}

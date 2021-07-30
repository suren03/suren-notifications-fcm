package com.suren.notifications.fcm.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Surendra Kumar S
 */

@Getter @Setter @NoArgsConstructor @ToString
public class NotificationRequest {

    private String topic;
    private String title;
    private String message;
    private String token;
//    private String payloadMessageId;
//    private String payloadData;
//    private String topic;
//    private String topic;
    private String image;


}
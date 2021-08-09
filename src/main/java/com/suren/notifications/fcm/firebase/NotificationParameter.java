package com.suren.notifications.fcm.firebase;

/**
 * @author Surendra Kumar S
 */
public enum NotificationParameter {

    SOUND("default"),
    COLOR("#FFFF00");

    private String value;

    NotificationParameter(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}

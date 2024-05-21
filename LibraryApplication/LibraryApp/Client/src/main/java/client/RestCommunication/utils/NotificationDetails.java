package client.RestCommunication.utils;

import common.restCommon.NotificationRest;

import java.util.Objects;

public class NotificationDetails {
    private NotificationRest notification;
    private String username;

    public NotificationDetails(NotificationRest notification, String username) {
        this.notification = notification;
        this.username = username;
    }

    public NotificationRest getNotification() {
        return notification;
    }

    public void setNotification(NotificationRest notification) {
        this.notification = notification;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NotificationDetails that)) return false;
        return getNotification() == that.getNotification() && Objects.equals(getUsername(), that.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNotification(), getUsername());
    }

    @Override
    public String toString() {
        return "NotificationDetails{" +
                "notification=" + notification +
                ", username='" + username + '\'' +
                '}';
    }
}

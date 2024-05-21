package client.RestCommunication.utils;

import common.restCommon.NotificationRest;

public class ClientNotificationParser {

    public static NotificationDetails parseNotifcation(String message) {
        String[] parts=message.split(",");
        if(parts.length!=2)
            return null;
        String username=null;
        NotificationRest notification=null;

            String [] subparts=parts[0].split(":");
            if(subparts.length!=2)
                return null;
            if(subparts[0].equals("Type"))
            {
                try {
                    notification=NotificationRest.valueOf(subparts[1]);
                } catch (IllegalArgumentException e) {
                    return null;
                }
            }
            else if(subparts[0].equals("Subscriber"))
            {
                username=subparts[1];
            }
            else
                return null;
            subparts=parts[1].split(":");
            if(subparts.length!=2)
                return null;
            if(subparts[0].equals("Subscriber"))
            {
                username=subparts[1];
            }
            else if(subparts[0].equals("Type"))
            {
                try {
                    notification=NotificationRest.valueOf(subparts[1]);
                } catch (IllegalArgumentException e) {
                    return null;
                }
            }
            else
                return null;
        return new NotificationDetails(notification,username);
    }
}

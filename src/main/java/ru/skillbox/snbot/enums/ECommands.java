package ru.skillbox.snbot.enums;

public enum ECommands {
    START("/start"),
    LOGIN("/login"),
    REGISTER("/register"),
    CONFIRM_KILLYOURSELF("/confirmkillyourself"),
    CANCEL_KILLYOURSELF("/cancelkillyourself"),
    KILLYOURSELF("/killyourself"),
    TOKEN("/token"),
    RESET_PASSWORD("/resetpassword"),
    FEEDS("/feeds"),
    CREATE_POST("/createpost"),
    CLOSE("/close"),
    MORE("/more"),
    FRIENDS_INCOMING_REQUESTS("/friendsincomingrequests"),
    ACCEPT_FRIEND_IN_REQ("/acceptfriendinreq"),
    DECLINE_FRIEND_IN_REQ("/declinefriendinreq"),
    ENABLE_NOTIFICATIONS("/enablenotifications"),
    DISABLE_NOTIFICATIONS("/disablenotifications"),
    MESSAGES("/messages");

    private final String command;

    ECommands(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}

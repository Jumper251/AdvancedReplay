package me.jumper251.replay.replaysystem.data.types;

import java.util.HashSet;
import java.util.Set;

public class ChatData extends PacketData {


    /**
     *
     */
    private static final long serialVersionUID = 6849586468365004854L;

    private Set<String> recipients;
    private String message;

    /**
     * Constructs a ChatData with provided message and an empty recipient.
     *
     * @param message the message
     */
    public ChatData(String message) {
        this.recipients = new HashSet<>();
        this.message = message;
    }

    /**
     * Constructs a ChatData with provided message and recipient.
     *
     * @param recipients the recipient
     * @param message the message
     */
    public ChatData(Set<String> recipients, String message) {
        this.recipients = recipients;
        this.message = message;
    }

    public Set<String> getRecipients() {
        return recipients;
    }

    public String getMessage() {
        return message;
    }
}

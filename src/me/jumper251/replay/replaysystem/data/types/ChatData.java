package me.jumper251.replay.replaysystem.data.types;

public class ChatData extends PacketData {


    /**
     *
     */
    private static final long serialVersionUID = 6849586468365004854L;

    private String recipient;
    private String message;

    /**
     * Constructs a ChatData with provided message and an empty recipient.
     *
     * @param message the message
     */
    public ChatData(String message) {
        this.recipient = "";
        this.message = message;
    }

    /**
     * Constructs a ChatData with provided message and recipient.
     *
     * @param recipient the recipient
     * @param message the message
     */
    public ChatData(String recipient, String message) {
        this.recipient = recipient;
        this.message = message;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessage() {
        return message;
    }
}

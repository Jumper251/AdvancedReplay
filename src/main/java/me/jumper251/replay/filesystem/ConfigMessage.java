package me.jumper251.replay.filesystem;

import org.bukkit.command.CommandSender;

public class ConfigMessage {

    private String key;

    private String defaultMessage;

    private String message;

    private boolean prefixed = true;

    public ConfigMessage(String key, String defaultMessage) {
        this.key = key;
        this.defaultMessage = defaultMessage;
    }

    public ConfigMessage(String key, String defaultMessage, boolean prefixed) {
        this(key, defaultMessage);
        this.prefixed = prefixed;
    }

    public String getKey() {
        return key;
    }

    public String getMessage() {
        return message != null ? message : defaultMessage;
    }

    public String getFullMessage() {
        return getBuilder().build();
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public boolean isEmpty() {
        return message == null || message.isEmpty();
    }

    private MessageBuilder getBuilder() {
        return new MessageBuilder(getMessage(), prefixed);
    }

    public MessageBuilder append(ConfigMessage message) {
        return getBuilder().append(message);
    }

    public MessageBuilder arg(String key, Object value) {
        return getBuilder().set(key, value);
    }

    public void send(CommandSender cs) {
        getBuilder().send(cs);
    }
}

package me.jumper251.replay.filesystem;


import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;


public class MessageBuilder {

    private String message;

    private boolean prefixed;

    public MessageBuilder(String message) {
        this.message = message;
    }

    public MessageBuilder(String message, boolean prefixed) {
        this(message);
        this.prefixed = prefixed;
    }

    public MessageBuilder(ConfigMessage... message) {
        for (ConfigMessage msg : message) {
            append(msg);
        }
    }

    public MessageBuilder set(String key, Object value) {
        if (this.message != null && this.message.contains("{" + key + "}")) {
            this.message = this.message.replace("{" + key + "}", value.toString());
        }

        return this;
    }

    public MessageBuilder arg(String key, Object value) {
        return set(key, value);
    }

    public MessageBuilder append(ConfigMessage message) {
        if (this.message != null) {
            this.message += "\n" + message.getMessage();
        } else {
            this.message = message.getMessage();
        }
        return this;
    }

    public void send(CommandSender cs) {
        if (this.message == null || this.message.isEmpty()) {
            return;
        }
        cs.sendMessage(build());
    }

    public String build() {
        if (this.message != null && this.message.length() > 0) {
            if (prefixed) {
                message = Messages.PREFIX.getMessage() + message;
            }

            return ChatColor.translateAlternateColorCodes('&', this.message);
        } else {
            return null;
        }
    }

}

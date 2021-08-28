package me.jumper251.replay.filesystem;


import org.bukkit.ChatColor;


public class MessageBuilder {

    private String message;

    public MessageBuilder(String message) {
        this.message = message;
    }

    public MessageBuilder set(String key, Object value) {
        if (this.message != null && this.message.contains("{" + key + "}")) {
            this.message = this.message.replace("{" + key + "}", value.toString());
        }

        return this;
    }


    public String build() {
        return this.message != null && this.message.length() > 0 ? ChatColor.translateAlternateColorCodes('&', this.message) : null;
    }

}

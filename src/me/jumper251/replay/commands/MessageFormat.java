package me.jumper251.replay.commands;

import me.jumper251.replay.filesystem.MessageBuilder;

public class MessageFormat {

	private String syntaxMessage, overviewMessage, permissionMessage, consoleMessage, notFoundMessage;
	
	
	public MessageFormat syntax(String syntaxMessage) {
		this.syntaxMessage = syntaxMessage;
		return this;
	}
	
	public MessageFormat overview(String overviewMessage) {
		this.overviewMessage = overviewMessage;
		return this;
	}
	
	public MessageFormat permission(String permissionMessage) {
		this.permissionMessage = permissionMessage;
		return this;
	}
	
	public MessageFormat console(String consoleMessage) {
		this.consoleMessage = consoleMessage;
		return this;
	}
	
	public MessageFormat notFound(String notFoundMessage) {
		this.notFoundMessage = notFoundMessage;
		return this;
	}
	
	public String getSyntaxMessage(String command, String arg) {
		return new MessageBuilder(this.syntaxMessage).
				set("command", command)
				.set("args", arg)
				.build();
	}
	
	public String getOverviewMessage(String command, String arg, String desc) {
		return new MessageBuilder(this.overviewMessage).
				set("command", command)
				.set("args", arg)
				.set("desc", desc)
				.build();
	}
	
	public String getConsoleMessage() {
		return consoleMessage != null ? consoleMessage : "You must be a player to execute this command.";
	}
	
	public String getPermissionMessage() {
		return this.permissionMessage;
	}
	
	public String getNotFoundMessage() {
		return notFoundMessage;
	}
}

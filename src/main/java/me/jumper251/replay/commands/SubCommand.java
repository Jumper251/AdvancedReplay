package me.jumper251.replay.commands;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public abstract class SubCommand {

	private AbstractCommand parent;
	private String label, description, args;
	private boolean playerOnly, enabled = true;
	private List<String> aliases;

	public SubCommand (AbstractCommand parent, String label, String description, String args, boolean playerOnly) {
		this.parent      = parent;
		this.label       = label;
		this.description = description;
		this.args        = args;
		this.playerOnly  = playerOnly;
		this.aliases     = new ArrayList<String> ();
	}

	public abstract boolean execute (CommandSender cs, Command cmd, String label, String[] args);

	public List<String> onTab (CommandSender cs, Command cmd, String label, String[] args) {
		return null;
	}

	public SubCommand addAlias (String alias) {
		if (!this.aliases.contains (alias))
			this.aliases.add (alias);

		return this;
	}

	public String getDescription () {
		return description;
	}

	public String getLabel () {
		return label;
	}

	public String getArgs () {
		return args;
	}

	public boolean isPlayerOnly () {
		return playerOnly;
	}

	public boolean isEnabled () {
		return enabled;
	}

	public void setEnabled (boolean enabled) {
		this.enabled = enabled;
	}

	public List<String> getAliases () {
		return aliases;
	}

	public AbstractCommand getParent () {
		return parent;
	}
}

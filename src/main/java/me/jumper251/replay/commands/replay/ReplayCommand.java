package me.jumper251.replay.commands.replay;

import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.commands.AbstractCommand;
import me.jumper251.replay.commands.MessageFormat;
import me.jumper251.replay.commands.SubCommand;
import me.jumper251.replay.filesystem.Messages;

public class ReplayCommand extends AbstractCommand {

	public ReplayCommand() {
		super("Replay", ReplaySystem.PREFIX + "AdvancedReplay §ev" + ReplaySystem.getInstance().getDescription().getVersion(), "replay.command");
	}

	@Override
	protected MessageFormat setupFormat() {
		return new MessageFormat()
				.overview(Messages.COMMAND_OVERVIEW.getFullMessage())
				.syntax(Messages.COMMAND_SYNTAX.getFullMessage())
				.permission(Messages.COMMAND_NO_PERMISSION.getFullMessage())
				.notFound(Messages.COMMAND_NOTFOUND.getFullMessage());
	}

	@Override
	protected SubCommand[] setupCommands() {
		
		return new SubCommand[] { new ReplayStartCommand(this), 
				new ReplayStopCommand(this).addAlias("save"), 
				new ReplayPlayCommand(this), 
				new ReplayDeleteCommand(this).addAlias("remove"),
				new ReplayJumpCommand(this),
				new ReplayLeaveCommand(this),
				new ReplayInfoCommand(this),
				new ReplayListCommand(this), 
				new ReplayReloadCommand(this),
				new ReplayReformatCommand(this),
				new ReplayMigrateCommand(this) };
	}

}

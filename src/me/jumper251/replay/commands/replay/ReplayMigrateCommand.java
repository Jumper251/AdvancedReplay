package me.jumper251.replay.commands.replay;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.commands.AbstractCommand;
import me.jumper251.replay.commands.SubCommand;
import me.jumper251.replay.filesystem.ConfigManager;
import me.jumper251.replay.filesystem.saving.DatabaseReplaySaver;
import me.jumper251.replay.filesystem.saving.DefaultReplaySaver;
import me.jumper251.replay.filesystem.saving.IReplaySaver;
import me.jumper251.replay.filesystem.saving.ReplaySaver;
import me.jumper251.replay.replaysystem.Replay;
import me.jumper251.replay.utils.LogUtils;
import me.jumper251.replay.utils.fetcher.Consumer;

public class ReplayMigrateCommand extends SubCommand {

	private List<String> options = Arrays.asList("file", "database");
	
	public ReplayMigrateCommand(AbstractCommand parent) {
		super(parent, "migrate", "Migrate all replays", "migrate <File|Database>", false);
	}

	@Override
	public boolean execute(CommandSender cs, Command cmd, String label, String[] args) {
		if (args.length != 2) return false;
		
		String option = args[1].toLowerCase();
		if (options.contains(option)) {
			IReplaySaver migrationSaver = null;
			
			if (option.equalsIgnoreCase("file") && ReplaySaver.replaySaver instanceof DatabaseReplaySaver) {
				migrationSaver = new DefaultReplaySaver();
				
			} else if (option.equalsIgnoreCase("database") && ReplaySaver.replaySaver instanceof DefaultReplaySaver) {
				ConfigManager.USE_DATABASE = true;
				ConfigManager.loadData(false);

				migrationSaver = new DatabaseReplaySaver();
			} else {
				cs.sendMessage(ReplaySystem.PREFIX + "§cYou can't migrate to the same system.");
				return true;
			}
			
			cs.sendMessage(ReplaySystem.PREFIX + "§7Migrating replays to §e" + option);
			for (String replayName : ReplaySaver.getReplays()) {
				this.migrate(replayName, option, migrationSaver);
			}
			
			
		} else {
			cs.sendMessage(ReplaySystem.PREFIX + "§cInvalid argument. " + options.stream().collect(Collectors.joining("|", "<", ">")));
		}
		
		return true;
	}
	
	private void migrate(String replayName, String option, IReplaySaver saver) {
		
		ReplaySaver.load(replayName, new Consumer<Replay>() {
			
			@Override
			public void accept(Replay replay) {
				try {
					if (!saver.replayExists(replayName)) {
						LogUtils.log("Migrating " + replayName + "...");

						saver.saveReplay(replay);
					}
				
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
	}
	
	@Override
	public List<String> onTab(CommandSender cs, Command cmd, String label, String[] args) {
		return options.stream()
				.filter(option -> StringUtil.startsWithIgnoreCase(option, args[1]))
				.collect(Collectors.toList());
	}

}

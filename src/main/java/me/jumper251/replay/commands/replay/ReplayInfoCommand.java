package me.jumper251.replay.commands.replay;



import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.commands.AbstractCommand;
import me.jumper251.replay.commands.SubCommand;
import me.jumper251.replay.filesystem.saving.ReplaySaver;
import me.jumper251.replay.replaysystem.data.ReplayInfo;
import me.jumper251.replay.replaysystem.recording.optimization.ReplayOptimizer;
import me.jumper251.replay.replaysystem.recording.optimization.ReplayQuality;
import me.jumper251.replay.replaysystem.recording.optimization.ReplayStats;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;

public class ReplayInfoCommand extends SubCommand {

	public ReplayInfoCommand(AbstractCommand parent) {
		super(parent, "info", "Information about a Replay", "info <Name>", false);
	}

	@Override
	public boolean execute(CommandSender cs, Command cmd, String label, String[] args) {
		if (args.length != 2) return false;
		
		String name = args[1];
		

		if (ReplaySaver.exists(name)) {
			cs.sendMessage(ReplaySystem.PREFIX + "Loading replay §e" + name + "§7...");
			
			ReplaySaver.load(name, replay -> {
				ReplayInfo info = replay.getReplayInfo() != null ? replay.getReplayInfo() : new ReplayInfo(replay.getId(), replay.getData().getCreator(), null, replay.getData().getDuration());
				ReplayStats stats = ReplayOptimizer.analyzeReplay(replay);				
				
				cs.sendMessage("§c» §7Information about §e§l" + replay.getId() + " §c«");
				cs.sendMessage("");
				if (info.getCreator() != null) {
					cs.sendMessage("§7§oCreated by: §9" + info.getCreator());
				}

				cs.sendMessage("§7§oDuration: §6" + (info.getDuration() / 20) + " §7§oseconds");
				cs.sendMessage("§7§oPlayers: §6" + stats.getPlayers().stream().collect(Collectors.joining("§7, §6")));
				cs.sendMessage("§7§oQuality: " + (replay.getData().getQuality() != null ? replay.getData().getQuality().getQualityName() : ReplayQuality.HIGH.getQualityName()));

				if (cs instanceof Player) {
					((Player)cs).spigot().sendMessage(buildMessage(stats));
				}
				if (stats.getEntityCount() > 0) {
					cs.sendMessage("§7§oEntities: §6" + stats.getEntityCount());
				}

				
				
			});
			
		} else {
			cs.sendMessage(ReplaySystem.PREFIX + "§cReplay not found.");
		}
		
		return true;
	}
	
	public BaseComponent[] buildMessage(ReplayStats stats) {
		return new ComponentBuilder("§7§oRecorded data: ")
				.append("§6§n" + stats.getActionCount() + "§r")
				.event(new HoverEvent(Action.SHOW_TEXT, new ComponentBuilder(stats.getSortedActions().entrySet().stream().map(e -> "§7" + e.getKey() + ": §b" + e.getValue()).collect(Collectors.joining("\n"))).create()))
				.append(" §7§oactions")
				.reset()
				.create();
 
	}
	
	@Override
	public List<String> onTab(CommandSender cs, Command cmd, String label, String[] args) {
		return ReplaySaver.getReplays().stream()
				.filter(name -> StringUtil.startsWithIgnoreCase(name, args.length > 1 ? args[1] : null))
				.collect(Collectors.toList());
	}

	
}

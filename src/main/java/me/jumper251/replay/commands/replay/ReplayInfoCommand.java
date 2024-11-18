package me.jumper251.replay.commands.replay;




import java.util.List;
import java.util.stream.Collectors;

import me.jumper251.replay.filesystem.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

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
			Messages.REPLAY_INFO_LOADING.arg("replay", name).send(cs);

			ReplaySaver.load(name, replay -> {
				ReplayInfo info = replay.getReplayInfo() != null ? replay.getReplayInfo() : new ReplayInfo(replay.getId(), replay.getData().getCreator(), null, replay.getData().getDuration());
				ReplayStats stats = ReplayOptimizer.analyzeReplay(replay);				
				

				Messages.REPLAY_INFO_HEADER.arg("replay", replay.getId()).send(cs);
				Messages.REPLAY_INFO_SPACE.send(cs);

				if (info.getCreator() != null) {
					Messages.REPLAY_INFO_CREATOR.arg("creator", info.getCreator()).send(cs);
				}

				Messages.REPLAY_INFO_DURATION.arg("duration", (info.getDuration() / 20)).send(cs);
				Messages.REPLAY_INFO_PLAYERS.arg("players", String.join("§7, §6", stats.getPlayers())).send(cs);
				Messages.REPLAY_INFO_QUALITY
						.arg("quality", (replay.getData().getQuality() != null ? replay.getData().getQuality().getQualityName() : ReplayQuality.HIGH.getQualityName()))
						.send(cs);
				if (cs instanceof Player && !Messages.REPLAY_INFO_HOVER_PART1.isEmpty()) {
					((Player)cs).spigot().sendMessage(buildMessage(stats));
				}
				if (stats.getEntityCount() > 0) {
					Messages.REPLAY_INFO_ENTITIES.arg("entities", stats.getEntityCount()).send(cs);
				}

				
				
			});
			
		} else {
			Messages.REPLAY_NOT_FOUND.send(cs);
		}
		
		return true;
	}
	
	public BaseComponent[] buildMessage(ReplayStats stats) {
		String data = stats.getSortedActions().entrySet()
				.stream()
				.map(e -> Messages.REPLAY_INFO_HOVER_PART3.arg("data", e.getKey()).arg("amount", e.getValue()).build())
				.collect(Collectors.joining("\n"));

		return new ComponentBuilder(Messages.REPLAY_INFO_HOVER_PART1.getFullMessage())
				.append(Messages.REPLAY_INFO_HOVER_PART2.arg("action_count", stats.getActionCount()).build())
				.event(new HoverEvent(Action.SHOW_TEXT, new ComponentBuilder(data).create()))
				.append(Messages.REPLAY_INFO_HOVER_PART4.getFullMessage())
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

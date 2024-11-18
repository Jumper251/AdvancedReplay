package me.jumper251.replay.commands.replay;

import java.io.File;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import me.jumper251.replay.filesystem.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.commands.AbstractCommand;
import me.jumper251.replay.commands.CommandPagination;
import me.jumper251.replay.commands.IPaginationExecutor;
import me.jumper251.replay.commands.SubCommand;
import me.jumper251.replay.filesystem.saving.DatabaseReplaySaver;
import me.jumper251.replay.filesystem.saving.DefaultReplaySaver;
import me.jumper251.replay.filesystem.saving.ReplaySaver;
import me.jumper251.replay.replaysystem.data.ReplayInfo;
import me.jumper251.replay.utils.MathUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;

public class ReplayListCommand extends SubCommand {

	public ReplayListCommand(AbstractCommand parent) {
		super(parent, "list", "Lists all replays", "list [Page]", false);
	}

	@Override
	public boolean execute(CommandSender cs, Command cmd, String label, String[] args) {
		if (args.length > 2) return false;

		
		if (ReplaySaver.getReplays().size() > 0) {
			int page = 1;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

			if (args.length == 2 && MathUtils.isInt(args[1])) page = Integer.valueOf(args[1]);
			

			List<String> replays = ReplaySaver.getReplays();
			replays.sort(dateComparator());
			
			CommandPagination<String> pagination = new CommandPagination<>(replays, 9);
			Messages.REPLAY_LIST_HEADER
					.arg("replays", ReplaySaver.getReplays().size())
					.arg("page", page)
					.arg("pages", pagination.getPages())
					.send(cs);

			pagination.printPage(page, element -> {
				String message = Messages.REPLAY_LIST_ENTRY
						.arg("date", (getCreationDate(element) != null ? format.format(getCreationDate(element)) : "")).arg("replay", element).build();

                if (cs instanceof Player) {
                    BaseComponent[] comps;
                    if (DatabaseReplaySaver.getInfo(element) != null && DatabaseReplaySaver.getInfo(element).getCreator() != null) {
                        ReplayInfo info = DatabaseReplaySaver.getInfo(element);

							String hoverText = Messages.combined(Messages.REPLAY_LIST_HOVER_HEADER, Messages.REPLAY_LIST_HOVER_SPACE, Messages.REPLAY_LIST_HOVER_CREATOR,
									Messages.REPLAY_LIST_HOVER_DURATION, Messages.REPLAY_LIST_HOVER_SPACE2, Messages.REPLAY_LIST_HOVER_ACTION)
									.arg("replay", element)
									.arg("creator", info.getCreator())
									.arg("duration", (info.getDuration() / 20)).build();

                        comps = new ComponentBuilder(message)
                                .event(new HoverEvent(Action.SHOW_TEXT, new ComponentBuilder(hoverText).create()))
                                .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/replay play " + info.getID()))
                                .create();
                    } else {
						String hoverText = Messages.combined(Messages.REPLAY_LIST_HOVER_HEADER, Messages.REPLAY_LIST_HOVER_SPACE, Messages.REPLAY_LIST_HOVER_ACTION)
								.arg("replay", element).build();

                        comps = new ComponentBuilder(message)
                                .event(new HoverEvent(Action.SHOW_TEXT, new ComponentBuilder(hoverText).create()))
                                .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/replay play " + element))
                                .create();
                    }
                    ((Player) cs).spigot().sendMessage(comps);
                } else {
                    cs.sendMessage(message);
                }

            });
						
			
		} else {
			Messages.REPLAY_LIST_NO_REPLAYS.send(cs);
		}
		return true;
	}
	
	private Date getCreationDate(String replay) {
		if (ReplaySaver.replaySaver instanceof DefaultReplaySaver) {
			return new Date(new File(DefaultReplaySaver.DIR, replay + ".replay").lastModified());
		}
		
		if (ReplaySaver.replaySaver instanceof DatabaseReplaySaver) {
			return new Date(DatabaseReplaySaver.replayCache.get(replay).getTime());
		}
		
		return null;
	}
	
	private Comparator<String> dateComparator() {
		return (s1, s2) -> {
			if (getCreationDate(s1) != null && getCreationDate(s2) != null) {
				return getCreationDate(s1).compareTo(getCreationDate(s2));
			} else {
				return 0;
			}
			
		};
	}
	
	@Override
	public List<String> onTab(CommandSender cs, Command cmd, String label, String[] args) {
		return IntStream.range(1, new CommandPagination<>(ReplaySaver.getReplays(), 9).getPages() + 1)
				.boxed()
				.map(String::valueOf)
				.collect(Collectors.toList());
	}

	
}

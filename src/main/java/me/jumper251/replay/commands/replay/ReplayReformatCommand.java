package me.jumper251.replay.commands.replay;


import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.commands.AbstractCommand;
import me.jumper251.replay.commands.SubCommand;
import me.jumper251.replay.filesystem.saving.DefaultReplaySaver;
import me.jumper251.replay.filesystem.saving.ReplaySaver;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ReplayReformatCommand extends SubCommand {

    public ReplayReformatCommand(AbstractCommand parent) {
        super(parent, "reformat", "Reformat the replays", "reformat", false);

        this.setEnabled(false);
    }

    @Override
    public boolean execute(CommandSender cs, Command cmd, String label, String[] args) {
        cs.sendMessage(ReplaySystem.PREFIX + "Reformatting Replay files...");
        ((DefaultReplaySaver) ReplaySaver.replaySaver).reformatAll();
        cs.sendMessage("§aFinished. Check console for details.");

        return true;
    }


}

package me.jumper251.replay.replaysystem.replaying;

import com.comphenix.packetwrapper.WrapperPlayClientUseEntity;
import com.comphenix.packetwrapper.WrapperPlayServerCamera;
import com.comphenix.packetwrapper.WrapperPlayServerEntityDestroy;
import com.comphenix.packetwrapper.WrapperPlayServerGameStateChange;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.EntityUseAction;
import java.util.HashMap;
import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.listener.AbstractListener;
import me.jumper251.replay.utils.VersionUtil;
import me.jumper251.replay.utils.VersionUtil.VersionEnum;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class ReplayPacketListener extends AbstractListener {

	private PacketAdapter packetAdapter;

	private Replayer replayer;

	private int previous;

	private HashMap<Player, Integer> spectating;

	public ReplayPacketListener (Replayer replayer) {
		this.replayer   = replayer;
		this.spectating = new HashMap<Player, Integer> ();
		this.previous   = -1;

		// Disable spectator function for 1.16
		if (!isRegistered () && !VersionUtil.isAbove (VersionEnum.V1_16))
			register();
	}

	@Override
	public void register() {
		this.packetAdapter = new PacketAdapter (ReplaySystem.getInstance (), ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY, PacketType.Play.Server.ENTITY_DESTROY) {
			@SuppressWarnings ("deprecation")
			@Override
			public void onPacketReceiving (PacketEvent event) {
				WrapperPlayClientUseEntity packet = new WrapperPlayClientUseEntity (event.getPacket ());
				Player p                          = event.getPlayer ();

				if (packet.getType () == EntityUseAction.ATTACK && ReplayHelper.replaySessions.containsKey (p.getName ()) && replayer.getNPCList ().values ().stream ().anyMatch (ent -> packet.getTargetID () == ent.getId ())) {
					if (p.getGameMode () != GameMode.SPECTATOR)
						previous = p.getGameMode ().getValue ();
					setCamera (p, packet.getTargetID (), 3F);
				}
			}

			@Override
			public void onPacketSending (PacketEvent event) {
				WrapperPlayServerEntityDestroy packet = new WrapperPlayServerEntityDestroy (event.getPacket ());
				Player p                              = event.getPlayer ();

				if (ReplayHelper.replaySessions.containsKey (p.getName ()) && isSpectating (p)) {
					for (int id : packet.getEntityIDs ()) {

						if (id == spectating.get (p)) {
							setCamera (p, p.getEntityId (), previous);
						}
					}
				}
			}
		};

		ProtocolLibrary.getProtocolManager ().addPacketListener (this.packetAdapter);
	}

	@Override
	public void unregister () {
		if (!VersionUtil.isAbove (VersionEnum.V1_16)) {
			ProtocolLibrary.getProtocolManager ().removePacketListener (this.packetAdapter);
		}
	}

	public boolean isRegistered () {
		return this.packetAdapter != null;
	}

	public int getPrevious () {
		return previous;
	}

	public boolean isSpectating (Player p) {
		return this.spectating.containsKey (p);
	}

	public void setCamera (Player p, int entityID, float gamemode) {
		WrapperPlayServerCamera camera = new WrapperPlayServerCamera ();
		camera.setCameraId (entityID);

		WrapperPlayServerGameStateChange state = new WrapperPlayServerGameStateChange ();
		state.setReason (3);
		state.setValue (gamemode < 0 ? 0 : gamemode);

		state.sendPacket (p);
		camera.sendPacket (p);

		if (gamemode == 3F) {
			this.spectating.put (p, entityID);
		} else if (this.spectating.containsKey (p)) {
			this.spectating.remove (p);
		}
	}
}

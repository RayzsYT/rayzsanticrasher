package de.rayzs.rayzsanticrasher.crasher.impl.client;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import de.rayzs.rayzsanticrasher.crasher.ext.ClientCheck;
import io.netty.channel.Channel;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInSpectate;

public class Spectate extends ClientCheck {

	private Integer max;

	public Spectate() {
		max = getFileManager("max", this).getInt(80);
	}

	@Override
	public boolean onCheck(Channel channel, Player player, String packetName, Packet<?> packet, Integer amount) {
		if (!(packet instanceof PacketPlayInSpectate))
			return false;
		try {
			if (player.getGameMode() != GameMode.SPECTATOR)
				return true;
			if (amount > max)
				return true;
		} catch (Exception error) { }
		return false;
	}
}
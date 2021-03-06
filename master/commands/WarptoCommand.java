package joris.multiserver.master.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import joris.multiserver.common.Waypoint;
import joris.multiserver.master.MSM;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

public class WarptoCommand extends CommandBase {

	@Override
	public String getCommandName() {
		return "warpto";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/warpto <waypoint name>";
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] list) {
		return new ArrayList(MSM.waypoints.func_150296_c());
	}
	
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return true;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] para) {
		if (sender instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) sender;
			if (para.length > 0) {
				if (MSM.waypoints.getTag(para[0]) != null) {
					Waypoint waypoint = new Waypoint((NBTTagCompound) MSM.waypoints.getTag(para[0]));
					if (waypoint.instanceName.equals("master")) {
						// wayppoint on this server
						if (player.dimension != waypoint.dimension) {
							MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(player, waypoint.dimension);
						}
						player.playerNetServerHandler.setPlayerLocation(waypoint.x + 0.5, waypoint.y, waypoint.z + 0.5, player.rotationYaw, player.rotationPitch);
					} else {
						// Waypoint on an instance
						try {
							if (MSM.Instances.get(waypoint.instanceName).isConnected()) {
								MSM.sendPlayerData(MSM.Instances.get(waypoint.instanceName), player, waypoint.travelData());
								MSM.scheduleTransfer(player.getUniqueID().toString());
							} else {
								sender.addChatMessage(new ChatComponentText("Instance not live"));
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} else {
					sender.addChatMessage(new ChatComponentText("Waypoint not found"));
				}
			} else {
				sender.addChatMessage(new ChatComponentText("No waypoint given"));
				sender.addChatMessage(new ChatComponentText("Available waypoints:"));
				sender.addChatMessage(new ChatComponentText(MSM.waypoints.func_150296_c().toString().replace("[", "").replace("]", "")));
			}
		} else {
			sender.addChatMessage(new ChatComponentText("Player only command"));
		}
	}
}

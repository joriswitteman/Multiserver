package joris.multiserver.slave.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import joris.multiserver.common.Waypoint;
import joris.multiserver.slave.MSS;
import joris.multiserver.slave.packet.PacketWaypoint;

public class CreateWarpCommand extends CommandBase {

	@Override
	public String getCommandName() {
		return "createwarp";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/createwarp <warpname>";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] para) {
		if (sender instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) sender;
			if (para.length == 1) { // <name>
				Waypoint waypoint = new Waypoint(player, MSS.Name);
				MSS.waypoints.setTag(para[0], waypoint.storeToNBT());
				MSS.TCPClient.send(new PacketWaypoint(para[0], waypoint.storeToNBT()));
			} 
		}
	}

}
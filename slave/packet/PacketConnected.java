package joris.multiserver.packet;

import jexxus.common.Connection;
import joris.multiserver.MultiServerSlave;
import net.minecraft.nbt.NBTTagCompound;

import org.apache.logging.log4j.Level;

public class PacketConnected extends Packet {

	private boolean	connected;
	private Integer	port;

	public PacketConnected(Connection conn, NBTTagCompound tag) {
		super(conn);
		this.loadFromNBT(tag);
	}

	public PacketConnected(Boolean connected, Integer port) {
		super(null);
		this.connected = connected;
		this.port = port;
	}

	@Override
	public int getID() {
		return 1;
	}

	@Override
	public void loadFromNBT(NBTTagCompound tag) {
		super.loadFromNBT(tag);
		this.connected = tag.getBoolean("connected");
		this.port = tag.getInteger("port");
	}

	@Override
	public void safeToNBT(NBTTagCompound tag) {
		super.safeToNBT(tag);
		tag.setBoolean("connected", this.connected);
		tag.setInteger("port", this.port);
	}

	@Override
	public void handle() {
		if (this.connected) {
			MultiServerSlave.logger.log(Level.INFO, "Connected to Master.");
			MultiServerSlave.ServerPort = this.port;
		} else {
			MultiServerSlave.logger.log(Level.INFO, "Auth failed.");
		}
	}

}
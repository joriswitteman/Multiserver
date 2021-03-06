package joris.multiserver.common;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import joris.multiserver.jexxus.common.Connection;
import net.minecraft.nbt.NBTTagCompound;

public class PacketRegistry {
	// test
	public static HashMap<Integer, Class<? extends Packet>>	classes	= new HashMap<Integer, Class<? extends Packet>>();
	private static String									serverName;

	public static void register(Class<? extends Packet> classname, int id) {
		if (classes.get(id) != null) {
			throw new RuntimeException("Packet id already exists.");
		} else {
			classes.put(id, classname);
		}
	}

	public static Packet createPacket(Connection conn, NBTTagCompound tag) {
		Class classname = classes.get(tag.getInteger("ID"));
		if (classname != null) {
			try {
				Constructor constructor = classname.getConstructor(Connection.class, NBTTagCompound.class);
				constructor.newInstance(conn, tag);
				return (Packet) constructor.newInstance(conn, tag);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("ID not found");
		}
		return null;
	}

	public static String getName() {
		return serverName;
	}

	public static void setName(String name) {
		serverName = name;
	}
}

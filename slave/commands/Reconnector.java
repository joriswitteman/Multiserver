package joris.multiserver.slave.commands;

import java.util.Timer;
import java.util.TimerTask;

import joris.multiserver.slave.MSS;

public class Reconnector {

	private Timer		timer;

	class RemindTask extends TimerTask {

		@Override
		public void run() {
			if (!MSS.TCPClient.isConnected()) {
				MSS.connect();
			}
			if (MSS.TCPClient.isConnected()) {
				this.cancel();
			}
		}
	}

	public Reconnector() {
		this.timer = new Timer();
		this.timer.scheduleAtFixedRate(new RemindTask(), 30000, 30000);
	}
}

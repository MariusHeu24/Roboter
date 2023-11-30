package de.vit.minimalbot;

import de.vitbund.netmaze.connector.NetMazeConnector;

public class Main {
	public static void main (String [] args) {
		MinimalBot bot = new MinimalBot();
		NetMazeConnector connector = new NetMazeConnector(bot);
		connector.play();
	}
}

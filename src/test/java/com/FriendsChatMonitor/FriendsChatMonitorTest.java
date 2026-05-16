package com.FriendsChatMonitor;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class FriendsChatMonitorTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(FriendsChatMonitorPlugin.class);
		RuneLite.main(args);
	}
}
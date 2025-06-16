package com.groupiron;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class GroupTileManTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(GroupTilemanPlugin.class);
		RuneLite.main(args);
	}
}
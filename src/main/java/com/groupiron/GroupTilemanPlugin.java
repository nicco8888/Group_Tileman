package com.groupiron;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.io.IOException;

@Slf4j
@PluginDescriptor(
	name = "Group Tileman"
)
public class GroupTilemanPlugin extends Plugin {
	@Inject
	private Client client;

	@Inject
	private GroupTilemanConfig config;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Group Tileman started!");

		String baseUrl = "https://ebmbwhlajxbbziopwyto.supabase.co";
		String apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImVibWJ3aGxhanhiYnppb3B3eXRvIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDc4NTU1NTgsImV4cCI6MjA2MzQzMTU1OH0.TVBFLk1mWCsnh9CHrI5bo98xK5zeHJP-ljmRC8smS1s";

		supabaseClient = new SupabaseClient(baseUrl, apiKey);
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Group Tileman stopped!");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Group Tileman says " + config.greeting(), null);
		}
	}

	@Provides
	GroupTilemanConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(GroupTilemanConfig.class);
	}

	private WorldPoint lastPlayerLocation;

	@Inject
	private TileUnlockService tileUnlockService;

	@Provides
	TileUnlockService providetileUnlockService() {
		return new TileUnlockService();
	}

	private SupabaseClient supabaseClient;

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (client.getLocalPlayer() == null)
		{
			return;
		}

		WorldPoint currentLocation = client.getLocalPlayer().getWorldLocation();

		// First tick or player hasn't moved
		if (lastPlayerLocation != null && lastPlayerLocation.equals(currentLocation))
		{
			return;
		}

		// TODO: Check if this tile is unlocked already
		// TODO: If not unlocked, add it to storage and call SupabaseClient
		String playerName = client.getLocalPlayer().getName();
		if (!tileUnlockService.isTileUnlocked(currentLocation, playerName)) {
			tileUnlockService.unlockTile(currentLocation, playerName);

			try {
				supabaseClient.sendTile(new TileData(currentLocation.getX(), currentLocation.getY(), currentLocation.getPlane(), playerName));
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}

		lastPlayerLocation = currentLocation;
	}

}

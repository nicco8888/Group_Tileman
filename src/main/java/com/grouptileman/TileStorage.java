package com.grouptileman;

import lombok.Getter;
import net.runelite.api.coords.WorldPoint;

import java.util.HashSet;
import java.util.Set;

public class TileStorage
{
    @Getter
    private final Set<GroupTilemanData> unlockedTiles = new HashSet<>();

    public boolean isTileUnlocked(WorldPoint point, String playerName)
    {
        return unlockedTiles.contains(GroupTilemanData.fromWorldPoint(point, playerName));
    }

    public void unlockTile(WorldPoint point, String playerName)
    {
        unlockedTiles.add(GroupTilemanData.fromWorldPoint(point, playerName));
    }
}
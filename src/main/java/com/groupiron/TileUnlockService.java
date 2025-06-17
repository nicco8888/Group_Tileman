package com.groupiron;

import net.runelite.api.coords.WorldPoint;

import java.util.HashSet;
import java.util.Set;

public class TileUnlockService {
    private final Set<TileData> unlockedTiles = new HashSet<>();

    public boolean isTileUnlocked(WorldPoint point, String player) {
        return unlockedTiles.stream().anyMatch(tile ->
                tile.getX() == point.getX()
                        && tile.getY() == point.getY()
                        && tile.getPlane() == point.getPlane()
                        && tile.getPlayer().equals(player));
    }

    public void unlockTile(WorldPoint point, String player) {
        unlockedTiles.add(new TileData(point.getX(), point.getY(), point.getPlane(), player));
    }

    public Set<TileData> getUnlockedTiles() {
        return unlockedTiles;
    }
}


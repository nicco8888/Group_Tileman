package com.grouptileman;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.runelite.api.coords.WorldPoint;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class GroupTilemanData
{
    private final int x;
    private final int y;
    private final int plane;
    private final String playerName; // Optional: for multi-user support

    public static GroupTilemanData fromWorldPoint(WorldPoint wp, String playerName)
    {
        return new GroupTilemanData(wp.getX(), wp.getY(), wp.getPlane(), playerName);
    }

    public WorldPoint toWorldPoint()
    {
        return new WorldPoint(x, y, plane);
    }
}
package com.grouptileman;

import net.runelite.api.Client;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.api.Tile;
import net.runelite.api.Scene;
import net.runelite.api.Perspective;

import javax.inject.Inject;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;

public class TileOverlay extends Overlay
{
    private final Client client;
    private final TileStorage tileStorage;

    @Inject
    public TileOverlay(Client client, TileStorage tileStorage)
    {
        this.client = client;
        this.tileStorage = tileStorage;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
        setPriority(OverlayPriority.MED);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (client.getLocalPlayer() == null)
            return null;

        String localPlayerName = client.getLocalPlayer().getName();
        Scene scene = client.getScene();

        for (GroupTilemanData tile : tileStorage.getUnlockedTiles())
        {
            if (!tile.getPlayerName().equals(localPlayerName))
                continue;

            WorldPoint wp = tile.toWorldPoint();
            if (wp.getPlane() != client.getPlane())
                continue;

            net.runelite.api.Tile gameTile = scene.getTiles()[wp.getPlane()][wp.getX() - client.getBaseX()][wp.getY() - client.getBaseY()];
            if (gameTile != null)
            {
                Polygon poly = Perspective.getCanvasTilePoly(client, gameTile.getLocalLocation());
                if (poly != null)
                {
                    OverlayUtil.renderPolygon(graphics, poly, Color.PINK);
                }
            }
        }

        return null;
    }
}
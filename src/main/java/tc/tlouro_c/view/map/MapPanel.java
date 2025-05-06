package tc.tlouro_c.view.map;

import tc.tlouro_c.GameContext;
import tc.tlouro_c.entity.MapSquare;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class MapPanel extends JPanel {

    private final GameContext gameContext;
    private int tileSize = 64; // pixels - now not final so it can adjust
    private final int viewWidth = 10; // number of tiles shown horizontally
    private final int viewHeight = 10; // number of tiles shown vertically

    private BufferedImage floorSprite;
    private BufferedImage playerSprite;

    public MapPanel(GameContext context) {
        this.gameContext = context;
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        // Remove fixed preferred size to allow dynamic sizing

        // Add a component listener to handle resizing
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Recalculate tile size based on panel dimensions
                int width = getWidth();
                int height = getHeight();
                tileSize = Math.min(width / viewWidth, height / viewHeight);
                repaint();
            }
        });

        loadSprites();
    }

    private void loadSprites() {
        try {
            floorSprite = ImageIO.read(Objects.requireNonNull(getClass().getResource("/sprites/floor.png")));
            playerSprite = ImageIO.read(Objects.requireNonNull(getClass().getResource("/sprites/player.png")));
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        var map = gameContext.getMapModel().getMapGrid();
        var playerCoords = gameContext.getPlayerModel().getCoordinates();

        // Always center the view on the player
        int startX = Math.max(0, playerCoords.getX() - viewWidth / 2);
        int startY = Math.max(0, playerCoords.getY() - viewHeight / 2);

        // Ensure we don't go out of bounds on the right/bottom
        startX = Math.min(startX, map[0].length - viewWidth);
        startY = Math.min(startY, map.length - viewHeight);

        // Ensure startX and startY don't go negative
        startX = Math.max(0, startX);
        startY = Math.max(0, startY);

        // Center the map in the panel if there's extra space
        int totalMapWidth = viewWidth * tileSize;
        int totalMapHeight = viewHeight * tileSize;
        int offsetX = (getWidth() - totalMapWidth) / 2;
        int offsetY = (getHeight() - totalMapHeight) / 2;

        // Ensure offsets are never negative
        offsetX = Math.max(0, offsetX);
        offsetY = Math.max(0, offsetY);

        for (int y = 0; y < viewHeight; y++) {
            for (int x = 0; x < viewWidth; x++) {

                int mapX = startX + x;
                int mapY = startY + y;

                if (mapY >= map.length || mapX >= map[0].length) continue;
                MapSquare square = map[mapY][mapX];

                int drawX = offsetX + x * tileSize;
                int drawY = offsetY + y * tileSize;

                // Draw floor
                g.drawImage(floorSprite, drawX, drawY, tileSize, tileSize, null);

                // Draw content
                if (playerCoords.equals(square.getCoordinates())) {
                    g.drawImage(playerSprite, drawX, drawY, tileSize, tileSize, null);
                } else if (square.hasEnemy()) {
                    var enemy = square.getEnemy().orElseThrow(IllegalStateException::new);

                    BufferedImage enemySprite = null;
                    try {
                        enemySprite = ImageIO.read(Objects.requireNonNull(getClass().getResource(enemy.getSpriteSrc())));
                    } catch (IOException e) {
                        throw new IllegalStateException(e);
                    }

                    g.drawImage(enemySprite, drawX, drawY, tileSize, tileSize, null);
                }
            }
        }
    }
}

import GameObject.*;
import java.util.ArrayList;

public class World {
    private Tile[][] worldMap;
    private String[][] mapDisplay;
    private ArrayList<Actor> actors;

    World() {
        initWorld(10, 20);
    }

    World(int height, int width) {
        initWorld(height, width);
    }

     int getWorldHeight() {
         return mapDisplay.length;
     }

     int getWorldWidth() {
         return mapDisplay[0].length;
     }

    private void initWorld(int height, int width) {
        this.worldMap = new Tile[height][width];
        this.mapDisplay = new String[height][width];
        // this.tiles = new ArrayList<>(height * width);
        this.actors = new ArrayList<>();
        this.actors.add(new Player(height, width));
        for (int row= 0; row < height; row++) {
            for (int col= 0; col < width; col++) {
                if( row == 0 || row == height - 1 || col == 0 || col == width - 1) {
                    this.worldMap[row][col] = new WallTile(row, col);
                } else {
                    this.worldMap[row][col] = new FloorTile(row, col);
                }
            }
        }
        // System.out.println("Height: " + this.worldMap.length);
        // System.out.println("Width: " + this.worldMap[0].length);
    }

    public void updateWorldDisplay() {
        for (Tile[] row : this.worldMap) {
            for (Tile tile : row) {
                this.mapDisplay[tile.getY()][tile.getX()] = tile.getSymbol();
            }
        }
        for (Actor actor : this.actors) {
            this.mapDisplay[actor.getY()][actor.getX()] = actor.getSymbol();
        }
    }

    String getRow(int row) {
        return String.join("", this.mapDisplay[row]);
    }

    void drawWorld() {
        updateWorldDisplay();
        for (String[] row : this.mapDisplay) { 
            for (String tile : row) {
                System.out.print(tile);
            }
            System.out.println();
        }
    }

    private boolean tilePassable(int[] loc) {
        return this.worldMap[loc[0]][loc[1]].isPassable();
    }

    boolean updateActors(char command) {
        for (Actor actor : this.actors) {
            if (actor instanceof Player player && tilePassable(player.proposedLocation(command))) {
                player.update(command);
                return true;
            } else {
                actor.update();
            }
        }
        return false;
    }

    Player getPlayer() {
        for (Actor actor : this.actors) {
            if (actor instanceof Player player) {
                return player;
            }
        }
        return null;
    }

}

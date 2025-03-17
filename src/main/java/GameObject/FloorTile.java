package GameObject;

public class FloorTile extends Tile {

    public FloorTile(int y, int x) {
        super(Glyph.FLOOR.get(), y, x, true);
    }

    public void update(){};

    public String toString() {
        return "Floor Object: " + this.getSymbol() + "\nCurrent Pos: (" + this.getX() + ", " + this.getY() + ")\n";
    }
    
}

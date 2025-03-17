package GameObject;

public class WallTile extends Tile {

    public WallTile(int y, int x) {
        super(Glyph.WALL.get(), y, x, false);
    }

    @Override
    public void update(){};

    public String toString() {
        return "Wall Object: " + this.getSymbol() + "\nCurrent Pos: (" + this.getX() + ", " + this.getY() + ")\n";
    }
    
}

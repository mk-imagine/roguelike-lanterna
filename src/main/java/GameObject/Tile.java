package GameObject;

public abstract class Tile extends GameObject {

    public Tile(String symbol, int y, int x, boolean isPassable) {
        super(symbol, y, x, isPassable);
    }

    @Override
    public void update(){}
}

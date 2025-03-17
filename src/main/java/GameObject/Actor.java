package GameObject;

public abstract class Actor extends GameObject implements Movable {
    
    public Actor(String symbol, int y, int x) {
        super(symbol, y, x, false);
    }

    @Override
    public abstract void update();

    @Override
    public abstract void move(char direction);

    @Override
    public abstract int[] proposedLocation(char direction);
}

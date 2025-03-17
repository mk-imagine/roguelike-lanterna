package GameObject;

public class Player extends Actor {

    public Player(int yUB, int xUB) {
        super(Glyph.PLAYER.get(), (int)(Math.random() * (yUB - 2) + 1), (int)(Math.random() * (xUB - 2) + 1));
    }

    @Override
    public void update() {}

    public void update(char command) {
        this.move(command);
    }

    @Override
    public void move(char direction) {
        switch (direction) {
            case 'n': setY(getY() - 1); break;
            case 's': setY(getY() + 1); break;
            case 'w': setX(getX() - 1); break;
            case 'e': setX(getX() + 1); break; 
        }
    }

    @Override
    public int[] proposedLocation(char direction) {
        switch (direction) {
            case 'n': return new int[]{(getY() - 1), getX()};
            case 's': return new int[]{(getY() + 1), getX()};
            case 'w': return new int[]{getY(), (getX() - 1)};
            case 'e': return new int[]{getY(), (getX() + 1)};
            default: return new int[]{getY(), getX()};
        }
    }
}

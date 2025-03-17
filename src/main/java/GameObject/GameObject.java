package GameObject;

public abstract class GameObject {
    private int x;
    private int y;
    private String symbol;
    private final boolean isPassable;

    public GameObject(String symbol, int y, int x, boolean isPassable) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
        this.isPassable = isPassable;
    }

    void setX(int x) {
        this.x = x;
    }

    void setY(int y) {
        this.y = y;
    }

    public boolean isPassable() {
        return this.isPassable;
    }

    public abstract void update();

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public String toString() {
        return "GameObject.GameObject: " + this.symbol + "\nCurrent Pos: (" + this.x + ", " + this.y + ")\n";
    }
}

package GameObject;

public interface Movable {

    public void move(char direction);

    public int[] proposedLocation(char direction);
}

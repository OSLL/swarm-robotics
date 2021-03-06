package swarm_robotics.map;

public class Cell {
    private Type type;
    private int x, y;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Cell() {
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}

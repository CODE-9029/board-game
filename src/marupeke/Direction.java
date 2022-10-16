package marupeke;

public enum Direction
{
    DOWNRIGHT(1, 1),
    DOWNLEFT(-1, 1),
    RIGHT(1, 0),
    DOWN(0, 1);

    final int x;
    final int y;

    Direction(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
}

package marupeke;

public class MarupekeTile
{
    public boolean isEditable;
    public State state;
    public int numb;
    
    public MarupekeTile(boolean isEditable, State state)
    {
        this.isEditable = isEditable;
        this.state = state;
        numb = 0;
    }
}

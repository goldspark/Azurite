package ai.path;

/**
 * <h1>Azurite</h1>
 *
 * @author Juyas
 * @version 17.06.2021
 * @since 17.06.2021
 */
public class NodePath {

    private PathNode start;
    private NodePath next;

    public NodePath(PathNode start )
    {
        this.start = start;
        this.next = null;
    }

    public void setNext( NodePath next )
    {
        this.next = next;
    }

    public PathNode getStart()
    {
        return start;
    }

    public NodePath getNext()
    {
        return next;
    }

    @Override
    public String toString()
    {
        return "[" + start + "] -> " + next;
    }
}
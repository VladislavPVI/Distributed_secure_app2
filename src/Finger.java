public class Finger {
    private int id;
    private int leftInterval;
    private int rightInterval;
    private ChordNode node;

    public Finger(int id, ChordNode n, int m) {
        this.id = id;
        this.leftInterval = (int) ((n.getId() + Math.pow(2, id)) % Math.pow(2, m));
        this.rightInterval = (int) ((n.getId() + Math.pow(2, id + 1)) % Math.pow(2, m));
        this.node = n;
    }

    public int getId() {
        return id;
    }

    public int getLeftInterval() {
        return leftInterval;
    }

    public int getRightInterval() {
        return rightInterval;
    }

    public ChordNode getNode() {
        return node;
    }

    public void setNode(ChordNode node) {
        this.node = node;
    }

    @Override
    public String toString() {
        return "\n\tFinger[" + id + "]: [" + leftInterval + ", " + rightInterval + ") " + "successor: " + node.getId();
    }
}

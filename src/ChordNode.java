import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChordNode {
    //next node
    private ChordNode successor;
    //previous node
    private ChordNode predecessor;
    private int id;
    private List<Finger> fingerTable = new ArrayList<>();
    private int m;

    public ChordNode(int id, int m) {
        this.id = id;
        this.m = m;
        for (int k = 0; k < m; k++)
            this.fingerTable.add(new Finger(k, this, m));

    }

    @Override
    public String toString() {
        return "ChordNode " +
                "id=" + id +
                ", fingerTable=" + fingerTable;
    }

    public ChordNode getSuccessor() {
        return successor;
    }

    public int getId() {
        return id;
    }

    public void setSuccessor(ChordNode successor) {
        this.successor = successor;
    }

    public ChordNode getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(ChordNode predecessor) {
        this.predecessor = predecessor;
    }

    public List<Finger> getFingerTable() {
        return fingerTable;
    }


    private boolean rightBound(int number, int left, int right) {
        if (right > left)
            return number > left && number <= right;
        else {
            if (number < left)
                number = number + (int) Math.pow(2, m);
            right = right + (int) Math.pow(2, m);
            return number > left && number <= right;
        }
    }

    private boolean leftBound(int number, int left, int right) {
        if (right > left)
            return number >= left && number < right;
        else {
            if (number < left)
                number = number + (int) Math.pow(2, m);
            right = right + (int) Math.pow(2, m);
            return number >= left && number < right;
        }
    }

    private boolean bound(int number, int left, int right) {
        if (right > left)
            return number > left && number < right;
        else if (number < left)
            number = number + (int) Math.pow(2, m);
        right = right + (int) Math.pow(2, m);
        return number > left && number < right;
    }

    private ChordNode find_successor(int id) {
        ChordNode predecessor = find_predecessor(id);
        return predecessor.getSuccessor();
    }

    private ChordNode find_predecessor(int id) {
        ChordNode node = this;
        while (!rightBound(id, node.getId(), node.getSuccessor().getId())) {
            node = closet_preceding_finger(node, id);
            //System.out.println("tut" + node);
        }
        return node;
    }

    private ChordNode closet_preceding_finger(ChordNode chordNode, int id) {
        for (int i = m - 1; i >= 0; i--) {
            ChordNode fingerNode = chordNode.getFingerTable().get(i).getNode();
            if (bound(fingerNode.getId(), chordNode.getId(), id))
                return fingerNode;
        }
        return chordNode;
    }

    public void init_finger_table(ChordNode node) {
        Finger finger = this.getFingerTable().get(0);
        finger.setNode(node.find_successor(finger.getLeftInterval()));
        successor = finger.getNode();
        predecessor = successor.getPredecessor();
        successor.setPredecessor(this);
        for (int i = 0; i < m - 1; i++) {
            Finger fingerHere = fingerTable.get(i);
            Finger fingerNext = fingerTable.get(i + 1);
            if (leftBound(
                    fingerNext.getLeftInterval(),
                    this.getId(),
                    fingerHere.getNode().getId()))
                fingerNext.setNode(fingerHere.getNode());
            else fingerNext.setNode(node.find_successor(fingerNext.getLeftInterval()));


        }
    }

    public void update_others() {
        for (int i = 0; i < m; i++) {
            int id = getId() - (int) Math.pow(2, i);
            ChordNode p = find_predecessor(id);
            p.update_finger_table(this, i);
        }
    }

    public void update_othersRemove() {
        for (int i = 0; i < m; i++) {
            int id = getId() - (int) Math.pow(2, i);
            ChordNode p = find_predecessor(id);
            p.update_finger_table(this.successor, i);
        }
    }

    public void update_finger_table(ChordNode s, int i) {
        Finger fingerI = fingerTable.get(i);
        if (leftBound(s.getId(), this.getId(), fingerI.getNode().getId())) {
            fingerI.setNode(s);
            predecessor.update_finger_table(s, i);
        }
    }

    public void join(ChordNode node) {
        if (node != null) {
            init_finger_table(node);
            update_others();
        } else {
            for (Finger finger : fingerTable) {
                finger.setNode(this);
            }
            this.setPredecessor(this);
        }
    }

    public void stabilize() {
        ChordNode x = successor.getPredecessor();
        if (bound(x.getId(), id, successor.getId()))
            setSuccessor(x);
        successor.noify(this);

    }

    public void noify(ChordNode node) {
        if (predecessor == null || bound(node.getId(), predecessor.getId(), this.getId()))
            this.setPredecessor(node);
    }

    public void fix_fingers() {
        Random r = new Random();
        int i = r.nextInt(m);
        Finger fingerFix = fingerTable.get(i);
        fingerFix.setNode(find_successor(fingerFix.getLeftInterval()));
    }


    public void remove() {
        getPredecessor().setSuccessor(this.successor);
        getSuccessor().setPredecessor(this.predecessor);
        update_othersRemove();
    }


}

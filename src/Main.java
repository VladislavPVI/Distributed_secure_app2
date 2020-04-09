import java.util.*;

public class Main {

    private static List<Integer> indexesOfNode = new ArrayList<>();
    private static Map<Integer, ChordNode> nodes = new HashMap<>();

    private static void stabilize() {
        for (int i = 0; i < 100; i++) {
            for (Integer index : indexesOfNode)
                nodes.get(index).stabilize();
        }

        for (int i = 0; i < 100; i++) {
            for (Integer index : indexesOfNode)
                nodes.get(index).fix_fingers();
        }
    }

    private static void printAll() {
        for (int key : indexesOfNode)
            System.out.println(nodes.get(key));
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Input number of bits in the node identifier");
        int m = scanner.nextInt();
        System.out.println("Input node position identifiers separated by spaces");
        scanner.nextLine();
        String idPos[] = scanner.nextLine().split(" ");

        for (String str : idPos)
            indexesOfNode.add(Integer.parseInt(str));

        Collections.sort(indexesOfNode);

        ChordNode firstNode = new ChordNode(indexesOfNode.get(0), m);
        ChordNode prevNode = firstNode;
        nodes.put(indexesOfNode.get(0), firstNode);

        for (int i = 1; i < indexesOfNode.size(); i++) {
            ChordNode chordNode = new ChordNode(indexesOfNode.get(i), m);
            chordNode.setPredecessor(prevNode);
            prevNode.setSuccessor(chordNode);
            prevNode = chordNode;
            nodes.put(indexesOfNode.get(i), chordNode);
        }

        prevNode.setSuccessor(firstNode);
        firstNode.setPredecessor(prevNode);

        for (Integer index : indexesOfNode) {
            ChordNode node = nodes.get(index);
            for (Finger finger : node.getFingerTable()) {
                int left = finger.getLeftInterval();
                if (left > indexesOfNode.get(indexesOfNode.size() - 1))
                    finger.setNode(nodes.get(indexesOfNode.get(0)));
                else
                    for (Integer index2 : indexesOfNode) {
                        if (index2 >= left) {
                            finger.setNode(nodes.get(index2));
                            break;
                        }
                    }
            }
        }

        printAll();

        System.out.println("\nInput id of new node");
        int newNodeId = scanner.nextInt();

        ChordNode newChordNode = new ChordNode(newNodeId, m);
        newChordNode.join(firstNode);
        indexesOfNode.add(newNodeId);
        nodes.put(newNodeId, newChordNode);

        stabilize();
        printAll();

        indexesOfNode.remove(indexesOfNode.size() - 1);
        nodes.get(newNodeId).remove();
        nodes.remove(newNodeId);

        System.out.println("\nNode number " + newNodeId + " has been removed");
        stabilize();
        printAll();

    }
}

import bgp.BGPEventListener;
import bgp.Board;

import java.awt.*;

public class DijkstraSearch {
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private static final String ERROR = "Process completed with failure evaluation, code 0";
    private static final String WARNING = "Press READ to get graph from text file";
    private static final String PATH = "graph.txt";
    private static Graph graph;
    private static Board board;  // Game board
    private static int numVertices;

    public static void main(String[] args) {
        // Create a game board
        initBoard(graph);
    }

    // Breadth-first traversal for a graph using a queue.
    public static String getShortestPath(int start, int finish, Graph graph) {
        graph.reset();
        initMarkers();
        NodePriorityQueueVertex[] nodes = new NodePriorityQueueVertex[graph.size()];
        StringBuilder result = new StringBuilder("VERTICES HAVE VISITED: ");

        PriorityQueue<VertexString> queue = new PriorityQueue<VertexString>();

        for (int i = 0; i < graph.size(); i++) {
            NodePriorityQueueVertex nodePriorityQueue = new NodePriorityQueueVertex(graph.find(i));
            if (i == start) {
                nodePriorityQueue.setPriority(0.0);
            } else {
                nodePriorityQueue.setPriority(Double.MAX_VALUE);
            }
            nodes[i] = nodePriorityQueue;
        }

        NodePriorityQueueVertex startNode = nodes[start];

        queue.addElement(startNode);

        while (!queue.isEmpty()) {

            VertexString currentVertex = queue.removeMin();

            if (currentVertex.isVisited()) {
                continue;
            }

            double currentCost = nodes[currentVertex.getId()].getPriority();
            for (int i = 0; i < graph.size(); i++) {
                if (i != currentVertex.getId()) {
                    double cost = graph.cost(currentVertex.getId(), i);
                    if (!graph.find(i).isVisited() && cost > 0 && cost < Double.MAX_VALUE) {
                        NodePriorityQueueVertex oldNode = nodes[i];
                        double bufferCost = cost + currentCost;
                        if (oldNode.getPriority() < bufferCost) {
                            continue;
                        }
                        graph.find(i).setParent(currentVertex);
                        NodePriorityQueueVertex nextVertex = nodes[i];
                        nextVertex.setPriority(bufferCost);
                        queue.addElement(nextVertex);
                    }
                }
            }
            currentVertex.visited(true);
        }

        VertexString finishVertex = graph.find(finish);
        double cost = nodes[finishVertex.getId()].getPriority();

        // Restoring path.
        StackListADT<VertexString> stack = new StackListADT<VertexString>();
        VertexString buffer = (VertexString) finishVertex.getParent();
        while (buffer != null) {
            stack.push(buffer);
            buffer = (VertexString)buffer.getParent();
        }

        while (!stack.isEmpty()) {
            VertexString vertexString = stack.pop();
            result.append(vertexString.getContent());
            result.append(" --> ");
            if (vertexString.getId() == start) {
                board.cellBackgroundColor(0, vertexString.getId() + 1, Color.RED);
                board.cellBackgroundColor(vertexString.getId() + 1, 0, Color.RED);
                continue;
            }
            board.cellBackgroundColor(0, vertexString.getId() + 1, Color.ORANGE);
            board.cellBackgroundColor(vertexString.getId() + 1, 0, Color.ORANGE);
        }

        board.cellBackgroundColor(0, graph.find(finish).getId() + 1, Color.RED);
        board.cellBackgroundColor(graph.find(finish).getId() + 1, 0, Color.RED);
        result.append(graph.find(finish).getContent());
        result.append("\n");
        result.append("THE SHORTEST PATH WEIGHT WAS FOUND: ");
        result.append(cost);
        result.append("\n");
        result.append("Process completed with success evaluation, code 1");

        return result.toString();
    }

    private static void initMarkers() {
        for (int i = 0; i <= numVertices; i++) {
            for (int j = 0; j <= numVertices; j++) {
                if (i == 0 && j > 0) {
                    board.cellBackgroundColor(i, j, Color.lightGray);
                }
                if (i > 0 && j == 0) {
                    board.cellBackgroundColor(i, j, Color.lightGray);
                }
            }
        }
    }

    private static void resetMapping() {
        // Reset mapping on board.
        board.setText("");
        board.clearText();
        initMarkers();
    }

    private static void initBoard(Graph newGraph) {
        if (newGraph == null) {
            numVertices = 0;
            board = new Board(1, 1, WIDTH, HEIGHT);
            board.appendText(WARNING);
            board.setText("graph.txt");
        } else {
            numVertices  = newGraph.size();
            board = new Board(numVertices + 1, numVertices + 1, WIDTH, HEIGHT);
            for (int i = 0; i <= numVertices; i++) {
                for (int j = 0; j <= numVertices; j++) {
                    if (i == 0 && j > 0) {
                        board.cellContent(i, j, newGraph.find(j - 1).getContent());
                    }
                    if (i > 0 && j == 0) {
                        board.cellContent(i, j, newGraph.find(i - 1).getContent());
                    }
                }
            }
            initMarkers();
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    double value = newGraph.cost(i, j);
                    if (value == Double.MAX_VALUE) {
                        board.cellBackgroundColor(i + 1, j + 1, Color.pink);
                    } else if (value == 0.0) {
                        board.cellBackgroundColor(i + 1, j + 1, Color.lightGray);
                    } else {
                        board.cellContent(i + 1, j + 1, String.valueOf(value));
                        board.cellBackgroundColor(i + 1, j + 1, Color.green);
                    }
                }
            }
        }

        board.button1SetName("SEARCH");
        board.button2SetName("READ");
        board.button1ClickEventListener(new BGPEventListener() {
            @Override
            public void clicked(int row, int col) {
                if (graph == null) {
                    resetMapping();
                    board.setText(PATH);
                    board.appendText(WARNING);
                    return;
                }
                String line = board.getText();
                resetMapping();
                String[] input = line.split("[\\s]+");
                if (input.length != 2) {
                    board.appendText(ERROR);
                    return;
                }
                VertexString start = graph.find(input[0]);
                VertexString finish = graph.find(input[1]);
                if (start == null || finish == null) {
                    board.appendText(ERROR);
                } else {
                    StringBuilder builder = new StringBuilder();
                    builder.append(start.getContent());
                    builder.append(" --> ");
                    builder.append(finish.getContent());
                    builder.append("\n");
                    builder.append(getShortestPath(start.getId(), finish.getId(), graph));
                    board.appendText(builder.toString());
                }
            }
        });

        board.button2ClickEventListener(new BGPEventListener() {
            @Override
            public void clicked(int row, int col) {
                String path = board.getText();
                board.dispose();
                resetMapping();
                if (path == null || path.isEmpty()) {
                    path = PATH;
                }
                try {
                    graph = Reader.getGraph(path);
                    initBoard(graph);
                } catch (Exception e) {
                    resetMapping();
                    board.setText(PATH);
                    board.appendText(e.getMessage());
                }
            }
        });
    }
}

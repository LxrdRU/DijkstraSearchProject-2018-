import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Class to read data from text file;
 */
public class Reader {
    private static final String BORDER = "[\\s]+";

    /**
     * Method reads text file.
     *
     * @param path Path to file.
     * @return Graph of VertexString objects with those adjacency matrix.
     */
    public static Graph getGraph(String path) throws Exception {
        int numberOfNodes;
        Scanner scanner;
        try {
            scanner = new Scanner(new File(path));
        } catch (IOException e) {
            return null;
        }
        String line;
        scanner.nextLine();  // Skip comments.
        line = scanner.nextLine();
        numberOfNodes = Integer.parseInt(line);
        Graph graph = new Graph(numberOfNodes);
        scanner.nextLine(); // Skip comments.
        for (int id = 0; id < numberOfNodes; id++) {
            line = scanner.nextLine();
            graph.keepVertex(id, new VertexString(id, line.split(BORDER)[1]));
        }
        scanner.nextLine(); // Skip comments.
        for (int i = 0; i < numberOfNodes; i++) {
            line = scanner.nextLine().trim();
            String[] adjacencyString = line.split(BORDER);
            for (int j = 0; j < numberOfNodes; j++) {
                double cost = Double.parseDouble(adjacencyString[j]);
                if (i == j) {
                    graph.setNeighbors(i, j, 0);
                    continue;
                }
                if (cost < 0) {
                    graph.setNeighbors(i, j, Double.MAX_VALUE);
                    continue;
                }
                graph.setNeighbors(i, j, cost);
            }
        }
        scanner.close();
        return graph;
    }
}

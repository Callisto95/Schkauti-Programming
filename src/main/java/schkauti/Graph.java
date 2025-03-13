package schkauti;

import java.util.*;

public class Graph {
	public static final int HAS_CONNECTION = 1;
	
	private final int[][]     graphData;
	private final boolean[][] usedEdges;
	
	public Graph(final int[][] graph) {
		this.graphData = graph;
		final int numVertices = graph.length;
		this.usedEdges = new boolean[numVertices][numVertices];
	}
	
	public void addEdge(final int u, final int v) {
		this.graphData[u][v] = 1;
		this.graphData[v][u] = 1;
	}
	
	public void printComponent(final boolean[] component) {
		final StringBuilder buffer = new StringBuilder("[");
		for (int i = 0; i < component.length; i++) {
			if (component[i]) {
				buffer.append(i);
				buffer.append(", ");
			}
		}
		
		buffer.delete(buffer.length() - 2, buffer.length());
		
		buffer.append("]");
		System.out.println(buffer);
	}
	
	// Berechnet die vom start erreichbaren Knoten mittels Tiefensuche (Depth-First-Search, DFS)
	public boolean[] calcDFS(final int start) {
		final boolean[] usedVertex = new boolean[this.graphData.length];
		usedVertex[start] = true;
		
		// LinkedList implements the Queue interface and as such may be used as one
		final LinkedList<Integer> queue = new LinkedList<>();
		queue.push(start);
		
		while (!queue.isEmpty()) {
			final int currentNode = queue.pop();
			
			for (int i = 0; i < this.graphData.length; i++) {
				if (HAS_CONNECTION == this.graphData[currentNode][i] && !usedVertex[i]) {
					queue.push(i);
					usedVertex[i] = true;
				}
			}
		}
		
		return usedVertex;
	}
	
	// Gibt eine Eulertour auf der Konsole aus.
	public String calcEulertour(final int start) {
		final StringBuilder builder = new StringBuilder();
		
		builder.append(start);
		builder.append("->");
		
		for (int i = 0; i < this.graphData.length; i++) {
			if (HAS_CONNECTION == this.graphData[start][i] && !this.usedEdges[start][i]) {
				this.usedEdges[start][i] = true;
				this.usedEdges[i][start] = true;
				
				builder.append(this.calcEulertour(i));
			}
		}
		
		if ('>' == builder.charAt(builder.length() - 1)) {
			builder.delete(builder.length() - 2, builder.length());
		}
		
		return builder.toString();
	}
	
	public static void main(final String[] args) {
		final int[][] matrix = {
			{ 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
			{ 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0 },
			{ 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0 },
			{ 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0 },
			{ 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0 },
			{ 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1 },
			{ 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0 },
			{ 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1 },
			{ 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0 }
		};
		
		final Graph     graph = new Graph(matrix);
		final boolean[] dfs   = graph.calcDFS(0);
		graph.printComponent(dfs);
		
		graph.addEdge(1, 7);
		graph.addEdge(3, 7);
		graph.addEdge(2, 7);
		final String eulertour = graph.calcEulertour(0);
		System.out.println(eulertour);
	}
}

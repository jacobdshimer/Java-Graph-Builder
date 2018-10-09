import java.util.*;

public class Graph {
    private int v; //List of vertices
    private ArrayList<Integer>[] adj;  //The list of adjacency's

    Graph(int v){
        this.v = v;
        adj = new ArrayList[v];

        for (int i = 0; i < adj.length; i++) adj[i] = new ArrayList<Integer>();

    }

    // Add an edge to the graph
    public void addEdge(int vertex, int edge){
        adj[vertex].add(edge);
    }

    public ArrayList<Integer> depthFirstTraversal(int s){
        ArrayList<Integer> results = new ArrayList<Integer>();
        Vector<Boolean> visited = new Vector<Boolean>(v);
        for (int i = 0; i< v; i++) visited.add(false);

        // Create the Depth First Traversal stack
        Stack<Integer> stack = new Stack<>();

        // Push the current source node
        stack.push(s);

        while(!stack.empty()){
            // Pop a vertex from stack and print it

            s = stack.peek();
            stack.pop();

            // Print the popped item only if it is not visited
            if (!visited.get(s)) {
                results.add(s);
                visited.set(s, true);
            }

            // Get all adjacent vertices of the popped vertex s
            // If an adjacent hasn't been visited, the push it back to the stack

            Iterator<Integer> itr = adj[s].iterator();

            while (itr.hasNext()) {
                Integer v = itr.next();
                if(!visited.get(v)) stack.push(v);
            }
        }

        return results;
    }
}

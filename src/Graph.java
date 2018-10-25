// This is the Graph Class. This class contains methods to add edges, perform DFT (DFS) and check if a graph is cyclic

import java.util.*;

public class Graph {
    //List of vertices
    private int numOfVertices;
    //The list of adjacency's
    private ArrayList[] adjacency;

    // Constructor for the Graph
    protected Graph(int numOfVertices){
        this.numOfVertices = numOfVertices;
        this.adjacency = new ArrayList[numOfVertices];

        // Create an ArrayList for each
        for (int i = 0; i < adjacency.length; i++) adjacency[i] = new ArrayList<>();
    }

    // Add an edge to the graph
    protected void addEdge(int vertex, int edge){ adjacency[vertex].add(edge);}

    protected ArrayList<Integer> depthFirstTraversal(int node){
        // Declaring variables that will be used:
        // results will hold the final results from the search
        // visited will hold whether it is visited or not
        // stack is the DFS stack
        ArrayList<Integer> results = new ArrayList<>();
        ArrayList<Boolean> visited = new ArrayList<>(numOfVertices);
        Stack<Integer> stack = new Stack<>();

        //Set visited to false for all of the edges
        for (int i = 0; i< numOfVertices; i++) visited.add(false);


        // Push the node that we are starting our search from to the stack
        stack.push(node);

        //While the stack has elements
        while(!stack.empty()){
            // Get the element from the top of the stack before popping it off
            node = stack.peek();
            stack.pop();

            // Save the popped item only if it is not visited
            if (!visited.get(node)) {
                results.add(node);
                visited.set(node, true);
            }

            // Get all of the edges of the current node
            // If an edge hasn't been visited, the push it back to the stack
            for (Integer edge : (Iterable<Integer>) adjacency[node]) {

                if (!visited.get(edge)) {
                    stack.push(edge);

                }
            }


        }

        // return the ArrayList results
        return results;
    }

    protected boolean hasCycle() {
        // Mark all the vertices as not visited and not part of the recursion stack
        boolean[] visited = new boolean[numOfVertices];
        boolean[] inStack = new boolean[numOfVertices];


        // Call the recursive version of method
        for (int i = 0; i < numOfVertices; i++ ){
            // If the recursive method returns true, return true to the calling program
            if (hasCycle(i, visited, inStack)) {
                return true;
            }
        }
        return false;
    }

    // This is the recursive version of hasCycle
    private boolean hasCycle(int i, boolean[] visited, boolean[] inStack) {

        // If i is already in the stack, return true that this contains a cycle
        if (inStack[i]) return true;
        // If i has already been visited, return false
        if (visited[i]) return false;

        // Mark the current node as visited and part of recursion stack
        visited[i] = true;
        inStack[i] = true;
        List<Integer> children = adjacency[i];

        // Loop through the the children with child.  Recursively call the isCyclicUtility
        for (Integer child: children) {
            // If the utility method comes back true for the children, return true
            if (hasCycle(child, visited, inStack)) {
                return true;
            }
        }

        // Set inStack at i to false
        inStack[i] = false;
        return false;
    }

}

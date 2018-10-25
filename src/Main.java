//This is the main part of the application
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.awt.GridBagConstraints.BOTH;

public class Main {


    //Plug and play frame creation, pretty much making sure that if the title or size isn't specified, then this will set them
    private class frame extends JFrame{
        static final int WIDTH = 300, HEIGHT = 300;
        public frame() {
            super("Base Frame");
            setFrame(WIDTH, HEIGHT);
        }
        public frame(String title) {
            super(title);
            setFrame(WIDTH, HEIGHT);
        }
        private frame(String title, int width, int height) {
            super(title);
            setFrame(width, height);
        }
        protected void display() {
            setVisible(true);
        }
        private void setFrame(int width, int height) {
            setSize(width, height);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }

    //This is the creation of the JPanel
    private class GraphDependencyPanel extends JPanel {
        //Create a new Infix object
        //Infix infix = new Infix();

        //create labels
        private JLabel fileLabel = new JLabel("Input File Name:", JLabel.CENTER);
        private JLabel recompileLabel = new JLabel("Class To Recompile:", JLabel.CENTER);

        //create text fields
        private JTextField fileField = new JTextField("", 20);
        private JTextField recompileField = new JTextField("", 20);

        //Create Button Fields
        private JButton buildDirectedGraph = new JButton("Build Directed Graph");
        private JButton topologicalOrder = new JButton("Topological Order");

        // Result Pane
        private JTextPane result = new JTextPane();
        // Create a StyledDocument and set it as results Styles Document
        StyledDocument doc = result.getStyledDocument();

        // Map for the the mapping of Classes to Integers
        private Map<String, Integer> map = new HashMap<>();
        // Graph Object, this will be constructed once we have our HashMap created
        private Graph graph;


        private GraphDependencyPanel () {
            GridBagLayout grid = new GridBagLayout();
            GridBagConstraints gbc = new GridBagConstraints();
            setLayout(grid);
            setBackground(Color.lightGray);

            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(grid);
            GridBagConstraints inputConstraint = new GridBagConstraints();

            // This is all just tweaking to get the borders and sizes to meet the specification.  You can see here I'm just playing
            // around with the weights and grid locations.
            inputPanel.setBorder(BorderFactory.createTitledBorder(" "));
            inputConstraint.weightx = 1;
            inputConstraint.gridx = 0;
            inputConstraint.gridy = 0;
            inputPanel.add(fileLabel, inputConstraint);
            inputConstraint.gridx = 1;
            inputConstraint.gridy = 0;
            inputPanel.add(fileField, inputConstraint);
            inputConstraint.gridx = 2;
            inputConstraint.gridy = 0;
            inputPanel.add(buildDirectedGraph, inputConstraint);
            inputConstraint.gridx = 0;
            inputConstraint.gridy = 1;
            inputPanel.add(recompileLabel, inputConstraint);
            inputConstraint.gridx = 1;
            inputConstraint.gridy = 1;
            inputPanel.add(recompileField, inputConstraint);
            inputConstraint.gridx = 2;
            inputConstraint.gridy = 1;
            inputPanel.add(topologicalOrder, inputConstraint);
            inputPanel.setBackground(Color.lightGray);


            //Create the outputPanel
            JPanel outputPanel = new JPanel();
            outputPanel.setLayout(new BorderLayout());
            outputPanel.setBorder(BorderFactory.createTitledBorder("Recompilation Order"));
            // Set the text to the center of the JTextPane
            SimpleAttributeSet center = new SimpleAttributeSet();
            StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
            doc.setParagraphAttributes(0, doc.getLength(), center, false);
            result.setEditable(false);

            outputPanel.add(result);

            // More playing with GridBagConstraints to setup the input and output panels
            gbc.fill = BOTH;
            gbc.weightx = 1;
            gbc.weighty = 1;
            gbc.gridy = 0;
            add(inputPanel, gbc);
            gbc.gridheight = 5;
            gbc.ipady = 100;
            gbc.gridy = 6;
            add(outputPanel, gbc);

            //Create a listener on the buildGraph and buildTopologicalOrder buttons
            buildDirectedGraph.addActionListener(e -> buildGraph());
            topologicalOrder.addActionListener(e -> buildTopologicalOrder());


        }

        private void buildGraph(){
            // Create a File object called file from the fileField.getText()
            File file = new File(fileField.getText());
            int count = 0;

            // Create two BufferedReaders, one will handle getting the data to the HashMap, the other to addEdges
            // I don't really like this implementation, but this was the only way I figured out how to create the HashMap
            // and add the edges to the graph using the same data.  The main problem is that the size of the HashMap is
            // used to construct the graph object that will handle adding the edges to the vertices
            BufferedReader mapReader;
            BufferedReader edgeReader;

            // Try-Catch for error checking
            try {
                mapReader = new BufferedReader(new FileReader(file));
                edgeReader = new BufferedReader(new FileReader(file));
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, "File Not Found","Message", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String theData;
            // IOException error handling
            try {
                if (mapReader != null) {
                    while ((theData = mapReader.readLine())!=null){
                        String[] splitString = theData.split(" ");
                        for (String split : splitString) {
                            if (map.get(split) != null) {
                                continue;
                            } else {
                                map.put(split, count);
                                count++;
                            }

                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Construct the graph by passing the size of map to the Graph constructor
            graph = new Graph(map.size());

            // IOException error handling
            try {
                //If edge reader is not null
                if (edgeReader != null) {

                    //Assign the line to theData

                    //While theData is not equal to null
                    while ((theData = edgeReader.readLine()) != null){
                        String[] splitString = theData.split(" ");
                        //Split the string and add everything except the 0th element as an edge.
                        //g.addEdge is looking for two items, the vertex and the edge
                        //We are giving it the map's key value for the key at splitString[0] as the vertex
                        //and the map's key value for the key at splitString[i] as the edge
                        //splitString[i] is whatever is at the ith place
                        for (int i = 1; i < splitString.length; i++){
                            graph.addEdge(map.get(splitString[0]), map.get(splitString[i]));
                        }
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error " + e.getMessage(), "Message", JOptionPane.ERROR_MESSAGE);
            }

            // Check if the graph contains a cycle. I chose to do it this way instead of within my depth first search function in order to throw the error out when the
            // user creates the graph instead of when the user searches the graph.  I felt like this made more sense.
            // If the graph comes back as having a cycle, throw a JOptionPane with an error message
            if(graph.hasCycle()) {
                JOptionPane.showMessageDialog(null, "Warning: This graph contains cycles", "Message", JOptionPane.WARNING_MESSAGE);
            }
            // If everything worked, display that the graph was built successfully
            JOptionPane.showMessageDialog(null, "Graph Built Successfully", "Message", JOptionPane.INFORMATION_MESSAGE);
        }

        // This builds the topological order (i.e. searches the graph starting at the given class)
        private void buildTopologicalOrder(){
            ArrayList<Integer> results = new ArrayList<>();

            // Try catch statement to check if the user provided a Class that is within the text file or if the user even provided a class
            try{
                results = graph.depthFirstTraversal(map.get(recompileField.getText()));
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(null, "You must specify a Class to recompile", "Message", JOptionPane.ERROR_MESSAGE);
            }

            // This is a map utility to search an HashMap by value instead of key
            // This is needed because the depth first search method returns an array list of numbers not classes
            MapUtility utility = new MapUtility();
            StringBuilder done = new StringBuilder();
            for (Integer result : results) {
                done.append(utility.getKeyFromValue(map, result)).append(" ");
            }
            result.setText(done.toString());
        }



    }
    // Actually call the frame and
    private class ClassDependencyGraph extends frame {
        ClassDependencyGraph(String title, int width, int height) {
            super(title, width, height);
            add(new GraphDependencyPanel());
        }
    }

    public static void main(String[] args) {
        //Cool way I found to create width and height for a GUI from a video game creation series I've been following
        int width = 600;
        int height = width / 16 * 9;
        ClassDependencyGraph dgApp = new Main().new ClassDependencyGraph("Class Dependency Graph", width, height);

        dgApp.display();


    }
}
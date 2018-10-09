import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        File file = new File("src/example_1.txt");
        int count = 0;


        BufferedReader br = null;
        BufferedReader edgeReader = null;
        try {
            br = new BufferedReader(new FileReader(file));
            edgeReader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String st;
        try {
            if (br != null) {
                while ((st = br.readLine())!=null){
                    String[] splitString = st.split(" ");
                    for (int i = 0; i < splitString.length; i++){
                        if (map.get(splitString[i]) != null){
                            continue;
                        } else {
                            map.put(splitString[i],count);
                            count++;
                        }

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Graph g = new Graph(map.size());
        try {
            if (edgeReader != null) {
                while ((st = edgeReader.readLine())!=null){
                    String[] splitString = st.split(" ");

                    for (int i = 1; i < splitString.length; i++){
                        g.addEdge(map.get(splitString[0]), map.get(splitString[i]));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        /*
        A = 0
        C = 1
        E = 2
        B = 3
        F = 4
        H = 5
        D = 6
        G = 7
        I = 8

        */
        System.out.println("Following is the Depth First Traversal");
        ArrayList<Integer> results = g.depthFirstTraversal(0);
        MapUtility utility = new MapUtility();
        for(int i = 0; i < results.size(); i++) {
            System.out.print(utility.getKey(map, results.get(i)) + " ");
        }

    }
}

// This is utility class to get the key of a map by its value
import java.util.Map;


class MapUtility {

    //This method will search through a map and return its key when provided a value
    public Object getKeyFromValue(Map map, Object value) {
        //For each map key, set it to Object o
        for(Object o: map.keySet()) {
            // If the ket at map.get(o) is equal to the value we passed in the method call, return that object o
            // Else return null
            if(map.get(o).equals(value)){
                return o;
            }
        }
        return null;
    }

}

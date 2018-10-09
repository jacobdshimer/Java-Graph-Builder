import java.util.Map;

class MapUtility {
    public <K, V> K getKey(Map<K, V> map, V value) {
        for(Map.Entry<K, V> entry : map.entrySet()){
            if(value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}

import java.util.HashMap;
import java.util.Map;

public class CountMapImpl<T> implements CountMap<T> {

    private Map<T, Integer> myMap = new HashMap<>();

    @Override
    public void add(T key) {
        if (myMap.containsKey(key)) {
            myMap.put(key, myMap.get(key) + 1);
        } else {
            myMap.put(key, 1);
        }
    }

    @Override
    public int getCount(T key) {
        return myMap.getOrDefault(key, 0);
    }

    @Override
    public int remove(T key) {
        if (myMap.containsKey(key)) {
            return myMap.remove(key);
        } else {
            return 0;
        }
    }

    @Override
    public int size() {
        return myMap.size();
    }

    @Override
    public void addAll(CountMap<? extends T> map) {
        for (Map.Entry<? extends T, Integer> entry: map.toMap().entrySet()) {
            if (myMap.containsKey(entry.getKey())) {
                myMap.put(entry.getKey(), myMap.get(entry.getKey()) + entry.getValue());
            } else {
                myMap.put(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public Map<T, Integer> toMap() {
        return myMap;
    }

    @Override
    public void toMap(Map<? super T, Integer> destination) {
        destination.clear();
        destination.putAll(myMap);
    }
}

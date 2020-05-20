import java.util.Map;

public interface CountMap<T> {
    void add(T key);

    int getCount(T key);

    int remove(T key);

    int size();

    void addAll(CountMap<? extends T> source);

    Map<T, Integer> toMap();

    void toMap(Map<? super T, Integer> destination);
}

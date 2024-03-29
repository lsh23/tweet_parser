import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MapSorter {
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValues(Map<K, V> map) {
		List<Entry<K, V>> list = new LinkedList<Entry<K, V>>(map.entrySet());
		
		Collections.sort(list, new Comparator<Entry<K, V>>() {
			public int compare(Entry<K, V> o1, Entry<K, V> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
}

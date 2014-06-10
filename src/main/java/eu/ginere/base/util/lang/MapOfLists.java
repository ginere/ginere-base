package eu.ginere.base.util.lang;
/**
 * Copyright: Angel-Ventura Mendo Gomez
 *	      ventura@free.fr
 *
 * $Id: MapOfLists.java 21 2007-06-23 00:22:03Z ventura $
 */


import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is a map of lists. The lists and the maps are syncronized to avoid concurent access 
 * problems
 *
 * @author Angel-Ventura Mendo Gomez
 * @version $Revision: 21 $ $Date: 2007-06-23 02:22:03 +0200 $
 */
public class MapOfLists<K,V> {

	private final Map<K,List<V>> map=new ConcurrentHashMap<K,List<V>>();
	
	public MapOfLists(){

	}

	/**
	 * This add the element value at the end of the list pointed by key
	 */
	public void put(K key,V value){
		List<V> list;
		if (map.containsKey(key)){
			list=(List<V>)map.get(key);
		} else {
			list=new Vector<V>();
			map.put(key,list);
		}		
		list.add(value);		
	}

	/**
	 * This add the element value at the end of the list pointed by key
	 */
	public List<V> get(K key){
		return (List<V>)map.get(key);
	}
	
	public boolean containsKey(K key){
		return (map.containsKey(key));
	}

	public void remove(K key){
		map.remove(key);
	}

	/*
	public void remove(String key,Object object){
		if (containsKey(key)){
			List list=get(key);
			list.remove(object);
			if (list.size()==0){
				map.remove(key);
			}
		}
	}
	*/

	public Collection<List <V>> values(){
		return map.values();
	}
	
	public Set<K> keySet(){
		return map.keySet();
	}
}

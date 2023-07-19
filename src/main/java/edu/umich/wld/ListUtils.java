package edu.umich.wld;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListUtils {
	
	public static <T> List<T> makeListFromCollection(Collection<T> collection_to_list) {
	
		List<T> lst = new ArrayList<T>();
		for (T entry : collection_to_list)
			lst.add(entry);
		return lst;
	}
	
	public static <T> List<T> uniqueEntries(List<T> list) {
		
		Map<T, String> map = new HashMap<T, String>();
		for (int i = 0; i < list.size(); i++)
			map.put(list.get(i), null);
		
		List<T> lst = new ArrayList<T>();
		for (T key : map.keySet())
			lst.add(key);
		
		return lst;
	}
	
	public static <T> boolean isEmptyOrNull(List<T> list) {
		return list == null || list.size() < 1;
	}

	public static List<IndexListItem<Double>> sortedList(Double [] data) {
		List<IndexListItem<Double>> sortedIndexList = new ArrayList<IndexListItem<Double>>();
		
		for (int i = 0; i < data.length; i++) {
			IndexListItem<Double> item = new IndexListItem<Double>(data[i], i);
			sortedIndexList.add(item);
		}
	
		Collections.sort(sortedIndexList, new Comparator<IndexListItem<Double>>() {
			public int compare(IndexListItem<Double> item1, IndexListItem<Double> item2) {
				try {
					return item1.getValue().compareTo(item2.getValue());
				} catch (NullPointerException npe) {
					npe.printStackTrace();
					return 0;
				} catch (ClassCastException cce) {
					cce.printStackTrace();
					return 0;
				}
		    }
		});
		return sortedIndexList;
	}
	
	public static List<IndexListItem<Integer>> sortedList(Integer [] data) {
		List<IndexListItem<Integer>> sortedIndexList = new ArrayList<IndexListItem<Integer>>();
		
		for (int i = 0; i < data.length; i++) {
			IndexListItem<Integer> item = new IndexListItem<Integer>(data[i], i);
			sortedIndexList.add(item);
		}
	
		Collections.sort(sortedIndexList, new Comparator<IndexListItem<Integer>>() {
			public int compare(IndexListItem<Integer> item1, IndexListItem<Integer> item2) {
				try {
					return item1.getValue().compareTo(item2.getValue());
				} catch (NullPointerException npe) {
					npe.printStackTrace();
					return 0;
				} catch (ClassCastException cce) {
					cce.printStackTrace();
					return 0;
				}
		    }
		});
		return sortedIndexList;
	}
	
	public static Boolean alreadySorted(Double [] data, Boolean fIncreasing) {
		for (int i = 0; i < data.length - 1; i++) {
			if (fIncreasing && data[i] > data[i + 1]) {
				return false;
			}
			if (!fIncreasing && data[i] < data[i + 1]) {
				return false;
			}
		}
		return true;
	}
	
	public static Boolean alreadySorted(Integer [] data, Boolean fIncreasing) {
		for (int i = 0; i < data.length - 1; i++) {
			if (fIncreasing && data[i] > data[i + 1]) {
				return false;
			}
			if (!fIncreasing && data[i] < data[i + 1]) {
				return false;
			}
		}
		return true;
	}
	
	public static List<IndexListItem<Double>> identityList(Double [] data) {
		List<IndexListItem<Double>> sortedIndexList = new ArrayList<IndexListItem<Double>>();
		
		for (int i = 0; i < data.length; i++) {
			IndexListItem<Double> item = new IndexListItem<Double>(data[i], i);
			sortedIndexList.add(item);
		}
		return sortedIndexList;
	}
	
	public static List<IndexListItem<Integer>> identityList(Integer [] data) {
		List<IndexListItem<Integer>> sortedIndexList = new ArrayList<IndexListItem<Integer>>();
		
		for (int i = 0; i < data.length; i++) {
			IndexListItem<Integer> item = new IndexListItem<Integer>(data[i], i);
			sortedIndexList.add(item);
		}
		return sortedIndexList;
	}
	
	
	public static Integer firstAvailableCol(List<Integer> mappedCols, Integer nCols) {
		for (int i = 0; i < nCols; i++) {
			if (!mappedCols.contains(i)) {
				mappedCols.add(i);
				return i;
			}
		}
		return 0;
	}
	
	
	public static Integer lastAvailableCol(List<Integer> mappedCols, Integer nCols) {
		for (int i = nCols - 1; i >= 0; --i) {
			if (!mappedCols.contains(i)) {
				mappedCols.add(i);
				return i;
			}
		}
		return nCols - 1;
	}
}

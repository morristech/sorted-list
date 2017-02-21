package fr.nihilus.sortedlist;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

public class SortedListTest {

	private static final Comparator<Integer> SORTING_ASC = new Comparator<Integer>() {
		@Override
		public int compare(Integer o1, Integer o2) {
			return Integer.compare(o1, o2);
		}
	};
	
	private ArrayList<Integer> list;
	private SortedList<Integer> sortedList;
	
	@Rule
	public Timeout globalTimeout = Timeout.millis(200);
	
	@Before
	public void initList() {
		list = new ArrayList<>(5);
		list.addAll(Arrays.asList(6, 3, 7, 1, 2));
		sortedList = new SortedList<>(list, SORTING_ASC);

		assertThat(list, contains(1, 2, 3, 6, 7));
	}
	
	@Test
	public void clear() {
		sortedList.clear();
		assertThat(list, hasSize(0));
	}
	
	@Test
	public void containsElement() {
		assertThat(sortedList.contains(1), is(true));
		assertThat(sortedList.contains(3), is(true));
		assertThat(sortedList.contains(4), is(false));
		assertThat("cannot contain null elements", 
				sortedList.contains(null), is(false));
	}
	
	@Test
	public void isEmpty() {
		assertThat(sortedList, is(not(empty())));
		list.clear();
		assertThat(sortedList, is(empty()));
	}
	
	@Test
	public void size() {
		assertThat(sortedList, hasSize(5));
		list.add(8);
		assertThat(sortedList, hasSize(6));
		list.removeAll(Arrays.asList(2, 3, 8));
		assertThat(sortedList, hasSize(3));
	}
	
	@Test(expected = NullPointerException.class)
	public void addNull() {
		sortedList.add(null);
	}
	
	@Test
	public void addElement() {
		boolean listHasChanged = sortedList.add(0);
		assertThat(listHasChanged, is(true));
		assertThat(list, contains(0, 1, 2, 3, 6, 7));
		
		listHasChanged = sortedList.add(4);
		assertThat(listHasChanged, is(true));
		assertThat(list, contains(0, 1, 2, 3, 4, 6, 7));
		
		listHasChanged = sortedList.add(2);
		assertThat(listHasChanged, is(true));
		assertThat(list, contains(0, 1, 2, 2, 3, 4, 6, 7));
	}
	
	
	@Test
	public void removeIndex() {
		Integer removedValue = sortedList.remove(0);
		assertThat(removedValue, is(1));
	}
	
	@Test(expected = ClassCastException.class)
	public void removeElementIncompatibleType() {
		sortedList.remove("6");
	}
	
	@Test
	public void removeElement() {
		boolean listHasChanged = sortedList.remove(Integer.valueOf(2));
		assertThat("must return true if an item has been removed", 
				listHasChanged, is(true));
		assertThat(list, contains(1, 3, 6, 7));
		
		listHasChanged = sortedList.remove(Integer.valueOf(12));
		assertThat("must return false if the item to remove is not present", 
				listHasChanged, is(false));
		assertThat(list, contains(1, 3, 6, 7));
	}
	
	@Test
	public void containsAllElements() {
		assertThat(sortedList.containsAll(Arrays.asList(1, 2, 3)), is(true));
	}
	
	@Test
	public void addAllElements() {
		boolean listHasChanged = sortedList.addAll(Arrays.asList(12, 5, 9));
		assertThat(listHasChanged, is(true));
		assertThat(list, hasSize(8));
		assertThat(list, contains(1, 2, 3, 5, 6, 7, 9, 12));
	}
	
	@Test
	public void removeAllElements() {
		boolean listHasChanged = sortedList.removeAll(Arrays.asList(1, 3, 7));
		assertThat(listHasChanged, is(true));
		assertThat(list, contains(2, 6));
		
		listHasChanged = sortedList.removeAll(Arrays.asList(2, 7));
		assertThat(listHasChanged, is(true));
		assertThat(list, contains(6));
		
		listHasChanged = sortedList.removeAll(Arrays.asList(1, 3, 7));
		assertThat(listHasChanged, is(false));
		assertThat(list, contains(6));
	}
	
	@Test
	public void toArray() {
		Object[] array = sortedList.toArray();
		for (int i = 0; i < list.size(); i++) {
			Integer valueAtI = (Integer) array[i];
			assertThat(valueAtI, equalTo(list.get(i)));
		}
	}
	
	@Test
	public void toTypedArray() {
		Integer[] array = sortedList.toArray(new Integer[0]);
		assertThat(array, is(arrayContaining(1, 2, 3, 6, 7)));
	}
	
	@Test
	public void getIndex() {
		Integer actual = sortedList.get(3);
		Integer expected = list.get(3);
		assertThat(actual, equalTo(expected));
	}
	
}

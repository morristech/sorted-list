package fr.nihilus.sortedlist;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

/**
 * This List implementation sorts its items as they are inserted.
 * This is particularly efficient for lists that contains few items.
 * @author Thib
 *
 * @param <E>
 */
public class SortedList<E> extends AbstractList<E> {

	private final ArrayList<E> list;
	private final Comparator<? super E> sorting;

	public SortedList(Comparator<? super E> comparator) {
		this.list = new ArrayList<>();
		this.sorting = comparator;
	}
	
	SortedList(ArrayList<E> list, Comparator<? super E> comparator) {
		list.sort(comparator);
		this.list = list;
		this.sorting = comparator;
	}

	@Override
	public void clear() {
		list.clear();
	}

	/**
	 * Returns true if the list contains the specified element.
	 * This implementation binary searches the specified element in the list 
	 * using the comparison function provided at construction.
	 * @return true if the list contains the specified element.
	 * @throws ClassCastException if the type of the specified element is incompatible with this list.
	 * @throws NullPointerException if the specified element is null.
	 */
	@Override
	public boolean contains(Object o) {
		// Aucun élément null dans cette liste.
		if (o == null) {
			return false;
		}
		
		// En cas d'erreur de cast, lance une ClassCastException comme prévu.
		@SuppressWarnings("unchecked")
		E el = (E) o;
		int index = binarySearch(el);
		return (index != -1);
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public int size() {
		return list.size();
	}

	/**
	 * Add the specified element to the list. 
	 * Note that unlike common List implementations, 
	 * this implementation does not append the element at the end of the list;
	 * instead, this element's position within the list is determined by the comparison 
	 * function provided to this list at construction.
	 * @returns <code>true</code> as specified by Collection.add(E).
	 * @throws NullPointerException if the specified element is null.
	 */
	@Override
	public boolean add(E e) {
		// On n'accepte pas les éléments null.
		if (e == null) {
			throw new NullPointerException("This list implementation does not support null elements.");
		}
		
		int low = 0;
		int high = list.size() - 1;
		int index;
		while (low <= high) {
			int middle = low + (high - low) / 2;
			E median = list.get(middle);
			// TODO Insert-sort
			
		}
		return list.add(e);
	}

	/**
	 * Add the specified element to the list.
	 * Note that unlike common List implementations, 
	 * this implementation does not append the element at the end of the list;
	 * instead, this element's position within the list is determined by the comparison 
	 * function provided to this list at construction, ignoring the <code>index</code> parameter.
	 */
	@Override
	public void add(int index, E element) {
		this.add(element);
	}
	
	@Override
	public E remove(int index) {
		return list.remove(index);
	}

	@Override
	public boolean remove(Object o) {
		@SuppressWarnings("unchecked")
		E el = (E) o;
		int index = binarySearch(el);
		if (index != -1) {
			list.remove(index);
			return true;
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		boolean containsAll = true;
		for (Object o : c) {
			containsAll = containsAll && contains(o);
		}
		return containsAll;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean listChanged = false;
		for (E e : c) {
			listChanged = listChanged || add(e);
		}
		return listChanged;
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		return this.addAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean listChanged = false;
		for (Object o : c) {
			listChanged = listChanged || this.remove(o);
		}
		return listChanged;
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}

	@Override
	public boolean equals(Object o) {
		return list.equals(o);
	}

	@Override
	public int hashCode() {
		return list.hashCode();
	}

	@Override
	public String toString() {
		return list.toString();
	}

	@Override
	public E get(int index) {
		return list.get(index);
	}

	@Override
	public int indexOf(Object o) {
		if (o == null) {
			throw new NullPointerException("This list implementation does not support null elements.");
		}
		@SuppressWarnings("unchecked")
		E el = (E) o;
		return binarySearch(el);
	}
	
	@Override
	public Iterator<E> iterator() {
		return list.iterator();
	}

	private int binarySearch(E e) {
		int low = 0;
		int high = list.size() - 1;
		while (low <= high) {
			// L'objet est dans low..high ou est absent.
			int middle = low + (high - low) / 2;
			E median = list.get(middle);
			if (sorting.compare(e, median) < 0) {
				// Recherche dans la moitié inférieure
				high = middle - 1;
			} else if (sorting.compare(e, median) > 0) {
				// Recherche dans la moitié supérieure
				low = middle + 1;
			} else {
				// C'est celui-ci !
				return middle;
			}
		}
		// On n'a rien trouvé
		return -1;
	}
}

package fr.nihilus.sortedlist;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * This List implementation sorts its items as they are inserted. This is
 * particularly efficient for lists that contains few items. For ordering
 * purposes, this list does not support null elements.
 * 
 * @author Thibault Seisel
 *
 * @param <E>
 *            Type of elements contained in the list.
 */
public class SortedList<E> extends AbstractList<E> {

	private final ArrayList<E> list;
	private Comparator<? super E> sorting;

	/**
	 * Creates a new instance of a List that sorts its elements.
	 * @param comparator
	 *            Comparison function used for ordering items in the list.
	 */
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
	 * Returns true if the list contains the specified element. This
	 * implementation binary searches the specified element in the list using
	 * the comparison function provided at construction.
	 * 
	 * @return true if the list contains the specified element.
	 * @throws ClassCastException
	 *             if the type of the specified element is incompatible with
	 *             this list.
	 * @throws NullPointerException
	 *             if the specified element is null.
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
	 * Add the specified element to the list. Note that unlike common List
	 * implementations, this implementation does not append the element at the
	 * end of the list; instead, this element's position within the list is
	 * determined by the comparison function provided to this list at
	 * construction.
	 * 
	 * @param e
	 *            The element to insert into this list.
	 * 
	 * @returns <code>true</code> as specified by Collection.add(E).
	 * @throws NullPointerException
	 *             if the specified element is null.
	 */
	@Override
	public boolean add(E e) {
		if (e == null) {
			throw new NullPointerException("This list implementation does not support null elements.");
		}

		int low = 0;
		int high = list.size() - 1;
		while (low <= high) {
			int middle = low + (high - low) / 2;
			E median = list.get(middle);
			if (sorting.compare(e, median) < 0) {
				high = middle - 1;
			} else if (sorting.compare(e, median) > 0) {
				low = middle + 1;
			} else {
				// Ils sont égaux, on le place à cet endroit
				list.add(middle, e);
				return true;
			}

		}
		// On n'a pas trouvé d'élément égal, donc on le place à low
		list.add(low, e);
		return true;
	}

	/**
	 * Add the specified element to the list. Note that unlike common List
	 * implementations, this implementation does not append the element at the
	 * end of the list; instead, this element's position within the list is
	 * determined by the comparison function provided to this list at
	 * construction, ignoring the <code>index</code> parameter.
	 * 
	 * @param index
	 *            Ignored parameter
	 * @param element
	 *            The element to insert into this list
	 */
	@Override
	public void add(int index, E element) {
		this.add(element);
	}

	/**
	 * Removes the element at the specified position in the list. Shifts any
	 * subsequent elements to the left (subtracts one from their indices).
	 * 
	 * @return the element that was removed from the list.
	 * @throws ArrayIndexOutOfBoundsException
	 *             if the index is out of range (index < 0 || index >= size())
	 */
	@Override
	public E remove(int index) {
		return list.remove(index);
	}

	/**
	 * Remove the first occurrence of the specified element from the list, if it
	 * is present. This implementation binary searches for the specified element
	 * using the comparison function provided at construction, and removes it if
	 * it is found.
	 * 
	 * @return <code>true</code> if an element has been removed as a result of
	 *         this call
	 * @throws ClassCastException
	 *             if the specified element is not compatible with this list
	 */
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

	/**
	 * Returns true if this list contains all of the elements of the specified collection.
	 * This implementation binary searches for each element and if one is not present, 
	 * it stops and returns <code>false</code>.
	 * @return <code>true</code> if this list contains all the elements of the specified collection
	 * @throws ClassCastException if the specified collection contains elements of incompatible type.
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		if (c == null || c.isEmpty()) {
			return false;
		}
		
		boolean containsAll = true;
		for (Object o : c) {
			containsAll = containsAll && contains(o);
		}
		return containsAll;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		if (c == null) {
			return false;
		}
		
		boolean hasChanged = false;
		for (E e : c) {
			hasChanged = add(e) || hasChanged;
		}
		return hasChanged;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		return this.addAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		if (c == null) {
			return false;
		}
		
		boolean hasChanged = false;
		for (Object o : c) {
			hasChanged = this.remove(o) || hasChanged;
		}
		return hasChanged;
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

	/**
	 * Replaces the element at the specified position by the specified element.
	 * This implementation will always throw an
	 * {@link UnsupportedOperationException}.
	 * 
	 * @throws UnsupportedOperationException
	 *             because replacing elements would compromise ordering.
	 */
	@Override
	public E set(int index, E element) {
		throw new UnsupportedOperationException("This implementation does not support replacing elements.");
	}
	
	/**
	 * Sorts this list according to the order induced by the specified Comparator.
	 * When used on this implementation, this method also changes the comparison function 
	 * used to sort this list's elements.
	 * @param c The new comparison function to use to sort items.
	 */
	@Override
	public void sort(Comparator<? super E> c) {
		sorting = c;
		list.sort(c);
	}

	@Override
	public int indexOf(Object o) {
		if (o == null) {
			return -1;
		}
		@SuppressWarnings("unchecked")
		E el = (E) o;
		return binarySearch(el);
	}

	@Override
	public Iterator<E> iterator() {
		return list.iterator();
	}

	@Override
	public ListIterator<E> listIterator() {
		return list.listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return list.listIterator(index);
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

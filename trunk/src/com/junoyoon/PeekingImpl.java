package com.junoyoon;

import java.util.Iterator;

/**
 * Implementation of PeekingIterator that avoids peeking unless necessary.
 */
public class PeekingImpl<E> implements PeekingIterator<E> {

	private final Iterator<? extends E> iterator;
	private boolean hasPeeked;
	private E peekedElement;

	public static <T> T checkNotNull(T reference) {
		if (reference == null) {
			throw new NullPointerException();
		}
		return reference;
	}

	public PeekingImpl(Iterator<? extends E> iterator) {
		this.iterator = checkNotNull(iterator);
	}

	public boolean hasNext() {
		return hasPeeked || iterator.hasNext();
	}

	public E next() {
		if (!hasPeeked) {
			return iterator.next();
		}
		E result = peekedElement;
		hasPeeked = false;
		peekedElement = null;
		return result;
	}

	public void remove() {
		iterator.remove();
	}

	public E peek() {
		if (!hasPeeked) {
			peekedElement = iterator.next();
			hasPeeked = true;
		}
		return peekedElement;
	}
}

/**
 * Copyright (C) 2009 JunHo Yoon
 *
 * bullshtml is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * bullshtml is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 */
package com.junoyoon;

import java.util.Iterator;

/**
 * Implementation of PeekingIterator that avoids peeking unless necessary.
 */
public class PeekingIterator<E> implements Iterator<E> {

	private final Iterator<? extends E> iterator;
	private boolean hasPeeked;
	private E peekedElement;

	public static <T> T checkNotNull(T reference) {
		if (reference == null) {
			throw new NullPointerException();
		}
		return reference;
	}

	public PeekingIterator(Iterator<? extends E> iterator) {
		this.iterator = checkNotNull(iterator);
	}

	public boolean hasNext() {
		return this.hasPeeked || this.iterator.hasNext();
	}

	public E next() {
		if (!this.hasPeeked) {
			return this.iterator.next();
		}
		E result = this.peekedElement;
		this.hasPeeked = false;
		this.peekedElement = null;
		return result;
	}

	public void remove() {
		this.iterator.remove();
	}

	public E peek() {
		if (!this.hasPeeked) {
			this.peekedElement = this.iterator.next();
			this.hasPeeked = true;
		}
		return this.peekedElement;
	}
}

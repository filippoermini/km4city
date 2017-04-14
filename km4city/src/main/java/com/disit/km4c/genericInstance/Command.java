package com.disit.km4c.genericInstance;

import com.disit.km4c.exceptions.MissingAttributeExceptions;

@FunctionalInterface
public interface Command<T> {
	public T valueGenerator(Object... args) throws MissingAttributeExceptions;
}
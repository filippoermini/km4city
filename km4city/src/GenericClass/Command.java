package GenericClass;

@FunctionalInterface
public interface Command<T> {
	public T valueGenerator(Object... args);
}
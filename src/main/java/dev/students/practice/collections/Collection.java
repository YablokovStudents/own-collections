package dev.students.practice.collections;

/** Коллекция объектов. */
public interface Collection<T> extends Iterable<T> {
    /** @return количество элементов в коллекции */
    int size();

    /** @return результат проверки коллекции на пустоту (отсутствие элементов) */
    boolean isEmpty();

    /** @return результат проверки на наличие элемента {@code item} в коллекции */
    boolean contains(T item);

    /** Вставка элемента {@code item} в конец коллекции. */
    boolean add(T item);

    /**
     * Удаление элемента {@code item} из коллекции.
     * Если элемент встречается несколько раз, то будут удалены всех вхождения элемента в коллекцию.
     * Возвращается признак удаления хотя бы одного элемента из коллекции.
     */
    boolean remove(T item);

    /** Удаление всех элементов коллекции. */
    void clear();
}

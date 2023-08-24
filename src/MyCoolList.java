import java.util.*;
import java.util.function.Function;

public class MyCoolList<T extends Number> implements List<T> {
    private int capacity = 10;
    private Object[] array = new Object[capacity];
    private int size = 0;

    private boolean isIndexCorrect(int index) {
        return index >= 0 && index < size;
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public Iterator<T> iterator() {
        return new MyCoolIterator();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(array, size);
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        int lastIndex = Math.min(size, a.length);
        for (int i = 0; i < lastIndex; i++) {
            a[i] = (T1)array[i];

        }
        return a;
    }

    @Override
    public boolean add(T t) {
        add(size, t);
        return true;
    }

    private void increaseArray() {
        capacity = 2 * capacity;
        Object[] newArray = new Object[capacity];
        for (int i = 0; i < size; i++) {
            newArray[i] = array[i];
        }
        array = newArray;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index != -1) {
            remove(index);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o: c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        for (T t: c) {
            add(t);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        for (T t : c) {
            add(index, t);
            index++;
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        int oldSize = size;
            for (Object o : c) {
                removeAll(o);
            }

        return oldSize > size;
    }

    private void removeAll(Object o) {
        while (contains(o)) {
            remove(o);
        }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        int i = 0;
        boolean result = false;
        while (i < size) {
            Object o = array[i];
            if (!c.contains(o)) {
                remove(o);
                result = true;
            } else {
                i++;
            }
        }

        return result;
    }

    @Override
    public void clear() {
        size = 0;
    }

    @Override
    public T get(int index) {
        if (isIndexCorrect(index)) {
            return (T)array[index];
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public T set(int index, T element) {
        T previousElement;
        if (isIndexCorrect(index)) {
            previousElement = (T)array[index];
            array[index] = element;
        } else {
            throw new IndexOutOfBoundsException();
        }

        return previousElement;
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        for (int i = 0; i < size; i++) {
            hashCode += array[i] == null ? 0 : array[i].hashCode();
        }
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(MyCoolList.class)) {
            return false;
        }
        MyCoolList<?> list = (MyCoolList<?>) obj;
        if (this.size != list.size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (list.get(i) == null) {
                if (this.get(i) != null) {
                    return false;
                }
            } else if (!list.get(i).equals(this.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return Arrays.toString(Arrays.copyOf(array, size));
    }

    @Override
    public void add(int index, T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }

        if (size == capacity) {
            increaseArray();
        }
        for (int i = size; i > index; i--) {
            array[i] = array[i - 1];
        }
        array[index] = element;
        size++;
    }

    @Override
    public T remove(int index) {
        T result;
        if (isIndexCorrect(index)) {
            result = (T)array[index];
            for (int i = index; i < size - 1; i++) {
                array[i] = array[i + 1];
            }
            size--;
        } else {
            throw new IndexOutOfBoundsException();
        }
        return result;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if ((o == null && array[i] == null) || (o != null && o.equals(array[i]))) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if ((o == null && array[i] == null) || (o != null && o.equals(array[i]))) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public ListIterator<T> listIterator() {
        return new MyCoolListIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new MyCoolListIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        MyCoolList<T> list = new MyCoolList<>();
        list.array = Arrays.copyOfRange(this.array, fromIndex, toIndex);
        return list;
    }

    private class MyCoolIterator implements Iterator<T> {
        private int index = 0;
        private boolean wasNextCalled = false;

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public T next() {
            T result;
            if (hasNext()) {
                result = (T)array[index];
                index++;
                wasNextCalled = true;
            } else {
                throw new IndexOutOfBoundsException();
            }
            return result;
        }

        @Override
        public void remove() {
            if (wasNextCalled) {
                index--;
                MyCoolList.this.remove(index);
                wasNextCalled = false;
            } else {
                throw new IllegalStateException();
            }

        }
    }

    private class MyCoolListIterator implements ListIterator<T> {
        private int index;
        private boolean wasNextCalled = false;
        private boolean wasPreviousCalled = false;

        private MyCoolListIterator(int index) {
            this.index = index;
        }

        private MyCoolListIterator() {
            this(0);
        }

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public T next() {
            T result;
            if (hasNext()) {
                result = (T)array[index];
                index++;
                wasNextCalled = true;
                wasPreviousCalled = false;
            } else {
                throw new IndexOutOfBoundsException();
            }
            return result;
        }

        @Override
        public boolean hasPrevious() {
            return index > 0;
        }

        @Override
        public T previous() {
            T result;
            if (hasPrevious()) {
                index--;
                result = (T)array[index];
                wasPreviousCalled = true;
                wasNextCalled = false;
            } else {
                throw new IndexOutOfBoundsException();
            }
            return result;
        }

        @Override
        public int nextIndex() {
            return index;
        }

        @Override
        public int previousIndex() {
            return index - 1;

        }

        @Override
        public void remove() {
            if (wasNextCalled) {
                index--;
                wasNextCalled = false;
            } else if (wasPreviousCalled) {
                index++;
                wasPreviousCalled = false;
            } else {
                throw new IllegalStateException();
            }
            MyCoolList.this.remove(index);
        }

        @Override
        public void set(T e) {
            int currentIndex;
            if (wasNextCalled) {
                currentIndex = index - 1;
                wasNextCalled = false;
            } else if (wasPreviousCalled) {
                currentIndex = index + 1;
                wasPreviousCalled = false;
            } else {
                throw new IllegalStateException();
            }
            array[currentIndex] = e;
        }

        @Override
        public void add(T e) {
            MyCoolList.this.add(index, e);
            index++;
            wasNextCalled = wasPreviousCalled = false;
        }
    }

    public <R extends Number> MyCoolList<R> map(Function<? super T, ? extends R> function) {
        MyCoolList<R> myCoolList = new MyCoolList<>();
        for (T t: this) {
            myCoolList.add(function.apply(t));
        }
        return myCoolList;
    }


}

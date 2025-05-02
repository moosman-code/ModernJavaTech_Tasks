package bg.sofia.uni.fmi.mjt.eventbus.events;

public class PayloadImpl<T> implements Payload<T> {

    private int size;
    private T value;

    private static final int HASH_CODE_NUMBER = 31;

    public PayloadImpl(int size, T value) {
        this.size = size;
        this.value = value;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public T getPayload() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        // I'm not relying on the generic type itself, I'm only comparing
        // size and value so it's fine to not check it
        PayloadImpl<T> other = (PayloadImpl<T>) obj;
        return size == other.size && (value == null ? other.value == null : value.equals(other.value));
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(size);
        result = HASH_CODE_NUMBER * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}

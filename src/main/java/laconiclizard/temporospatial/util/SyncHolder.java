package laconiclizard.temporospatial.util;

/** A value-holder that synchronizes on get/set. */
public class SyncHolder<T> {

    public final Object lock = new Object();
    public T value;

    public SyncHolder(T value) {
        this.value = value;
    }

    public T get() {
        synchronized (lock) {
            return value;
        }
    }

    public void set(T value) {
        synchronized (lock) {
            this.value = value;
        }
    }

}

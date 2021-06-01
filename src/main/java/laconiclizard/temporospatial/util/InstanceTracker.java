package laconiclizard.temporospatial.util;

import java.util.HashSet;
import java.util.Set;

public class InstanceTracker<T> {

    public final Object lock = new Object();
    public final Set<T> instances = new HashSet<>();

    /** Utility method to add an instance to this tracker. */
    public void add(T instance) {
        synchronized (lock) {
            instances.add(instance);
        }
    }

}

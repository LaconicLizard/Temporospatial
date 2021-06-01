package laconiclizard.temporospatial.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Set;


/**
 * Tracks whether objects are flagged, including a default value for unflagged objects.
 *
 * @param <T> type of objects being tracked
 */
public class FlagTracker<T> {

    private final Object lock = new Object();  // for synchronization
    public final boolean byIdentity;  // whether tracking by identity or equality
    private final Set<T> flagged;  // set of specifically flagged objects
    private boolean allFlagged;  // if true then all objects are flagged, regardless of whether they are in .flagged

    /**
     * A new FlagTracker.
     *
     * @param byIdentity whether to track by object identity or equality
     * @param allFlagged initial value of allFlagged
     */
    public FlagTracker(boolean byIdentity, boolean allFlagged) {
        this.byIdentity = byIdentity;
        if (byIdentity) {
            flagged = Collections.newSetFromMap(new IdentityHashMap<>());
        } else {
            flagged = new HashSet<>();
        }
        this.allFlagged = allFlagged;
    }

    /**
     * Set whether the given object is flagged.
     *
     * @param obj     object of interest
     * @param flagged if true, then obj will be flagged no matter what;
     *                if false, then obj will be flagged iff allFlagged
     */
    public void setFlagged(T obj, boolean flagged) {
        synchronized (lock) {
            if (flagged) {
                this.flagged.add(obj);
            } else {
                this.flagged.remove(obj);
            }
        }
    }

    /**
     * Whether the given object is flagged.  Returns true if obj is specifically flagged (via .setFlagged(obj, true))
     * or .getAllFlagged() is true.
     *
     * @param obj object of interest
     * @return whether obj is flagged
     */
    public boolean isFlagged(T obj) {
        synchronized (lock) {
            return allFlagged || flagged.contains(obj);
        }
    }

    /**
     * Set whether all un-flagged objects are treated as flagged
     *
     * @param allFlagged whether all un-flagged objects are treated as flagged
     */
    public void setAllFlagged(boolean allFlagged) {
        synchronized (lock) {
            this.allFlagged = allFlagged;
        }
    }

    /**
     * Get whether all un-flagged objects are treated as flagged
     *
     * @return whether all un-flagged objects are treated as flagged
     */
    public boolean getAllFlagged() {
        synchronized (lock) {
            return allFlagged;
        }
    }

}

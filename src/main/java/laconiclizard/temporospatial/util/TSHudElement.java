package laconiclizard.temporospatial.util;

import laconiclizard.hudelements.api.HudElement;

public abstract class TSHudElement<C extends InstanceConfig<C>> extends HudElement {

    // if true, then this will disable itself upon exiting /alterhud
    private boolean disableOnExitAlterHud = false;
    public final C config;  // config for this TSHudElement

    public TSHudElement(C config) {
        this.config = config;
        // cannot updateFromConfig() here because subclass' instance vars may not be initialized
        // there's a java/fabric/sponge/something bug where private final fields will be set to null if you access them
        // via a super call before they are initialized
    }

    /**
     * Updates the behavior of this TSHudElement to reflect the current state of its config.
     * Assumes .lock has already been acquired.
     */
    public abstract void updateFromConfig();

    /**
     * Save all instances of this type of TSHudElement.
     * Assumes .lock has already been acquired.
     */
    public abstract void saveAll();

    /** Invoked when the /alterhud screen is entered. */
    public void enterAlterHud() {
        synchronized (lock) {
            if (!isEnabled()) {
                disableOnExitAlterHud = true;
                enable();
            }
        }
    }

    /** Invoked when the /alterhud screen is exited. */
    public void exitAlterHud() {
        synchronized (lock) {
            if (disableOnExitAlterHud) {
                disable();
                disableOnExitAlterHud = false;
            }
        }
    }

    @Override public int alterHudBorderColor() {
        synchronized (lock) {
            return disableOnExitAlterHud ?
                    0xff0000ff
                    : 0xffffffff;
        }
    }

}

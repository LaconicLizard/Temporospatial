package laconiclizard.temporospatial;

import laconiclizard.hudelements.api.HudElement;

public abstract class TSHudElement extends HudElement {

    // if true, then this will disable itself upon exiting /alterhud
    private boolean disableOnExitAlterHud = false;

    public void enterAlterHud() {
        synchronized (lock) {
            if (!isEnabled()) {
                disableOnExitAlterHud = true;
                enable();
            }
        }
    }

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

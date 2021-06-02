package laconiclizard.temporospatial.util;

public abstract class InstanceConfig_TextHE<T extends InstanceConfig_TextHE<T>> extends InstanceConfig_HE<T> {

    public int textColor = 0xffffff, backgroundColor = 0x40000000, borderColor = 0x40ffffff;
    public int borderThickness = 1;

    @Override public void load(T src) {
        super.load(src);
        textColor = src.textColor;
        backgroundColor = src.backgroundColor;
        borderColor = src.borderColor;
        borderThickness = src.borderThickness;
    }

}

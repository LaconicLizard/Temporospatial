package laconiclizard.temporospatial.util;

public class TextConfigData implements GenericConfig<TextConfigData> {

    public int textColor = 0xffffff, backgroundColor = 0x40000000, borderColor = 0x40ffffff;
    public float borderThickness = 1;

    @Override public TextConfigData newInstance() {
        return new TextConfigData();
    }

    @Override public void load(TextConfigData src) {
        textColor = src.textColor;
        backgroundColor = src.backgroundColor;
        borderColor = src.borderColor;
        borderThickness = src.borderThickness;
    }

}

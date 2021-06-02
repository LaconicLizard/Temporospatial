package laconiclizard.temporospatial.util;

import me.shedaniel.autoconfig.ConfigData;

public interface GenericConfig<T extends GenericConfig<T>> extends ConfigData {

    T newInstance();

    void load(T src);

    default T copy() {
        T result = newInstance();
        //noinspection unchecked
        result.load((T) this);
        return result;
    }

}

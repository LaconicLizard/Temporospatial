package laconiclizard.temporospatial.util;

import me.shedaniel.autoconfig.ConfigData;

public abstract class InstanceConfig<T extends InstanceConfig<T>> implements ConfigData {

    public abstract T newInstance();

    public T copy() {
        T result = newInstance();
        //noinspection unchecked
        result.load((T) this);
        return result;
    }

    public abstract void load(T src);

}

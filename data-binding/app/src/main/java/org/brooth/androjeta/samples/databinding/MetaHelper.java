package org.brooth.androjeta.samples.databinding;

import org.brooth.jeta.metasitory.MapMetasitory;
import org.brooth.jeta.metasitory.Metasitory;

/**
 * @author Oleg Khalidov (brooth@gmail.com)
 */
public class MetaHelper {

    private static MetaHelper instance;
    private final Metasitory metasitory;

    public static MetaHelper getInstance() {
        if (instance == null)
            instance = new MetaHelper("org.brooth.androjeta.samples");
        return instance;
    }

    private MetaHelper(String metaPackage) {
        metasitory = new MapMetasitory(metaPackage);
    }

    public static void applyDataBinding(Object master) {
        new DataBindController<>(getInstance().metasitory, master).apply();
    }
}
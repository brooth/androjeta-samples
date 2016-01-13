/*
 * Copyright 2015 Oleg Khalidov
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.brooth.androjeta.samples;

import android.app.Activity;
import android.os.Bundle;

import org.brooth.androjeta.retain.RetainController;
import org.brooth.androjeta.ui.FindViewController;
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

    public static void findViews(Activity activity) {
        new FindViewController(getInstance().metasitory, activity).findViews();
    }

    public static void saveRetains(Activity activity, Bundle bundle) {
        new RetainController(getInstance().metasitory, activity).save(bundle);
    }

    public static void restoreRetains(Activity activity, Bundle bundle) {
        new RetainController(getInstance().metasitory, activity).restore(bundle);
    }
}

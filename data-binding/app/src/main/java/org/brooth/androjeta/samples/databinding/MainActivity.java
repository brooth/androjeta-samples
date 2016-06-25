package org.brooth.androjeta.samples.databinding;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author Oleg Khalidov (brooth@gmail.com)
 */
@DataBind(layout = "activity_main")
public class MainActivity extends Activity {

    final User user = new User("John", "Smith");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MetaHelper.applyDataBinding(this);
    }
}

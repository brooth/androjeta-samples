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

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import org.brooth.androjeta.retain.Retain;
import org.brooth.androjeta.ui.FindView;
import org.brooth.androjeta.ui.OnClick;
import org.brooth.androjeta.ui.OnLongClick;

/**
 * @author Oleg Khalidov (brooth@gmail.com)
 */
public class SampleActivity extends BaseActivity {

    @FindView
    protected TextView textView;
    @FindView
    protected EditText editText;

    @Retain
    protected String text = "Retain text";
    @Retain
    protected int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        textView.setText(String.format("Recreated %d times", count++));
        editText.setText(text);
    }

    @OnClick
    void onClickSaveButton() {
        text = editText.getText().toString();
    }

    @OnLongClick
    void onLongClickSaveButton() {
        count = 0;
        text = "";
    }
}

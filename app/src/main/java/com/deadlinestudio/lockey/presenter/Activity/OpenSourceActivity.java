package com.deadlinestudio.lockey.presenter.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import com.deadlinestudio.lockey.R;
import com.deadlinestudio.lockey.presenter.Service.AppLockService;

public class OpenSourceActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView openText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opensource);

        mToolbar  = findViewById(R.id.OpenSourceToolbar);
        mToolbar.setTitle("오픈 소스 라이센스");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        openText = findViewById(R.id.OpenSourceText);
        openText.setMovementMethod(new ScrollingMovementMethod());
        openText.setText("## Google Auth API ##\n" +
                " * \n" +
                " * Copyright 2016 Google Inc. All Rights Reserved.\n" +
                " *\n" +
                " * Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                " * you may not use this file except in compliance with the License.\n" +
                " * You may obtain a copy of the License at\n" +
                " *\n" +
                " * http://www.apache.org/licenses/LICENSE-2.0\n" +
                " *\n" +
                " * Unless required by applicable law or agreed to in writing, software\n" +
                " * distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                " * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                " * See the License for the specific language governing permissions and\n" +
                " * limitations under the License." +
                "" +
                "" +
                "\n\n----------------------------------------------------------------\n\n ## OSS Notice | OPEN\\_KakaoSDK-Android ##\n" +
                "\n" +
                "This application is Copyright © Kakao Corp. All rights reserved.\n" +
                "\n" +
                "This application use Open Source Software (OSS). You can find the source code of these open source projects, along with applicable license information, below. We are deeply grateful to these developers for their work and contributions.\n" +
                "\n" +
                "Any questions about our use of licensed work can be sent to [opensource@kakaocorp.com][opensource_kakaocorp.com]\n" +
                "\n" +
                "Android - platform - frameworks - volley\n" +
                "\n" +
                "https://android.googlesource.com/platform/frameworks/volley\n" +
                "\n" +
                "Copyright 2011 The Android Open Source Project\n" +
                "\n" +
                "Apache License 2.0\n" +
                "\n" +
                "\n" +
                "android platform\\_frameworks\\_support\n" +
                "\n" +
                "https://android.googlesource.com/platform/frameworks/support\n" +
                "\n" +
                "Copyright 2014 The Android Open Source Project\n" +
                "\n" +
                "Apache License 2.0\n" +
                "\n" +
                "\n" +
                "Apache Commons Codec\n" +
                "\n" +
                "https://github.com/apache/commons-codec\n" +
                "\n" +
                "Copyright 2002-2016 The Apache Software Foundation\n" +
                "\n" +
                "Apache License 2.0\n" +
                "\n" +
                "\n" +
                "Asynchronous Http Client\n" +
                "\n" +
                "https://github.com/AsyncHttpClient/async-http-client\n" +
                "\n" +
                "Copyright 2015 AsyncHttpClient Project.\n" +
                "\n" +
                "Apache License 2.0\n" +
                "\n" +
                "\n" +
                "[opensource_kakaocorp.com]: mailto:opensource@kakaocorp.com" +
                "" +
                "" +
                " \n\n----------------------------------------------------------------\n\n ## MPAndroidChart ##\n" +
                "\nCopyright 2018 Philipp Jahoda *" +
                "" +
                "\n" +
                "\n" +
                "Licensed under the Apache License, Version 2.0 (the \"License\"); you may not use this file except in compliance with the License. You may obtain a copy of the License at\n" +
                "\n" +
                "http://www.apache.org/licenses/LICENSE-2.0\n" +
                "Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License." +
                "" +
                "" +
                "\n\n----------------------------------------------------------------\n\n ## Android Pull To Refresh ##\n" +
                "\n* Copyright 2011, 2012 Chris Banes. \n" +
                "* Copyright 2013 Naver Business Platform Corp. \n" +
                "* \n" +
                "* Licensed under the Apache License, Version 2.0 (the \"License\"); \n" +
                "* you may not use this file except in compliance with the License. \n" +
                "* You may obtain a copy of the License at \n" +
                "* \n" +
                "* http://www.apache.org/licenses/LICENSE-2.0 \n" +
                "* \n" +
                "* Unless required by applicable law or agreed to in writing, software \n" +
                "* distributed under the License is distributed on an \"AS IS\" BASIS, \n" +
                "* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. \n" +
                "* See the License for the specific language governing permissions and \n" +
                "* limitations under the License. \n");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

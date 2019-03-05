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
                "\n\n---------------------------------------------------------------------------------\n\n ## Facebook Auth API ##\n" +
                "\n" +
                " * Copyright (c) 2014-present, Facebook, Inc. All rights reserved.\n" +
                "\n" +
                " * You are hereby granted a non-exclusive, worldwide, royalty-free license to use,\n" +
                " * copy, modify, and distribute this software in source code or binary form for use\n" +
                " * in connection with the web services and APIs provided by Facebook.\n" +
                "\n" +
                " * As with any software that integrates with the Facebook platform, your use of\n" +
                " * this software is subject to the Facebook Developer Principles and Policies\n" +
                " * [http://developers.facebook.com/policy/]. This copyright notice shall be\n" +
                " * included in all copies or substantial portions of the software.\n" +
                "\n" +
                " * THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n" +
                " * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS\n" +
                " * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR\n" +
                " * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER\n" +
                " * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN\n" +
                " * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE." +
                "" +
                "" +
                "\n\n---------------------------------------------------------------------------------\n\n ## OSS Notice | OPEN\\_KakaoSDK-Android ##\n" +
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
                " \n\n---------------------------------------------------------------------------------\n\n ## Circular SeekBar view for Android ##" +
                "" +
                "\n\nThe MIT License (MIT)\n" +
                "\n" +
                "Copyright (c) 2013 Triggertrap Ltd\n" +
                "Author Neil Davies \n" +
                "\n" +
                "Permission is hereby granted, free of charge, to any person obtaining a copy of\n" +
                "this software and associated documentation files (the \"Software\"), to deal in\n" +
                "the Software without restriction, including without limitation the rights to\n" +
                "use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of\n" +
                "the Software, and to permit persons to whom the Software is furnished to do so,\n" +
                "subject to the following conditions:\n" +
                "\n" +
                "The above copyright notice and this permission notice shall be included in all\n" +
                "copies or substantial portions of the Software.\n" +
                "\n" +
                "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n" +
                "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS\n" +
                "FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR\n" +
                "COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER\n" +
                "IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR \n" +
                "IN\n" +
                "CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE." +
                "" +
                " \n\n---------------------------------------------------------------------------------\n\n ## MPAndroidChart ##\n" +
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
                "\n\n---------------------------------------------------------------------------------\n\n ## Android Pull To Refresh ##\n" +
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
                Intent mintent = new Intent(getApplicationContext(), MainActivity.class);
                mintent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(mintent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent mintent = new Intent(getApplicationContext(),MainActivity.class);
        mintent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(mintent);
    }
}

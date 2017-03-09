/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jyq.android.ui.qrcode;

/**
 * This class provides the constants to use when sending an Intent to Barcode Scanner.
 * These strings are effectively API and cannot be changed.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class Intents {
    private Intents() {
    }

    /**
     */
    public static final class Scan {

        public static final int RESTART_PREVIEW = 0;
        public static final int DECODE_SUCCEEDED = 1;
        public static final int DECODE_FAILED = 2;
        public static final int RETURN_SCAN_RESULT = 3;
        public static final int LAUNCH_PRODUCT_QUERY = 4;
        public static final int DECODE = 5;
        public static final int QUIT = 6;

    }
}

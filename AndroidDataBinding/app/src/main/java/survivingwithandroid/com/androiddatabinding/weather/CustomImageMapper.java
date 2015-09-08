package survivingwithandroid.com.androiddatabinding.weather;/*
 * Copyright (C) 2015, francesco Azzola 
 *
 *(http://www.survivingwithandroid.com)
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
 *
 * 27/07/15
 */

import com.survivingwithandroid.weather.lib.WeatherCode;

import survivingwithandroid.com.androiddatabinding.R;

public class CustomImageMapper {

    public static int getWeatherDrawable(int code) {


        switch (code) {
            case 900:
                return -1;
            case 901:
                return -1;
            case 902:
                return -1;
            case 212:
                return R.drawable.thundercircleday;
            case 200:
            case 210:
            case 211:
            case 232:
                return R.drawable.thundercircleday;
            case 615:
            case 616:
                return R.drawable.snowcircleday;
            case 314:
                return R.drawable.snowcircleday;
            case 300:
            case 301:
            case 302:
            case 310:
            case 311:
            case 312:
            case 313:
            case 315:
                return R.drawable.snowcircleday;
            case 511:
                return R.drawable.snowcircleday;
            case 500:
            case 501:
            case 520:
                return R.drawable.showerraincircleday;
            case 502:
            case 503:
            case 504:
            case 521:
            case 522:
                return R.drawable.showerraincircleday;
            case 600:
            case 601:
            case 620:
                return R.drawable.snowcircleday;
            case 906:
                return -1;
            case 611:
                return -1;
            case 761:
                return -1;
            case 701:
            case 741:
                return -1;
            case 711:
                return -1;
            case 721:
                return -1;
            case 905:
                return -1;
            case 903:
                return -1;
            case 802:
            case 803:
            case 804:
                return R.drawable.brokencloudscircleday;
            case 801:
                return R.drawable.brokencloudscircleday;
            case 800:
                return R.drawable.soleadocircleday;
            case 221:
                return R.drawable.thundercircleday;
            case 531:
                return R.drawable.scatteredcircleday;
            case 602:
            case 622:
                return R.drawable.snowcircleday;
            case 201:
            case 202:
            case 230:
            case 231:
                return R.drawable.thundercircleday;
            case 621:
                return R.drawable.snowcircleday;
            default:
                throw new UnsupportedOperationException();
        }
    }
}

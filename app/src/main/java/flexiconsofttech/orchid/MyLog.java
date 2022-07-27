package flexiconsofttech.orchid;

import android.util.Log;

public class MyLog {

    public final static String tag = "BHAVNAGAR";
    public final static boolean DEBUG = true;

    public static void p(String msg){

        if(DEBUG == true) {

            Log.d(tag, msg);

        }
    }

    public static void p(String msg , int type){

        if(DEBUG == true) {

            Log.d(tag, msg);

            if (type == Log.ERROR) {
                Log.e(tag, msg);
            } else if (type == Log.WARN) {
                Log.w(tag, msg);
            } else {
                Log.i(tag, msg);
            }
        }
    }
}

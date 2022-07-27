package flexiconsofttech.orchid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RetryPolicy;

import androidx.appcompat.app.ActionBarDrawerToggle;

public class common {

    public static String GetBaseUrl(){

       return "http://192.168.249.76/flower/";

    }

    public static String GetWsUrl(){

        return  GetBaseUrl() + "ws/";
    }

    public static String GetImageUrl(){

        return GetBaseUrl() + "images/";
    }

    public static DefaultRetryPolicy getRetryPolicy() {

        return new DefaultRetryPolicy(2000,3,1);
    }

    public static void ShowDialogBox(Context ctx , String Title , String Message){

        AlertDialog.Builder b1 = new AlertDialog.Builder(ctx);

        b1.setTitle(Title);
        b1.setMessage(Message);
        b1.setIcon(R.mipmap.logo);
        b1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        b1.create().show();
    }

    public static String getMessage() {

        return "-> Check Your Ip Address\n" +
                "-> Check path of web service\n" +
                "-> Check your phone or computer are connected with same network\n" +
                "-> Check This All Things And Retry It";
    }



}

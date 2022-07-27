package flexiconsofttech.orchid;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    DataStorage storage;

    @Override
    public void onNewToken(String token) {

        Context ctx = getApplicationContext();
        storage = new DataStorage(ctx);
        MyLog.p("Refreshed token: " + token);
        storage.write("regid",token);
        MyLog.p("stored token: " + storage.read("regid",DataStorage.STRING).toString());

        //FirebaseMessaging.getInstance().subscribeToTopic("global");
       // FirebaseMessaging.getInstance().subscribeToTopic("android");
       // FirebaseMessaging.getInstance().subscribeToTopic("android_batch_28");
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Check if message contains a data payload (data send from server).
        if (remoteMessage.getData().size() > 0) {
            MyLog.p("Message data payload: "+remoteMessage.getData());
            // {data={"image":null,"is_background":false,"payload":{"subject":"second message","detail":"second message content"},
            // "title":null,"message":null,"timestamp":"2020-05-18 23:47:50"}}
            String ReceivedText = remoteMessage.getData().toString();
            try
            {
                JSONObject first = new JSONObject(ReceivedText);
                MyLog.p("First Object" + first.toString());

                JSONObject second = new JSONObject(first.getString("data"));
                MyLog.p("second Object" + second.toString());

                JSONObject third = new JSONObject(second.getString("payload"));
                MyLog.p("third Object" + third.toString());

                String subject,detail;
                int activityindex;
                subject = third.getString("subject");
                detail = third.getString("detail");
                activityindex = Integer.parseInt(third.getString("activityindex"));
                MyLog.p("subject " + subject);
                MyLog.p("detail " + detail);
                MyLog.p("Activity Index " + activityindex);
                Context ctx = getApplicationContext();
                Intent intent=null;
                if(activityindex==1)
                {
                    intent = new Intent(ctx,Dashbord.class);
                }
                else if(activityindex==2)
                {
                    intent = new Intent(ctx,Product_Container.class);
                }
                PendingIntent pintent = PendingIntent.getActivity(ctx,100,intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                MyNotificationManager.addNotification(subject,detail,ctx,pintent,true,true);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

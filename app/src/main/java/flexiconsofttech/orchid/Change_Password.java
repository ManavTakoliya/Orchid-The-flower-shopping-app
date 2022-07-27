package flexiconsofttech.orchid;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class Change_Password extends AppCompatActivity {

    @BindView(R.id.txtcngoldpassword)
    TextInputEditText txtcngoldpassword;
    @BindView(R.id.txtcngnewpassword)
    TextInputEditText txtcngnewpassword;
    @BindView(R.id.txtcngnewconfirmpassword)
    TextInputEditText txtcngnewconfirmpassword;
    private String oldpassword, newpassword, newconfirmpassword;
    private Context ctx = this;
    private DataStorage storage;

    @Optional
    @OnClick(R.id.cngpassword)
    void OnClick(View v) {

        Send_Reuest();

    }

    private void Send_Reuest() {

        if (IsValidInput() == true) {

            String WebUrl = common.GetWsUrl() + "change_password.php";
            StringRequest request = new StringRequest(StringRequest.Method.POST, WebUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String res) {

                    try {
                        JSONArray response = new JSONArray(res);
                        String error = response.getJSONObject(0).getString("error");
                        String message = response.getJSONObject(2).getString("message");
                        if(error.equals("no error"))
                        {
                            String success = response.getJSONObject(1).getString("success");

                            if(success.equals("yes") == true)
                            {
                                Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
                                startActivity(new Intent(ctx,Dashbord.class));
                                finish();
                            }
                            else
                            {
                                Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(ctx, error, Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        MyLog.p(e.getMessage());
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    common.ShowDialogBox(ctx,"Time Out",common.getMessage());

                }
            }){

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> KeyValuePair = new HashMap<>();
                    String id = storage.read("id",storage.INTEGER).toString();
                    KeyValuePair.put("id",id);
                    KeyValuePair.put("oldpassword",oldpassword);
                    KeyValuePair.put("newpassword",newpassword);
                    return KeyValuePair;
                }
            };
            request.setRetryPolicy(common.getRetryPolicy());
            AppController.getInstance().addToRequestQueue(request);

        }
    }

    private boolean IsValidInput() {

        boolean isvalid = true;
        oldpassword = txtcngoldpassword.getText().toString().trim();
        newpassword = txtcngnewpassword.getText().toString().trim();
        newconfirmpassword = txtcngnewconfirmpassword.getText().toString().trim();

        if (oldpassword.length() == 0) {
            txtcngoldpassword.setError("old password mut be require");
            isvalid = false;
        } else if (newpassword.length() == 0) {
            txtcngnewpassword.setError("new password must be require");
            isvalid = false;
        } else if (newpassword.length() > 8 || newpassword.length() < 8) {
            txtcngnewpassword.setError("password must be 8 character");
            isvalid = false;
        } else if (newconfirmpassword.length() == 0) {
            txtcngnewconfirmpassword.setError("new confirm password require");
            isvalid = false;
        } else if (newpassword.equals(newconfirmpassword) == false) {
            Toast.makeText(ctx,"new password not match with confirm password",Toast.LENGTH_LONG).show();
            isvalid = false;

        } else
            {
            isvalid = true;}

        return isvalid;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__password);
        ButterKnife.bind(this);
        storage = new DataStorage(this);
    }
}

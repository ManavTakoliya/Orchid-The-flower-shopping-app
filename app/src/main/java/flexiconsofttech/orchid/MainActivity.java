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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String email, password;
     @BindView(R.id.txtlogemail) TextInputEditText txtlogemail;
     @BindView(R.id.txtlogpassword) TextInputEditText txtlogpassword;
    private Context ctx = this;
    private DataStorage storage;
    private int FIRST_USE_APP=0;
    private int REGISTERED=-1;

    @Optional
    @OnClick({R.id.lblregisterhere, R.id.btnlogin, R.id.lblforgotpassword})
    void OnClick(View v) {

        int id = v.getId();

        switch (id) {

            case R.id.lblregisterhere:
                startActivity(new Intent(ctx, Register.class));
                break;

            case R.id.btnlogin:
                Send_Request();
                break;

            case R.id.lblforgotpassword:
                startActivity(new Intent(ctx, ForgotPassword.class));
                break;
        }


    }

    private void Send_Request() {

        if (IsvalidInput() == true) {
            String WebUrl = common.GetWsUrl() + "login.php";
            StringRequest request = new StringRequest(StringRequest.Method.POST, WebUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String res) {

                    try {
                        JSONArray response = new JSONArray(res);
                        String error = response.getJSONObject(0).getString("error");
                        String message = response.getJSONObject(2).getString("message");
                        if (error.equals("no error")) {

                            String success = response.getJSONObject(1).getString("success");
                            if (success.equals("yes")) {
                                int id = Integer.parseInt(response.getJSONObject(3).getString("id"));
                                storage.write("id",id);
                                Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
                                startActivity(new Intent(ctx, Dashbord.class));
                                finish();
                            } else {
                                Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(ctx, error, Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        MyLog.p(e.getMessage());
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    common.ShowDialogBox(ctx, "TimeOut", common.getMessage());
                }
            }){

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    HashMap<String,String> KeyValuePair = new HashMap<>();
                    KeyValuePair.put("email",email);
                    KeyValuePair.put("password",password);
                    return KeyValuePair;
                }
            };
            request.setRetryPolicy(common.getRetryPolicy());
            AppController.getInstance().addToRequestQueue(request);

        }

    }

    private boolean IsvalidInput() {

        boolean isvalid = true;
        email = txtlogemail.getText().toString().trim();
        password = txtlogpassword.getText().toString().trim();
        if (txtlogemail.length() == 0) {
            txtlogemail.setError("Enter Email");
            isvalid = false;
        } else if (txtlogpassword.length() == 0) {

            txtlogpassword.setError("Enter Password");
            isvalid = false;
        } else {
            isvalid = true;
        }
        return isvalid;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        storage = new DataStorage(this);
        CheckUserReg();

    }

    public void CheckUserReg(){

        int id = Integer.parseInt(storage.read("id",storage.INTEGER).toString());
        if (id != FIRST_USE_APP)
        {
            if(id == REGISTERED)
            {

            }
            else
            {
                startActivity(new Intent(ctx,Dashbord.class));
                finish();
            }
        }


    }

}

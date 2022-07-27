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
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    @BindView(R.id.txtregemail)
    TextInputEditText txtregemail;
    @BindView(R.id.txtregpassword)
    TextInputEditText txtregpassword;
    @BindView(R.id.txtregconfirmpassword)
    TextInputEditText txtregconfirmpassword;
    private Context ctx = this;
    private String email, password, confirmpassword;
    DataStorage storage;
    private static final int REGISTERD = -1;

    @Optional
    @OnClick(R.id.btnregister)
    public void OnClick(View v) {

        Send_Request();

    }

    private void Send_Request() {

        String WebUrl = common.GetWsUrl() + "register.php";

        if (IsValidInput() == true) {
            StringRequest request = new StringRequest(StringRequest.Method.POST, WebUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String res) {

                    try {
                        JSONArray response = new JSONArray(res);
                        String error = response.getJSONObject(0).getString("error");
                        String message = response.getJSONObject(2).getString("message");
                        if (error.equals("no error")) {

                            String success = response.getJSONObject(1).getString("success");
                            if (success.equals("yes") == true) {
                                storage.write("id", REGISTERD);
                                Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
                                startActivity(new Intent(ctx, MainActivity.class));
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

                    common.ShowDialogBox(ctx, "Time Out", common.getMessage());

                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    HashMap<String, String> KeyValuePair = new HashMap<>();
                    KeyValuePair.put("email", email);
                    KeyValuePair.put("password", password);
                    KeyValuePair.put("regid",storage.read("regid",DataStorage.STRING).toString());
                    return KeyValuePair;

                }
            };
            request.setRetryPolicy(common.getRetryPolicy());
            AppController.getInstance().addToRequestQueue(request);
        }

    }

    private boolean IsValidInput() {

        boolean isvalid = true;
        email = txtregemail.getText().toString().trim();
        password = txtregpassword.getText().toString().trim();
        confirmpassword = txtregconfirmpassword.getText().toString().trim();

        if (email.length() == 0) {
            txtregemail.setError("Email Must Be Required");
            isvalid = false;
        } else if (password.length() == 0) {
            txtregpassword.setError("Password Must Be Required");
            isvalid = false;
        } else if (password.length() < 8 || 8 < password.length()) {

            txtregpassword.setError("Password Must Be 8 Character");
        } else if (confirmpassword.length() == 0) {
            txtregconfirmpassword.setError("Confirm Password Must Be Required");
            isvalid = false;
        } else if (password.equals(confirmpassword) == false) {
            txtregconfirmpassword.setError("Confirm Password Not Match With Password");
            isvalid = false;
        } else {
            isvalid = true;
        }

        return isvalid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        storage = new DataStorage(this);
    }
}

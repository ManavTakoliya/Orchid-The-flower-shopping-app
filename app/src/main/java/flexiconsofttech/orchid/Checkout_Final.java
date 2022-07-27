package flexiconsofttech.orchid;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.internal.service.Common;
import com.google.android.material.textfield.TextInputEditText;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Checkout_Final extends AppCompatActivity implements PaymentResultListener {

    protected Toolbar toolbar;
    @BindView(R.id.txtfullname)
    EditText txtfullname;
    @BindView(R.id.txtaddress1) TextInputEditText txtaddress1;
    @BindView(R.id.txtaddress2) TextInputEditText txtaddress2;
    @BindView(R.id.txtcity) TextInputEditText txtcity;
    @BindView(R.id.txtpincode) TextInputEditText txtpincode;
    @BindView(R.id.txtremarks) TextInputEditText txtremarks;
    @BindView(R.id.txtmobile5) TextInputEditText txtmobile;
    @BindView(R.id.txtstate) TextInputEditText txtstate;
    String fullname,address1,address2,pincode,city,state,mobile,remarks;
    Context ctx = this;
    DataStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        ButterKnife.bind(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(R.drawable.backarrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ctx,Cart_Container.class));
            }
        });



        toolbar.setTitle("Checkout_Final");

        storage = new DataStorage(ctx);


    }


    @Optional
    @OnClick(R.id.btnpay) void OnClick(View v){
        if(ValidateInput()==true)
        {


            // Payment Gateway

            String samount = "100";
            int amount = Math.round(Float.parseFloat(samount));
            Checkout checkout = new Checkout();
            checkout.setKeyID("rzp_test_OY59EbMIHu0VET");
            checkout.setImage(R.drawable.logo);
            //initalize jsonObejct

            JSONObject object = new JSONObject();

            try {

                object.put("name","Flexicon Soft Tech");
                object.put("description","Test Payment");
                object.put("theme.color","#0093DD");
                object.put("currency","INR");
                object.put("amount",amount);
                object.put("prefill.contact","8888888888");
                object.put("prefill.email","takoliya.manav777@gmail.com");
                checkout.open(Checkout_Final.this,object);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    private void SendRequest() {
        String WebServiceUrl = common.GetWsUrl() + "checkout.php";
        StringRequest request = new StringRequest(StringRequest.Method.POST, WebServiceUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String res) {
                MyLog.p(res);
                try {
                    JSONArray response = new JSONArray(res);
                    String error = response.getJSONObject(0).getString("error");
                    if(error.equals("no error")==false)
                    {
                        Toast.makeText(ctx,error, Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        String success = response.getJSONObject(1).getString("success");
                        if(success.equals("yes") || success.equals("no"))
                        {
                            Toast.makeText(ctx,response.getJSONObject(2).getString("message"),Toast.LENGTH_LONG).show();
                        }
                        else if(success.equals("out of stock"))
                        {
                            JSONArray message = response.getJSONArray(2);
                            int messagesize = message.length();
                            StringBuffer temp = new StringBuffer(512);
                            temp.append(message.getJSONObject(0).getString("message"));
                            for (int i=1;i<messagesize;i++){
                                temp.append("\n" + message.getJSONObject(i).getString("name"));
                            }
                            Toast.makeText(ctx,temp.toString(),Toast.LENGTH_LONG).show();
                            startActivity(new Intent(ctx,Dashbord.class));
                            finish();
                        }
                    }
                } catch (JSONException e) {
                    MyLog.p(e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                common.ShowDialogBox(ctx, "timeout", common.getMessage());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> KeyValuePair = new HashMap<>();
                //usersid,fullname,address1,address2,city,pincode,state,remarks,mobile(required)
                KeyValuePair.put("usersid",storage.read("id",DataStorage.INTEGER).toString());
                KeyValuePair.put("fullname",fullname);
                KeyValuePair.put("address",address1 + " "+ address2);
                KeyValuePair.put("city",city);
                KeyValuePair.put("pincode",pincode);
                KeyValuePair.put("state",state);
                KeyValuePair.put("mobile",mobile);
                return KeyValuePair;

            }
        };
        request.setRetryPolicy(common.getRetryPolicy());
        AppController.getInstance().addToRequestQueue(request);
    }

    public boolean ValidateInput()
    {
        boolean isValid = true;
        fullname = txtfullname.getText().toString().trim().toLowerCase();
        address1 = txtaddress1.getText().toString().trim().toLowerCase();
        address2 = txtaddress2.getText().toString().trim().toLowerCase();
        pincode = txtpincode.getText().toString().trim().toLowerCase();
        city = txtcity.getText().toString().trim().toLowerCase();
        state = txtstate.getText().toString().trim().toLowerCase();
        remarks = txtremarks.getText().toString().trim().toLowerCase();
        mobile = txtmobile.getText().toString();
        if(fullname.length()==0)
        {
            isValid=false;
            txtfullname.setError("fullname is required");
        }

        if(address1.length()==0)
        {
            isValid=false;
            txtaddress1.setError("address 1 is required");
        }

        if(pincode.length() > 6 || pincode.length() < 6)
        {
            isValid=false;
            txtpincode.setError("pincode is required and must be of 6 digit");
        }

        if(city.length()==0)
        {
            isValid=false;
            txtcity.setError("City is required");
        }

        if(state.length()==0)
        {
            isValid=false;
            txtstate.setError("State is required");
        }

        if(mobile.length()<10 || mobile.length()>10)
        {
            isValid=false;
            txtmobile.setError("Mobile is required and must be 10 digit long");
        }
        return isValid;
    }

    @Override
    public void onPaymentSuccess(String s) {
        SendRequest();
        startActivity(new Intent(ctx,Dashbord.class).putExtra("Payment_Id",s));
        finish();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(ctx,s,Toast.LENGTH_LONG).show();
    }
}


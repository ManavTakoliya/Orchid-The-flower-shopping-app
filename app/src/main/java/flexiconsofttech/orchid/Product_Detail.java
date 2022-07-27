package flexiconsofttech.orchid;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import ss.com.bannerslider.Slider;

import android.content.Context;
import android.os.Bundle;
import android.transition.Slide;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Product_Detail extends OptionMenuActivity{

    @BindView(R.id.pdslider) Slider pdslider;
    private String pid;
    private Context ctx=this;
    @BindView(R.id.lblpdname) TextView lblpdname;
    @BindView(R.id.lblpdprice) TextView lblpdprice;
    @BindView(R.id.lblpddetails) TextView lblpddetails;
    ArrayList<String> imagelist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product__detail);
        ButterKnife.bind(this);


        // Drawer
        drawerLayout = findViewById(R.id.drawer_lay);
        navigationView = findViewById(R.id.drawernav);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Open, R.string.Close);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        toolbar.setTitle("");

        pid = this.getIntent().getExtras().getString("pid");
        MyLog.p("pid = " + pid);
        Send_Request();
    }

    private void Send_Request() {

        String WebUrl = common.GetWsUrl() + "product.php";
        StringRequest request = new StringRequest(StringRequest.Method.POST, WebUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String res) {

                try {
                    JSONArray response = new JSONArray(res);
                    String error = response.getJSONObject(0).getString("error");
                    if(error.equals("no error"))
                    {
                        int total = response.getJSONObject(1).getInt("total");
                        if(total!=0){

                            JSONObject object = response.getJSONObject(2);
                            lblpdname.setText(object.getString("name"));
                            lblpdprice.setText("Rs "+object.getString("price"));
                            lblpddetails.setText("Size : " + object.getString("size") + "\n" +
                                    "Weight : " + object.getString("weight") + "\n" +
                            "Other Details : " + object.getString("details"));
                            imagelist.add(common.GetImageUrl() + "product/" + object.getString("photo"));
                            Send_Slider_Request();
                        }
                    }
                    else
                    {
                        Toast.makeText(ctx, error, Toast.LENGTH_SHORT).show();
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
                KeyValuePair.put("productid",pid);
                return KeyValuePair;
            }
        };
        request.setRetryPolicy(common.getRetryPolicy());
        AppController.getInstance().addToRequestQueue(request);

    }

    private void Send_Slider_Request() {

        String WebUrl = common.GetWsUrl() + "slider.php";
        StringRequest request = new StringRequest(StringRequest.Method.POST, WebUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String res) {

                try {
                    JSONArray response = new JSONArray(res);
                    String error = response.getJSONObject(0).getString("error");
                    if(error.equals("no error")==false)
                    {
                        Toast.makeText(ctx,error,Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        int total = response.getJSONObject(1).getInt("total");
                        if(total!=0)
                        {
                            for(int i=2; i<response.length(); i++)
                            {
                                JSONObject object = response.getJSONObject(i);
                                imagelist.add(common.GetImageUrl() + "slider/"+ object.getString("photo"));
                            }
                        }
                        Slider.init(new PicassoImageLoadingService(ctx));
                        pdslider.setAdapter(new MainSliderAdapter1(imagelist));
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
                KeyValuePair.put("productid",pid);
                return KeyValuePair;
            }
        };
        request.setRetryPolicy(common.getRetryPolicy());
        AppController.getInstance().addToRequestQueue(request);

    }
}

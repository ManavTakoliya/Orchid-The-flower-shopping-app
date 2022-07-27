package flexiconsofttech.orchid;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Product_Container extends OptionMenuActivity {

    ArrayList<Product> productList = new ArrayList<Product>();
    private Context ctx = this;
    private String cid;
    private String pid, name, photo, price;
    RecyclerView recproductcontainer;
    private String searchtextname;
    Bundle extras;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product__container);

        drawerLayout = findViewById(R.id.drawer_lay);
        navigationView = findViewById(R.id.drawernav);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Products");
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Open, R.string.Close);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        extras =  getIntent().getExtras();
        recproductcontainer = findViewById(R.id.recproductcontainer);
        Send_request();

    }

    private void Send_request() {

        String WebUrl = common.GetWsUrl() + "product.php";
        StringRequest request = new StringRequest(StringRequest.Method.POST, WebUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String res) {

                try {
                    JSONArray response = new JSONArray(res);
                    String error = response.getJSONObject(0).getString("error");
                    if (error.equals("no error")) {

                        int total = response.getJSONObject(1).getInt("total");
                        if (total == 0) {
                            Toast.makeText(ctx, "No Products", Toast.LENGTH_SHORT).show();
                        } else {
                            for (int i = 2; i < response.length(); i++) {
                                JSONObject object = response.getJSONObject(i);
                                pid = object.getString("id");
                                name = object.getString("name");
                                photo = object.getString("photo");
                                price = object.getString("price");
                                Product p = new Product(pid, name, photo, price);
                                productList.add(p);
                            }
                            Product_Adapter adapter = new Product_Adapter(ctx, productList);
                            recproductcontainer.setLayoutManager(new GridLayoutManager(ctx, 1));
                            recproductcontainer.setItemAnimator(new DefaultItemAnimator());
                            recproductcontainer.setAdapter(adapter);

                        }

                    } else {

                        Toast.makeText(ctx, "Eroororoc", Toast.LENGTH_SHORT).show();
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

                if(extras.getString("cid") == null)
                {
                    cid = extras.getString("searchtext");
                    KeyValuePair.put("searchtext", cid);
                }
                else {
                    cid = extras.getString("cid");
                    KeyValuePair.put("categoryid", cid);
                }


                return KeyValuePair;

            }
        };
        request.setRetryPolicy(common.getRetryPolicy());
        AppController.getInstance().addToRequestQueue(request);

    }
}


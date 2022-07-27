package flexiconsofttech.orchid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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



public class Cart_Container extends AppCompatActivity {

    private Context ctx = this;
    String cid,name,photo,price,quantity;
    ArrayList<Cart> cartlist = new ArrayList<>();
    @BindView(R.id.reccart) RecyclerView reccart;
    DataStorage storage;
    public static TextView lbltotalprice;
    @BindView(R.id.btncheckout) Button btncheckout;
    private String productid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart__container);
        ButterKnife.bind(this);
        lbltotalprice = findViewById(R.id.lbltotalprice);
        Send_Request();
        btncheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ctx, Checkout_Final.class));
            }
        });


    }



    private void Send_Request() {
        String WebUrl = common.GetWsUrl() + "cart.php";
        StringRequest request = new StringRequest(StringRequest.Method.POST, WebUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String res) {

                try {
                    JSONArray response = new JSONArray(res);
                    String error = response.getJSONObject(0).getString("error");
                    if(error.equals("no error")){

                        int total = response.getJSONObject(1).getInt("total");
                        if(total == 0)
                        {
                            Toast.makeText(ctx, "no product added into cart", Toast.LENGTH_SHORT).show();
                            btncheckout.setEnabled(false);
                        }
                        else
                        {
                            for(int i=2; i<response.length(); i++){

                                JSONObject object = response.getJSONObject(i);
                                cid = object.getString("id");
                                productid = object.getString("productid");
                                name = object.getString("name");
                                photo = object.getString("photo");
                                price = object.getString("price");
                                quantity = object.getString("quantity");
                                Cart c = new Cart(cid,productid,name,photo,price,quantity);
                                cartlist.add(c);
                            }


                            CartAdapter adapter = new CartAdapter(ctx,cartlist);
                            reccart.setLayoutManager(new GridLayoutManager(ctx,1));
                            reccart.setItemAnimator(new DefaultItemAnimator());
                            reccart.setAdapter(adapter);


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
                storage = new DataStorage(ctx);
                KeyValuePair.put("usersid",storage.read("id",DataStorage.INTEGER).toString());
                return KeyValuePair;
            }
        };
        request.setRetryPolicy(common.getRetryPolicy());
        AppController.getInstance().addToRequestQueue(request);

    }
}

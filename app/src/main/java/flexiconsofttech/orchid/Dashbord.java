package flexiconsofttech.orchid;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ss.com.bannerslider.Slider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Dashbord extends OptionMenuActivity {

    protected Context ctx = this;
    private ArrayList<category> categoryArrayList = new ArrayList<>();
    private ArrayList<String> stringArrayList = new ArrayList<>();
    ArrayList<offers> ImageList = new ArrayList<>();
    ArrayList<top4> top4list = new ArrayList<>();
    private String oid, pid, discount, ophoto;
    private String Pname, name, photo, id;
    private String tphoto,tname,tid;
    private Slider slider;
    @BindView(R.id.recdashcat) RecyclerView recdashcat;
    @BindView(R.id.txtsearch) AutoCompleteTextView txtsearch;
    @BindView(R.id.rectop4) RecyclerView rectop4;
    @BindView(R.id.offer1) ImageView offer1;
    private String searchTextname;
    private String o1id;
    private String p1id;
    private String discount1;
    private String o1photo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashbord);
        ButterKnife.bind(this);

        String Payment_Id = getIntent().getStringExtra("Payment_Id");
        if(Payment_Id == null){

        }
        else
        {
            common.ShowDialogBox(this,"Payment Id",Payment_Id);
        }

        // Slider
        slider = findViewById(R.id.dash_banner_slider);



        // Drawer
        drawerLayout = findViewById(R.id.drawer_lay);
        navigationView = findViewById(R.id.drawernav);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Open, R.string.Close);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        toolbar.setTitle("");

        // Common
        storage = new DataStorage(this);
        Send_Request();
        Send_Request_For_Pname();
        Set_Event_AutocompleteTextview();
        Set_Event_Go();
        Send_Request_Slider();
        Send_Request_For_Top4();
        Send_Request_OFFER1();


    }

    private void Send_Request_OFFER1() {

        String WebUrl = common.GetWsUrl() + "offers.php";
        StringRequest request = new StringRequest(StringRequest.Method.POST, WebUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String res) {

                try {
                    JSONArray response = new JSONArray(res);
                    String error = response.getJSONObject(0).getString("error");
                    if (error.equals("no error") == true) {
                        int total = response.getJSONObject(1).getInt("total");
                        if (total == 0) {
                            Toast.makeText(ctx, "no offers", Toast.LENGTH_LONG).show();
                        } else {
                                o1id = response.getJSONObject(2).getString("id");
                                p1id = response.getJSONObject(2).getString("pid");
                                discount1 = response.getJSONObject(2).getString("discount");
                                o1photo = response.getJSONObject(2).getString("photo");
                                Picasso.with(ctx).load(common.GetImageUrl() + "offers/" + ophoto).into(offer1);
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
        });
        request.setRetryPolicy(common.getRetryPolicy());
        AppController.getInstance().addToRequestQueue(request);

    }


    private void Send_Request_For_Top4() {

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
                        if(total!=0)
                        {
                            for (int i=2; i<response.length(); i++){

                                JSONObject object = response.getJSONObject(i);
                                tid = object.getString("id");
                                tphoto = object.getString("photo");
                                tname = object.getString("name");
                                top4 t = new top4(tid,tphoto,tname);
                                top4list.add(t);
                            }
                            topproductAdapter tadapter = new topproductAdapter(ctx,top4list);
                            rectop4.setLayoutManager(new GridLayoutManager(ctx,2));
                            rectop4.setItemAnimator(new DefaultItemAnimator());
                            rectop4.setAdapter(tadapter);


                        }
                        else
                        {
                            Toast.makeText(ctx,"No Top Products", Toast.LENGTH_SHORT).show();
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

                HashMap<String,String> KeyValuePair =  new HashMap<>();
                KeyValuePair.put("top4","2");
                return KeyValuePair;
            }
        };
        request.setRetryPolicy(common.getRetryPolicy());
        AppController.getInstance().addToRequestQueue(request);

    }

    private void Send_Request_Slider() {

        String WebUrl = common.GetWsUrl() + "offers.php";
        StringRequest request = new StringRequest(StringRequest.Method.POST, WebUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String res) {

                try {
                    JSONArray response = new JSONArray(res);
                    String error = response.getJSONObject(0).getString("error");
                    if (error.equals("no error") == true) {
                        int total = response.getJSONObject(1).getInt("total");
                        if (total == 0) {
                            Toast.makeText(ctx, "no offers", Toast.LENGTH_LONG).show();
                        } else {
                            for (int i = 2; i < response.length(); i++) {

                                JSONObject object = response.getJSONObject(i);
                                oid = object.getString("id");
                                pid = object.getString("pid");
                                discount = object.getString("discount");
                                ophoto = object.getString("photo");
                                offers o = new offers(oid,pid,discount,ophoto);
                                ImageList.add(o);
                            }
                            slider.setAdapter(new MainSliderAdapter(ImageList));
                            Slider.init(new PicassoImageLoadingService(ctx));
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
        });
        request.setRetryPolicy(common.getRetryPolicy());
        AppController.getInstance().addToRequestQueue(request);

    }


    private void Set_Event_Go() {

        txtsearch.setOnEditorActionListener(editorListner);

    }

    private TextView.OnEditorActionListener editorListner = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

            searchTextname = txtsearch.getText().toString();
            switch (actionId) {
                case EditorInfo
                        .IME_ACTION_SEARCH:
                    startActivity(new Intent(ctx, Product_Container.class).putExtra("searchtext", searchTextname));
            }
            return false;
        }
    };

    private void Set_Event_AutocompleteTextview() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                ctx, R.layout.support_simple_spinner_dropdown_item, stringArrayList);
        txtsearch.setAdapter(adapter);

    }

    private void Send_Request_For_Pname() {

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
                                Pname = object.getString("name");
                                stringArrayList.add(Pname);
                                MyLog.p(stringArrayList.toString());
                            }
                        }

                    } else {

                        Toast.makeText(ctx, "Error", Toast.LENGTH_SHORT).show();
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
        });
        request.setRetryPolicy(common.getRetryPolicy());
        AppController.getInstance().addToRequestQueue(request);

    }

    private void Send_Request() {

        String WebUrl = common.GetWsUrl() + "category.php";
        StringRequest request = new StringRequest(StringRequest.Method.POST, WebUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String res) {

                try {
                    JSONArray response = new JSONArray(res);
                    String error = response.getJSONObject(0).getString("error");
                    if (error.equals("no error")) {

                        int total = response.getJSONObject(1).getInt("total");
                        if (total == 0) {
                            Toast.makeText(ctx, "No Category Found", Toast.LENGTH_LONG).show();
                        } else {
                            for (int i = 2; i < response.length(); i++) {

                                JSONObject object = response.getJSONObject(i);
                                name = object.getString("name");
                                photo = object.getString("photo");
                                id = object.getString("id");
                                category c = new category(id, name, photo);
                                categoryArrayList.add(c);
                            }

                            Category_Adapter adapter = new Category_Adapter(categoryArrayList, ctx);
                            recdashcat.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false));
                            recdashcat.setItemAnimator(new DefaultItemAnimator());
                            recdashcat.setAdapter(adapter);
                        }

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
        });
        request.setRetryPolicy(common.getRetryPolicy());
        AppController.getInstance().addToRequestQueue(request);

    }
}

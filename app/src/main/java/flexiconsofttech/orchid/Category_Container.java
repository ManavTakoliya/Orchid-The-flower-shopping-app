package flexiconsofttech.orchid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Category_Container extends OptionMenuActivity {

    private Context ctx = this;
    String name,photo,id;
    private ArrayList<category> categoryArrayList = new ArrayList<>();
    @BindView(R.id.reccategory) RecyclerView reccategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_container);
        ButterKnife.bind(this);
        drawerLayout = findViewById(R.id.drawer_lay);
        drawerLayout.closeDrawers();
        navigationView = findViewById(R.id.drawernav);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.Open,R.string.Close);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        toolbar.setTitle("Categories");
        Send_Request();
    }



    private void Send_Request() {

        String WebUrl = common.GetWsUrl() + "category.php";
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
                            Toast.makeText(ctx, "No Category Found", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            for(int i=2; i<response.length(); i++){

                                JSONObject object = response.getJSONObject(i);
                                name = object.getString("name");
                                photo =object.getString("photo");
                                id = object.getString("id");
                                category c = new category(id,name,photo);
                                categoryArrayList.add(c);
                            }

                            Category_Adapter adapter = new Category_Adapter(categoryArrayList,ctx);
                            reccategory.setLayoutManager(new GridLayoutManager(ctx,2));
                            reccategory.setItemAnimator(new DefaultItemAnimator());
                            reccategory.setAdapter(adapter);
                        }

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
        });
        request.setRetryPolicy(common.getRetryPolicy());
        AppController.getInstance().addToRequestQueue(request);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return super.onNavigationItemSelected(item);
    }
}

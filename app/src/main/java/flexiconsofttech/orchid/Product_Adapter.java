package flexiconsofttech.orchid;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

public class Product_Adapter extends RecyclerView.Adapter {


    private Context ctx;
    private ArrayList<Product> productList;
    private String imgurl;

    public Product_Adapter(Context ctx, ArrayList<Product> productList) {

        this.ctx = ctx;
        this.productList = productList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View productrow = inflater.inflate(R.layout.product_row, null);
        MyWidgetContainer container = new MyWidgetContainer(productrow);
        return container;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyWidgetContainer container = (MyWidgetContainer) holder;
        container.lblprotitle.setText(productList.get(position).getName());
        container.lblproprice.setText("RS " + productList.get(position).getPrice());
        imgurl = common.GetImageUrl() + "product/" + productList.get(position).getPhoto();
        MyLog.p(imgurl);
        Picasso.with(ctx).load(imgurl).into(container.imgprophoto);

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class MyWidgetContainer extends RecyclerView.ViewHolder {

        @BindView(R.id.lblproprice)
        public TextView lblproprice;
        @BindView(R.id.lblprotitle)
        public TextView lblprotitle;
        @BindView(R.id.imgprophoto)
        public ImageView imgprophoto;
        @BindView(R.id.btnaddtocart) public ImageView btnaddtocart;
        DataStorage storage;

        public MyWidgetContainer(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Optional
        @OnClick({R.id.imgprophoto, R.id.btnaddtocart})
        public void OnClick(View v) {
            int id = v.getId();
            switch (id) {

                case R.id.imgprophoto :
                    int position = getAdapterPosition();
                    Product p = productList.get(position);
                    ctx.startActivity(new Intent(ctx, Product_Detail.class).
                            putExtra("pid", p.getPid()));
                    break;

                case R.id.btnaddtocart:

                    String WebUrl = common.GetWsUrl() + "addtocart.php";
                    StringRequest request = new StringRequest(StringRequest.Method.POST, WebUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String res) {

                            try {
                                JSONArray response = new JSONArray(res);
                                String error = response.getJSONObject(0).getString("error");
                                if(error.equals("no error"))
                                {
                                    String message = response.getJSONObject(2).getString("message");
                                    String success = response.getJSONObject(1).getString("success");
                                    if(success.equals("yes"))
                                    {
                                        Toast.makeText(ctx,message,Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(ctx,message,Toast.LENGTH_SHORT).show();

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
                            String uid = storage.read("id",DataStorage.INTEGER).toString();
                            KeyValuePair.put("pid",productList.get(getAdapterPosition()).getPid());
                            KeyValuePair.put("usersid",uid);
                            return KeyValuePair;

                        }
                    };
                    request.setRetryPolicy(common.getRetryPolicy());
                    AppController.getInstance().addToRequestQueue(request);

                    break;


            }

        }
    }
}

package flexiconsofttech.orchid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;


public class CartAdapter extends RecyclerView.Adapter {

    private Context ctx;
    private ArrayList<Cart> cartlist;
    int totalprice;


    public CartAdapter(Context ctx, ArrayList<Cart> cartlist) {

        this.ctx = ctx;
        this.cartlist = cartlist;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View cartrow = inflater.inflate(R.layout.cart_row, null);
        MyWidgetContainer container = new MyWidgetContainer(cartrow);
        return container;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyWidgetContainer container = (MyWidgetContainer) holder;
        container.crtname.setText(cartlist.get(position).getName());
        container.crtprice.setText("Rs " + cartlist.get(position).getPrice());
        container.btnquantity.setNumber(cartlist.get(position).getQuantity());
        String ImgUrl = common.GetImageUrl() + "product/" + cartlist.get(position).getPhoto();
        Picasso.with(ctx).load(ImgUrl).into(container.crtphoto);
        totalprice  =  totalprice + (Integer.parseInt(cartlist.get(position).getPrice()) *
                Integer.parseInt(cartlist.get(position).getQuantity()));
        Cart_Container.lbltotalprice.setText(String.valueOf(totalprice));

    }




    @Override
    public int getItemCount() {
        return cartlist.size();
    }


    class MyWidgetContainer extends RecyclerView.ViewHolder {

        @BindView(R.id.crtname) public TextView crtname;
        @BindView(R.id.crtphoto) public ImageView crtphoto;
        @BindView(R.id.crtprice) public TextView crtprice;
        @BindView(R.id.btnquantity) public ElegantNumberButton btnquantity;

        public MyWidgetContainer(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


            btnquantity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                @Override
                public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {

                    if(newValue>=20){

                        Toast.makeText(ctx, "Max Item Only 20", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        if (oldValue < newValue) {

                            oldValue += 1;
                        } else {
                            oldValue -= 1;
                        }

                        Send_Request(oldValue);

                    }
                }
                private void Send_Request(int oldValue) {

                    String url = common.GetWsUrl() + "cart_update.php";

                    StringRequest request = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                        @Override
                            public void onResponse(String res) {

                            try {
                                JSONArray response = new JSONArray(res);
                                String error = response.getJSONObject(0).getString("error").toString();

                                if (error.equals("no error") == true) {

                                    if (response.getJSONObject(1).getString("success").equals("yes") == true) {

                                        MyLog.p("Success");

                                    } else {
                                        MyLog.p("unsuccess");
                                    }

                                } else {
                                    Toast.makeText(ctx, error.toString(), Toast.LENGTH_LONG).show();
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
                            KeyValuePair.put("price",cartlist.get(getAdapterPosition()).getPrice().toString());
                            MyLog.p(cartlist.get(getAdapterPosition()).getPrice().toString());
                            KeyValuePair.put("qun",Integer.valueOf(oldValue).toString());
                            MyLog.p(Integer.valueOf(oldValue).toString());
                            KeyValuePair.put("productid",cartlist.get(getAdapterPosition()).getProductid());
                            MyLog.p(cartlist.get(getAdapterPosition()).getProductid());
                            DataStorage storage = new DataStorage(ctx);
                            String id = storage.read("id",storage.INTEGER).toString();
                            KeyValuePair.put("usersid",id);
                            MyLog.p(id);

                            return KeyValuePair;
                        }
                    };

                    request.setRetryPolicy(common.getRetryPolicy());
                    AppController.getInstance().addToRequestQueue(request);

                }

            });
        }



    }

}

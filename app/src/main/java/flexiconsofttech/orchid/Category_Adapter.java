package flexiconsofttech.orchid;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;


public class Category_Adapter extends RecyclerView.Adapter {

    private ArrayList<category> categoryList;
    private Context ctx;
    String ImageUrl;

    public Category_Adapter(ArrayList<category> categoryArrayList, Context ctx) {

        this.categoryList = categoryArrayList;
        this.ctx = ctx;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View categoryrow;

        LayoutInflater inflater = LayoutInflater.from(ctx);
        if (ctx instanceof Dashbord) {
             categoryrow = inflater.inflate(R.layout.category_row, null);
             MyLog.p(ctx.toString() + "Dash");
        } else {
             categoryrow = inflater.inflate(R.layout.category_row1, null);
            MyLog.p(ctx.toString() + "cate");
        }
        MyWidgetContainer container = new MyWidgetContainer(categoryrow);
        return container;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyWidgetContainer container = (MyWidgetContainer) holder;
        container.lblcategorytitle.setText(categoryList.get(position).getName());
        ImageUrl = common.GetImageUrl() + "category/" + categoryList.get(position).getPhoto();
        Picasso.with(ctx).load(ImageUrl).into(container.imgcategory);

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

     class MyWidgetContainer extends RecyclerView.ViewHolder {

        @BindView(R.id.imgcategory) public ImageView imgcategory;
        @BindView(R.id.lblcategorytitle) public TextView lblcategorytitle;
        @BindView(R.id.categorycrd) public CardView categorycrd;

        public MyWidgetContainer(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Optional
        @OnClick({R.id.categorycrd}) public  void OnClick(View v){

            int position = getAdapterPosition();
            category c = categoryList.get(position);
            ctx.startActivity(new Intent(ctx,Product_Container.class).putExtra("cid",c.getId()));
        }
    }
}
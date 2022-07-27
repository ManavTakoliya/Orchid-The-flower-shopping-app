package flexiconsofttech.orchid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class topproductAdapter extends RecyclerView.Adapter {
    private Context ctx;
    private ArrayList<top4> top4list;

    public topproductAdapter(Context ctx, ArrayList<top4> top4list) {

        this.ctx = ctx;
        this.top4list = top4list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View productrow = inflater.inflate(R.layout.top4_row,null);
        MyWidgetContainer container = new MyWidgetContainer(productrow);
        return container;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyWidgetContainer container = (MyWidgetContainer) holder;
        container.tlblname.setText(top4list.get(position).getTname());
        String ImgUrl = common.GetImageUrl() + "product/" + top4list.get(position).getTphoto();
        Picasso.with(ctx).load(ImgUrl).into(container.timgphoto);
    }

    @Override
    public int getItemCount() {
        return top4list.size();
    }

    class MyWidgetContainer extends RecyclerView.ViewHolder {

        @BindView(R.id.timgphoto) ImageView timgphoto;
        @BindView(R.id.tlblname) TextView tlblname;

        public MyWidgetContainer(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

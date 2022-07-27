package flexiconsofttech.orchid;

import android.content.Context;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class MainSliderAdapter extends SliderAdapter {
    private ArrayList<offers> imageList;


    public MainSliderAdapter(ArrayList<offers> imageList) {

        this.imageList = imageList;
    }


    @Override
    public int getItemCount() {
        return imageList.size();
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder viewHolder) {
        MyLog.p(imageList.get(position).getOphoto());
        viewHolder.bindImageSlide(common.GetImageUrl() + "offers/" + imageList.get(position).getOphoto());

    }
}
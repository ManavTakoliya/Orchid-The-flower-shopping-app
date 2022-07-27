package flexiconsofttech.orchid;

import java.util.ArrayList;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

class MainSliderAdapter1 extends SliderAdapter {
    private ArrayList<String> imagelist;

    public MainSliderAdapter1(ArrayList<String> imagelist) {

        this.imagelist = imagelist;
    }

    @Override
    public int getItemCount() {
        return imagelist.size();
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder imageSlideViewHolder) {
        imageSlideViewHolder.bindImageSlide(imagelist.get(position));

    }
}

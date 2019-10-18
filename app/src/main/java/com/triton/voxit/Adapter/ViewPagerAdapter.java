package com.triton.voxit.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.shape.RoundedCornerTreatment;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapper;
import com.bumptech.glide.signature.StringSignature;
import com.squareup.picasso.Picasso;
import com.triton.voxit.R;
import com.triton.voxit.model.BannerResponseBean;
import com.triton.voxit.model.RoundCornersDrawable;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {
    private final ArrayList<BannerResponseBean> imageUrls;
    private Context context;
//    private String[] imageUrls;
    private LayoutInflater inflater;
    private View itemView;

    public ViewPagerAdapter(Context context, ArrayList<BannerResponseBean> imageUrls){

        this.context = context;
        this.imageUrls = imageUrls;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return imageUrls.size();
//        return imageUrls.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);

    }


    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View itemView = inflater.inflate(R.layout.sliding_image, view, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.itemImage);

        BannerResponseBean imageitem = imageUrls.get(position);

        try {
            String imageURL = imageitem.getImage_path();
//            String imageURL = imageUrls[position];
//            Picasso.with(context).load(imageURL).into(imageView);

//        Glide.with(context)
//                .load(imageURL)
//                .animate(R.anim.abc_fade_in)
//                .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
//                .centerCrop()
//                .into(imageView);
            Picasso.with(context)
                    .load(imageURL)
                    .into(imageView);

        } catch (NumberFormatException nfe) {
            // Handle the condition when str is not a number.
            Log.i("nummmberfromae", "" + nfe);
        }


        view.addView(itemView);

        return itemView;

    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}

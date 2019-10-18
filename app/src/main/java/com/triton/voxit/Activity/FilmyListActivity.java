package com.triton.voxit.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.triton.voxit.Adapter.FilmyListAdapter;
import com.triton.voxit.Adapter.GenreAdapter;
import com.triton.voxit.Adapter.TopGenreAdapter;
import com.triton.voxit.R;
import com.triton.voxit.model.Genre;
import com.triton.voxit.model.RecyclerViewItem;
import com.triton.voxit.model.Trending;

import java.util.ArrayList;
import java.util.List;

public class FilmyListActivity extends Activity {
    public static RecyclerViewItem filmy_list = new RecyclerViewItem();
    public static boolean toRefresh;

    RecyclerView recyclerView;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filmy_list_items);
        initUI();
        setFilmyAdapter();
        displayData();

    }//end of oncreate

    public void displayData(){
        Intent i = getIntent();
        String imageUrl = i.getStringExtra("imageUrl");
        Glide.with(this).load(imageUrl).into(imageView);
    }

    private void initUI(){
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        imageView = (ImageView)findViewById(R.id.itemImage);
    }
    public void setFilmyAdapter(){

//        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
//        //add space item decoration and pass space you want to give
//        recyclerView.addItemDecoration(new EqualSpacingItemDecoration(0));
//        //finally set adapter
//        recyclerView.setAdapter(new FilmyListAdapter(filmyList(),this));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),1,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        //add space item decoration and pass space you want to give
        recyclerView.addItemDecoration(new EqualSpacingItemDecoration(0));
        //finally set adapter
        recyclerView.setAdapter(new FilmyListAdapter( filmyList(),this));
    }
    private List<RecyclerViewItem> filmyList() {
        List<RecyclerViewItem> recyclerViewItems = new ArrayList<>();

        String[] imageUrls = {"https://cdn.pixabay.com/photo/2016/11/18/17/42/barbecue-1836053_640.jpg",
                "https://cdn.pixabay.com/photo/2016/07/11/03/23/chicken-rice-1508984_640.jpg",
                "https://cdn.pixabay.com/photo/2017/03/30/08/10/chicken-intestine-2187505_640.jpg",
                "https://cdn.pixabay.com/photo/2017/02/15/15/17/meal-2069021_640.jpg",
                "https://cdn.pixabay.com/photo/2017/06/01/07/15/food-2362678_640.jpg"};
        String[] titles = {"suryan",
                "peace",
                "yoga", "suryan", "yoga"};
        String[] descriptions = {"jason ma",
                "rejman",
                "Pink",
                "Harry",
                "Harry"};

        for (int i = 0; i < imageUrls.length; i++) {
            Trending listItem = new Trending(titles[i], descriptions[i], imageUrls[i]);
            //add food items
            recyclerViewItems.add(listItem);
        }


        return recyclerViewItems;
    }

}

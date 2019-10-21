package com.triton.voxit.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.triton.voxit.Activity.AudioActivity;
import com.triton.voxit.Activity.LoginActivity;
import com.triton.voxit.R;
import com.triton.voxit.SessionManager.SessionManager;
import com.triton.voxit.Utlity.MediaPlayerSingleton;
import com.triton.voxit.app.App;
import com.triton.voxit.model.PreventListener;
import com.triton.voxit.model.SearchData;
import com.triton.voxit.model.SearchListData;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private ArrayList<SearchData> contactList;
    private ArrayList<SearchData> contactListFiltered;
    //private AttendLocationListAdapter.ContactsAdapterListener listener;
    SearchData contact;
    String result;
   // private final String flag;
    SessionManager session;
    private MediaPlayerSingleton mps;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv, desTv,header_name,header_des;
         public ImageView imageView,imageView1;
        public LinearLayout downloadLt;

        public MyViewHolder(View view) {
            super(view);
            nameTv = view.findViewById(R.id.tvTitle);
            desTv = view.findViewById(R.id.tvDes);
//            header_name = view.findViewById(R.id.tvTitle1);
//            header_des = view.findViewById(R.id.tvDes1);

             imageView = view.findViewById(R.id.itemImage);
            //imageView1 = view.findViewById(R.id.itemImage1);
            session = new SessionManager(context);
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    contact = contactListFiltered.get(getAdapterPosition());
//                    gotoSFMopsstatus();
//
//                }
//            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (PreventListener.getClickEnable()) {
                        contact = contactListFiltered.get(getAdapterPosition());
                        gotoSFMopsstatus();
                    }
                }
            });
        }
    }

    public void gotoSFMopsstatus() {


       // Log.e("isLoggedIn",session.isLoggedIn());
        if (!App.appUtils.isNetAvailable()) {
            alertUserP(context, "Connection Error", "No Internet connection available", "OK");
        }else {
            if (session.isLoggedIn()) {
                mps.releasePlayer();
                context.startActivity(new Intent(context, AudioActivity.class)
                        .putExtra("jockey_id", String.valueOf(contact.getJockey_id()))
                        .putExtra("song", contact.getAudio_path())
                        .putExtra("title", contact.getTitle())
                        .putExtra("description", contact.getDiscription())
                        .putExtra("image", contact.getImage_path())
                        .putExtra("name", contact.getName())
                        .putExtra("audio_length", contact.getAudio_length())
                        .putExtra("type", "Search")
                        .putExtra("audio_id", contact.getAudio_id())
                        .putExtra("songsList", contactListFiltered)
                        .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));

            } else {
                context.startActivity(new Intent(context, LoginActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
            }
        }
    }
    public void alertUserP(Context context, String title, String msg, String btn) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setMessage(msg).setTitle(title).setCancelable(false)
                .setPositiveButton(btn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog alert = builder.create();
        alert.show();
    }
    public SearchAdapter(Context context, ArrayList<SearchData> contactList) {
        this.context = context;
        //this.flag = flag;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
        mps = MediaPlayerSingleton.getInstance(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_list_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        for (SearchData data : contactListFiltered) {
            Log.d("Data", data.toString());
        }

        if (contactListFiltered.size() == 0) {

            Log.d("error ", String.valueOf(contactListFiltered.size()));

            // Toast.makeText(context," No Items Found",Toast.LENGTH_LONG).show();

        } else {
            final SearchData contact = contactListFiltered.get(position);
            holder.nameTv.setText(contact.getTitle());
            holder.desTv.setText(contact.getDiscription());
            Glide.with(context).load(contact.getImage_path()).into(holder.imageView);
//            holder.header_name.setText(contact.getHeader_title());
//            holder.header_des.setText(contact.getHeader_des());
//            Glide.with(context).load(contact.getHeader_imageUrl()).into(holder.imageView1);
        }


    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = contactList;

                } else {
                    ArrayList<SearchData> filteredList = new ArrayList<>();
                    for (SearchData row : contactList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<SearchData>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(SearchListData contact);
    }
}

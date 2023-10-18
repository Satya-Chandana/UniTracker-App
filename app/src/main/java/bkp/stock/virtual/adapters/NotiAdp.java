package bkp.stock.virtual.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import org.json.JSONArray;

import java.util.List;

import bkp.stock.virtual.R;
import bkp.stock.virtual.dto.MenuData;
import bkp.stock.virtual.interfaces.ItemClickListner;

/*
 * adapter for home categories grid
 * with image loading
 * */
public class NotiAdp extends RecyclerView.Adapter<NotiAdp.MyViewHolder> {

    /*
     * list and activity delaration
     * */
    private JSONArray l1;
    private Activity activity;
    private ItemClickListner listner;

    /*
     * view holder class for init View objects from xml layout file
     * */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        //        public TextView txt1;
        public RelativeLayout rl1;
        public MyViewHolder(View view) {
            super(view);

//            txt1 = view.findViewById(R.id.txt1);

            rl1 = view.findViewById(R.id.rl1);
        }
    }

    /*
     * constructor for taking data from activity classe
     * */
    public NotiAdp(Activity activity, JSONArray l1) {
        this.l1 = l1;
        this.activity = activity;
        listner = (ItemClickListner) activity;
    }

    public NotiAdp(Activity activity, Fragment fragment, JSONArray l1) {
        this.l1 = l1;
        this.activity = activity;
        listner = (ItemClickListner) fragment;
    }
    /*
     * connecting layout xml file with java file
     * connecting item layout with adapter
     * */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.noti_item, parent, false);

        return new MyViewHolder(itemView);
    }
    /*
     *
     * adding click listner to view
     * and setting values from list to view
     * */
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        try {


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return l1.length();
    }

}
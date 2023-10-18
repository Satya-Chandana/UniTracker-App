package bkp.stock.virtual.adapters;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;

import java.util.List;

import bkp.stock.virtual.R;
import bkp.stock.virtual.activities.AddClass;
import bkp.stock.virtual.activities.AddTeacher;
import bkp.stock.virtual.activities.LoginScreen;
import bkp.stock.virtual.dto.ClassDto1;
import bkp.stock.virtual.dto.MenuData;
import bkp.stock.virtual.dto.UserConst;
import bkp.stock.virtual.interfaces.ItemClickListner;
import bkp.stock.virtual.other.FrbDb;

import bkp.stock.virtual.other.MySharedPref;
/*
 * adapter for home categories grid
 * with image loading
 * */
public class ClassAdp extends RecyclerView.Adapter<ClassAdp.MyViewHolder> {

    /*
     * list and activity delaration
     * */
    private List<ClassDto1> l1;
    private Activity activity;
    private ItemClickListner listner;

    /*
     * view holder class for init View objects from xml layout file
     * */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txt1;
        public RelativeLayout rl1;
        public ImageView cross_icon,edit_icon;
        public MyViewHolder(View view) {
            super(view);

            txt1 = view.findViewById(R.id.txt1);
            cross_icon = view.findViewById(R.id.cross_icon);
            edit_icon = view.findViewById(R.id.edit_icon);

            rl1 = view.findViewById(R.id.rl1);
        }
    }

    /*
     * constructor for taking data from activity classe
     * */
    public ClassAdp(Activity activity, List<ClassDto1> l1) {
        this.l1 = l1;
        this.activity = activity;
        listner = (ItemClickListner) activity;
    }

    public ClassAdp(Activity activity, Fragment fragment, List<ClassDto1> l1) {
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
                .inflate(R.layout.class_item, parent, false);

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
            ClassDto1 classDto1 = l1.get(holder.getAdapterPosition());
            holder.txt1.setText(classDto1.getClass_name());
            if(new MySharedPref().getData(activity.getApplicationContext(),"ltype", UserConst.user).equals(UserConst.admin)){
                holder.edit_icon.setVisibility(View.VISIBLE);
                holder.cross_icon.setVisibility(View.VISIBLE);
            }
            else{
                holder.edit_icon.setVisibility(View.GONE);
                holder.cross_icon.setVisibility(View.GONE);
            }
            holder.edit_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity.getApplicationContext(), AddClass.class);
                    intent.putExtra("uid",classDto1.getClass_id());
                    activity.startActivity(intent);
                }
            });
            holder.cross_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(activity)
                            .setTitle("Delete")
                            .setMessage("Are you sure you want to delete?")

                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef = database.getReference(FrbDb.class_tbl1);
                                    myRef.child(classDto1.getClass_id()).removeValue();

                                }
                            })

                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return l1.size();
    }

}
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;

import java.util.List;

import bkp.stock.virtual.R;
import bkp.stock.virtual.activities.AddTeacher;
import bkp.stock.virtual.activities.AssignDetail;
import bkp.stock.virtual.activities.LoginScreen;
import bkp.stock.virtual.activities.MainActivity;
import bkp.stock.virtual.dto.AssignDto;
import bkp.stock.virtual.dto.MenuData;
import bkp.stock.virtual.dto.UpDto;
import bkp.stock.virtual.dto.UserConst;
import bkp.stock.virtual.dto.UserDto;
import bkp.stock.virtual.interfaces.ItemClickListner;
import bkp.stock.virtual.other.Const;
import bkp.stock.virtual.other.FrbDb;
import bkp.stock.virtual.other.MySharedPref;

/*
 * adapter for home categories grid
 * with image loading
 * */
public class UpAdp extends RecyclerView.Adapter<UpAdp.MyViewHolder> {

    /*
     * list and activity delaration
     * */
    private List<UpDto> l1;
    private Activity activity;
    private ItemClickListner listner;

    /*
     * view holder class for init View objects from xml layout file
     * */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txt1,txt2,txt20,txt3;
        public RelativeLayout rl1;
        public ImageView cross_icon;
        public MyViewHolder(View view) {
            super(view);

            txt1 = view.findViewById(R.id.txt1);
            txt2 = view.findViewById(R.id.txt2);
            txt20 = view.findViewById(R.id.txt20);
            cross_icon = view.findViewById(R.id.cross_icon);
            txt3 = view.findViewById(R.id.txt3);

            rl1 = view.findViewById(R.id.rl1);
        }
    }

    /*
     * constructor for taking data from activity classe
     * */
    public UpAdp(Activity activity, List<UpDto> l1) {
        this.l1 = l1;
        this.activity = activity;
        listner = (ItemClickListner) activity;
    }

    public UpAdp(Activity activity, Fragment fragment, List<UpDto> l1) {
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
                .inflate(R.layout.up_item, parent, false);

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
            UpDto assignDto = l1.get(holder.getAbsoluteAdapterPosition());
            holder.txt1.setText(assignDto.getTitle());
            holder.txt2.setText(assignDto.getDatetime());
            holder.txt3.setText(assignDto.getDesc());

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(FrbDb.user_tbl);
            myRef.child(new MySharedPref().getData(activity.getApplicationContext(),"ldata","")).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot!=null && snapshot.getValue()!=null){
                        UserDto userDto = snapshot.getValue(UserDto.class);
                        if(userDto!=null){


                            if(userDto.getType().equals(UserConst.admin)){
                                holder.cross_icon.setVisibility(View.VISIBLE);
                            }
                            else  if(userDto.getType().equals(UserConst.teacher)){

                                holder.cross_icon.setVisibility(View.VISIBLE);
                            }
                            else{
                                if(userDto.getClassname().equals("1")) {
                                    holder.cross_icon.setVisibility(View.VISIBLE);
                                }
                            }

                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

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
                                    DatabaseReference myRef = database.getReference(FrbDb.up_tbl);
                                    myRef.child(assignDto.getUp_id()).removeValue();
                                    l1.remove(holder.getAdapterPosition());
                                    notifyDataSetChanged();
                                }
                            })

                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference(FrbDb.user_tbl);
            myRef.child(assignDto.getBy()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot!=null && snapshot.getValue()!=null){
                        UserDto userDto = snapshot.getValue(UserDto.class);
                        if(userDto!=null){
                            holder.txt20.setText("By: "+userDto.getName());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

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
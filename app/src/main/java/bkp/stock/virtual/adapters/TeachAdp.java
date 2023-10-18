package bkp.stock.virtual.adapters;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;

import java.util.List;

import bkp.stock.virtual.R;
import bkp.stock.virtual.activities.AddTeacher;
import bkp.stock.virtual.dto.MenuData;
import bkp.stock.virtual.dto.UserConst;
import bkp.stock.virtual.dto.UserDto;
import bkp.stock.virtual.interfaces.ItemClickListner;
import bkp.stock.virtual.other.FrbDb;

/*
 * adapter for home categories grid
 * with image loading
 * */
public class TeachAdp extends RecyclerView.Adapter<TeachAdp.MyViewHolder> {

    /*
     * list and activity delaration
     * */
    private List<UserDto> l1;
    private Activity activity;
    private ItemClickListner listner;

    /*
     * view holder class for init View objects from xml layout file
     * */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txt1,txt2,txt3;
        public RelativeLayout rl1;
        public ImageView cross_icon,edit_icon;
        public Button bt1;
        public MyViewHolder(View view) {
            super(view);

            txt1 = view.findViewById(R.id.txt1);
            txt2 = view.findViewById(R.id.txt2);
            txt3 = view.findViewById(R.id.txt3);
            cross_icon = view.findViewById(R.id.cross_icon);
            edit_icon = view.findViewById(R.id.edit_icon);
            rl1 = view.findViewById(R.id.rl1);
            bt1 = view.findViewById(R.id.bt1);
        }
    }

    /*
     * constructor for taking data from activity classe
     * */
    public TeachAdp(Activity activity, List<UserDto> l1) {
        this.l1 = l1;
        this.activity = activity;
        listner = (ItemClickListner) activity;
    }

    public TeachAdp(Activity activity, Fragment fragment, List<UserDto> l1) {
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
                .inflate(R.layout.teach_item, parent, false);

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
            UserDto userDto = l1.get(holder.getAdapterPosition());

            if(userDto.getType().equals(UserConst.user)){
                if(userDto.getClassname().equals("1")){
                    holder.bt1.setText(activity.getString(R.string.privillged));
                }
                else{
                    holder.bt1.setText(activity.getString(R.string.not_privillged));
                }
                holder.bt1.setVisibility(View.VISIBLE);
            }
            else{
                holder.bt1.setVisibility(View.GONE);
            }

            holder.bt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(userDto.getClassname().equals("1")){
                        userDto.setClassname("0");
                        holder.bt1.setText(activity.getString(R.string.not_privillged));
                    }
                    else{
                        userDto.setClassname("1");
                        holder.bt1.setText(activity.getString(R.string.privillged));
                    }
                    l1.set(holder.getAdapterPosition(),userDto);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference(FrbDb.user_tbl);
                    myRef.child(userDto.getUid()).child("classname").setValue(userDto.getClassname());
                }
            });

            holder.txt1.setText(userDto.getName());
            holder.txt2.setText(userDto.getEmail());

            String keyw = "Roll No:";
            if(userDto.getType().equals(UserConst.teacher)){
                keyw = "Class Name:";
            }
            holder.txt3.setText(keyw+" "+userDto.getRollno());

            holder.edit_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity.getApplicationContext(), AddTeacher.class);
                    intent.putExtra("type", userDto.getType());
                    intent.putExtra("uid",userDto.getUid());
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
                                    DatabaseReference myRef = database.getReference(FrbDb.user_tbl);
                                    myRef.child(userDto.getUid()).removeValue();


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
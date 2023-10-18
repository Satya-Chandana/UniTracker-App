package bkp.stock.virtual.fragments;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import bkp.stock.virtual.R;
import bkp.stock.virtual.adapters.AssAdp;
import bkp.stock.virtual.adapters.UpAdp;
import bkp.stock.virtual.dto.AssignDto;
import bkp.stock.virtual.dto.UpDto;
import bkp.stock.virtual.dto.UserConst;
import bkp.stock.virtual.dto.UserDto;
import bkp.stock.virtual.interfaces.ItemClickListner;
import bkp.stock.virtual.other.Const;
import bkp.stock.virtual.other.FrbDb;
import bkp.stock.virtual.other.MySharedPref;

/*
 * Assignments page of home screen
 * */
public class Assignments1 extends Fragment implements ItemClickListner {

    /*
     * Ui Declaration
     * */
    private Activity activity;
    private RecyclerView rlv1;
    private List<UpDto> l1 = new ArrayList<>();
    private List<String> lk = new ArrayList<>();
    private View v;
    private UserDto ldata = null;

    public Assignments1(Activity activity) {
        this.activity = activity;
    }

    /*
     * this method connects Ui xml file with java file
     * */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_assignments, container, false);
        initUI();

        loadNow();
        return v;
    }

    private void loadNow() {
        try{
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(FrbDb.user_tbl);
            myRef.child(new MySharedPref().getData(activity.getApplicationContext(),"ldata",""))
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot!=null && snapshot.getValue()!=null){
                                ldata = snapshot.getValue(UserDto.class);
                                if(ldata!=null){
                                    l1 = new ArrayList<>();
                                    addAdp();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    /*
     * UI Init with list config
     * */
    private void initUI() {
        rlv1 = v.findViewById(R.id.rlv1);

    }


    /*
     * connecting list with adapter
     * */
    private void addAdp() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(FrbDb.up_tbl);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!=null && snapshot.hasChildren()){


                    System.out.println("regfdd----------------"+snapshot.getChildrenCount());
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        UpDto assignDto = dataSnapshot.getValue(UpDto.class);
                        if(lk.contains("#"+assignDto.getUp_id()+"#")==false) {
                            l1.add(assignDto);
                            lk.add("#"+assignDto.getUp_id()+"#");

                        }

                    }

                    UpAdp catAdp = new UpAdp(activity,Assignments1.this,l1);
                    LinearLayoutManager layoutManager
                            = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
                    rlv1.setLayoutManager(layoutManager);
                    rlv1.setItemAnimator(new DefaultItemAnimator());
                    rlv1.setNestedScrollingEnabled(false);
                    rlv1.setAdapter(catAdp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        if(ldata.getType().equals(UserConst.teacher)){
            System.out.println("sdc------class_name------"+ldata.getRollno());
            myRef.orderByChild("class_name").equalTo(ldata.getRollno())
                    .addValueEventListener(valueEventListener);
        }
        else if(ldata.getType().equals(UserConst.user)){
            if(ldata.getClassname().equals("1")){
                myRef.orderByChild("class_name").equalTo(ldata.getRollno().substring(0, 6))
                        .addValueEventListener(valueEventListener);
            }
            else {
                myRef.orderByChild("created_by").equalTo(ldata.getUid())
                        .addValueEventListener(valueEventListener);
                addAdp1();
            }
        }
        else {
            myRef.addValueEventListener(valueEventListener);
        }

    }



    private void addAdp1() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(FrbDb.up_tbl);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!=null && snapshot.hasChildren()){
                    System.out.println("regfdd-----1-----------"+snapshot.getChildrenCount());
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        UpDto assignDto = dataSnapshot.getValue(UpDto.class);
                        if(assignDto.getType().equals(UserConst.teacher)) {
                            if(lk.contains("#"+assignDto.getUp_id()+"#")==false) {
                                l1.add(assignDto);
                                lk.add("#"+assignDto.getUp_id()+"#");
                                
                            }
                        }

                    }

                    UpAdp assAdp = new UpAdp(activity,Assignments1.this,l1);
                    LinearLayoutManager layoutManager
                            = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
                    rlv1.setLayoutManager(layoutManager);
                    rlv1.setItemAnimator(new DefaultItemAnimator());
                    rlv1.setNestedScrollingEnabled(false);
                    rlv1.setAdapter(assAdp);
                    
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        myRef.orderByChild("class_name").equalTo(ldata.getRollno().substring(0, 6))
                .addValueEventListener(valueEventListener);

    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemClick1(int position) {

    }

    @Override
    public void onItemClick(int position, View v) {

    }

    @Override
    public void onItemClick(int position, int position1) {

    }
}


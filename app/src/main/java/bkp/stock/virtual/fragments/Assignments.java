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
import bkp.stock.virtual.dto.AssignDto;
import bkp.stock.virtual.dto.UserConst;
import bkp.stock.virtual.dto.UserDto;
import bkp.stock.virtual.interfaces.ItemClickListner;
import bkp.stock.virtual.other.Const;
import bkp.stock.virtual.other.FrbDb;
import bkp.stock.virtual.other.MySharedPref;

/*
 * Assignments page of home screen
 * */
public class Assignments extends Fragment implements ItemClickListner {

    /*
     * Ui Declaration
     * */
    private Activity activity;
    private RecyclerView rlv1;
    private List<AssignDto> l1 = new ArrayList<>();
    private List<String> lk = new ArrayList<>();
    private View v;
    private int rtype = 1;
    private UserDto ldata = null;

    public Assignments(Activity activity,int rtype) {
        this.activity = activity;
        this.rtype = rtype;
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
        DatabaseReference myRef = database.getReference(FrbDb.assign_tbl);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!=null && snapshot.hasChildren()){

                    List<AssignDto> l2 = new ArrayList<>();

                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        AssignDto assignDto = dataSnapshot.getValue(AssignDto.class);
                        long ass_date = Const.getLocalTimeStamp(assignDto.getLast_date(),"dd-MM-yyyy, HH:mm");
                        long curr_date = Const.getLocalTimeStamp(Const.getLocalTime(Const.getUtcTime(),"dd-MM-yyyy, HH:mm"),"dd-MM-yyyy, HH:mm");
                        if(rtype==1){
                            if(ass_date>curr_date){
                                if(lk.contains("#"+assignDto.getAss_id()+"#")==false) {
                                    System.out.println("regfdd----1j------------"+assignDto.getTitle());
                                    l1.add(assignDto);
                                    lk.add("#"+assignDto.getAss_id()+"#");
                                    l2.add(assignDto);
                                }
                            }
                        }
                        else{
                            if(ass_date<=curr_date){
                                if(lk.contains("#"+assignDto.getAss_id()+"#")==false) {
                                    System.out.println("regfdd-----0j-----------"+assignDto.getTitle());
                                    lk.add("#"+assignDto.getAss_id()+"#");
                                    l1.add(assignDto);
                                }
                            }
                        }

                    }

                    AssAdp catAdp = new AssAdp(activity,Assignments.this,l1);
                    LinearLayoutManager layoutManager
                            = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
                    rlv1.setLayoutManager(layoutManager);
                    rlv1.setItemAnimator(new DefaultItemAnimator());
                    rlv1.setNestedScrollingEnabled(false);
                    rlv1.setAdapter(catAdp);
//                    if(ldata.getType().equals(UserConst.user)){
//                        if(ldata.getClassname().equals("1")){
//
//                        }
//                        else {
//                            addAdp1();
//
//                        }
//                    }
                    if(l2.size()>0){
                        addReminder(l2);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        if(ldata.getType().equals(UserConst.teacher)){
            System.out.println("regfdd----1j-----search3-------"+ldata.getUid());
            System.out.println("sdc------class_name------"+ldata.getRollno());
            myRef.orderByChild("class_name").equalTo(ldata.getRollno())
                    .addValueEventListener(valueEventListener);
        }
        else if(ldata.getType().equals(UserConst.user)){
            if(ldata.getClassname().equals("1")){
                System.out.println("regfdd----1j-----search2-------"+ldata.getUid());
                myRef.orderByChild("class_name").equalTo(ldata.getRollno().substring(0, 6))
                        .addValueEventListener(valueEventListener);
            }
            else {
                System.out.println("regfdd----1j-----search-------"+ldata.getUid());
                myRef.orderByChild("created_by").equalTo(ldata.getUid())
                        .addValueEventListener(valueEventListener);
                addAdp1();
            }
        }
        else {
            System.out.println("regfdd----1j-----search4-------"+ldata.getUid());
            myRef.addValueEventListener(valueEventListener);
        }

    }

    private void addReminder(List<AssignDto> l2) {
        try{

            for(AssignDto assignDto:l2){
                if(readCalendarEvent(activity,assignDto.getTitle())==false) {
                    long ass_date = Const.getLocalTimeStamp(assignDto.getLast_date(), "dd-MM-yyyy, HH:mm");
                    String new_date1 = Const.getLocalTime((ass_date - (1000 * 60 * 60 * 24)), "dd-MM-yyyy, HH:mm");
                    System.out.println("ass_date--------------" + assignDto.getLast_date());
                    System.out.println("ass_date-----new---------" + new_date1);

                    ContentResolver cr = activity.getContentResolver();
                    ContentValues values = new ContentValues();
                    values.put(CalendarContract.Events.DTSTART, (ass_date));
                    values.put(CalendarContract.Events.DTEND, (ass_date));
                    values.put(CalendarContract.Events.TITLE, assignDto.getTitle());
                    values.put(CalendarContract.Events.DESCRIPTION, assignDto.getDesc());
                    values.put(CalendarContract.Events.CALENDAR_ID, getCalendar(activity));
                    values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
                    Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

                    long eventID = Long.parseLong(uri.getLastPathSegment());
                    System.out.println("ass_date-----eventID---------" + eventID);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public String getCalendar(Context c) {

        String projection[] = {"_id", "calendar_displayName"};
        Uri calendars;
        calendars = Uri.parse("content://com.android.calendar/calendars");

        ContentResolver contentResolver = c.getContentResolver();
        Cursor managedCursor = contentResolver.query(calendars, projection, null, null, null);
        String calName = "";
        String calID = "";
        if (managedCursor.moveToFirst()){


            int cont= 0;
            if(cont==0) {
                int nameCol = managedCursor.getColumnIndex(projection[1]);
                int idCol = managedCursor.getColumnIndex(projection[0]);
                do {
                    if(cont==0) {
                        calName = managedCursor.getString(nameCol);
                        calID = managedCursor.getString(idCol);

                        cont++;
                    }
                } while (managedCursor.moveToNext());
            }
            managedCursor.close();
        }
        return calID;

    }


    public static boolean readCalendarEvent(Context context,String title) {
        Cursor cursor = context.getContentResolver()
                .query(
                        Uri.parse("content://com.android.calendar/events"),
                        new String[] { "calendar_id", "title", "description",
                                "dtstart", "dtend", "eventLocation" }, null,
                        null, null);
        cursor.moveToFirst();
        // fetching calendars name
        String CNames[] = new String[cursor.getCount()];



        for (int i = 0; i < CNames.length; i++) {

           if(cursor.getString(1).equals(title)){
               return true;

           }
            cursor.moveToNext();

        }
        return false;
    }


    private void addAdp1() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(FrbDb.assign_tbl);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!=null && snapshot.hasChildren()){
                    List<AssignDto> l2 = new ArrayList<>();
                    System.out.println("regfdd-----1-----------"+snapshot.getChildrenCount());
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        AssignDto assignDto = dataSnapshot.getValue(AssignDto.class);
                        long ass_date = Const.getLocalTimeStamp(assignDto.getLast_date(),"dd-MM-yyyy, HH:mm");
                        long curr_date = Const.getLocalTimeStamp(Const.getLocalTime(Const.getUtcTime(),"dd-MM-yyyy, HH:mm"),"dd-MM-yyyy, HH:mm");
                        if(rtype==1){
                            if(ass_date>curr_date){
                                if(assignDto.getCreated_by_type().equals(UserConst.teacher)) {
                                    if(lk.contains("#"+assignDto.getAss_id()+"#")==false) {
                                        l1.add(assignDto);
                                        lk.add("#"+assignDto.getAss_id()+"#");
                                        l2.add(assignDto);
                                    }
                                }
                            }
                        }
                        else{
                            if(ass_date<=curr_date){
                                if(assignDto.getCreated_by_type().equals(UserConst.teacher)) {
                                    if(lk.contains("#"+assignDto.getAss_id()+"#")==false) {
                                        lk.add("#"+assignDto.getAss_id()+"#");
                                        l1.add(assignDto);
                                    }
                                }
                            }
                        }

                    }

                    AssAdp assAdp = new AssAdp(activity,Assignments.this,l1);
                    LinearLayoutManager layoutManager
                            = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
                    rlv1.setLayoutManager(layoutManager);
                    rlv1.setItemAnimator(new DefaultItemAnimator());
                    rlv1.setNestedScrollingEnabled(false);
                    rlv1.setAdapter(assAdp);
                    if(l2.size()>0){
                        addReminder(l2);
                    }
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


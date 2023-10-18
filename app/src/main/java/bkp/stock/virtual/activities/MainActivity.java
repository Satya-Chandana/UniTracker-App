package bkp.stock.virtual.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import bkp.stock.virtual.R;
import bkp.stock.virtual.adapters.MenuAdp;
import bkp.stock.virtual.adapters.ViewPagerAdapter;
import bkp.stock.virtual.dto.MenuData;
import bkp.stock.virtual.dto.UserConst;
import bkp.stock.virtual.dto.UserDto;
import bkp.stock.virtual.fragments.Assignments;
import bkp.stock.virtual.fragments.Assignments1;
import bkp.stock.virtual.interfaces.ItemClickListner;
import bkp.stock.virtual.other.Const;
import bkp.stock.virtual.other.FrbDb;
import bkp.stock.virtual.other.MySharedPref;
import bkp.stock.virtual.other.MySharedPref;
import bkp.stock.virtual.other.MySharedPref;

public class MainActivity extends AppCompatActivity implements ItemClickListner {

    private TabLayout tabs;
    private ViewPager vp1;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private RecyclerView menu_lv1;
    private TextView txt_name,email_txt,utype_txt;
    private  UserDto userDto = null;
    private List<MenuData> menus = new ArrayList<>();
    String str1[] = new String[]{
            Manifest.permission.READ_CALENDAR
            , Manifest.permission.WRITE_CALENDAR
    };
    private boolean checkIfAlreadyhavePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED
        ) {
            return true;
        } else {
            return false;
        }
    }
    private void requestReadPhoneStatePermission() {
        ActivityCompat.requestPermissions(MainActivity.this, str1,
                1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(checkIfAlreadyhavePermission()){
            setupViewPager(vp1);
            tabs.setupWithViewPager(vp1);
        }
        else{
            requestReadPhoneStatePermission();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        initUI();

        vp1.setOffscreenPageLimit(3);
        vp1.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if(userDto.getType().equals(UserConst.admin) ||
                        (userDto.getType().equals(UserConst.user) &&
                                (userDto.getClassname().equals("1")==false))){
                    findViewById(R.id.fab11).setVisibility(View.GONE);
                }
                else{
                    findViewById(R.id.fab11).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if(checkIfAlreadyhavePermission()){
            setupViewPager(vp1);
            tabs.setupWithViewPager(vp1);
        }
        else{
            requestReadPhoneStatePermission();
        }
    }


    public void AddNow(View v){
        if(vp1.getCurrentItem()==2){
            startActivity(new Intent(getApplicationContext(), AddUpdate.class));

        }
        else {
            startActivity(new Intent(getApplicationContext(), AddAssgin.class));
        }
    }
    public void NotiNow(View v){
        startActivity(new Intent(getApplicationContext(),Notis.class));
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Assignments(MainActivity.this,1), "Due Assignments");
        adapter.addFragment(new Assignments(MainActivity.this,0), "Past Assignments");
        adapter.addFragment(new Assignments1(MainActivity.this), "Updates");
        viewPager.setAdapter(adapter);
    }
    private void initUI() {
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        tabs = findViewById(R.id.tabs);
        vp1 = findViewById(R.id.vp1);
        menu_lv1 = navigationView.getRootView().findViewById(R.id.menu_lv1);
        txt_name = navigationView.getRootView().findViewById(R.id.txt_name);
        email_txt = navigationView.getRootView().findViewById(R.id.email_txt);
        utype_txt = navigationView.getRootView().findViewById(R.id.utype_txt);
    }

    @Override
    protected void onResume() {
        super.onResume();

        try{
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(FrbDb.user_tbl);
            myRef.child(new MySharedPref().getData(getApplicationContext(),"ldata","")).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot!=null && snapshot.getValue()!=null){
                        userDto = snapshot.getValue(UserDto.class);
                        if(userDto!=null){

                            txt_name.setText(userDto.getName());
                            email_txt.setText(userDto.getEmail());
                            if(userDto.getType().equals(UserConst.admin)){
                                utype_txt.setText("Admin");
                                findViewById(R.id.fab11).setVisibility(View.GONE);
                            }
                            else  if(userDto.getType().equals(UserConst.teacher)){
                                utype_txt.setText("Teacher");
                                findViewById(R.id.fab11).setVisibility(View.VISIBLE);
                            }
                            else{
                                utype_txt.setText("Student");
                                findViewById(R.id.fab11).setVisibility(View.VISIBLE);
                            }

                        }

                        setMenu();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                @Override
                public void onComplete(@NonNull Task<String> task) {
                    try {
                        System.out.println("token----------------" + task.getResult().toString());
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference(FrbDb.token_tbl);
                        myRef.child(new MySharedPref().getData(getApplicationContext(),"ldata","")).setValue(task.getResult().toString());
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setMenu() {
        try {
            menus = new ArrayList<>();
            if(userDto!=null && userDto.getType().equals(UserConst.admin)) {
                menus.add(new MenuData(getString(R.string.teachers)));
                menus.add(new MenuData(getString(R.string.students)));
                menus.add(new MenuData(getString(R.string.classrooms)));
                menus.add(new MenuData(getString(R.string.subjects)));
            }
            else if(userDto!=null && userDto.getType().equals(UserConst.teacher)) {
                menus.add(new MenuData(getString(R.string.students)));
            }

            menus.add(new MenuData(getString(R.string.logout)));

            MenuAdp catAdp = new MenuAdp(MainActivity.this, menus);

            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
            menu_lv1.setLayoutManager(layoutManager);
            menu_lv1.setItemAnimator(new DefaultItemAnimator());
            menu_lv1.setNestedScrollingEnabled(false);

            menu_lv1.setRecycledViewPool(new RecyclerView.RecycledViewPool());
            menu_lv1.setAdapter(catAdp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(int position) {
        try{
            if(menus.get(position).getTitle().equals(getString(R.string.teachers))){
                Intent intent = new Intent(getApplicationContext(),Teachers.class);
                intent.putExtra("type",UserConst.teacher);
                startActivity(intent);
            }
            else if(menus.get(position).getTitle().equals(getString(R.string.students))){
                Intent intent = new Intent(getApplicationContext(),Teachers.class);
                intent.putExtra("type",UserConst.user);
                startActivity(intent);
            }
            else if(menus.get(position).getTitle().equals(getString(R.string.classrooms))){
                startActivity(new Intent(getApplicationContext(),ClassAc.class));
            }
            else if(menus.get(position).getTitle().equals(getString(R.string.subjects))){
                startActivity(new Intent(getApplicationContext(),SubjectsAc.class));
            }
            else if(menus.get(position).getTitle().equals(getString(R.string.logout))){
                new AlertDialog.Builder(this)
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")

                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                new MySharedPref().saveData(getApplicationContext(),"ldata","");
                                startActivity(new Intent(getApplicationContext(),LoginScreen.class));
                                finish();
                            }
                        })

                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Exit")
                    .setMessage("Are you sure you want to exit?")

                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })

                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }
    public void NavNow(View v) {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
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

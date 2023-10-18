package bkp.stock.virtual.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;

import bkp.stock.virtual.R;
import bkp.stock.virtual.adapters.NotiAdp;
import bkp.stock.virtual.adapters.TeachAdp;
import bkp.stock.virtual.interfaces.ItemClickListner;

public class Notis extends AppCompatActivity implements ItemClickListner {

    private RecyclerView rlv1;
    private JSONArray l1 = new JSONArray();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notis);
        initUI();

        addAdp();
    }

    public void  AddNow(View v){
        System.out.println("sdxzcc----------fab1----------");
        startActivity(new Intent(getApplicationContext(),AddTeacher.class));
        System.out.println("sdxzcc----------fab1---2-------");
    }

    public void NavNow(View v){
        finish();
    }

    private void addAdp() {
        l1 = new JSONArray();
        l1.put("");
        l1.put("");
        l1.put("");
        l1.put("");
        l1.put("");
        l1.put("");
        l1.put("");
        l1.put("");
        l1.put("");

        NotiAdp catAdp = new NotiAdp(this,l1);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rlv1.setLayoutManager(layoutManager);
        rlv1.setItemAnimator(new DefaultItemAnimator());
        rlv1.setNestedScrollingEnabled(false);
        rlv1.setAdapter(catAdp);

    }

    private void initUI() {
        TextView title_txt = findViewById(R.id.title);
        title_txt.setText(getString(R.string.notifications));
        rlv1 = findViewById(R.id.rlv1);
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

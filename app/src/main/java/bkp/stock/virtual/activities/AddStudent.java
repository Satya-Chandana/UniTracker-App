package bkp.stock.virtual.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import bkp.stock.virtual.R;

public class AddStudent extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_add);
        TextView title_txt = findViewById(R.id.title);
        title_txt.setText(getString(R.string.new_student));
    }

    public void NavNow(View v){
        finish();
    }
}

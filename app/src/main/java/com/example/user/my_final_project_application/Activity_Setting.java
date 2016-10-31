package com.example.user.my_final_project_application;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

public class Activity_Setting extends AppCompatActivity implements View.OnClickListener {
    public EditText editRadius;
    private RadioGroup radioGroup;
    private SharedPreferences sp;
    private int radius;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity__setting);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        radioGroup = (RadioGroup) findViewById(R.id.radio);
        editRadius = (EditText) findViewById(R.id.RadiousNum);
        findViewById(R.id.Save).setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Save:
                sp.edit().putInt("Rad",Integer.parseInt(editRadius.getText().toString())).apply();
                Intent in = new Intent(this,MainActivity.class);
                startActivity(in);

                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.Km_radio_but:
                        sp.edit().putString("KILOMETERS", "KM").apply();
                        break;
                    case R.id.Miles_radio_but:
                        sp.edit().putString("MILES", "MILES").apply();
                        break;


                }
        }
    }
}

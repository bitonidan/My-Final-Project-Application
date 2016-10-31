package com.example.user.my_final_project_application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.widget.Toast;

/**
 * Created by User on 29/09/2016.
 */
public class PowerConnectionReceiver extends BroadcastReceiver {

    public boolean isCharging;
    @Override
    public void onReceive(Context context, Intent intent) {
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        if ( isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ){
            Toast.makeText(context, "Battery  Charging!!", Toast.LENGTH_SHORT).show();
        }
        if (isCharging=status == BatteryManager.BATTERY_STATUS_DISCHARGING){
            Toast.makeText(context, "Battery Not Charging!!!", Toast.LENGTH_SHORT).show();
        }


    }

}


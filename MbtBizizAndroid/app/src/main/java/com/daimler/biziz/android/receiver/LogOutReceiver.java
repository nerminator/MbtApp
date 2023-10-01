package com.daimler.biziz.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class LogOutReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        /*MainActivity mainActivity = ((MyApplication) context.getApplicationContext()).mainActivity;
        mainActivity.etReceivedBroadcast.append("broadcast: "+intent.getAction()+"\n");*/
    }
}

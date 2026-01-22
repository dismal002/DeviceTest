package com.dismal.devicetest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class FactoryReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Log.e("lsz", "onreceive-->");
        int[] intArrayExtra = intent.getIntArrayExtra("testflag");
        Intent intent2 = new Intent(context, MainActivity.class);
        intent2.putExtra("testflag", intArrayExtra);
        intent2.addFlags(268468224);
        context.startActivity(intent2);
    }
}

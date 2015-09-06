package com.zhengping.lp;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

public class BootCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_WORLD_WRITEABLE);
		TelephonyManager tm  = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		boolean isStart = sp.getBoolean("start", false);
		if(!isStart) {
			return;
		} else {
			String saveSubscriberId = sp.getString("subscriberid", "");
			String safenumber = sp.getString("safenumber", "");
			
			//IMSI
			String subscriberId = tm.getSubscriberId();
			System.out.println(subscriberId);
			if(subscriberId.trim().equals(saveSubscriberId)) {
				return;
			} else {
				//SmsManager
				SmsManager manager = SmsManager.getDefault();
				List<String> message = manager.divideMessage("手机IMSI码为:" + saveSubscriberId + " 的手机，SIM已被更换，更换的IMSI码为:" + subscriberId);
				for(String msg : message) {
					manager.sendTextMessage(safenumber, null, msg, null, null);
				}
				
			}
			
		}
		
		
		
	}

}

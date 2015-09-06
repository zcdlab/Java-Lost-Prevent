package com.zhengping.lp;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class SMSReceivedBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_WORLD_WRITEABLE);
		boolean isStart = sp.getBoolean("start", false);
		String savePhoneNumber = sp.getString("safenumber", "");
		if(isStart) {
			Object[] object = (Object[]) intent.getSerializableExtra("pdus");
			
			byte[][] pdus = new byte[object.length][];
			
			for(int i=0;i<pdus.length;i++) {
				pdus[i] = (byte[]) object[i];
			}
			SmsMessage[] msgs = new SmsMessage[object.length];
			for(int i=0;i<pdus.length;i++) {
				msgs[i] = SmsMessage.createFromPdu(pdus[i]);
			}
			
			for(int i=0;i<msgs.length;i++) {
				String oriAddress = msgs[i].getDisplayOriginatingAddress();
				if(oriAddress.trim().equals(savePhoneNumber)) {
					String body = msgs[i].getDisplayMessageBody();
					if(body.contains("ilovedog")) {
						SmsManager manager = SmsManager.getDefault();
						List<String> message = manager.divideMessage("your phone is mine");
						for(String msg : message) {
							manager.sendTextMessage(savePhoneNumber, null, msg, null, null);
						}
					}
				}
				
				
			}
			
		}
		
	}

}

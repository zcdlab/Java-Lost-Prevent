package com.zhengping.lp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityMain extends Activity {
	
	AlertDialog setPasswordDialog;
	AlertDialog showPasswordDialog;
	SharedPreferences sp;
	
	EditText et_safenumber;
	Button btn_start;
	Button btn_modify;
	
	boolean isFirst;
	boolean isStart;
	TelephonyManager tm;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        sp = this.getSharedPreferences("data", MODE_WORLD_READABLE);
        isFirst = sp.getBoolean("first", true);
        if(isFirst) {
        	showSetPasswordDialog();
        	return;
        } else {
        	showInputPasswordDialog();
        }
        
		
        
    }
    
    private void showInputPasswordDialog() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.showpassworddialog, null);
		final EditText et_username = (EditText) view.findViewById(R.id.username);
		final EditText et_password = (EditText) view.findViewById(R.id.password);
		
		Button btn_ok = (Button) view.findViewById(R.id.ok);
		Button btn_cancle = (Button) view.findViewById(R.id.cancle);
		
		btn_ok.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String username = et_username.getText().toString();
				String password = et_password.getText().toString();
				
				if(username.trim().equals("") || password.trim().equals("")) {
					Toast.makeText(ActivityMain.this, "用户名和密码不能为空", Toast.LENGTH_LONG).show();
					return;
				}
				String savedUsername = sp.getString("username", "");
				String savedPassword = sp.getString("password", "");
				
				if(username.trim().equals(savedUsername) && password.trim().equals(savedPassword)) {
					init();
					showPasswordDialog.dismiss();
				} else {
					Toast.makeText(ActivityMain.this, "用户名或者密码错误", Toast.LENGTH_LONG).show();
					return;
				}
				
			}});
		
		btn_cancle.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
				
			}});
		
		builder.setTitle("输入密码");
		builder.setView(view);
		showPasswordDialog = builder.create();
		showPasswordDialog.show();
	}

	private void init() {
    	setContentView(R.layout.main);
    	et_safenumber = (EditText) this.findViewById(R.id.safenumber);
    	btn_start = (Button) this.findViewById(R.id.start);
    	btn_modify = (Button) this.findViewById(R.id.modify);
    	
    	String savedNumber = sp.getString("safenumber", "");
    	et_safenumber.setText(savedNumber);
    	
    	isStart = sp.getBoolean("start", false);
    	if(isStart) {
    		et_safenumber.setEnabled(false);
    		btn_modify.setEnabled(false);
    		btn_start.setText("停止防盗");
    	} 
    	btn_start.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				if(isStart) {
					Editor editor = sp.edit();
					editor.putBoolean("start", false);
					editor.commit();
					et_safenumber.setEnabled(true);
		    		btn_modify.setEnabled(true);
		    		btn_start.setText("开始防盗");
		    		isStart = false;
				} else {
					String safeNumber = et_safenumber.getText().toString();
					if(safeNumber.trim().equals("")) {
						Toast.makeText(ActivityMain.this, "安全号码不能为空,请重新设置", Toast.LENGTH_LONG).show();
						return;
					} else {
						
						String phoneNumber = tm.getLine1Number();
						//IMSI   
						String subScribeerId = tm.getSubscriberId();
						
						Editor editor = sp.edit();
						editor.putString("safenumber", safeNumber);
						isStart = true;
						editor.putBoolean("start",isStart);
						editor.putString("subscriberid", subScribeerId);
						editor.commit();
						btn_modify.setEnabled(false);
						et_safenumber.setEnabled(false);
						btn_start.setText("停止防盗");
					}
				}
			}});
    	
    	btn_modify.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				showSetPasswordDialog();
			}});
    	
    	
    }

	private void showSetPasswordDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.showpassworddialog, null);
		final EditText et_username = (EditText) view.findViewById(R.id.username);
		final EditText et_password = (EditText) view.findViewById(R.id.password);
		
		Button btn_ok = (Button) view.findViewById(R.id.ok);
		Button btn_cancle = (Button) view.findViewById(R.id.cancle);
		
		btn_ok.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String username = et_username.getText().toString();
				String password = et_password.getText().toString();
				
				if(username.trim().equals("") || password.trim().equals("")) {
					Toast.makeText(ActivityMain.this, "用户名和密码不能为空", Toast.LENGTH_LONG).show();
					return;
				}
				
				Editor editor = sp.edit();
				editor.putString("username", username);
				editor.putString("password", password);
				editor.putBoolean("first", false);
				editor.commit();
				setPasswordDialog.dismiss();
				init();
				
			}});
		
		btn_cancle.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if(isFirst) {
					finish();
				} else {
					setPasswordDialog.dismiss();
				}
				
			}});
		
		builder.setTitle("设置密码");
		builder.setView(view);
		setPasswordDialog = builder.create();
		setPasswordDialog.show();
	}
}
package com.example.aidlclient;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.aidl.IPerson;
import com.example.aidl.StringServiceAIDL;

public class MainActivity extends Activity {

	private static final String TAG = "AIDL Cilent";
	// Intent action defined in AIDLService -> AndroidManifest.xml
	private static final String SIMPLEDATA_ACTION = "com.example.aidl.StringServiceAIDL.STRINGSERVICE";
	private static final String SELFDEFDATA_ACTION = "com.example.aidl.IPerson.PERSONSERVICE";
	// two different remote service
	private IPerson personService = null;
	public StringServiceAIDL strService = null;
	
	private TextView tv_output;
	private Button bt_simpledata;
	private Button bt_userdefineddata;
	private Button bt_callback;
	private Intent intent;

	// handle the message and put it to TextView
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			tv_output.append(msg.getData().getString("msg"));
		}

	};

	public void handleMsg(String str) {
		Message msg = new Message();
		Bundle b = new Bundle();
		b.putString("msg", str + "\n");
		msg.setData(b);
		handler.sendMessage(msg);
	}

	private ServiceConnection strConn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			Log.i(TAG, "onServiceDisconnected()");
		}

		@Override
		public void onServiceConnected(ComponentName arg0, IBinder arg1) {
			Log.i(TAG, "onServiceConnected()");
			strService = StringServiceAIDL.Stub.asInterface(arg1);
			try {
				handleMsg(strService.getString());
			} catch (RemoteException e) {
				handleMsg("RemoteException onServiceConnected()");
				e.printStackTrace();
			}
		}
	};
	
	private ServiceConnection personConn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			Log.i(TAG, "onServiceDisconnected()");
		}

		@Override
		public void onServiceConnected(ComponentName arg0, IBinder arg1) {
			Log.i(TAG, "onServiceConnected()");
			personService = IPerson.Stub.asInterface(arg1);
			try {
				handleMsg("name : " + personService.getPerson().getName());
				handleMsg("age : " + personService.getPerson().getAge());
				float temp[] = personService.getPerson().getScores();
				for (int i = 0; i < temp.length; i++) {
					handleMsg("score " + (i + 1) + " : " + temp[i]);
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tv_output = (TextView) this.findViewById(R.id.tv_output);
		bt_simpledata = (Button) this.findViewById(R.id.bt_simpledata);
		bt_userdefineddata = (Button) this
				.findViewById(R.id.bt_userdefineddata);
		bt_callback = (Button) this.findViewById(R.id.bt_callback);

		bt_simpledata.setOnClickListener(new MyClickListener());
		bt_userdefineddata.setOnClickListener(new MyClickListener());
		bt_callback.setOnClickListener(new MyClickListener());

		intent = new Intent();

	}

	public void func(Intent it, ServiceConnection conn, String action) {
		it.setAction(action);
		Log.i(TAG, " ---- start service ---- ");
		startService(it);
		Log.i(TAG, " ---- bind service ---- ");
		bindService(it, conn, BIND_AUTO_CREATE);
	}

	private class MyClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.bt_simpledata:
				// clieked to get simple data from remote Thread
				// here just receive a String from Service 
				tv_output.setText("Simple data test \n------------------------\n");
				func(intent, strConn, MainActivity.SIMPLEDATA_ACTION);
				bt_simpledata.setEnabled(false);
				break;
			case R.id.bt_userdefineddata:
				// clieked to get user defined data from remote Thread
				tv_output.setText("User defined data test \n------------------------\n");
				func(intent, personConn, MainActivity.SELFDEFDATA_ACTION);
				bt_userdefineddata.setEnabled(false);
				break;
			case R.id.bt_callback:
				// clieked to execute a callback function
				tv_output.setText("Callback Test \n------------------------\n");
				bt_callback.setEnabled(false);
				break;
			}
		}

	}

}

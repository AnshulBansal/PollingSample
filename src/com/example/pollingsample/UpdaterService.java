package com.example.pollingsample;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

public class UpdaterService extends Service {

	// I am changing Something
	String master;
	HttpClient client;
	JSONObject jason;
	JSONArray jsonArray;
	String message = "zingalala";
	SharedPreferences sp;
	Editor editor;
	String local;
	boolean spinlock = false;
	boolean running = true;	
	final static String URL = "https://graph.facebook.com/340759132604566/feed?access_token=CAACTzPZAxblQBAPFxBm5T4ZBRr9nL05RnMxMXLjav4GGTGpZBMWLD9A8ZAybSFwokZAW0DbEPk1CdRXIsaNZCJxYRpg7jC1vqXwfJZC7C7Cz0cxxItV4uiFAlW5bZCF2L3uSqsBWWUHKDhbEYZAKl3C35";

	@Override
	public void onCreate() {
		client = new DefaultHttpClient();
		super.onCreate();
	}

	@Override
	public void onDestroy() {
running = false;
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		sp = getSharedPreferences("filename", Context.MODE_PRIVATE);
		Toast.makeText(getApplicationContext(), "Started", Toast.LENGTH_LONG)
				.show();
		
		int minutes = 1;
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
	    Intent i = new Intent(this, UpdaterService.class);
	    PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
	    am.cancel(pi);
	    // by my own convention, minutes <= 0 means notifications are disabled
	    if (minutes > 0) {
	        am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
	            SystemClock.elapsedRealtime() + minutes*60*1000,
	            minutes*60*1000, pi);
	    }
	    // by my own convention, minutes <= 0 means notifications are disabled
	    if (minutes > 0) {
	        am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
	            SystemClock.elapsedRealtime() + minutes*60*1000,
	            minutes*60*1000, pi);
	    }
	

		
				
					
				
					
					local = sp.getString("message0", "gggg");
					message = local;
					new read().execute("id");
				
			
				
				
				
		
	

	return 1;
}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}
	class read extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			

			try {
				
					HttpGet get = new HttpGet(URL);
					HttpResponse r;
					r = client.execute(get);
					int stautus = r.getStatusLine().getStatusCode();

					if (stautus == 200) {

						HttpEntity entity = r.getEntity();
						String data = EntityUtils.toString(entity);
						JSONObject timeline = new JSONObject(data);
						jsonArray = timeline.getJSONArray("data");
						message = jsonArray.getJSONObject(0)
								.getString("id");
						Log.d("tag", message);
					}
				
			}

			catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				message = local;
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				message = local;
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				message = local;
				e.printStackTrace();
			}
			
			
			
			return message;
		}
		

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			
			if(!message.contentEquals(local)){
				editor = sp.edit();
				try {
					editor.putString("message0", message);
					editor.putString("message1", jsonArray.getJSONObject(1).getString("id"));
					editor.putString("message2", jsonArray.getJSONObject(2).getString("id"));
					editor.putString("message3", jsonArray.getJSONObject(3).getString("id"));
					editor.putString("message4", jsonArray.getJSONObject(4).getString("id"));
					editor.putString("message5", jsonArray.getJSONObject(5).getString("id"));
				editor.commit();
				NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
				Intent intent1 = new Intent(UpdaterService.this,MainActivity.class);
				PendingIntent pi = PendingIntent.getActivity(UpdaterService.this, 0, intent1, 0);
				String title = "All hail king of the losers";
				Notification notification = new Notification(R.drawable.ic_launcher,title,System.currentTimeMillis());
				notification.setLatestEventInfo(getApplicationContext(), title, message, pi);
				notification.defaults = Notification.DEFAULT_ALL;
				nm.notify(5688675, notification);
			
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG)
						.show();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				
				NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
				Intent intent1 = new Intent(UpdaterService.this,MainActivity.class);
				PendingIntent pi = PendingIntent.getActivity(UpdaterService.this, 0, intent1, 0);
				String title = "Else Block";
				Notification notification = new Notification(R.drawable.ic_launcher,title,System.currentTimeMillis());
				notification.setLatestEventInfo(getApplicationContext(), title, message, pi);
				notification.defaults = Notification.DEFAULT_ALL;
				nm.notify(5688679, notification);
				
				
			}
			
			
			
			
			stopSelf();
			
			
			
			
			
			
			
		
	
			
			
	}
	}
}

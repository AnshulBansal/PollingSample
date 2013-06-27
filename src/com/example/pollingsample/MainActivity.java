package com.example.pollingsample;

import java.io.IOException;
import java.util.ArrayList;

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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class MainActivity extends Activity {

	TextView tvhttp;
	HttpClient client;
	JSONObject jason;
	JSONArray jsonArray;
//	ListView lv;
	PullToRefreshListView lv;
	ListAdapter adapter ;
	SharedPreferences sp;
	ArrayList<String> list = new ArrayList<String>();
	final static String URL = "https://graph.facebook.com/340759132604566/feed?access_token=CAACTzPZAxblQBAPFxBm5T4ZBRr9nL05RnMxMXLjav4GGTGpZBMWLD9A8ZAybSFwokZAW0DbEPk1CdRXIsaNZCJxYRpg7jC1vqXwfJZC7C7Cz0cxxItV4uiFAlW5bZCF2L3uSqsBWWUHKDhbEYZAKl3C35";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final PullToRefreshListView lv = (PullToRefreshListView) findViewById(R.id.pull_to_refresh_listview);
		lv.setOnRefreshListener(new OnRefreshListener<ListView>() {
		    @Override
		    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		    	  sp = getSharedPreferences("filename", Context.MODE_PRIVATE);
		    	  list.clear();
		          list.add(sp.getString("message1", "1"));
		          list.add(sp.getString("message2", "2"));
		          list.add(sp.getString("message3", "3"));
		          list.add(sp.getString("message4", "4"));
		          list.add(sp.getString("message5", "5"));
		          adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,list);
		          lv.setAdapter(adapter);
		          Toast.makeText(getApplicationContext(), "List has been updated", Toast.LENGTH_LONG)
					.show();
		          lv.onRefreshComplete();
		    }
		});

		tvhttp = (TextView) findViewById(R.id.http);
      //  lv = (ListView) findViewById(R.id.lvids);
        sp = getSharedPreferences("filename", Context.MODE_PRIVATE);
        list.add(sp.getString("message1", "1"));
        list.add(sp.getString("message2", "2"));
        list.add(sp.getString("message3", "3"));
        list.add(sp.getString("message4", "4"));
        list.add(sp.getString("message5", "5"));
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list);
        lv.setAdapter(adapter);
		client = new DefaultHttpClient();
		read r = new read();
		r.execute("id");

	}

	public JSONObject lasttweet(String username)
			throws ClientProtocolException, IOException, JSONException {


		HttpGet get = new HttpGet(URL);

		HttpResponse r = client.execute(get);
		int stautus = r.getStatusLine().getStatusCode();

		if (stautus == 200) {

			HttpEntity entity = r.getEntity();
			String data = EntityUtils.toString(entity);
			JSONObject timeline = new JSONObject(data);
			jsonArray = timeline.getJSONArray("data");

			return jsonArray.getJSONObject(0);

		} else {

			Toast t = new Toast(MainActivity.this);
			t.setText("Failed");
			t.setDuration(Toast.LENGTH_LONG);
			return null;
		}
	}



@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
	getMenuInflater().inflate(R.menu.newmenu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this,UpdaterService.class);
		
		switch(item.getItemId()){
		
		case R.id.b_start_service:
			stopService(intent);
			startService(intent);
			
			break;
		case R.id.b_stop_service:
			stopService(intent);
			break;
		
		
		
		}
		return super.onOptionsItemSelected(item);
	}



class read extends AsyncTask<String, Integer, String> {

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		try {
			jason = lasttweet("mybringback");
		
		
		return jason.getString(params[0]);
		
		
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		tvhttp.setText(result);
	}

}
}

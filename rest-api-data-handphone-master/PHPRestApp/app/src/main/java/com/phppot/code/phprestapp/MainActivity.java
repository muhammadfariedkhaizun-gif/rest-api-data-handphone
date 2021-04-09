package com.phppot.code.phprestapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private String apiPath = "http://192.168.1.103/phpsamples/php-mysql-rest-api-for-android/mobile/list/";
    private ProgressDialog processDialog;
    private JSONArray restulJsonArray;
    private int success = 0;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.mobile_name_list);
        new ServiceStubAsyncTask(this, this).execute();
    }

    private class ServiceStubAsyncTask extends AsyncTask<Void, Void, Void> {

        private Context mContext;
        private Activity mActivity;
        String response = "";
        HashMap<String, String> postDataParams;

        public ServiceStubAsyncTask(Context context, Activity activity) {
            mContext = context;
            mActivity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            processDialog = new ProgressDialog(mContext);
            processDialog.setMessage("Please  Wait ...");
            processDialog.setCancelable(false);
            processDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            postDataParams = new HashMap<String, String>();
            postDataParams.put("HTTP_ACCEPT", "application/json");

            HttpConnectionService service = new HttpConnectionService();
            response = service.sendRequest(apiPath, postDataParams);
            try {
                success = 1;
                JSONObject resultJsonObject = new JSONObject(response);
                restulJsonArray = resultJsonObject.getJSONArray("output");
            } catch (JSONException e) {
                success = 0;
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            if (processDialog.isShowing()) {
                processDialog.dismiss();
            }

            if (success == 1) {
                if (null != restulJsonArray) {
                    ArrayAdapter listViewAdapter = new ArrayAdapter<String>(mContext, R.layout.mobile_name_listview);

                    for (int i = 0; i < restulJsonArray.length(); i++) {
                        try {
                            JSONObject jsonObject = restulJsonArray.getJSONObject(i);
                            listViewAdapter.add(jsonObject.get("name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    listView.setAdapter(listViewAdapter);
                }
            }
        }

    }//end of async task
}

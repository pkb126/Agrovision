package com.praveenbhati.agrovision;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.praveenbhati.agrovision.model.Lead;
import com.praveenbhati.agrovision.adapter.LeadAdapter;
import com.praveenbhati.agrovision.database.AndroidDatabaseManager;
import com.praveenbhati.agrovision.database.Database;
import com.praveenbhati.agrovision.service.AgroServer;
import com.praveenbhati.agrovision.util.ConnectionDetector;
import com.praveenbhati.agrovision.util.PrefManager;
import com.praveenbhati.agrovision.util.ServiceHandler;
import com.praveenbhati.agrovision.util.UtilClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    PrefManager prefManager;
    LeadAdapter leadAdapter;
    ListView mLvLeads;
    TextView mTvNoData;
    ArrayList<Lead> leadArrayList = new ArrayList<>();
    ConnectionDetector connectionDetector;
    ProgressDialog progressDialog;
    Database database;
    public static Intent serviceIntent;
    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefManager = new PrefManager(this);
        progressDialog = new ProgressDialog(MainActivity.this);
        connectionDetector = new ConnectionDetector(getApplicationContext());
        database = new Database(getApplicationContext());
        initView();
        /*Lead lead = new Lead();
        lead.setPersonName("Praveen Bhati");
        lead.setMobileNumber("8446115291");
        lead.setProductName("POLY HOUSE");
        lead.setSubProductName("FLAT TYPE(6*6 SPAN 5 MTR HEIGHT)");
        lead.setStatus("New");
        lead.setContactDate("2015-10-12");
        lead.setTotalAmount(15000);

        Lead lead1 = new Lead();
        lead1.setPersonName("Shakti Singh");
        lead1.setMobileNumber("8983547228");
        lead1.setProductName("SHAD NET");
        lead1.setSubProductName("FLAT TYPE(6*6 SPAN HEIGHT)");
        lead1.setStatus("Qualified");
        lead1.setContactDate("2015-10-12");
        lead1.setTotalAmount(50000);

        leadArrayList.add(lead);
        leadArrayList.add(lead1);
        leadAdapter = new LeadAdapter(getApplicationContext(),leadArrayList);
        mLvLeads.setAdapter(leadAdapter);*/

        if (connectionDetector.isConnectingToInternet()){
            new GetLeadAsync().execute(prefManager.getUserID());
        }else {
            Toast.makeText(MainActivity.this, "Please connect to Internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void initView() {
        mLvLeads = (ListView) findViewById(R.id.activity_main_lv_lead);
        mTvNoData = (TextView) findViewById(R.id.activity_main_tv_nodata);
        mLvLeads.setEmptyView(mTvNoData);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (!isMyServiceRunning()){
            serviceIntent = new Intent(getApplicationContext(), AgroServer.class);
            startService(serviceIntent);
        }
    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if ("com.praveenbhati.agrovision.service.AgroServer".equals(service.service
                    .getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()){
            case R.id.action_addlead:
                Intent intentAddLead = new Intent(MainActivity.this,AddLeadActivity.class);
                startActivity(intentAddLead);
                break;

            case R.id.action_logout:
                prefManager.clearSession();
                database.deleteProduct();
                Intent intentLogin = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intentLogin);
                finish();
                break;

            case R.id.action_appoint:
                Intent intentAppoint = new Intent(MainActivity.this,AppointmentActivity.class);
                startActivity(intentAppoint);
                break;

            /*case R.id.action_database:
                Intent intentDatabase = new Intent(MainActivity.this, AndroidDatabaseManager.class);
                startActivity(intentDatabase);
                break;*/

            case R.id.action_expense:
                Intent intentExpense = new Intent(MainActivity.this,AddExpenseActivity.class);
                startActivity(intentExpense);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private class GetLeadAsync extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("getting lead from agrovision");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                ServiceHandler handler = new ServiceHandler();
                String jsonResult = handler.makeServiceCall(UtilClass.GET_LEAD_URL+prefManager.getUserID(),ServiceHandler.POST);
                Log.i(TAG, jsonResult);
                return jsonResult;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();


            if (result !=null){
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i =0 ;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Lead lead = new Lead();

                        lead.setFirstName(jsonObject.getString("firstName"));
                        lead.setMiddleName(jsonObject.getString("middleName"));
                        lead.setLastName(jsonObject.getString("lastName"));
                        lead.setMobileNumber(jsonObject.getString("mobileNumber"));
                        lead.setProductName(jsonObject.getString("intrestType"));
                        lead.setSubProductName(jsonObject.getString("intrestIn"));
                        lead.setContactDate(jsonObject.getString("contactDate"));
                        lead.setStatus(jsonObject.getString("finalStatus"));
                        lead.setTotalAmount(jsonObject.getDouble("totalAmount"));
                        lead.setLeadId(jsonObject.getInt("leadId"));
                        lead.setLeadStatusId(jsonObject.getInt("LeadStatusId"));

                        leadArrayList.add(lead);
                    }

                    leadAdapter = new LeadAdapter(getApplicationContext(),leadArrayList);
                    mLvLeads.setAdapter(leadAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(MainActivity.this, "Server Error, Please try again.", Toast.LENGTH_SHORT).show();
            }

        }
    }
}

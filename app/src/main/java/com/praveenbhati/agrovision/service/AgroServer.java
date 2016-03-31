package com.praveenbhati.agrovision.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.praveenbhati.agrovision.database.Database;
import com.praveenbhati.agrovision.model.Lead;
import com.praveenbhati.agrovision.util.ConnectionDetector;
import com.praveenbhati.agrovision.util.PrefManager;
import com.praveenbhati.agrovision.util.ServiceHandler;
import com.praveenbhati.agrovision.util.UtilClass;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This service class is user sending offline data to server.
 * @author Bhati
 */
public class AgroServer extends Service {
    Updater updater;
    ConnectionDetector connectionDetector;
    PrefManager prefManager;
    Database database;

    public AgroServer() {
    }

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        updater = new Updater();
        connectionDetector = new ConnectionDetector(getApplicationContext());
        prefManager = new PrefManager(getApplicationContext());
        database = new Database(getApplicationContext());
    }

    @Override
    public synchronized int onStartCommand(Intent intent, int flags, int startId) {
        if (!updater.isRunning()){
            updater.start();
            updater.isRunning = true;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public synchronized void onDestroy() {
        super.onDestroy();
        if (updater.isRunning){
            updater.interrupt();
            updater.isRunning = false;
            updater = null;
        }
    }

    class Updater extends Thread{
        public boolean isRunning = false;
        public long DELAY = 10000;

        @Override
        public void run() {
            super.run();
            while (isRunning) {
                Log.d("service", "Running...");
                if (connectionDetector.isConnectingToInternet() && prefManager.getLogin()){
                    if (database.getLeadCount() >0){
                        sendLeadToServer();
                    }
                }
                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    isRunning = false;
                }
            }
        }

        public boolean isRunning() {
            return this.isRunning;
        }
    }

    private void sendLeadToServer() {
        try {
            new AddLeadAsync().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class AddLeadAsync extends AsyncTask<String,String,String> {
        Lead lead = database.getLead(Integer.parseInt(prefManager.getUserID()));
        @Override
        protected String doInBackground(String... params) {
            try {
                ServiceHandler handler = new ServiceHandler();

                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
                nameValuePair.add(new BasicNameValuePair("userId",String.valueOf(lead.getUserId())));
                nameValuePair.add(new BasicNameValuePair("firstName",lead.getFirstName()));
                nameValuePair.add(new BasicNameValuePair("middleName",lead.getMiddleName()));
                nameValuePair.add(new BasicNameValuePair("lastName",lead.getLastName()));
                nameValuePair.add(new BasicNameValuePair("companyName",lead.getCompanyName()));
                nameValuePair.add(new BasicNameValuePair("mobileNo",lead.getMobileNumber()));
                nameValuePair.add(new BasicNameValuePair("phoneNo",lead.getPhoneNumber()));
                nameValuePair.add(new BasicNameValuePair("email",lead.getEmail()));
                nameValuePair.add(new BasicNameValuePair("address",lead.getAddressDetail()));
                nameValuePair.add(new BasicNameValuePair("country",String.valueOf(lead.getCountryId())));
                nameValuePair.add(new BasicNameValuePair("state",String.valueOf(lead.getStateId())));
                nameValuePair.add(new BasicNameValuePair("district",String.valueOf(lead.getDistrictId())));
                nameValuePair.add(new BasicNameValuePair("taluka",String.valueOf(lead.getTalukaId())));
                nameValuePair.add(new BasicNameValuePair("village",String.valueOf(lead.getVillageId())));
                nameValuePair.add(new BasicNameValuePair("intIn",String.valueOf(lead.getSubProductId())));
                nameValuePair.add(new BasicNameValuePair("intType",String.valueOf(lead.getProductId())));
                nameValuePair.add(new BasicNameValuePair("appDate",lead.getContactDate()));
                nameValuePair.add(new BasicNameValuePair("contactTime",lead.getContactTime()));
                nameValuePair.add(new BasicNameValuePair("requrment",lead.getCustReq()));
                nameValuePair.add(new BasicNameValuePair("productRate",String.valueOf(lead.getRate())));
                nameValuePair.add(new BasicNameValuePair("Quantity",String.valueOf(lead.getQuantity())));
                nameValuePair.add(new BasicNameValuePair("total",String.valueOf(lead.getTotalAmount())));
                nameValuePair.add(new BasicNameValuePair("category",lead.getCategory()));
                nameValuePair.add(new BasicNameValuePair("area",lead.getLandArea()));
                nameValuePair.add(new BasicNameValuePair("customerResponse",lead.getCustomerResponse()));
                nameValuePair.add(new BasicNameValuePair("requirements",lead.getRequirement()));
                nameValuePair.add(new BasicNameValuePair("responsePrice",lead.getResponse()));
                nameValuePair.add(new BasicNameValuePair("quotationCopy",lead.getQuotHardCopy()));
                nameValuePair.add(new BasicNameValuePair("bankrelation",lead.getBankRelation()));
                nameValuePair.add(new BasicNameValuePair("bankName",lead.getBankName()));
                nameValuePair.add(new BasicNameValuePair("branchName",lead.getBranchName()));
                nameValuePair.add(new BasicNameValuePair("startWorkDate",lead.getStartWork()));
                nameValuePair.add(new BasicNameValuePair("closingLeadDate",lead.getCloseLead()));
                nameValuePair.add(new BasicNameValuePair("schemeType",lead.getSchemeType()));
                nameValuePair.add(new BasicNameValuePair("schemeStatus",lead.getSchemeStatus()));


                Log.i("SendInfo",nameValuePair.toString());

                String jsonResult = handler.makeServiceCall(UtilClass.ADD_LEAD_URL,ServiceHandler.POST,nameValuePair);

                Log.i("INSERVICE",jsonResult);
                return jsonResult;
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {

            if (result !=null){
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String auth = jsonObject.getString("auth");
                    if (auth.equals("Success")){
                        Toast.makeText(getApplicationContext(), "Lead added successfully", Toast.LENGTH_SHORT).show();
                        database.deleteLead(lead.getLeadId());
                    }else if(auth.equals("Error")) {
                        Toast.makeText(getApplicationContext(),"Error in adding lead", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(getApplicationContext(), "Server Error, Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

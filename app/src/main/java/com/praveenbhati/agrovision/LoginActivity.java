package com.praveenbhati.agrovision;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.praveenbhati.agrovision.model.Product;
import com.praveenbhati.agrovision.model.ProductType;
import com.praveenbhati.agrovision.database.Database;
import com.praveenbhati.agrovision.util.ConnectionDetector;
import com.praveenbhati.agrovision.util.PrefManager;
import com.praveenbhati.agrovision.util.ServiceHandler;
import com.praveenbhati.agrovision.util.UtilClass;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    EditText mEtUserName,mEtPassword;
    Button mBtnLogin;
    ProgressDialog progressDialog;
    ConnectionDetector connectionDetector;
    PrefManager prefManager;
    Database database;
    public static final String TAG = LoginActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        progressDialog = new ProgressDialog(LoginActivity.this);
        connectionDetector = new ConnectionDetector(getApplicationContext());
        prefManager = new PrefManager(this);
        database = new Database(getApplicationContext());
    }

    private void initView() {
        mEtUserName = (EditText) findViewById(R.id.activity_login_et_username);
        mEtPassword = (EditText) findViewById(R.id.activity_login_et_password);
        mBtnLogin = (Button) findViewById(R.id.activity_login_btn_login);
        mBtnLogin.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_login_btn_login:
                userLogin();
                break;
        }
    }

    private void userLogin() {
        String username = mEtUserName.getText().toString().trim();
        String password = mEtPassword.getText().toString().trim();

        if (username.isEmpty()){
            Toast.makeText(LoginActivity.this, "Please Enter your username", Toast.LENGTH_SHORT).show();
            mEtUserName.requestFocus();
        }else if (password.isEmpty()){
            Toast.makeText(LoginActivity.this, "Please Enter your password", Toast.LENGTH_SHORT).show();
            mEtPassword.requestFocus();
        }else {
            if (connectionDetector.isConnectingToInternet()){
                new LoginAsync().execute(username, password);
                /*if (username.equals("pkb") && password.equals("pkb")){
                    prefManager.setLogin();
                    Product product = new Product();
                    product.setProductID(1);
                    product.setProductName("SHADENET HOUSE(UNIT= SQ.MTR)");

                    Product product1 = new Product();
                    product1.setProductID(2);
                    product1.setProductName("POLY HOUSE  (UNIT= SQ.MTR)");

                    database.addProduct(product);
                    database.addProduct(product1);

                    ProductType productType =  new ProductType();
                    productType.setProductTypeId(1);
                    productType.setProductId(1);
                    productType.setProductTypeName("FLAT TYPE (6X4 SPAN 4 MTR HEIGHT)");
                    productType.setProductRate(12000);


                    ProductType productType1 =  new ProductType();
                    productType1.setProductTypeId(2);
                    productType1.setProductId(1);
                    productType1.setProductTypeName("FLAT TYPE (6X6 SPAN 4 MTR HEIGHT)");
                    productType1.setProductRate(15000);


                    ProductType productType2 =  new ProductType();
                    productType2.setProductTypeId(3);
                    productType2.setProductId(2);
                    productType2.setProductTypeName("NATURALLY VENTILATED POLY HOUSE");
                    productType2.setProductRate(25000);



                    ProductType productType3 =  new ProductType();
                    productType3.setProductTypeId(4);
                    productType3.setProductId(2);
                    productType3.setProductTypeName("FAN PAD POLY HOUSE");
                    productType3.setProductRate(30000);

                    database.addProductType(productType);
                    database.addProductType(productType1);
                    database.addProductType(productType2);
                    database.addProductType(productType3);

                    Intent intentDashboard = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intentDashboard);
                    finish();
                }else {
                    Toast.makeText(LoginActivity.this, "Wrong username and password", Toast.LENGTH_SHORT).show();
                }*/
            }else {
                Toast.makeText(LoginActivity.this, "Please connect to Internet", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class LoginAsync extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Connecting to agrovision");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                ServiceHandler handler = new ServiceHandler();

                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
                nameValuePair.add(new BasicNameValuePair("username",params[0] ));
                nameValuePair.add(new BasicNameValuePair("password", params[1]));

                String jsonResult = handler.makeServiceCall(UtilClass.LOGIN_URL,ServiceHandler.POST,nameValuePair);

                Log.i(TAG,jsonResult);
                return jsonResult;
            }catch (Exception e) {
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
                    JSONObject jsonObject = new JSONObject(result);
                    String auth = jsonObject.getString("auth");

                    if (auth.equals("Success")){
                        //prefManager.setLogin("True");
                        prefManager.setLogin();
                        prefManager.setUserID(jsonObject.getString("userId"));
                        prefManager.setUserName(jsonObject.getString("name"));

                        JSONArray  jsonArray = jsonObject.getJSONArray("productsArray");
                        for (int i= 0; i<jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            Product product = new Product();
                            product.setProductID(object.getInt("productId"));
                            product.setProductName(object.getString("productName"));

                            database.addProduct(product);
                        }

                        JSONArray  jsonArrayType = jsonObject.getJSONArray("subProductArray");
                        for (int i= 0; i<jsonArrayType.length();i++){
                            JSONObject object = jsonArrayType.getJSONObject(i);
                            ProductType productType = new ProductType();

                            productType.setProductTypeId(object.getInt("subProductId"));
                            productType.setProductTypeName(object.getString("subProductName"));
                            productType.setProductId(object.getInt("productId"));
                            productType.setProductRate(object.getDouble("subProductRate"));

                            database.addProductType(productType);
                        }

                        JSONArray jsonArrayCountry = jsonObject.getJSONArray("countryArray");
                        for (int i=0;i<jsonArrayCountry.length();i++){
                            JSONObject object = jsonArrayCountry.getJSONObject(i);
                            database.addCountry(object.getInt("countryId"), object.getString("countryName"));
                        }


                        JSONArray jsonArrayState = jsonObject.getJSONArray("stateArray");
                        for (int i=0;i<jsonArrayState.length();i++){
                            JSONObject object = jsonArrayState.getJSONObject(i);
                            database.addState(object.getInt("stateId"), object.getString("stateName"), object.getInt("countryId"));
                        }


                        JSONArray jsonArrayDistrict = jsonObject.getJSONArray("districtArray");
                        for (int i=0;i<jsonArrayDistrict.length();i++){
                            JSONObject object = jsonArrayDistrict.getJSONObject(i);
                            database.addDistrict(object.getInt("districtId"), object.getString("districtName"), object.getInt("stateId"));
                        }


                        JSONArray jsonArrayTaluka = jsonObject.getJSONArray("talukaArray");
                        for (int i=0;i<jsonArrayTaluka.length();i++){
                            JSONObject object = jsonArrayTaluka.getJSONObject(i);
                            database.addTaluka(object.getInt("talukaId"), object.getString("talukaName"), object.getInt("districtId"));
                        }


                        JSONArray jsonArrayVillage = jsonObject.getJSONArray("villageArray");
                        for (int i=0;i<jsonArrayVillage.length();i++){
                            JSONObject object = jsonArrayVillage.getJSONObject(i);
                            database.addVillage(object.getInt("villageId"),object.getString("villageName"),object.getInt("talukaId"));
                        }

                        Intent intentDashboard = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intentDashboard);
                        finish();
                    }else if(auth.equals("Error")) {
                        Toast.makeText(LoginActivity.this,"Wrong Username and Password", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(LoginActivity.this, "Server Error, Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

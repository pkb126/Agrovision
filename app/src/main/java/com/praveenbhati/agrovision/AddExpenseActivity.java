package com.praveenbhati.agrovision;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.praveenbhati.agrovision.util.ConnectionDetector;
import com.praveenbhati.agrovision.util.PrefManager;
import com.praveenbhati.agrovision.util.ServiceHandler;
import com.praveenbhati.agrovision.util.UtilClass;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddExpenseActivity extends AppCompatActivity implements View.OnClickListener {
    EditText mEtExpenseDate, mEtExpenseDes, mEtFrom, mEtViaOne, mEtViaTwo, mEtViaThree,
            mEtTo, mEtStartKM, mEtEndKM, mEtTotalKM, mEtTravelTotal, mEtAllowDesc, mEtAllowBill,
            mEtAllowTotal, mEtPandTDesc, mEtPandTBill, mEtPandTTotal, mEtLodgingDesc, mEtLodgingBill,
            mEtLodgingTotal, mEtTransDesc, mEtTransBill, mEtTransTotal, mEtDayTotal;
    Button mBtnAddExpense;
    String expenseDate, expenseDes, from, viaOne, viaTwo, viaThree, to, allowDesc, allowBill,
            pandtDesc, pandtBill, lodgingDesc, lodgingBill, transDesc, transBill;
    String startKM, endKM, totalKM;
    String travelTotal, allowTotal, pandtTotal, lodgingTotal, transTotal, dayTotal;
    DatePickerDialog expanseDatePickerDialog;
    private SimpleDateFormat dateFormat;
    public static final String TAG = AddExpenseActivity.class.getSimpleName();
    ProgressDialog progressDialog;
    ConnectionDetector connectionDetector;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        initView();
        progressDialog = new ProgressDialog(AddExpenseActivity.this);
        connectionDetector = new ConnectionDetector(getApplicationContext());
        prefManager = new PrefManager(getApplicationContext());
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        final Calendar calendar = Calendar.getInstance();

        expanseDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mEtExpenseDate.setText(dateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        mEtEndKM.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (!mEtStartKM.getText().toString().trim().isEmpty() && !mEtEndKM.getText().toString().trim().isEmpty()){
                        mEtTotalKM.setText(String.valueOf(Integer.parseInt(mEtEndKM.getText().toString().trim())
                                -Integer.parseInt(mEtStartKM.getText().toString().trim())));
                    }else {
                        Toast.makeText(AddExpenseActivity.this, "Please enter start Km and end Km", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        mEtTotalKM.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (!mEtTotalKM.getText().toString().trim().isEmpty()){
                        mEtTravelTotal.setText(String.valueOf(Integer.parseInt(mEtTotalKM.getText().toString().trim()) * 1.5 ));
                    }

                }
            }
        });

        mEtTransTotal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    mEtDayTotal.setText(String.valueOf(
                            Double.parseDouble(mEtTravelTotal.getText().toString().trim()) +
                                    Double.parseDouble(mEtLodgingTotal.getText().toString().trim()) +
                                    Double.parseDouble(mEtAllowTotal.getText().toString().trim()) +
                                    Double.parseDouble(mEtPandTTotal.getText().toString().trim()) +
                                    Double.parseDouble(mEtTransTotal.getText().toString().trim())
                    ));
                }
            }
        });

    }

    private void initView() {
        mEtExpenseDate = (EditText) findViewById(R.id.activity_add_expanse_et_date);
        mEtExpenseDes = (EditText) findViewById(R.id.activity_add_expanse_et_description);
        mEtFrom = (EditText) findViewById(R.id.activity_add_expanse_et_from);
        mEtViaOne = (EditText) findViewById(R.id.activity_add_expanse_et_via1);
        mEtViaTwo = (EditText) findViewById(R.id.activity_add_expanse_et_via2);
        mEtViaThree = (EditText) findViewById(R.id.activity_add_expanse_et_via3);
        mEtTo = (EditText) findViewById(R.id.activity_add_expanse_et_to);
        mEtStartKM = (EditText) findViewById(R.id.activity_add_expanse_et_startkm);
        mEtEndKM = (EditText) findViewById(R.id.activity_add_expanse_et_endkm);
        mEtTotalKM = (EditText) findViewById(R.id.activity_add_expanse_et_totalkm);
        mEtTravelTotal = (EditText) findViewById(R.id.activity_add_expanse_et_travel_total);
        mEtAllowDesc = (EditText) findViewById(R.id.activity_add_expanse_et_allow_particular);
        mEtAllowBill = (EditText) findViewById(R.id.activity_add_expanse_et_allow_bill_no);
        mEtAllowTotal = (EditText) findViewById(R.id.activity_add_expanse_et_allow_total);
        mEtLodgingDesc = (EditText) findViewById(R.id.activity_add_expanse_et_lodging_particular);
        mEtLodgingBill = (EditText) findViewById(R.id.activity_add_expanse_et_lodging_bill_no);
        mEtLodgingTotal = (EditText) findViewById(R.id.activity_add_expanse_et_lodging_total);
        mEtPandTDesc = (EditText) findViewById(R.id.activity_add_expanse_et_pandt_particular);
        mEtPandTBill = (EditText) findViewById(R.id.activity_add_expanse_et_pandt_bill_no);
        mEtPandTTotal = (EditText) findViewById(R.id.activity_add_expanse_et_pandt_total);
        mEtTransDesc = (EditText) findViewById(R.id.activity_add_expanse_et_trans_particular);
        mEtTransBill = (EditText) findViewById(R.id.activity_add_expanse_et_trans_bill_no);
        mEtTransTotal = (EditText) findViewById(R.id.activity_add_expanse_et_trans_total);
        mEtDayTotal = (EditText) findViewById(R.id.activity_add_expanse_et_total);
        mBtnAddExpense = (Button) findViewById(R.id.activity_add_expanse_btn_add);

        mBtnAddExpense.setOnClickListener(this);
        mEtExpenseDate.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_add_expanse_btn_add:
                addExpanse();
                break;
            case R.id.activity_add_expanse_et_date:
                expanseDatePickerDialog.show();
                break;

        }
    }

    private void addExpanse() {
        expenseDate = mEtExpenseDate.getText().toString().trim();
        expenseDes = mEtExpenseDes.getText().toString().trim();
        from = mEtFrom.getText().toString().trim();
        viaOne = mEtViaOne.getText().toString().trim();
        viaTwo = mEtViaTwo.getText().toString().trim();
        viaThree = mEtViaThree.getText().toString().trim();
        to = mEtTo.getText().toString().trim();
        startKM = mEtStartKM.getText().toString().trim();
        endKM = mEtEndKM.getText().toString().trim();
        totalKM = mEtTotalKM.getText().toString().trim();
        travelTotal = mEtTravelTotal.getText().toString().trim();
        allowDesc = mEtAllowDesc.getText().toString().trim();
        allowBill = mEtAllowBill.getText().toString().trim();
        allowTotal = mEtAllowTotal.getText().toString().trim();
        pandtDesc = mEtPandTDesc.getText().toString().trim();
        pandtBill = mEtPandTBill.getText().toString().trim();
        pandtTotal = mEtPandTTotal.getText().toString().trim();
        lodgingDesc = mEtLodgingDesc.getText().toString().trim();
        lodgingBill = mEtLodgingBill.getText().toString().trim();
        lodgingTotal = mEtLodgingTotal.getText().toString().trim();
        transDesc = mEtTransDesc.getText().toString().trim();
        transBill = mEtTransBill.getText().toString().trim();
        transTotal = mEtTransTotal.getText().toString().trim();
        dayTotal = mEtDayTotal.getText().toString().trim();

        try {
            if (connectionDetector.isConnectingToInternet()){
                new AddExpenseAsync().execute();
            }else{
                Toast.makeText(AddExpenseActivity.this, "Please connect to Internet", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class AddExpenseAsync extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Sending expense to agrovision");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                ServiceHandler serviceHandler = new ServiceHandler();
                List<NameValuePair> nameValuePairList = new ArrayList<>();
                nameValuePairList.add(new BasicNameValuePair("userId",prefManager.getUserID()));
                nameValuePairList.add(new BasicNameValuePair("expenseDate",expenseDate));
                nameValuePairList.add(new BasicNameValuePair("expenseDescription",expenseDes));
                nameValuePairList.add(new BasicNameValuePair("routeForm",from));
                nameValuePairList.add(new BasicNameValuePair("routevia1",viaOne));
                nameValuePairList.add(new BasicNameValuePair("routevia2",viaTwo));
                nameValuePairList.add(new BasicNameValuePair("routevia3",viaThree));
                nameValuePairList.add(new BasicNameValuePair("routeViaTo",to));
                nameValuePairList.add(new BasicNameValuePair("statingkmTravel",startKM));
                nameValuePairList.add(new BasicNameValuePair("endKmTravel",endKM));
                nameValuePairList.add(new BasicNameValuePair("totalKmTravel",totalKM));
                nameValuePairList.add(new BasicNameValuePair("toatlExpTravel",travelTotal));
                nameValuePairList.add(new BasicNameValuePair("particularLogging",lodgingDesc));
                nameValuePairList.add(new BasicNameValuePair("billNoLogging",lodgingBill));
                nameValuePairList.add(new BasicNameValuePair("toatalExpLogging",lodgingTotal));
                nameValuePairList.add(new BasicNameValuePair("particularDayAllowans",allowDesc));
                nameValuePairList.add(new BasicNameValuePair("billNoAllowans",allowBill));
                nameValuePairList.add(new BasicNameValuePair("toatlExpTravel",allowTotal));
                nameValuePairList.add(new BasicNameValuePair("particularPrinting",pandtDesc));
                nameValuePairList.add(new BasicNameValuePair("billNoPrinting",pandtBill));
                nameValuePairList.add(new BasicNameValuePair("toatalExpPrinting",pandtTotal));
                nameValuePairList.add(new BasicNameValuePair("particularTransport",transDesc));
                nameValuePairList.add(new BasicNameValuePair("billNoTransport",transBill));
                nameValuePairList.add(new BasicNameValuePair("toatalExpTransport",transTotal));
                nameValuePairList.add(new BasicNameValuePair("totalDayExpenses",dayTotal));

                Log.i(TAG, "Sent Data: " + nameValuePairList.toString());
                String jsonResult = serviceHandler.makeServiceCall(UtilClass.ADD_EXPENSE_URL, ServiceHandler.POST, nameValuePairList);
                Log.i(TAG, "Expense Result: " + jsonResult);
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

            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    boolean success = jsonObject.getBoolean("success");
                    if (success){
                        Toast.makeText(getApplicationContext(), "Expense added successfully", Toast.LENGTH_SHORT).show();
                        finish();

                    }else if(!success) {
                        Toast.makeText(getApplicationContext(),"Error in sending expense", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Server Error, Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

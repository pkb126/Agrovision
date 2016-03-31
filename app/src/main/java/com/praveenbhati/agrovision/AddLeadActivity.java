package com.praveenbhati.agrovision;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.praveenbhati.agrovision.model.Country;
import com.praveenbhati.agrovision.model.District;
import com.praveenbhati.agrovision.model.Lead;
import com.praveenbhati.agrovision.model.Product;
import com.praveenbhati.agrovision.model.ProductType;
import com.praveenbhati.agrovision.model.State;
import com.praveenbhati.agrovision.model.Taluka;
import com.praveenbhati.agrovision.model.Village;
import com.praveenbhati.agrovision.adapter.CountryAdapter;
import com.praveenbhati.agrovision.adapter.DistrictAdapter;
import com.praveenbhati.agrovision.adapter.InterestSpinnerAdapter;
import com.praveenbhati.agrovision.adapter.InterestTypeSpinnerAdapter;
import com.praveenbhati.agrovision.adapter.StateAdapter;
import com.praveenbhati.agrovision.adapter.TalukaAdapter;
import com.praveenbhati.agrovision.adapter.VillageAdapter;
import com.praveenbhati.agrovision.database.Database;
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

public class AddLeadActivity extends AppCompatActivity implements View.OnClickListener{

    Button mBtnAddLead;
    EditText mEtFirstName,mEtMiddleName,mEtLastName,mEtCompanyName,mEtMobNumber,mEtPhoneNumber,mEtEmail,mEtAddress,
            mEtRate,mEtQuantity,mEtTotalAmount,mEtCustReq,mEtLandArea,mEtBankName,mEtBranchName,
            mEtDate,mEtStartWork,mEtCloseLead;
    static EditText mEtTime;
    Spinner mSpCategory,mSpCountry,mSpState,mSpDistrict,mSpTaluka,mSpVillage,mSpInterestType,mSpInterestIn,
            mSpCustomerResponse,mSpRequirement,mSpResponsePricing,mSpQuatationHardCopy,mSpBankRelation,
            mSpSchemeType,mSpSchemeStatus;

    Database database;
    InterestSpinnerAdapter interestSpinnerAdapter;
    InterestTypeSpinnerAdapter interestTypeSpinnerAdapter;
    CountryAdapter countryAdapter;
    StateAdapter stateAdapter;
    DistrictAdapter districtAdapter;
    TalukaAdapter talukaAdapter;
    VillageAdapter villageAdapter;

    ProgressDialog progressDialog;
    ConnectionDetector connectionDetector;
    PrefManager prefManager;
    private static final String TAG = AddLeadActivity.class.getSimpleName();
    String firstName,middleName,lastName,companyName,mobileNumber,phoneNumber,email,address,date,time,custReq,
            category,landArea,customerResponse,requirement,response,quotHardCopy,bankRelation,bankName,
            branchName,startwork,closelead,schemeType,schemeStatus;
    int productId, subProductId,countryId,stateId,districtId,talukaId,villageId;
    String rate,quantity,total;

    DatePickerDialog appDatePickerDialog,startDatePickerDialog,closeDatePickerDialog;
    private SimpleDateFormat dateFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lead);
        database = new Database(getApplicationContext());
        progressDialog = new ProgressDialog(AddLeadActivity.this);
        connectionDetector = new ConnectionDetector(getApplicationContext());
        prefManager = new PrefManager(this);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        initView();
        setStaticSpinner();
        setDynamicSpinner();

        final Calendar calendar = Calendar.getInstance();

        appDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mEtDate.setText(dateFormat.format(newDate.getTime()));
            }
        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        startDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mEtStartWork.setText(dateFormat.format(newDate.getTime()));
            }
        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));


        closeDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mEtCloseLead.setText(dateFormat.format(newDate.getTime()));
            }
        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));


        mSpInterestIn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Product product = interestSpinnerAdapter.getItem(position);
                Log.i("ID", String.valueOf(product.getProductID()));
                productId = product.getProductID();
                interestTypeSpinnerAdapter = new InterestTypeSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, database.getAllProductType(product.getProductID()));
                mSpInterestType.setAdapter(interestTypeSpinnerAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpInterestType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ProductType productType = interestTypeSpinnerAdapter.getItem(position);
                subProductId = productType.getProductTypeId();

                mEtRate.setText(String.valueOf(productType.getProductRate()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Country country = countryAdapter.getItem(position);
                countryId = country.getCountryID();
                stateAdapter = new StateAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,database.getAllState(countryId));
                mSpState.setAdapter(stateAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                State state = stateAdapter.getItem(position);
                stateId = state.getStateId();
                districtAdapter = new DistrictAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,database.getAllDistrict(stateId));
                mSpDistrict.setAdapter(districtAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                District district = districtAdapter.getItem(position);
                districtId = district.getDistrictId();
                talukaAdapter = new TalukaAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,database.getAllTaluka(districtId));
                mSpTaluka.setAdapter(talukaAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpTaluka.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Taluka taluka = talukaAdapter.getItem(position);
                talukaId = taluka.getTalukaId();
                villageAdapter = new VillageAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,database.getAllVillage(talukaId));
                mSpVillage.setAdapter(villageAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpVillage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Village village = villageAdapter.getItem(position);
                villageId = village.getVillageId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpSchemeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position ==0){
                    schemeType ="";
                }else {
                    schemeType = (String) parent.getItemAtPosition(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                schemeType = "";
            }
        });

        mSpSchemeStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0) {
                    schemeStatus = "";
                }else {
                    schemeStatus = (String) parent.getItemAtPosition(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                schemeStatus = "";
            }
        });

        mSpCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    category = "";
                }else {
                    category = (String) parent.getItemAtPosition(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                category = "";
            }
        });

        mSpCustomerResponse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    customerResponse = "";
                }else{
                    customerResponse = (String) parent.getItemAtPosition(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                customerResponse = "";
            }
        });

        mSpRequirement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    requirement = "";
                }else{
                    requirement = (String) parent.getItemAtPosition(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                requirement = "";
            }
        });

        mSpResponsePricing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    response = "";
                }else{
                    response = (String) parent.getItemAtPosition(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                response = "";
            }
        });

        mSpQuatationHardCopy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    quotHardCopy = "";
                }else{
                    quotHardCopy = (String) parent.getItemAtPosition(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                quotHardCopy = "";
            }
        });

        mSpBankRelation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position ==0){
                    bankRelation = "";
                }else {
                    bankRelation = (String) parent.getItemAtPosition(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                bankRelation = "";
            }
        });

        mEtQuantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!mEtQuantity.getText().toString().isEmpty())
                        mEtTotalAmount.setText(String.valueOf(Double.parseDouble(mEtRate.getText().toString().trim()) * Integer.parseInt(mEtQuantity.getText().toString().trim())));
                }
            }
        });
    }

    private void setDynamicSpinner() {
        interestSpinnerAdapter = new InterestSpinnerAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,database.getAllProduct());
        mSpInterestIn.setAdapter(interestSpinnerAdapter);

        countryAdapter = new CountryAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,database.getAllCountry());
        mSpCountry.setAdapter(countryAdapter);
    }

    private void setStaticSpinner() {

        ArrayAdapter<String> adapterCategories = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.categories)){
            @Override
            public boolean isEnabled(int position) {
                if (position == 0){
                    return true;
                }else{
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpCategory.setAdapter(adapterCategories);

        ArrayAdapter<String> adapterCustomerResponse = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.customer_response)){
            @Override
            public boolean isEnabled(int position) {
                if (position == 0){
                    return true;
                }else{
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        adapterCustomerResponse.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpCustomerResponse.setAdapter(adapterCustomerResponse);

        ArrayAdapter<String> adapterRequirement = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.requirement)){
            @Override
            public boolean isEnabled(int position) {
                if (position == 0){
                    return true;
                }else{
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        adapterRequirement.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpRequirement.setAdapter(adapterRequirement);

        ArrayAdapter<String> adapterResponse = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.response)){
            @Override
            public boolean isEnabled(int position) {
                if (position == 0){
                    return true;
                }else{
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        adapterResponse.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpResponsePricing.setAdapter(adapterResponse);

        ArrayAdapter<String> adapterQuotation = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.quotation_hard)){
            @Override
            public boolean isEnabled(int position) {
                if (position == 0){
                    return true;
                }else{
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        adapterQuotation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpQuatationHardCopy.setAdapter(adapterQuotation);

        ArrayAdapter<String> adapterBankRelation = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.bank_relation)){
            @Override
            public boolean isEnabled(int position) {
                if (position == 0){
                    return true;
                }else{
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        adapterBankRelation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpBankRelation.setAdapter(adapterBankRelation);


        ArrayAdapter<String> adapterSchemeType = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.scheme_type)){
            @Override
            public boolean isEnabled(int position) {
                if (position == 0){
                    return true;
                }else{
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        adapterSchemeType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpSchemeType.setAdapter(adapterSchemeType);

        ArrayAdapter<String> adapterSchemeStatus = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.scheme_status)){
            @Override
            public boolean isEnabled(int position) {
                if (position == 0){
                    return true;
                }else{
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        adapterSchemeStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpSchemeStatus.setAdapter(adapterSchemeStatus);
}

    private void initView() {
        mEtFirstName = (EditText) findViewById(R.id.activity_add_lead_et_person_firstname);
        mEtMiddleName = (EditText) findViewById(R.id.activity_add_lead_et_person_middlename);
        mEtLastName = (EditText) findViewById(R.id.activity_add_lead_et_person_lastname);
        mEtCompanyName = (EditText) findViewById(R.id.activity_add_lead_et_company_name);
        mEtMobNumber = (EditText) findViewById(R.id.activity_add_lead_et_mob_no);
        mEtPhoneNumber = (EditText) findViewById(R.id.activity_add_lead_et_phone_no);
        mEtEmail = (EditText) findViewById(R.id.activity_add_lead_et_email);
        mEtAddress = (EditText) findViewById(R.id.activity_add_lead_et_address);
        mEtRate = (EditText)findViewById(R.id.activity_add_lead_et_rate);
        mEtQuantity = (EditText)findViewById(R.id.activity_add_lead_et_quantiry);
        mEtTotalAmount = (EditText)findViewById(R.id.activity_add_lead_et_total_amount);
        mEtLandArea = (EditText)findViewById(R.id.activity_add_lead_et_landarea);
        mEtDate = (EditText)findViewById(R.id.activity_add_lead_et_date);
        mEtTime = (EditText)findViewById(R.id.activity_add_lead_et_time);
        mEtStartWork = (EditText) findViewById(R.id.activity_add_lead_et_startwork);
        mEtCloseLead = (EditText) findViewById(R.id.activity_add_lead_et_closelead);
        mEtCustReq = (EditText)findViewById(R.id.activity_add_lead_et_cust_req);
        mEtBankName = (EditText)findViewById(R.id.activity_add_lead_et_bank_name);
        mEtBranchName = (EditText)findViewById(R.id.activity_add_lead_et_branch_name);
        mSpCategory = (Spinner) findViewById(R.id.activity_add_lead_sp_categories);
        mSpCountry = (Spinner) findViewById(R.id.activity_add_lead_sp_country);
        mSpState = (Spinner) findViewById(R.id.activity_add_lead_sp_state);
        mSpDistrict = (Spinner) findViewById(R.id.activity_add_lead_sp_district);
        mSpTaluka = (Spinner) findViewById(R.id.activity_add_lead_sp_taluka);
        mSpVillage = (Spinner) findViewById(R.id.activity_add_lead_sp_village);
        mSpInterestType = (Spinner) findViewById(R.id.activity_add_lead_sp_interest_type);
        mSpInterestIn = (Spinner) findViewById(R.id.activity_add_lead_sp_interest_in);
        mSpCustomerResponse = (Spinner)findViewById(R.id.activity_add_lead_sp_customer_response);
        mSpRequirement = (Spinner)findViewById(R.id.activity_add_lead_sp_requirement);
        mSpResponsePricing = (Spinner)findViewById(R.id.activity_add_lead_sp_response);
        mSpQuatationHardCopy = (Spinner)findViewById(R.id.activity_add_lead_sp_quat_hardcopy);
        mSpBankRelation = (Spinner)findViewById(R.id.activity_add_lead_sp_bank_relation);
        mSpSchemeType = (Spinner) findViewById(R.id.activity_add_lead_sp_scheme_type);
        mSpSchemeStatus = (Spinner) findViewById(R.id.activity_add_lead_sp_scheme_status);

        mBtnAddLead = (Button) findViewById(R.id.activity_add_lead_btn_add);
        mBtnAddLead.setOnClickListener(this);

        mEtDate.setOnClickListener(this);
        mEtTime.setOnClickListener(this);
        mEtCloseLead.setOnClickListener(this);
        mEtStartWork.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_add_lead_btn_add:
                addNewLead();
                break;
            case R.id.activity_add_lead_et_date:
                appDatePickerDialog.show();
                break;
            case R.id.activity_add_lead_et_startwork:
                startDatePickerDialog.show();
                break;
            case R.id.activity_add_lead_et_closelead:
                closeDatePickerDialog.show();
                break;
            case R.id.activity_add_lead_et_time:
                showTimePickerDialog();
                break;
        }
    }

    private void showTimePickerDialog() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    private void addNewLead() {


        firstName = mEtFirstName.getText().toString().trim();
        middleName = mEtMiddleName.getText().toString().trim();
        lastName = mEtLastName.getText().toString().trim();
        companyName = mEtCompanyName.getText().toString().trim();
        mobileNumber = mEtMobNumber.getText().toString().trim();
        phoneNumber = mEtPhoneNumber.getText().toString().trim();
        email = mEtEmail.getText().toString().trim();
        address = mEtAddress.getText().toString().trim();
        custReq = mEtCustReq.getText().toString().trim();
        date = mEtDate.getText().toString().trim();
        time = mEtTime.getText().toString().trim();
        rate = mEtRate.getText().toString().trim();
        quantity = mEtQuantity.getText().toString().trim();
        total = mEtTotalAmount.getText().toString().trim();
        landArea = mEtLandArea.getText().toString().trim();
        bankName = mEtBankName.getText().toString().trim();
        branchName = mEtBranchName.getText().toString().trim();
        startwork = mEtStartWork.getText().toString().trim();
        closelead = mEtCloseLead.getText().toString().trim();


        if (firstName.isEmpty()){
            mEtFirstName.setError("Please enter first name");
            mEtFirstName.requestFocus();
        }else if (lastName.isEmpty()){
            mEtLastName.setError("Please enter last name");
            mEtLastName.requestFocus();
        }else if (mobileNumber.isEmpty()){
            mEtMobNumber.setError("Please enter mobile number");
            mEtMobNumber.requestFocus();
        }else if (mobileNumber.length() <10){
            mEtMobNumber.setError("Please enter valid mobile number");
            mEtMobNumber.requestFocus();
        }else if (!UtilClass.isValidEmail(email) && !email.isEmpty()){
            mEtEmail.setError("Please enter valid email");
            mEtEmail.requestFocus();
        }else if (quantity.isEmpty()){
            mEtQuantity.setError("Please enter quantity");
            mEtQuantity.requestFocus();
        }/*else if (connectionDetector.isConnectingToInternet()){
            new AddLeadAsync().execute();
        }*/else {
            Lead lead = new Lead();
            lead.setFirstName(firstName);
            lead.setMiddleName(middleName);
            lead.setLastName(lastName);
            lead.setCompanyName(companyName);
            lead.setCategory(category);
            lead.setMobileNumber(mobileNumber);
            lead.setPhoneNumber(phoneNumber);
            lead.setEmail(email);
            lead.setAddressDetail(address);
            lead.setCountryId(countryId);
            lead.setStateId(stateId);
            lead.setDistrictId(districtId);
            lead.setTalukaId(talukaId);
            lead.setVillageId(villageId);
            lead.setProductId(productId);
            lead.setSubProductId(subProductId);
            lead.setRate(Double.valueOf(rate));
            lead.setQuantity(Integer.parseInt(quantity));
            lead.setTotalAmount(Double.valueOf(total));
            lead.setLandArea(landArea);
            lead.setCustomerResponse(customerResponse);
            lead.setRequirement(requirement);
            lead.setResponse(response);
            lead.setQuotHardCopy(quotHardCopy);
            lead.setBankRelation(bankRelation);
            lead.setBankName(bankName);
            lead.setBranchName(branchName);
            lead.setContactDate(date);
            lead.setContactTime(time);
            lead.setStartWork(startwork);
            lead.setCloseLead(closelead);
            lead.setCustReq(custReq);
            lead.setSchemeType(schemeType);
            lead.setSchemeStatus(schemeStatus);
            lead.setUserId(Integer.parseInt(prefManager.getUserID()));
            database.insertLead(lead);
            Toast.makeText(AddLeadActivity.this, "Lead add successfully", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public static class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            String timeSet = "";
            if (hourOfDay > 12) {
                hourOfDay -= 12;
                timeSet = "PM";
            } else if (hourOfDay == 0) {
                hourOfDay += 12;
                timeSet = "AM";
            } else if (hourOfDay == 12)
                timeSet = "PM";
            else
                timeSet = "AM";


            String minutes = "";
            if (minute < 10)
                minutes = "0" + minute;
            else
                minutes = String.valueOf(minute);

            String aTime = new StringBuilder().append(hourOfDay).append(':')
                    .append(minutes).append(" ").append(timeSet).toString();
            mEtTime.setText(aTime);
        }
    }

}

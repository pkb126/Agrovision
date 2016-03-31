package com.praveenbhati.agrovision.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.praveenbhati.agrovision.model.Country;
import com.praveenbhati.agrovision.model.District;
import com.praveenbhati.agrovision.model.Lead;
import com.praveenbhati.agrovision.model.Product;
import com.praveenbhati.agrovision.model.ProductType;
import com.praveenbhati.agrovision.model.State;
import com.praveenbhati.agrovision.model.Taluka;
import com.praveenbhati.agrovision.model.Village;

import java.util.ArrayList;

/**
 * Created by Bhati on 11/9/2015.
 */
public class Database extends SQLiteOpenHelper {


    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "agrovision";

    // Lead table name
    private static final String TABLE_LEAD = "tbl_lead";

    // Product table name
    private static final String TABLE_PRODUCT = "tbl_product";

    // Product table name
    private static final String TABLE_PRODUCT_TYPE = "tbl_product_type";

    // Country table name
    private static final String TABLE_COUNTRY = "tbl_country";

    // State table name
    private static final String TABLE_STATE = "tbl_state";

    // District  table name
    private static final String TABLE_DISTRICT = "tbl_district";

    // Taluka  table name
    private static final String TABLE_TALUKA = "tbl_taluka";

    // Village  table name
    private static final String TABLE_VILLAGE = "tbl_village";

    String CREATE_PRODUCT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCT + "(" +
            "PRODUCT_ID  INTEGER PRIMARY KEY, PRODUCT_NAME TEXT)";

    String CREATE_COUNTRY_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_COUNTRY + "(" +
            "COUNTRY_ID  INTEGER PRIMARY KEY, COUNTRY_NAME TEXT)";

    String CREATE_STATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_STATE + "(" +
            "STATE_ID  INTEGER PRIMARY KEY, STATE_NAME TEXT, COUNTRY_ID INTEGER )";

    String CREATE_DISTRICT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_DISTRICT + "(" +
            "DISTRICT_ID  INTEGER PRIMARY KEY, DISTRICT_NAME TEXT, STATE_ID INTEGER )";

    String CREATE_TALUKA_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_TALUKA + "(" +
            "TALUKA_ID  INTEGER PRIMARY KEY, TALUKA_NAME TEXT, DISTRICT_ID INTEGER )";

    String CREATE_VILLAGE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_VILLAGE + "(" +
            "VILLAGE_ID  INTEGER PRIMARY KEY, VILLAGE_NAME TEXT, TALUKA_ID INTEGER )";

    String CREATE_PRODUCT_TYPE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCT_TYPE + "(" +
            "PRODUCT_TYPE_ID  INTEGER PRIMARY KEY, PRODUCT_TYPE_NAME TEXT, PRODUCT_ID INTEGER, PRODUCT_TYPE_RATE INTEGER)";

    String CREATE_LEAD_TABLE = "CREATE TABLE " + TABLE_LEAD + "("
            + " LEAD_ID INTEGER PRIMARY KEY, "
            + " FIRST_NAME TEXT,"
            + " MIDDLE_NAME TEXT,"
            + " LAST_NAME TEXT,"
            + " COMPANY_NAME TEXT, "
            + " CATEGORY TEXT, "
            + " MOBILE_NUMBER TEXT, "
            + " PHONE_NUMBER TEXT, "
            + " EMAIL_ID TEXT, "
            + " ADDRESS_DETAILS TEXT, "
            + " COUNTRY INTEGER, "
            + " STATE INTEGER,"
            + " DISTRICT INTEGER, "
            + " TALUKA INTEGER, "
            + " VILLAGE INTEGER, "
            + " INTREST_TYPE INTEGER,"
            + " INTREST_IN INTEGER,"
            + " RATE INTEGER,"
            + " QUANTITY INTEGER,"
            + " TOTAL_AMOUNT INTEGER,"
            + " LAND_AREA TEXT, "
            + " CUSTOMER_RESPONSE TEXT, "
            + " REQUIREMENT TEXT, "
            + " RESPONSE TEXT, "
            + " QUOT_HARDCOPY TEXT, "
            + " BANK_RELATION TEXT, "
            + " BANK_NAME TEXT, "
            + " BRANCH_NAME TEXT, "
            + " CONTACT_DATE TEXT,"
            + " CONTACT_TIME TEXT,"
            + " START_OF_WORK TEXT, "
            + " CLOSE_LEAD TEXT, "
            + " CUST_REQ TEXT,"
            + " SCHEME_TYPE TEXT,"
            + " SCHEME_STATUS TEXT,"
            + " USER_ID INTEGER,"
            + " SYNC_STATUS INTEGER )";


    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LEAD_TABLE);
        db.execSQL(CREATE_PRODUCT_TABLE);
        db.execSQL(CREATE_PRODUCT_TYPE_TABLE);
        db.execSQL(CREATE_COUNTRY_TABLE);
        db.execSQL(CREATE_STATE_TABLE);
        db.execSQL(CREATE_DISTRICT_TABLE);
        db.execSQL(CREATE_TALUKA_TABLE);
        db.execSQL(CREATE_VILLAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_TYPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEAD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISTRICT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TALUKA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VILLAGE);

        onCreate(db);
    }

    public void addCountry(int countryId,String countryName){
        try {
            SQLiteDatabase database = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("COUNTRY_ID", countryId);
            values.put("COUNTRY_NAME", countryName);

            database.insert(TABLE_COUNTRY, null, values);
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addState(int stateId,String stateName,int countryId){
        try {
            SQLiteDatabase database = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("STATE_ID", stateId);
            values.put("STATE_NAME", stateName);
            values.put("COUNTRY_ID",countryId);

            database.insert(TABLE_STATE, null, values);
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addDistrict(int districtId,String districtName,int stateId){
        try {
            SQLiteDatabase database = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("DISTRICT_ID", districtId);
            values.put("DISTRICT_NAME", districtName);
            values.put("STATE_ID",stateId);

            database.insert(TABLE_DISTRICT, null, values);
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addTaluka(int talukaId,String talukaName,int districtId){
        try {
            SQLiteDatabase database = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("TALUKA_ID", talukaId);
            values.put("TALUKA_NAME", talukaName);
            values.put("DISTRICT_ID", districtId);

            database.insert(TABLE_TALUKA, null, values);
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addVillage(int villageId,String villageName,int talukaId){
        try {
            SQLiteDatabase database = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("VILLAGE_ID", villageId);
            values.put("VILLAGE_NAME", villageName);
            values.put("TALUKA_ID",talukaId);

            database.insert(TABLE_VILLAGE, null, values);
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addProduct(Product product) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("PRODUCT_ID", product.getProductID());
        values.put("PRODUCT_NAME", product.getProductName());

        database.insert(TABLE_PRODUCT, null, values);
        database.close();
    }

    public void addProductType(ProductType productType) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("PRODUCT_TYPE_ID", productType.getProductTypeId());
        values.put("PRODUCT_TYPE_NAME", productType.getProductTypeName());
        values.put("PRODUCT_ID", productType.getProductId());
        values.put("PRODUCT_TYPE_RATE", productType.getProductRate());

        database.insert(TABLE_PRODUCT_TYPE, null, values);
        database.close();
    }

    public void insertLead(Lead lead) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("FIRST_NAME", lead.getFirstName());
        values.put("MIDDLE_NAME", lead.getMiddleName());
        values.put("LAST_NAME", lead.getLastName());
        values.put("COMPANY_NAME", lead.getCompanyName());
        values.put("CATEGORY",lead.getCategory());
        values.put("MOBILE_NUMBER", lead.getMobileNumber());
        values.put("PHONE_NUMBER", lead.getPhoneNumber());
        values.put("EMAIL_ID", lead.getEmail());
        values.put("ADDRESS_DETAILS ", lead.getAddressDetail());
        values.put("COUNTRY", lead.getCountryId());
        values.put("STATE", lead.getStateId());
        values.put("DISTRICT", lead.getDistrictId());
        values.put("TALUKA",lead.getTalukaId());
        values.put("VILLAGE",lead.getVillageId());
        values.put("INTREST_TYPE", lead.getProductId());
        values.put("INTREST_IN", lead.getSubProductId());
        values.put("RATE", lead.getRate());
        values.put("QUANTITY", lead.getQuantity());
        values.put("TOTAL_AMOUNT", lead.getTotalAmount());
        values.put("LAND_AREA",lead.getLandArea());
        values.put("CUSTOMER_RESPONSE",lead.getCustomerResponse());
        values.put("REQUIREMENT", lead.getRequirement());
        values.put("RESPONSE",lead.getResponse());
        values.put("QUOT_HARDCOPY",lead.getQuotHardCopy());
        values.put("BANK_RELATION",lead.getBankRelation());
        values.put("BANK_NAME",lead.getBankName());
        values.put("BRANCH_NAME",lead.getBranchName());
        values.put("CONTACT_DATE", lead.getContactDate());
        values.put("CONTACT_TIME", lead.getContactTime());
        values.put("START_OF_WORK",lead.getStartWork());
        values.put("CLOSE_LEAD",lead.getCloseLead());
        values.put("CUST_REQ", lead.getCustReq());
        values.put("SCHEME_TYPE",lead.getSchemeType());
        values.put("SCHEME_STATUS",lead.getSchemeStatus());
        values.put("USER_ID", lead.getUserId());

        database.insert(TABLE_LEAD, null, values);
        database.close();
    }

    public Lead getLead(int userId){
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM "+TABLE_LEAD+ " WHERE USER_ID ="+ userId +" LIMIT 1";
        Cursor cursor = database.rawQuery(query,null);
        Lead lead = new Lead();

        if (cursor.moveToFirst()){
            lead.setLeadId(cursor.getInt(0));
            lead.setFirstName(cursor.getString(1));
            lead.setMiddleName(cursor.getString(2));
            lead.setLastName(cursor.getString(3));
            lead.setCompanyName(cursor.getString(4));
            lead.setCategory(cursor.getString(5));
            lead.setMobileNumber(cursor.getString(6));
            lead.setPhoneNumber(cursor.getString(7));
            lead.setEmail(cursor.getString(8));
            lead.setAddressDetail(cursor.getString(9));
            lead.setCountryId(cursor.getInt(10));
            lead.setStateId(cursor.getInt(11));
            lead.setDistrictId(cursor.getInt(12));
            lead.setTalukaId(cursor.getInt(13));
            lead.setVillageId(cursor.getInt(14));
            lead.setProductId(cursor.getInt(15));
            lead.setSubProductId(cursor.getInt(16));
            lead.setRate(cursor.getDouble(17));
            lead.setQuantity(cursor.getInt(18));
            lead.setTotalAmount(cursor.getDouble(19));
            lead.setLandArea(cursor.getString(20));
            lead.setCustomerResponse(cursor.getString(21));
            lead.setRequirement(cursor.getString(22));
            lead.setResponse(cursor.getString(23));
            lead.setQuotHardCopy(cursor.getString(24));
            lead.setBankRelation(cursor.getString(25));
            lead.setBankName(cursor.getString(26));
            lead.setBranchName(cursor.getString(27));
            lead.setContactDate(cursor.getString(28));
            lead.setContactTime(cursor.getString(29));
            lead.setStartWork(cursor.getString(30)  );
            lead.setCloseLead(cursor.getString(31));
            lead.setCustReq(cursor.getString(32));
            lead.setSchemeType(cursor.getString(33));
            lead.setSchemeStatus(cursor.getString(34));
            lead.setUserId(cursor.getInt(35));
        }
        cursor.close();
        database.close();
        return lead;
    }

    public void deleteLead(int leadId){
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            int id = database.delete(TABLE_LEAD, "LEAD_ID = ?", new String[]{String.valueOf(leadId)});
            Log.i("Delete Lead",id+" Deleted");
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getLeadCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LEAD;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public ArrayList<Product> getAllProduct() {

        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<Product> productArrayList = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_PRODUCT;

        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();

                product.setProductID(cursor.getInt(0));
                product.setProductName(cursor.getString(1));

                productArrayList.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return productArrayList;
    }

    public ArrayList<Country> getAllCountry() {

        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<Country> countryArrayList = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_COUNTRY;

        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {

                Country country = new Country();
                country.setCountryID(cursor.getInt(0));
                country.setCountryName(cursor.getString(1));
                countryArrayList.add(country);
            } while (cursor.moveToNext());
        }

        database.close();

        return countryArrayList;
    }

    public ArrayList<State> getAllState(int productID) {

        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<State> stateArrayList = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_STATE + " WHERE COUNTRY_ID = " + productID;

        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                State state = new State();
                state.setStateId(cursor.getInt(0));
                state.setStateName(cursor.getString(1));
                state.setCountryId(cursor.getInt(2));

                stateArrayList.add(state);
            } while (cursor.moveToNext());
        }

        database.close();

        return stateArrayList;
    }

    public ArrayList<Village> getAllVillage(int productID) {

        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<Village> villageArrayList = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_VILLAGE + " WHERE TALUKA_ID = " + productID;

        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Village village = new Village();
                village.setVillageId(cursor.getInt(0));
                village.setVillageName(cursor.getString(1));
                village.setTalukaId(cursor.getInt(2));
                villageArrayList.add(village);
            } while (cursor.moveToNext());
        }

        database.close();

        return villageArrayList;
    }

    public ArrayList<Taluka> getAllTaluka(int productID) {

        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<Taluka> talukaArrayList = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_TALUKA + " WHERE DISTRICT_ID = " + productID;

        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Taluka taluka = new Taluka();
                taluka.setTalukaId(cursor.getInt(0));
                taluka.setTalukaName(cursor.getString(1));
                taluka.setDistrictId(cursor.getInt(2));

                talukaArrayList.add(taluka);
            } while (cursor.moveToNext());
        }

        database.close();

        return talukaArrayList;
    }

    public ArrayList<District> getAllDistrict(int productID) {

        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<District> districtArrayList = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_DISTRICT + " WHERE STATE_ID = " + productID;

        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                District district = new District();
                district.setDistrictId(cursor.getInt(0));
                district.setDistrictName(cursor.getString(1));
                district.setStateId(cursor.getInt(2));

                districtArrayList.add(district);
            } while (cursor.moveToNext());
        }

        database.close();

        return districtArrayList;
    }

    public ArrayList<ProductType> getAllProductType(int productID) {

        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<ProductType> productTypeArrayList = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_PRODUCT_TYPE + " WHERE PRODUCT_ID = " + productID;

        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                ProductType productType = new ProductType();

                productType.setProductTypeId(cursor.getInt(0));
                productType.setProductTypeName(cursor.getString(1));
                productType.setProductId(cursor.getInt(2));
                productType.setProductRate(cursor.getDouble(3));

                productTypeArrayList.add(productType);
            } while (cursor.moveToNext());
        }

        database.close();

        return productTypeArrayList;
    }

    public ArrayList<Cursor> getData(String Query) {
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[]{"mesage"};
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2 = new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try {
            String maxQuery = Query;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[]{"Success"});

            alc.set(1, Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0, c);
                c.moveToFirst();

                return alc;
            }
            return alc;
        } catch (SQLException sqlEx) {
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + sqlEx.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        } catch (Exception ex) {

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + ex.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        }


    }

    public void deleteProduct() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_PRODUCT, null, null);
        database.delete(TABLE_PRODUCT_TYPE, null, null);
        database.delete(TABLE_COUNTRY,null,null);
        database.delete(TABLE_STATE,null,null);
        database.delete(TABLE_DISTRICT,null,null);
        database.delete(TABLE_TALUKA,null,null);
        database.delete(TABLE_VILLAGE,null,null);
        database.close();
    }
}

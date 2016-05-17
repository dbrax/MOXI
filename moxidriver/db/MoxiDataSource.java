package tz.co.delis.www.moxidriver.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by apple on 3/29/16.
 */
public class MoxiDataSource {

    private SQLiteDatabase mdatabase;
    private MoxiHelper mMoxiHelper;
    private Context mContext;

    private static final String TAG=MoxiDataSource.class.getSimpleName();
    public MoxiDataSource(Context context){
        mContext=context;
        mMoxiHelper=new MoxiHelper(this.mContext);

    }

    public void open() {
        mdatabase=mMoxiHelper.getWritableDatabase();
    }

    public void close() {
        mdatabase.close();


    }

    public void insertLanguage(String lang){
mdatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(MoxiHelper.LANG_COLUMN, lang);
            mdatabase.insert(MoxiHelper.Table_Name,null,values);

            mdatabase.setTransactionSuccessful();

        }
        catch(Exception e){

            Log.d(TAG,"Error with database insert");
        }finally {
            mdatabase.endTransaction();
        }


    }

    public void updateLanguage(String newLang){


        mdatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(MoxiHelper.LANG_COLUMN, newLang);

// Which row to update, based on the ID
            String selection = MoxiHelper.LANG_COLUMN+ " LIKE ?";
            String[] selectionArgs = { String.valueOf(0) };

            int count = mdatabase.update(
                    MoxiHelper.Table_Name,
                    values,
                    selection,
                    selectionArgs);

            mdatabase.setTransactionSuccessful();

        }
        catch(Exception e){

            Log.d(TAG,"Error with database insert");
        }finally {
            mdatabase.endTransaction();
        }




    }

    public void updateUseStatus(String USE_STATUS){


        mdatabase.beginTransaction();
        try {
        ContentValues values = new ContentValues();
        values.put(MoxiHelper.USE_STATUS_COLUMN, USE_STATUS);

// Which row to update, based on the ID
        String selection = MoxiHelper.USE_STATUS_COLUMN+ " LIKE ?";
        String[] selectionArgs = { String.valueOf(0) };

        int count = mdatabase.update(
                MoxiHelper.Table_Name,
                values,
                selection,
                selectionArgs);

            mdatabase.setTransactionSuccessful();

        }
        catch(Exception e){

            Log.d(TAG,"Error with database insert");
        }finally {
            mdatabase.endTransaction();
        }


    }


    public void updateGcmRegIdBoolean(String id){

        mdatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(MoxiHelper.GCM_STATUS_COLUMN, id);

// Which row to update, based on the ID
            String selection = MoxiHelper.GCM_STATUS_COLUMN+ " LIKE ?";
            String[] selectionArgs = { String.valueOf(0) };

            int count = mdatabase.update(
                    MoxiHelper.Driver_Table_Name,
                    values,
                    selection,
                    selectionArgs);

            mdatabase.setTransactionSuccessful();

        }
        catch(Exception e){

            Log.d(TAG,"Error with database insert");
        }finally {
            mdatabase.endTransaction();
        }


    }

    public Cursor selectLanguage(){

        String whereClause="where "+MoxiHelper.ID_COLUMN+"=1";

        Cursor cursor =mdatabase.query(
                MoxiHelper.Table_Name,
                new String[]{ MoxiHelper.LANG_COLUMN},
                null,
                null,
                null,
                null,
                null
        );


return cursor;
    }

    public Cursor selectGcmStatus(){

        Cursor cursor =mdatabase.query(
                MoxiHelper.Driver_Table_Name,
                new String[]{ MoxiHelper.GCM_STATUS_COLUMN},
                null,
                null,
                null,
                null,
                null
        );

        return cursor;


    }

    public Cursor selectUseStatus(){

       // String whereClause="where "+MoxiHelper.ID_COLUMN+"=1";

        Cursor cursor =mdatabase.query(
                MoxiHelper.Table_Name,
                new String[]{ MoxiHelper.USE_STATUS_COLUMN},
                null,
                null,
                null,
                null,
                null
        );

        return cursor;
    }

    public Cursor selectDriverLoginDetails(){

        // String whereClause="where "+MoxiHelper.ID_COLUMN+"=1";

        Cursor cursor =mdatabase.query(
                MoxiHelper.Driver_Table_Name,
                new String[]{ MoxiHelper.Driver_loginDetail_COLUMN},
                null,
                null,
                null,
                null,
                null
        );

        return  cursor;
    }

    public Cursor selectDriverPassword(){

        // String whereClause="where "+MoxiHelper.ID_COLUMN+"=1";

        Cursor cursor =mdatabase.query(
                MoxiHelper.Driver_Table_Name,
                new String[]{ MoxiHelper.Driver_Password_COLUMN},
                null,
                null,
                null,
                null,
                null
        );

        return  cursor;
    }



    public void insertBooking(int Customer_id,String Customer_name,String  Customer_Longitude,
                             String Customer_Latitude,String Customer_Location_Name,String
                                     Destination_Location_Name,String  Customer_Telephone  ){


        mdatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(MoxiHelper.Customer_ID_COLUMN,Customer_id);
            values.put(MoxiHelper.Customer_Latitude_COLUMN,Customer_Latitude);
            values.put(MoxiHelper.Customer_Longitude_COLUMN,Customer_Longitude);
            values.put(MoxiHelper.Customer_Name_COLUMN,Customer_name);
            values.put(MoxiHelper.Customer_Telephone_COLUMN,Customer_Telephone);
            values.put(MoxiHelper.Destination_Location_Name_COLUMN,Destination_Location_Name);

            values.put(MoxiHelper.Customer_Location_Name_COLUMN,Customer_Location_Name);

            mdatabase.insert(MoxiHelper.Booking_Table_Name,null,values);

            mdatabase.setTransactionSuccessful();

        }
        catch(Exception e){

            Log.d(TAG,"Error with database insert");
        }finally {
            mdatabase.endTransaction();
        }


    }
    public  void insertDriverData(int Driver_id,String Driver_name,String Driver_surname,String
            Driver_phone_number,String Driver_license_id,String Driver_email,String password,
                                  String DriverLogindata){
        mdatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(MoxiHelper.Driver_ID_COLUMN,Driver_id);
            values.put(MoxiHelper.Driver_Name_COLUMN,Driver_name);
            values.put(MoxiHelper.Driver_Surname_COLUMN,Driver_surname);
            values.put(MoxiHelper.Driver_phone_number_COLUMN,Driver_phone_number);
            values.put(MoxiHelper.Driver_licence_id_COLUMN,Driver_license_id);
            values.put(MoxiHelper.Driver_Email_COLUMN,Driver_email);
            values.put(MoxiHelper.Driver_loginDetail_COLUMN, DriverLogindata);
            values.put(MoxiHelper.Driver_Password_COLUMN,password);
            mdatabase.insert(MoxiHelper.Driver_Table_Name,null,values);

            mdatabase.setTransactionSuccessful();

        }
        catch(Exception e){

            Log.d(TAG,"Error with database insert");
        }finally {
            mdatabase.endTransaction();
        }




    }

    public Cursor selectDriverData(){

        // String whereClause="where "+MoxiHelper.ID_COLUMN+"=1";

        Cursor cursor =mdatabase.query(
                MoxiHelper.Driver_Table_Name,
                new String[]{ MoxiHelper.Driver_Email_COLUMN,MoxiHelper.Driver_Name_COLUMN,
                        MoxiHelper.Driver_phone_number_COLUMN,MoxiHelper.Driver_ID_COLUMN},
                null,
                null,
                null,
                null,
                null,
                null

        );

        return  cursor;
    }


    public void insertDriverLoginDetails(String email_phone, String password) {
        mdatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(MoxiHelper.Driver_loginDetail_COLUMN, email_phone);
            values.put(MoxiHelper.Driver_Password_COLUMN,password);
            mdatabase.insert(MoxiHelper.Driver_Table_Name,null,values);

            mdatabase.setTransactionSuccessful();

        }
        catch(Exception e){

            Log.d(TAG,"Error with database insert");
        }finally {
            mdatabase.endTransaction();
        }


    }



    public void insertDriverDetails(String lang){
        mdatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(MoxiHelper.LANG_COLUMN, lang);
            mdatabase.insert(MoxiHelper.Table_Name,null,values);

            mdatabase.setTransactionSuccessful();

        }
        catch(Exception e){

            Log.d(TAG,"Error with database insert");
        }finally {
            mdatabase.endTransaction();
        }


    }

}

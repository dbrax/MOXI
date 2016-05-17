package tz.co.delis.www.moxidriver.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by apple on 3/29/16.
 */

public class MoxiHelper extends SQLiteOpenHelper {


    private static final String db_Name="MoxiDriver.db";
    private static final int db_version=1;

    //-----------------App configuration table and database and columns------------
    public static final String Table_Name="AppConfig";
    public static final String ID_COLUMN ="_ID ";
    public static final String LANG_COLUMN ="lang";
    public static final String USE_STATUS_COLUMN="USE_STATUS";


    //--------------Driver column details-----------------------------
    public static final String Driver_Table_Name="Driver";
    public static final String Driver_ID_COLUMN="Driver_id";
    public static final String Driver_Name_COLUMN="Driver_Name";
    public static final String Driver_Surname_COLUMN="Driver_Surname";
    public static final String Driver_Password_COLUMN="Driver_Password";
    public static final String Driver_Email_COLUMN="Driver_Email";
    public static final String Driver_phone_number_COLUMN="Driver_phone_number";
    public static final String Driver_licence_id_COLUMN="Driver_licence_id";
    public static final String Driver_loginDetail_COLUMN= "driver_login_details";
    public static final String GCM_STATUS_COLUMN = "gcm_status";

    //------------------- booking details table and columns -------------------

    public static final String Booking_Table_Name="Booking";
    public static final String Booking_ID_COLUMN="Booking_id";
    public static final String Customer_ID_COLUMN="customer_id";
    public static final String Customer_Longitude_COLUMN="customer_longitude";
    public static final String Customer_Latitude_COLUMN="customer_latitude";
    public static final String Customer_Location_Name_COLUMN="customer_location_name";
    public static final String Destination_Location_Name_COLUMN="destination_name";
    public static final String Customer_Telephone_COLUMN="customer_telephone";
    public static final String Customer_Name_COLUMN="customer_name";




    private static final String DB_CREATE="CREATE TABLE  "+ Table_Name+"("+ID_COLUMN+" INTEGER " +
            " PRIMARY KEY " +
            " AUTOINCREMENT ,"+LANG_COLUMN +" VARCHAR(5),"+USE_STATUS_COLUMN+" INTEGER DEFAULT 0 " +
            " )";




    private static final String DB_Driver ="CREATE TABLE  "+ Driver_Table_Name+"("+Driver_ID_COLUMN+" INTEGER " +
            " PRIMARY KEY " +
            " AUTOINCREMENT ,"+Driver_Name_COLUMN +" VARCHAR(5),"+Driver_Surname_COLUMN+"  " +
            "VARCHAR(5), " +Driver_Password_COLUMN+"  " +
            "VARCHAR(5), " +Driver_Email_COLUMN+"  " +
            "VARCHAR(5), " +Driver_phone_number_COLUMN+"  " +
            "VARCHAR(5), " +Driver_licence_id_COLUMN+"  " +
            "VARCHAR(5), " +Driver_loginDetail_COLUMN+"  " +
            "VARCHAR(5) ," +GCM_STATUS_COLUMN+" INTEGER DEFAULT 0 " +
            " )";


    private static final String DB_Booking ="CREATE TABLE  "+ Booking_Table_Name+"" +
            "("+Booking_ID_COLUMN+" INTEGER " +
            " PRIMARY KEY " +
            " AUTOINCREMENT ,"+Customer_ID_COLUMN+"  INTEGER, "+Customer_Longitude_COLUMN +" " +
            "VARCHAR(5), " + " "+Customer_Latitude_COLUMN+"  " +
            "VARCHAR(5), " +Customer_Location_Name_COLUMN+"  " +
            "VARCHAR(5), " +Destination_Location_Name_COLUMN+"  " +
            "VARCHAR(5), " +Driver_phone_number_COLUMN+"  " +
            "VARCHAR(5), " +Customer_Telephone_COLUMN+"  " +
            "VARCHAR(5), " +Customer_Name_COLUMN+"  " +

            " )";


    public MoxiHelper(Context context) {
        super(context, db_Name, null, db_version);
    }


    //super();

    @Override
    public void onCreate(SQLiteDatabase db) {
      db.execSQL(DB_CREATE);
      db.execSQL(DB_Driver);
        db.execSQL(DB_Booking);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }

}

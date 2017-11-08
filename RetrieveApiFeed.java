package com.example.android.currencyconverter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Abdulfattah Hamzah on 11/2/2017.
 */

/**
 * Create a subclass of AsyncTask call RetrieveApiTask to retrieve data from an api
 */
public class RetrieveApiFeed extends AsyncTask<Void, Void, String> {

    private Exception exception;

    //Declare the selected item string to contain the currency code selected
    private  String mSelectedItem;

    //Initialize the API to extract data from
    static final String API_URL = "https://min-api.cryptocompare.com/data/pricemulti?fsyms=ETH,BTC&tsyms=";

    //BTC object from response data object
    private JSONObject BTCObject;

    //ETH object from response data object
    private JSONObject ETHObject;

    //BTC corresponding currency price in BTCObject
    private String itemBTCPrice;

    //ETH corresponding currency price in ETHObject
    private String itemETHPrice;

    //Create a context instance
    private Context mContext;

    //Create a btc response Text view instance
    TextView btcResponseTextView;

    //Create a eth response text view instance
    TextView ethResponseTextView;


    /**
     * Create a public constructor for RetrieveApiFeed Class with two parameters
     */

    public RetrieveApiFeed(Context context, String selectedItem){

        mContext = context;
        mSelectedItem = selectedItem;

    }


    /**
     * Create a public constructor for RetrieveApiFeed Class with four parameters
     */

    public RetrieveApiFeed(Context context, TextView btcView, TextView ethView, String selectedItem){

        btcResponseTextView = btcView;
        mContext = context;
        ethResponseTextView = ethView;
        mSelectedItem = selectedItem;

    }



    protected void onPreExecute(){

        //Set the BTC and ETH Price text view to empty before execution
        ethResponseTextView.setText("");
        btcResponseTextView.setText("");

    }

    protected String doInBackground(Void... urls){

        //Create
        try {
            //Create the URL to request data from
            URL url = new URL(API_URL + mSelectedItem);

            //setup and open HttpURLConnection to make an API request
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            //Read complete response string using BufferReader and StringBuilder
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) !=  null){
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            }

            finally {
                //disconnect HttpURLConnection
                urlConnection.disconnect();

            }
        }

        //catch error exceptions
        catch (Exception e){
            //log error message
            Log.e("ERROR", e.getMessage(), e);

            //return null if error exist
            return  null;
        }
    }

    protected void onPostExecute(String response){
        if(response == null){
            response = "There Was an Error";
        }
        Log.i("INFO", response);


        //Retrieve responseJSON values
        try{
            //Get the JSON object data from the response gotten from API
            JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
            BTCObject = (JSONObject) object.getJSONObject("BTC");
            ETHObject = (JSONObject) object.getJSONObject("ETH");
            itemBTCPrice = (String) BTCObject.getString(mSelectedItem);
            itemETHPrice = (String) ETHObject.getString(mSelectedItem);

            if(ethResponseTextView != null){
                //Display ETH price in currency_code text view
                ethResponseTextView.setText(itemETHPrice);
            }

            if(btcResponseTextView != null){
                //display response in response text view
                btcResponseTextView.setText(itemBTCPrice);
            }

        }

        //Throw exception when error occur
        catch (JSONException e){
            e.printStackTrace();
        }

    }

}

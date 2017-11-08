package com.example.android.currencyconverter;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //declare the string that takes the selected item text in the spinner
    private String mItem;

    //Declare an instance of spinner
    private  Spinner spinner;

    private String BTCPrice;

    private String ETHPrice;

    private RetrieveApiFeed ApiFeed;

    private ImageView converterMenuView;

    //Create an instance of bundle to take data that is to be sent to another activity
    private Bundle bundle = new Bundle();

    private TextView btcPriceView;

    private TextView  ethPriceView;

    private Intent btcConverterIntent;

    private Intent ethConverterIntent;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Get the select item string value
        mItem = parent.getItemAtPosition(position).toString();

        // getting the selected currency into  the created bundle
        bundle.putString("CurrencyABV", mItem);

        //display the selected item
        Toast.makeText(parent.getContext(), mItem + " option was selected", Toast.LENGTH_SHORT).show();

        //Find text view for BTC price
        btcPriceView = (TextView) findViewById(R.id.btc_price);

        //Find text view for eth price
        ethPriceView = (TextView) findViewById(R.id.eth_price);

        //Request for API data through the RetrieveApiFeed class
        ApiFeed = new RetrieveApiFeed(this, btcPriceView, ethPriceView, mItem);

        //execute request
        ApiFeed.execute();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find spinner view in main activity xml
        spinner = (Spinner) findViewById(R.id.currency_list);

        //Set onItemSelectedListener on the spinner
        spinner.setOnItemSelectedListener(this);

        //Create an Array adapter which uses the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.currency_code_array,
                R.layout.spinner_item);

        //Specifies the layout to use when the list of dropdown appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        //Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        converterMenuView = (ImageView) findViewById(R.id.converter_menu);

        converterMenuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Call showPopupMenu to display the menu popup
                showPopupMenu(converterMenuView);
            }
        });




    }


    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public  class GridSpacingItemDecoration extends RecyclerView.ItemDecoration{

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge){

            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            //declare item position and set to a value
            int position = parent.getChildAdapterPosition(view);

            //declare item column and set to a value
            int column = position % spanCount;

            if(includeEdge){
                //item left
                outRect.left = spacing - column * spacing / spanCount;

                //item right
                outRect.right = (column + 1) * spacing / spanCount;

                if(position < spanCount){
                    //item top edge
                    outRect.top = spacing;
                }

                //item bottom
                outRect.bottom = spacing;
            } else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;

                if(position >= spanCount){
                    outRect.top = spacing;
                }
            }


        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp){
        Resources resources = getResources();

        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics()));
    }


    private void showPopupMenu(View view) {
        // Create an instance of PopupMenu
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);

        //Get menu inflate
        MenuInflater menuInflater = popupMenu.getMenuInflater();

        //Inflate menu
        menuInflater.inflate(R.menu.converter_menu, popupMenu.getMenu());


        /**
         * Showing popup menu when tapping on 3 dots
         */
        popupMenu.setOnMenuItemClickListener(new MyMenuItemClickListener());

        popupMenu.show();

    }


    /**
     * Create MyMenuItemClickListener which implements onMenuItemClickListener Methods
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener{

        //Create MyMenuItemClickListener public constructor
        public MyMenuItemClickListener(){

        }

        //Override the onMenuItemClick method
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            //Checking for which menu is clicked
            switch (item.getItemId()){
                //if btc converter menu is clicked
                case(R.id.btc_converter):
                    Toast.makeText(MainActivity.this, "Convert to BTC", Toast.LENGTH_SHORT).show();

                    //Create an intent to lik to Converter Activity
                    btcConverterIntent = new Intent(MainActivity.this, BTCConverterActivity.class);

                    //Get text from btc price view
                    BTCPrice = btcPriceView.getText().toString();
                    bundle.putString("BTCPrice", BTCPrice);
                    btcConverterIntent.putExtras(bundle);
                    startActivity(btcConverterIntent);
                    return  true;

                //if eth converter menu is clicked
                case (R.id.eth_converter):
                    Toast.makeText(MainActivity.this, "Convert to ETH", Toast.LENGTH_SHORT).show();

                    //Create an intent to lik to Converter Activity
                    ethConverterIntent = new Intent(MainActivity.this, ETHConverterActivity.class);

                    //Get text from btc price view
                    ETHPrice = ethPriceView.getText().toString();
                    bundle.putString("ETHPrice", ETHPrice);
                    ethConverterIntent.putExtras(bundle);
                    startActivity(ethConverterIntent);

                    return true;
                default:
            }

            return false;
        }
    }


}



package com.example.android.currencyconverter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ETHConverterActivity extends AppCompatActivity {

    private double amountInputDouble;

    private double conversion;

    private double ETHPriceDouble;

    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.converter);

        //get the data that was passed from the Main Activity
        Bundle bundle = getIntent().getExtras();

        //get the ETH Price from the passed bundle data
        final String ETHPrice = bundle.getString("ETHPrice");

        //get the selected currency abbreviation from the passed bundle data
        final String currencyABV = bundle.getString("CurrencyABV");

        //Find the convert button
        Button convertButton = (Button) findViewById(R.id.convert_button);

        //Set onClick Listener on the convert button
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Find the input edit text view
                TextView amountInputView = (TextView) findViewById(R.id.amount_input);

                //get the input text
                String amountInput = amountInputView.getText().toString();

                //get the text view to populate the conversion result
                TextView conversionResult = (TextView) findViewById(R.id.conversion_result);

                try{
                    //convert the string input to double number type
                    amountInputDouble  = Double.parseDouble(amountInput);

                    //string result to populate in result text view
                    result = "Amount Input: " + amountInput + currencyABV + "\n" ;

                    //first set conversion result text to empty
                    conversionResult.setText("");

                    //convert ETH price string format to double number format
                    ETHPriceDouble = tryParseDouble(ETHPrice);

                    //Set result into btcResult
                    String ethResult = result;

                    //Do the conversion calculation
                    conversion = amountInputDouble / ETHPriceDouble;

                    //Add conversion rate to result to populate
                    ethResult += "Conversion Rate: 1 ETH = " + ETHPrice + " " + currencyABV + "\n";

                    //Add conversion result to populate
                    ethResult += "Conversion Result: " + conversion + " ETH";

                    //Set the result to conversion result text view
                    conversionResult.setText(ethResult);

                }
                catch (NullPointerException npe){
                    //what to show if the input value is not a number
                    String errorMessage = amountInput + " is not a number";
                    conversionResult.setText(errorMessage);
                }

            }
        });

    }

    /**
     * Function to convert price in string format to double number format
     * @param value
     * @return
     */
    public double tryParseDouble(String value){
        try{
            return Double.parseDouble(value);
        }catch (NullPointerException npe){
            return 0;
        }
    }
}

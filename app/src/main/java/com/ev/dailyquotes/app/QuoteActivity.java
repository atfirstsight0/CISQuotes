package com.ev.dailyquotes.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ev.dailyquotes.ConnectionDetector;
import com.ev.dailyquotes.MyPreferences;
import com.ev.dailyquotes.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class QuoteActivity extends AppCompatActivity {

    public Button nxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);

        MyPreferences pref = new MyPreferences(QuoteActivity.this);
        if (pref.isFirstTime()) {
            Toast.makeText(QuoteActivity.this, "Hi " + pref.getUserName(), Toast.LENGTH_LONG).show();
            pref.setOld(true);
        } else {
            Toast.makeText(QuoteActivity.this, "Welcome back " + pref.getUserName(),
                    Toast.LENGTH_LONG).show();
        }

        // create a "next" button to get the next quote.
        Button nxt = (Button) findViewById(R.id.next_quote);
        nxt.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                getQuoteOnline();
            }
        });

        ConnectionDetector cd = new ConnectionDetector(this);
        if (cd.isConnectingToInternet())
            getQuoteOnline();
        else
            readQuoteFromFile();
    }

    public void getQuoteOnline()
    {
        // set the quote text to loading
        final TextView quoteTxt = (TextView) findViewById(R.id.quotes);
        quoteTxt.setText(R.string.loading);

        // create an instance of the request
        JsonObjectRequest Request = new
                JsonObjectRequest(com.android.volley.Request.Method.GET,
                "http://quotes.stormconsultancy.co.uk/random.json",
                (String)null, // <-- error occurred because null needed to be cast to String
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.d("Response", response.toString());
                        String quote;
                        // parse the quote
                        try
                        {
                            quote = response.getString("quote");
                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            quote = "Error";
                        }
                        // set the quote text to the parsed quote
                        quoteTxt.setText(quote);
                        writeToFile(quote);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                VolleyLog.d("Response", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        // add request to request queue
        AppController.getInstance().addToRequestQueue(Request);
    }

    private void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                    openFileOutput("Quote.json", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Message:", "File write failed: " + e.toString());
        }
    }

    private void readQuoteFromFile()
    {
        String quote = "";
        try
        {
            InputStream inputStream = openFileInput("Quote.json");
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();
                Log.v("Message:", "reading...");
                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                quote = stringBuilder.toString();
            }
            } catch (FileNotFoundException e)
        {
            Log.e("Message:", "File not found: " + e.toString());
        } catch (IOException e)
        {
            Log.e("Message:", "Can not read file: "+ e.toString());
        }
        TextView quoteTxt = (TextView) findViewById(R.id.quotes);
        quoteTxt.setText(quote);
    }


}

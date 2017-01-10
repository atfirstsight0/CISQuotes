package com.ev.dailyquotes.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.ev.dailyquotes.MyPreferences;
import com.ev.dailyquotes.R;

/* Quotes Provider Application objectives are:
 * Add an external library dependency using Gradle
 * Initialize Volley objects in a singleton class
 * Create a CLass that Manages Shared Preferences
 * Create the Launcher Activity
 * Create QuoteActivity
 * Add "next" button
 * (Second iteration will add Author's name to each quote)
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyPreferences pref = new MyPreferences(MainActivity.this);
        if (!pref.isFirstTime()){
            Intent i = new Intent(getApplicationContext(), QuoteActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
            finish();
        }
    }

    public void SaveUserName(View v){
        EditText usrName = (EditText)findViewById(R.id.editText1);
        MyPreferences pref = new MyPreferences(MainActivity.this);
        pref.setUserName(usrName.getText().toString().trim());
        Intent i = new Intent(getApplicationContext(), QuoteActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
        finish();
    }


}

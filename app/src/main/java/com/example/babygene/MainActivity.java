package com.example.babygene;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;


//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//import org.json.JSON
//import org.json.parser.JSONParser;
//import org.json.parser.ParseException;

import android.util.Log;
import android.support.v4.app.FragmentActivity;




import org.w3c.dom.Text;

import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        generate();
        birthday();
    }
    private void birthday() {
        Button birthday = findViewById(R.id.Birthday);
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                int month = random.nextInt(12) + 1;
                int day = random.nextInt(28) + 1;
                String theDate = month + "/" + day;
                TextView date = findViewById(R.id.Date);
                TextView holiday = findViewById(R.id.Holiday);
                date.setText(theDate);
                String monthString;
                String dayString;
                if (month < 10) {
                    monthString = "0" + month;
                } else {
                    monthString = "" + month;
                }
                if (day < 10) {
                    dayString = "0" + day;
                } else {
                    dayString = "" + day;
                }
                HttpHandler sh = new HttpHandler();
                // Making a request to url and getting response
                String url = "https://holidayapi.com/v1/holidays?" +
                        "key=fcb87d16-5a12-4026-88a5-5b44ce0f39da&country=US&year=2018&" +
                        "month=" + monthString +
                        "&day=" + dayString;
                String jsonStr = sh.makeServiceCall(url);
                Log.e(TAG, "Response from url: " + jsonStr);
                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        JSONArray holidays = jsonObj.getJSONArray("holidays");
                        JSONObject days = holidays.getJSONObject(0);
                        String theHoliday = days.getString("name");
                        holiday.setText(theHoliday);
                    } catch (final JSONException e) {
                        String error = "error";
                        holiday.setText(error);
                    } catch (final Exception e) {
                        String exception = e.toString();
                        holiday.setText(exception);
                    }
                } else {
                    Log.e(TAG, "Couldn't get json from server.");
                    /**
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Couldn't get json from server. Check LogCat for possible errors!",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                     */
                }

            }
        });
    }
    private void generate() {
        Button generate = findViewById(R.id.Generate);
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView widowB = findViewById(R.id.WidowB);
                final TextView dimplesB = findViewById(R.id.DimplesB);
                final TextView earlobesB = findViewById(R.id.EarlobesB);
                final TextView frecklesB = findViewById(R.id.FrecklesB);
                final TextView brownB = findViewById(R.id.BrownB);
                final TextView greenB = findViewById(R.id.GreenB);
                final TextView blueB = findViewById(R.id.BlueB);
                final Switch widowF = findViewById(R.id.WidowF);
                final Switch dimplesF = findViewById(R.id.DimplesF);
                final Switch earlobesF = findViewById(R.id.EarlobesF);
                final Switch frecklesF = findViewById(R.id.FrecklesF);
                final CheckBox brownF = findViewById(R.id.BrownF);
                final CheckBox greenF = findViewById(R.id.GreenF);
                //final CheckBox blueF = (CheckBox) findViewById(R.id.BlueF);
                final Switch widowM = findViewById(R.id.WidowM);
                final Switch dimplesM = findViewById(R.id.DimplesM);
                final Switch earlobesM = findViewById(R.id.EarlobesM);
                final Switch frecklesM = findViewById(R.id.FrecklesM);
                final CheckBox brownM = findViewById(R.id.BrownM);
                final CheckBox greenM = findViewById(R.id.GreenM);
                //final CheckBox blueM = (CheckBox) findViewById(R.id.BlueM);2
                String fatherEyeColor;
                String motherEyeColor;
                if (brownF.isChecked()) {
                    fatherEyeColor = "brown";
                } else if (greenF.isChecked()) {
                    fatherEyeColor = "green";
                } else {
                    fatherEyeColor = "blue";
                }
                if (brownM.isChecked()) {
                    motherEyeColor = "brown";
                } else if (greenM.isChecked()) {
                    motherEyeColor = "green";
                } else {
                    motherEyeColor = "blue";
                }
                final String[] eyeColor = eyeColor(fatherEyeColor, motherEyeColor);
                final String widowBaby = "Widow's Peak: " + percentage(widowF.isChecked(), widowM.isChecked());
                final String dimplesBaby = "Dimples: " + percentage(dimplesF.isChecked(), dimplesM.isChecked());
                final String earlobesBaby = "Free earlobes: " + percentage(earlobesF.isChecked(), earlobesM.isChecked());
                final String frecklesBaby = "Freckles: " + percentage(frecklesF.isChecked(), frecklesM.isChecked());
                final String brownBaby = "Brown: " + eyeColor[0];
                final String greenBaby = "Green: " + eyeColor[1];
                final String blueBaby = "Blue: " + eyeColor[2];
                widowB.setText(widowBaby);
                dimplesB.setText(dimplesBaby);
                earlobesB.setText(earlobesBaby);
                frecklesB.setText(frecklesBaby);
                brownB.setText(brownBaby);
                greenB.setText(greenBaby);
                blueB.setText(blueBaby);
            }
        });
    }
    private String percentage(final boolean fatherDominant, final boolean motherDominant) {
        if (fatherDominant && motherDominant) {
            return "93.75%";
        }
        if (!fatherDominant && !motherDominant) {
            return "0.00%";
        }
        return "75.00%";
    }
    private String[] eyeColor(String fatherColor, String motherColor) {
        String[] eyeColor = new String[3];
        if (fatherColor.equals("brown") && motherColor.equals("brown")) {
            eyeColor[0] = "93.75%";
            eyeColor[1] = "4.6875%";
            eyeColor[2] = "1.5625%";
        } else if (fatherColor.equals("brown") || motherColor.equals("brown")) {
            eyeColor[0] = "75.00%";
            if (fatherColor.equals("green") || motherColor.equals("green")) {
                eyeColor[1] = "21.875%";
                eyeColor[2] = "3.125%";
            } else {
                eyeColor[1] = "12.50%";
                eyeColor[2] = "12.50%";
            }
        } else {
            eyeColor[0] = "0.00%";
            if (fatherColor.equals("green") && motherColor.equals("green")) {
                eyeColor[1] = "93.75%";
                eyeColor[2] = "6.25%";
            } else if (fatherColor.equals("blue") && motherColor.equals("blue")) {
                eyeColor[1] = "0.00%";
                eyeColor[2] = "100.00%";
            } else {
                eyeColor[1] = "75.00%";
                eyeColor[2] = "25.00%";
            }
        }
        return eyeColor;
    }
}

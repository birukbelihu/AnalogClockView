package com.biruk.analogclockview;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;

public class MainActivity extends Activity {

    private AnalogClockView analogclockview1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        analogclockview1 = findViewById(R.id.analogClockView1);

        // Set custom numerals (Ethiopic)
        String[] customNumerals = {"፩", "፪", "፫", "፬", "፭", "፮", "፯", "፰", "፱", "፲", "፲፩", "፲፪"};
        analogclockview1.setNumerals(customNumerals);

        // Customize clock appearance
        analogclockview1.setClockFaceColor(Color.DKGRAY);        // Clock outline color
        analogclockview1.setNumeralColor(Color.BLUE);            // Numeral color
        analogclockview1.setFontSize(48);                        // Font size of numerals
        analogclockview1.set24HourFormat(false);                 // 12-hour format

        // Set custom hand colors and thickness
        analogclockview1.setHourHandColor(Color.BLACK);
        analogclockview1.setMinuteHandColor(Color.GREEN);
        analogclockview1.setSecondHandColor(Color.RED);

        analogclockview1.setHourHandThickness(12f);
        analogclockview1.setMinuteHandThickness(10f);
        analogclockview1.setSecondHandThickness(4f);

        // Set custom font (optional, assuming you have it in res/font)
        //Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/your_font.ttf");
        //analogclockview1.setNumeralFont(customFont);
    }
}

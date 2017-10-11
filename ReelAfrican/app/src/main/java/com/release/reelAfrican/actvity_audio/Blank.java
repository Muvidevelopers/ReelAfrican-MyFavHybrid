package com.release.reelAfrican.actvity_audio;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.release.reelAfrican.R;
import com.release.reelAfrican.activity.MainActivity;
import com.release.reelAfrican.utils.Util;

public class Blank extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);


        if(Util.MianActivityDestoryed)
        {

            Util.MianActivityDestoryed = false;

            Intent startIntent = new Intent(Blank.this, MainActivity.class);
            startActivity(startIntent);
            finish();
        }
        else
        {
            Util.MianActivityDestoryed = false;
            finish();
        }



    }
}

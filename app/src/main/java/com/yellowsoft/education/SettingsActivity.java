package com.yellowsoft.education;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class SettingsActivity extends RootActivity {
        TextView edit_profile,change_academics,change_sub,lang,logout,change_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Session.forceRTLIfSupported(this);
        setContentView(R.layout.settings_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        edit_profile = (TextView) findViewById(R.id.edit_profile);
        edit_profile.setText(Session.getword(this, "Edit_profile"));
        change_academics = (TextView) findViewById(R.id.change_academics);
        change_academics.setText(Session.getword(this,"change_academics"));
        change_sub = (TextView) findViewById(R.id.change_subjects);
        change_sub.setText(Session.getword(this,"change_subjects"));
        change_pass = (TextView) findViewById(R.id.change_pass);
        change_pass.setText(Session.getword(this,"change_password"));
        lang = (TextView) findViewById(R.id.language);
        lang.setText(Session.getword(this,"language"));
        logout = (TextView) findViewById(R.id.logout);
        logout.setText(Session.getword(this,"logout"));

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this,SignupActivity.class);
                intent.putExtra("type","change");
                startActivity(intent);
            }
        });
        change_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this,SelectSubjectsActivity.class);
                intent.putExtra("type","change");
                startActivity(intent);
            }
        });
        change_academics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this,ChooseLevelGradeSemActivity.class);
                intent.putExtra("type","change");
                startActivity(intent);
            }
        });
        change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this,ChangePasswordActivity.class);
                intent.putExtra("type","change");
                startActivity(intent);
            }
        });
        lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_alert();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session.setUserid(SettingsActivity.this,"-1","name");
                Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }
    ArrayList<String> langs;
    public void show_alert(){
        langs = new ArrayList<>();
        langs.add("English");
        langs.add("Arabic");
        AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
        alert1.setTitle(Session.getword(this,"choose_lang"));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, langs);
        alert1.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    Session.set_user_language(SettingsActivity.this, "en");
                    Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                } else {
                    Session.set_user_language(SettingsActivity.this, "ar");
                   Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                }
            }

        });
        final AlertDialog dialog = alert1.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

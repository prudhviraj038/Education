package com.techbox.education;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.gcm.GCMRegistrar;

import org.json.JSONObject;

import static com.techbox.education.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.techbox.education.CommonUtilities.EXTRA_MESSAGE;
import static com.techbox.education.CommonUtilities.SENDER_ID;

public class SplashScreen extends RootActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1000;
    AlertDialogManager alert = new AlertDialogManager();
    AsyncTask<Void, Void, Void> mRegisterTask;
    ConnectionDetector cd;
    public static String name="education";
    public static String email="education";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        cd = new ConnectionDetector(getApplicationContext());
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(SplashScreen.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", true,null);
            // stop executing code by return
            Intent i = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(i);
            finish();
        }
       // GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);
        registerReceiver(mHandleMessageReceiver, new IntentFilter(
                DISPLAY_MESSAGE_ACTION));
        final String regId = GCMRegistrar.getRegistrationId(this);
        if (regId.equals("")) {
            // Registration is not present, register now with GCM
            GCMRegistrar.register(this, SENDER_ID);
            Intent i = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(i);
            finish();

        } else {
            // Device is already registered on GCM
            if (GCMRegistrar.isRegisteredOnServer(this) && false) {
                // Skips registration.
                //Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                 startActivity(i);
                finish();
            } else {
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.
                final Context context = this;
                mRegisterTask = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        // Register on our server
                        // On server creates a new user
                        ServerUtilities.register(context, name, email, regId);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                        Intent i = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }

                };
                mRegisterTask.execute(null, null, null);
               // Intent i = new Intent(SplashScreen.this, MainActivity.class);
                //startActivity(i);
                //finish();
            }
        }

    }


    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());
            /**
             * Take appropriate action on this message
             * depending upon your app requirement
             * For now i am just displaying it on the screen
             * */

            // Showing received message
            //Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();

            // Releasing wake lock
            WakeLocker.release();
        }
    };

    @Override
    protected void onDestroy() {
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
          //  Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }




}

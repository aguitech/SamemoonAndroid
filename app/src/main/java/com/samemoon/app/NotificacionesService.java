package com.samemoon.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hectoraguilar on 28/11/16.
 */

public class NotificacionesService extends Service {

    private Context context;
    private Context mContext;
    private int NOTIFICATION_ID = 1;
    //private Notification mNotification;
    private NotificationManager mNotificationManager;

    Handler handler = new Handler();
    /*
    private int NOTIFICATION_ID = 1;
    private Notification mNotification;
    private NotificationManager mNotificationManager;
    private Context mContext;
    */

    int mStartMode;       // indicates how to behave if the service is killed
    IBinder mBinder;      // interface for clients that bind
    boolean mAllowRebind; // indicates whether onRebind should be used

    NotificationManager notificationManager;
    Notification mNotification;
    PendingIntent mPendingIntent;

    public static final String idu = "idu";
    public static final String MyPREFERENCES = "MyPrefs";

    private int valueID = 1;

    private Timer timer = new Timer();

    SharedPreferences sharedpreferences;


    private String _url;

    @Override
    public void onCreate() {

        context = this;

        //createNotification(c, b, ExampleService.this);

        new Timer().scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run() {
                //method(); // call your method
                Log.d("ahola", "hola");
                //_url = "http://hyperion.init-code.com/zungu/app/vt_get_notificaciones_pendientes.php?idv=" + valueID;
                //_url = "http://hyperion.init-code.com/zungu/app/vt_get_notificaciones_pendientes.php?idv=" + valueID;
                _url = "http://aguitech.com/samemoon/cobradores/android_get_notificaciones.php?idv=" + valueID;
                //_url = "http://hyperion.init-code.com/zungu/app/vt_get_notificaciones_pendientes.php";
                new NotificacionesService.RetrieveFeedTask().execute();
            }
        }, 0, 30000);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        valueID = sharedpreferences.getInt("idu", 0);



        /*

        // The service is being created

        // Sleep for 5 seconds
        //Thread.sleep(5*1000);



        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);



        //Intent intent = new Intent("com.viralandroid.androidpushnotificationtutorial");
        //Intent intent = new Intent("init_code.hyperion.zunguveterinarios");
        Intent intent = new Intent(this, Notificaciones.class);
        mPendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, 0);
        Notification.Builder mBuilder = new Notification.Builder(getApplicationContext());


         //Vibration
         //builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

         //LED
         //builder.setLights(Color.RED, 3000, 3000);

         //Ton
         //builder.setSound(Uri.parse("uri://sadfasdfasdf.mp3"));

        mBuilder.setLights(Color.RED, 3000, 3000);

        mBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

        //mBuilder.setAutoCancel(false);
        mBuilder.setAutoCancel(true);
        mBuilder.setContentTitle("6Notificación Zungu Veterinarios");
        mBuilder.setTicker("ticker text here");
        mBuilder.setContentText("Has recibido una notificación");
        //mBuilder.setSmallIcon(R.drawable.ic_launcher);
        mBuilder.setSmallIcon(R.drawable.circle_image);
        mBuilder.setSound(soundUri);
        mBuilder.setContentIntent(mPendingIntent);
        //mBuilder.setOngoing(true);
        //DISMISS WITH SWIPE HA
        mBuilder.setOngoing(false);
        //API level 16
        mBuilder.setSubText("Haz click para mostrar tus notificaciones");
        mBuilder.setNumber(150);
        mBuilder.build();
        mNotification = mBuilder.getNotification();
        //notificationManager.notify(11, mNotification);
        notificationManager.notify(1, mNotification);

        */







    }





    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {
/*
        private Context mContext;
        //private int NOTIFICATION_ID = 1;
        private Notification mNotification;
        private NotificationManager mNotificationManager;
*/

        private Context mContext;

        private Exception exception;

        protected void onPreExecute() {
        }

        protected String doInBackground(Void... urls) {
            try {
                Log.i("INFO url: ", _url);
                URL url = new URL(_url);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }


        protected void onPostExecute(String response) {
            if (response == null) {
                response = "THERE WAS AN ERROR";
            } else {

                try {
                    JSONTokener tokener = new JSONTokener(response);
                    JSONArray arr = new JSONArray(tokener);


                    /*

                    notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);



                    //Intent intent = new Intent("com.viralandroid.androidpushnotificationtutorial");
                    //Intent intent = new Intent("init_code.hyperion.zunguveterinarios");
                    //Intent intent = new Intent(this, Notificaciones.class);
                    Intent intent = new Intent(context, Notificaciones.class);
                    mPendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, 0);
                    Notification.Builder mBuilder = new Notification.Builder(getApplicationContext());


                    //Vibration
                    //builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

                    //LED
                    //builder.setLights(Color.RED, 3000, 3000);

                    //Ton
                    //builder.setSound(Uri.parse("uri://sadfasdfasdf.mp3"));

                    mBuilder.setLights(Color.RED, 3000, 3000);

                    mBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

                    //mBuilder.setAutoCancel(false);
                    mBuilder.setAutoCancel(true);
                    mBuilder.setContentTitle("6Notificación Zungu Veterinarios");
                    mBuilder.setTicker("ticker text here");
                    mBuilder.setContentText("Has recibido una notificación");
                    //mBuilder.setSmallIcon(R.drawable.ic_launcher);
                    mBuilder.setSmallIcon(R.drawable.circle_image);
                    mBuilder.setSound(soundUri);
                    mBuilder.setContentIntent(mPendingIntent);
                    //mBuilder.setOngoing(true);
                    //DISMISS WITH SWIPE HA
                    mBuilder.setOngoing(false);
                    //API level 16
                    mBuilder.setSubText("Haz click para mostrar tus notificaciones");
                    mBuilder.setNumber(150);
                    mBuilder.build();
                    mNotification = mBuilder.getNotification();
                    //notificationManager.notify(11, mNotification);
                    notificationManager.notify(1, mNotification);

                    */

/*

                    notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);



                    //Intent intent = new Intent("com.viralandroid.androidpushnotificationtutorial");
                    //Intent intent = new Intent("init_code.hyperion.zunguveterinarios");
                    Intent intent = new Intent(context, Notificaciones.class);
                    mPendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, 0);
                    Notification.Builder mBuilder = new Notification.Builder(getApplicationContext());


                    //Vibration
                    //builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

                    //LED
                    //builder.setLights(Color.RED, 3000, 3000);

                    //Ton
                    //builder.setSound(Uri.parse("uri://sadfasdfasdf.mp3"));

                    mBuilder.setLights(Color.RED, 3000, 3000);

                    mBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

                    //mBuilder.setAutoCancel(false);
                    mBuilder.setAutoCancel(true);
                    mBuilder.setContentTitle("XXNotificación Zungu Veterinarios");
                    mBuilder.setTicker("ticker text here");
                    mBuilder.setContentText("Has recibido una notificación");
                    //mBuilder.setSmallIcon(R.drawable.ic_launcher);
                    mBuilder.setSmallIcon(R.drawable.circle_image);
                    mBuilder.setSound(soundUri);
                    mBuilder.setContentIntent(mPendingIntent);
                    //mBuilder.setOngoing(true);
                    //DISMISS WITH SWIPE HA
                    mBuilder.setOngoing(false);
                    //API level 16
                    mBuilder.setSubText("Haz click para mostrar tus notificaciones");
                    mBuilder.setNumber(150);
                    mBuilder.build();
                    mNotification = mBuilder.getNotification();
                    //notificationManager.notify(11, mNotification);
                    //notificationManager.notify(1, mNotification);
                    notificationManager.notify(1000, mNotification);
*/
                    /*
                    notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);



                    //Intent intent = new Intent("com.viralandroid.androidpushnotificationtutorial");
                    //Intent intent = new Intent("init_code.hyperion.zunguveterinarios");
                    Intent intent = new Intent(context, Notificaciones.class);
                    mPendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, 0);
                    Notification.Builder mBuilder = new Notification.Builder(getApplicationContext());


                    //Vibration
                    //builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

                    //LED
                    //builder.setLights(Color.RED, 3000, 3000);

                    //Ton
                    //builder.setSound(Uri.parse("uri://sadfasdfasdf.mp3"));

                    mBuilder.setLights(Color.RED, 3000, 3000);

                    mBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

                    //mBuilder.setAutoCancel(false);
                    mBuilder.setAutoCancel(true);
                    mBuilder.setContentTitle("XXNotificación Zungu Veterinarios");
                    mBuilder.setTicker("ticker text here");
                    mBuilder.setContentText("Has recibido una notificación");
                    //mBuilder.setSmallIcon(R.drawable.ic_launcher);
                    mBuilder.setSmallIcon(R.drawable.circle_image);
                    mBuilder.setSound(soundUri);
                    mBuilder.setContentIntent(mPendingIntent);
                    //mBuilder.setOngoing(true);
                    //DISMISS WITH SWIPE HA
                    mBuilder.setOngoing(false);
                    //API level 16
                    mBuilder.setSubText("Haz click para mostrar tus notificaciones");
                    mBuilder.setNumber(150);
                    mBuilder.build();
                    mNotification = mBuilder.getNotification();
                    //notificationManager.notify(11, mNotification);
                    //notificationManager.notify(1, mNotification);
                    notificationManager.notify(1000, mNotification);
                    */


                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject jsonobject = arr.getJSONObject(i);

                        //int NOTIFICATION_ID = 1;

                        //sendNotification()
                        //createNotification(c, b, mContext);
                        //createNotification(c, b, MainActivity.this);


                        //createNotification(c, b, ExampleService.this);


                        //listaContenidoServicios.add("El veterinario: " + jsonobject.getString("nombre") + " desea agregarlo a su red.");
                        //listaImagenServicios.add(jsonobject.getString("foto"));
                        //showMsg(jsonobject.getString("id_redveterinario"));


                        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);



                        //Intent intent = new Intent("com.viralandroid.androidpushnotificationtutorial");
                        //Intent intent = new Intent("init_code.hyperion.zunguveterinarios");
                        //Intent intent = new Intent(context, Notificaciones.class);
                        Intent intent = new Intent(context, Detalle_evento.class);

                        //intent.putExtra("id_evento", true);
                        intent.putExtra("idevento", jsonobject.getString("id_evento"));
                        mPendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, 0);
                        //ORIGINAL CODE
                        //Notification.Builder mBuilder = new Notification.Builder(getApplicationContext());

                        //PARA MOSTRAR COLOR
                        //NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());

                        //Vibration
                        //builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

                        //LED
                        //builder.setLights(Color.RED, 3000, 3000);

                        //Ton
                        //builder.setSound(Uri.parse("uri://sadfasdfasdf.mp3"));

                        mBuilder.setLights(Color.RED, 3000, 3000);

                        mBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

                        //mBuilder.setAutoCancel(false);
                        mBuilder.setAutoCancel(true);
                        mBuilder.setContentTitle("Notificación Samemoon");
                        mBuilder.setTicker("ticker text here");
                        //mBuilder.setContentText(jsonobject.getString("notificacion"));
                        mBuilder.setContentText(jsonobject.getString("evento"));
                        //mBuilder.setContentText("dssad");
                        //mBuilder.setSmallIcon(R.drawable.ic_launcher);
                        //mBuilder.setSmallIcon(R.drawable.circle_image);

                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            //mBuilder.setSmallIcon(R.drawable.icon_transperent);
                            //mBuilder.setSmallIcon(R.drawable.circle_image);
                            mBuilder.setSmallIcon(R.drawable.icon);
                        } else {
                            //mBuilder.setSmallIcon(R.drawable.icon);
                            //mBuilder.setSmallIcon(R.drawable.icon);
                            mBuilder.setSmallIcon(R.drawable.circle_image);
                        }


                        mBuilder.setSound(soundUri);
                        mBuilder.setContentIntent(mPendingIntent);
                        //mBuilder.setOngoing(true);
                        //DISMISS WITH SWIPE HA
                        mBuilder.setOngoing(false);
                        //API level 16
                        mBuilder.setSubText("Haz click para mostrar tus notificaciones");
                        mBuilder.setNumber(150);
                        mBuilder.build();
                        mNotification = mBuilder.getNotification();
                        //notificationManager.notify(11, mNotification);
                        //notificationManager.notify(1, mNotification);
                        notificationManager.notify(i + 100, mNotification);

/*
                        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);





                        //Intent intent = new Intent("com.viralandroid.androidpushnotificationtutorial");
                        //Intent intent = new Intent("init_code.hyperion.zunguveterinarios");


                        //Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        //Intent intent = new Intent(context, Notificaciones.class);
                        mPendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, 0);
                        //Notification.Builder mBuilder = new Notification.Builder(getApplicationContext());


                        //Vibration
                        //builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

                        //LED
                        //builder.setLights(Color.RED, 3000, 3000);

                        //Ton
                        //builder.setSound(Uri.parse("uri://sadfasdfasdf.mp3"));

                        mBuilder.setLights(Color.RED, 3000, 3000);

                        mBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

                        //mBuilder.setAutoCancel(false);
                        mBuilder.setAutoCancel(true);
                        mBuilder.setContentTitle("6Notificación Zungu Veterinarios" + jsonobject.getString("nombre"));
                        mBuilder.setTicker("ticker text here");
                        mBuilder.setContentText("Has recibido una notificación");
                        //mBuilder.setSmallIcon(R.drawable.ic_launcher);
                        mBuilder.setSmallIcon(R.drawable.circle_image);
                        mBuilder.setSound(soundUri);
                        mBuilder.setContentIntent(mPendingIntent);
                        //mBuilder.setOngoing(true);
                        //DISMISS WITH SWIPE HA
                        mBuilder.setOngoing(false);
                        //API level 16
                        mBuilder.setSubText("Haz click para mostrar tus notificaciones");
                        mBuilder.setNumber(150);
                        mBuilder.build();
                        mNotification = mBuilder.getNotification();
                        //notificationManager.notify(11, mNotification);
                        //notificationManager.notify(1, mNotification);
                        notificationManager.notify(i, mNotification);

*/



                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            Log.i("INFO", response);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void createNotification(String contentTitle, String contentText, Context context) {

        mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        //Build the notification using Notification.Builder
        Notification.Builder builder = new Notification.Builder(mContext)
                //.setSmallIcon(android.R.drawable.stat_sys_download)
                //.setSmallIcon(android.R.drawable.stat_sys_download)
                .setSmallIcon(R.drawable.circle_image)
                .setAutoCancel(true)
                .setContentTitle(contentTitle)
                .setContentText(contentText);


        //Show the notification
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void showMsg(CharSequence text){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // The service is starting, due to a call to startService()





        return mStartMode;
    }
    @Override
    public IBinder onBind(Intent intent) {
        // A client is binding to the service with bindService()




        return mBinder;
    }
    @Override
    public boolean onUnbind(Intent intent) {
        // All clients have unbound with unbindService()






        return mAllowRebind;
    }
    @Override
    public void onRebind(Intent intent) {
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called




    }
    @Override
    public void onDestroy() {
        // The service is no longer used and is being destroyed





    }
}
package com.samemoon.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.samemoon.app.adapters.NotificacionesAdapter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Notificaciones extends AppCompatActivity {

    ListView lv;
    //EditText buscador;
    Context context;
    private String _url;
    private String _urlNotificaciones;

    public static ArrayList<String> listaContenidoNotificaciones = new ArrayList<String>();
    public static ArrayList<String> listaImagenNotificaciones = new ArrayList<String>();
    public static ArrayList<String> listaIdNotificaion = new ArrayList<String>();

    public Notificaciones mActivity = this;
    public NotificacionesAdapter _mascotasAdapter;

    public static final String idu = "idu";
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    private int valueID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        valueID = sharedpreferences.getInt("idu", 0);

        //_url = "http://hyperion.init-code.com/zungu/app/vt_get_notificaciones.php";
        _url = "http://hyperion.init-code.com/zungu/app/vt_get_notificaciones.php?idv=" + valueID;
        new Notificaciones.RetrieveFeedTask().execute();

        _urlNotificaciones = "http://hyperion.init-code.com/zungu/app/vt_editar_numero_notificaciones.php?idv=" + valueID;
        new Notificaciones.RetrieveFeedTaskNotificaciones().execute();

        lv = (ListView) findViewById(R.id.list_notificaciones);
        _mascotasAdapter = new NotificacionesAdapter(mActivity,valueID,listaContenidoNotificaciones,listaImagenNotificaciones,listaIdNotificaion);
    }

    public void goMenu(View v){
        Intent i = new Intent(Notificaciones.this, Menu.class);
        startActivity(i);
    }
    public void goNotificaciones(View v){
        Intent i = new Intent(Notificaciones.this, Notificaciones.class);
        startActivity(i);
    }
    public void goSolicitudes(View v){
        //DESCOMENTAR
        //Intent i = new Intent(Notificaciones.this, Solicitudes.class);
        //startActivity(i);
    }
    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

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
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            } else {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                try {
                    JSONTokener tokener = new JSONTokener(response);
                    JSONArray arr = new JSONArray(tokener);

                    listaContenidoNotificaciones.clear();
                    listaImagenNotificaciones.clear();
                    listaIdNotificaion.clear();

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject jsonobject = arr.getJSONObject(i);
                        //Log.d("nombre",jsonobject.getString("nombre"));

                        listaContenidoNotificaciones.add(jsonobject.getString("notificacion"));
                        //listaImagenNotificaciones.add("icon");
                        //listaImagenNotificaciones.add("icon");
                        listaImagenNotificaciones.add(jsonobject.getString("id_notificacion"));
                        //listaIdNotificaion.add(jsonobject.getString("id_notificacion"));
                        listaIdNotificaion.add(jsonobject.getString("fecha"));
                    }

                    _mascotasAdapter = new NotificacionesAdapter(mActivity,valueID,listaContenidoNotificaciones, listaImagenNotificaciones, listaIdNotificaion);
                    lv.setAdapter(_mascotasAdapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Log.i("INFO", response);
        }
    }
    class RetrieveFeedTaskNotificaciones extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
        }

        protected String doInBackground(Void... urls) {
            try {
                Log.i("INFO url: ", _urlNotificaciones);
                URL url = new URL(_urlNotificaciones);
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
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            } else {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                try {
                    JSONTokener tokener = new JSONTokener(response);
                    JSONArray arr = new JSONArray(tokener);

                    TextView numeroNotificaciones = (TextView) findViewById(R.id.numeroNotificaciones);


                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject jsonobject = arr.getJSONObject(i);
                        //Log.d("nombre",jsonobject.getString("nombre"));

                        //"":"45","":"9","":"0","fecha":"2016-11-19 15:59:00","":"","acepto":"0","":"","":"1","id_pago":"0","id_veterinaria":"0"},

                        if(jsonobject.getString("numero_notificaciones").equals("")){
                            numeroNotificaciones.setVisibility(View.GONE);
                        }else{
                            numeroNotificaciones.setVisibility(View.VISIBLE);
                            numeroNotificaciones.setText(jsonobject.getString("numero_notificaciones"));
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Log.i("INFO", response);
        }
    }
}

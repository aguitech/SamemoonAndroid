package com.samemoon.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.samemoon.app.adapters.CitasHoyAdapter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Citas extends AppCompatActivity {

    ListView lv;
    Context context;
    private String _url;

    Date date_hoy;
    Date date_seleccionada;

    public static ArrayList<String> listaIdCita = new ArrayList<String>();
    public static ArrayList<String> listaFechaDias = new ArrayList<String>();
    public static ArrayList<String> listaHora = new ArrayList<String>();
    public static ArrayList<String> listaMotivo = new ArrayList<String>();
    public static ArrayList<String> listaIdMotivo = new ArrayList<String>();
    public static ArrayList<String> listaIdUsuario = new ArrayList<String>();
    public static ArrayList<String> listaNombreUsuario = new ArrayList<String>();

    public Citas mActivity = this;
    public CitasHoyAdapter _mascotasAdapter;

    public static final String idu = "idu";
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    private int valueID = 0;

    private String _urlNotificaciones;

    String fechaValor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citas);

        lv = (ListView)findViewById(R.id.list_cita);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        valueID = sharedpreferences.getInt("idu", 0);

        //showMsg(String.valueOf(valueID));
        listaIdCita.clear();
        listaFechaDias.clear();
        listaHora.clear();
        listaMotivo.clear();
        listaIdMotivo.clear();
        listaIdUsuario.clear();
        listaNombreUsuario.clear();

        _mascotasAdapter = new CitasHoyAdapter(mActivity, listaIdCita, listaFechaDias, listaHora, listaMotivo, listaIdMotivo, listaIdUsuario, listaNombreUsuario);
        lv.setAdapter(_mascotasAdapter);

        Button btnFechaValor = (Button) findViewById(R.id.btnFechaValor);


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                //fechaValor = null;
                fechaValor = "";
                _url = "http://hyperion.init-code.com/zungu/app/vt_get_solicitudes_citas_dia.php?idv=" + valueID;
                new Citas.RetrieveFeedTask().execute();
            } else {
                fechaValor = extras.getString("valor_fecha");
                //showMsg(fechaValor);
                _url = "http://hyperion.init-code.com/zungu/app/vt_get_solicitudes_citas_dia.php?idv=" + valueID + "&fecha=" + fechaValor;
                new Citas.RetrieveFeedTask().execute();
                btnFechaValor.setText(fechaValor);

            }
        }

        Date fecha_hoy = new Date();
        //System.out.println(fecha_hoy);
        String valor_fecha = new SimpleDateFormat("yyyy-MM-dd").format(fecha_hoy);

        String dtStart = valor_fecha;
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            //Date date_hoy = format.parse(dtStart);
            date_hoy = format.parse(dtStart);
            //System.out.println(date);
            //Log.d("valor Inicio", date_hoy.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(fechaValor.equals("")){

        }else{
            String dtFin = fechaValor;
            try {
                //Date date_seleccionada = format.parse(dtFin);
                date_seleccionada = format.parse(dtFin);
                //System.out.println(date);
                //Log.d("valor Fin", date_seleccionada.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(date_seleccionada.compareTo(date_hoy)>0){
                //ES MAYOR
                //showMsg("Test");
                //btnMesDia.setText(String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(dayOfMonth));
            }else if(date_seleccionada.compareTo(date_hoy)<0){
                //ES MENOR
                //showMsg("Test 2");
                //btnMesDia.setText(valor_fecha);
                TextView btnAgregarCita = (TextView) findViewById(R.id.btnAgregarCita);
                btnAgregarCita.setVisibility(View.GONE);
            }else{
                //ES IGUAL
                //showMsg("Test 3");
                //btnMesDia.setText(String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(dayOfMonth));

            }
        }


        _urlNotificaciones = "http://hyperion.init-code.com/zungu/app/vt_get_numero_notificaciones.php?idv=" + valueID;
        new Citas.RetrieveFeedTaskNotificaciones().execute();

        //_url = "http://hyperion.init-code.com/zungu/app/vt_get_solicitudes_citas_dia.php?idv=" + valueID;
        //new Citas.RetrieveFeedTask().execute();

    }
    private void showMsg(CharSequence text){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
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

                    listaIdCita.clear();
                    listaFechaDias.clear();
                    listaHora.clear();
                    listaMotivo.clear();
                    listaIdMotivo.clear();
                    listaIdUsuario.clear();
                    listaNombreUsuario.clear();

                    //int valor_long = arr.length();

                    //showMsg(String.valueOf(valor_long));

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject jsonobject = arr.getJSONObject(i);
                        //Log.d("nombre",jsonobject.getString("nombre"));

                        //"":"45","":"9","":"0","fecha":"2016-11-19 15:59:00","":"","acepto":"0","":"","":"1","id_pago":"0","id_veterinaria":"0"},

                        listaIdCita.add(jsonobject.getString("id_cita"));
                        listaFechaDias.add(jsonobject.getString("fecha_dias"));
                        //listaHora.add(jsonobject.getString("hora"));
                        //listaHora.add(jsonobject.getString("diferencia"));
                        listaHora.add(jsonobject.getString("hora"));
                        listaMotivo.add(jsonobject.getString("motivo"));
                        listaIdMotivo.add(jsonobject.getString("id_motivo"));
                        listaIdUsuario.add(jsonobject.getString("id_mascota"));
                        listaNombreUsuario.add(jsonobject.getString("nombre"));
                    }

                    _mascotasAdapter = new CitasHoyAdapter(mActivity, listaIdCita, listaFechaDias, listaHora, listaMotivo, listaIdMotivo, listaIdUsuario, listaNombreUsuario);
                    lv.setAdapter(_mascotasAdapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Log.i("INFO", response);
        }
    }

    public void goMenu(View v){
        Intent i = new Intent(Citas.this, Menu.class);
        startActivity(i);
    }
    public void goNotificaciones(View v){
        Intent i = new Intent(Citas.this, Notificaciones.class);
        startActivity(i);
    }

    public void goAgregar(View v){
        Intent i = new Intent(Citas.this, Agregar_cita.class);
        i.putExtra("valor_fecha", fechaValor);
        startActivity(i);
    }

    public void goSolicitudes(View v){
        //DESCOMENTAR
        //Intent i = new Intent(Citas.this, Citas_solicitudes.class);
        //startActivity(i);
    }

    public void goCalendario(View v){
        //DESCOMENTAR
        //Intent i = new Intent(Citas.this, Citas_calendario.class);
        //startActivity(i);
    }

    public void goBack(View v){
        finish();
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

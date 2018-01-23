package com.samemoon.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Codigo_canjeado_exitosamente extends AppCompatActivity {

    Context context;

    private String _urlCita;
    private String _urlGet;

    public static final String idu = "idu";
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";

    private String _urlNotificaciones;

    private String idCita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codigo_canjeado_exitosamente);

        context = this;

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        int valueID = sharedpreferences.getInt("idu", 0);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                idCita = null;
                _urlGet = "http://hyperion.init-code.com/zungu/app/vt_detalle_cita_last.php?id_veterinario=" + valueID;
                new Codigo_canjeado_exitosamente.RetrieveFeedTaskGet().execute();
            } else {
                idCita = extras.getString("id_cita");
                //showMsg(idCita);
                //_urlGet = "http://hyperion.init-code.com/zungu/app/vt_detalle_cita.php?id_cita=" + idCita;

                _urlGet = "http://hyperion.init-code.com/zungu/app/vt_detalle_cita_last.php?id_veterinario=" + valueID + "&id_cita=" + idCita;
                new Codigo_canjeado_exitosamente.RetrieveFeedTaskGet().execute();
            }
        }



        _urlNotificaciones = "http://hyperion.init-code.com/zungu/app/vt_get_numero_notificaciones.php?idv=" + valueID;
        new Codigo_canjeado_exitosamente.RetrieveFeedTaskNotificaciones().execute();
    }

    public void goBack(View v){
        finish();
    }

    public void goMenu(View v){
        Intent i = new Intent(Codigo_canjeado_exitosamente.this, Menu.class);
        startActivity(i);
    }
    public void goNotificaciones(View v){
        Intent i = new Intent(Codigo_canjeado_exitosamente.this, Notificaciones.class);
        startActivity(i);
    }

    public void goCitas(View v){
        Intent i = new Intent(Codigo_canjeado_exitosamente.this, Citas.class);
        startActivity(i);
    }

    public void goCancelar(View v){



        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(context);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("¿ Estas seguro que desea borrarlo ?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                int valueID = sharedpreferences.getInt("idu", 0);

                _urlCita = "http://hyperion.init-code.com/zungu/app/vt_cancelar_cita.php?id_veterinario=" + valueID;
                Log.d("url_cita", _urlCita);
                new Codigo_canjeado_exitosamente.RetrieveFeedTaskCita().execute();

                Toast.makeText(context, "Eliminado", Toast.LENGTH_SHORT).show();

                //DESCOMENTAR
                //Intent i = new Intent(Cita_generada_exitosamente.this, Citas_calendario.class);
                //startActivity(i);
            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {

            }
        });
        dialogo1.show();

    }


    private void showMsg(CharSequence text) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
    class RetrieveFeedTaskGet extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
        }

        protected String doInBackground(Void... urls) {
            try {
                Log.i("INFO url: ", _urlGet);
                URL url = new URL(_urlGet);
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

                TextView txtFechaHora = (TextView) findViewById(R.id.txtFechaHora);
                TextView txtMotivo = (TextView) findViewById(R.id.txtMotivo);
                TextView costoCita = (TextView) findViewById(R.id.costoCita);
                TextView txtVeterinario = (TextView) findViewById(R.id.txtVeterinario);


                try {

                    JSONObject object = (JSONObject) new JSONTokener(response).nextValue();

                    txtFechaHora.setText(object.getString("fecha_dias") + " " + object.getString("hora"));
                    txtMotivo.setText("Servicio: " + object.getString("nombre"));
                    costoCita.setText("$" +     object.getString("costo") + ".00");
                    txtVeterinario.setText("Veterinario: " + object.getString("veterinario"));



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            Log.i("INFO", response);
        }
    }
    class RetrieveFeedTaskCita extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
        }

        protected String doInBackground(Void... urls) {
            try {
                Log.i("INFO url: ", _urlCita);
                URL url = new URL(_urlCita);
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

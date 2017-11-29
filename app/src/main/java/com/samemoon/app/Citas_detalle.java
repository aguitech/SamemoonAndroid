package com.samemoon.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by hectoraguilar on 11/11/16.
 */

public class Citas_detalle extends AppCompatActivity {

    private String _url;
    private String _urlGet;
    private String _urlEditar;

    public static final String idu = "idu";
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    private String idCita;

    private String _urlNotificaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citas_detalle);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        int valueID = sharedpreferences.getInt("idu", 0);

        _urlNotificaciones = "http://hyperion.init-code.com/zungu/app/vt_get_numero_notificaciones.php?idv=" + valueID;
        new Citas_detalle.RetrieveFeedTaskNotificaciones().execute();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                idCita = null;
                _urlGet = "http://hyperion.init-code.com/zungu/app/vt_detalle_cita.php?id_cita=20";
                new Citas_detalle.RetrieveFeedTaskGet().execute();
            } else {
                idCita = extras.getString("id_cita");
                //showMsg(idCita);
                _urlGet = "http://hyperion.init-code.com/zungu/app/vt_detalle_cita.php?id_cita=" + idCita;
                new Citas_detalle.RetrieveFeedTaskGet().execute();
            }
        }

        //_urlGet = "http://hyperion.init-code.com/zungu/app/vt_obtener_cuenta.php?id_veterinario=" + String.valueOf(valueID);

    }

    public void aceptarCita(View v){
        //showMsg("test");

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        int valueID = sharedpreferences.getInt("idu", 0);

        _url = "http://hyperion.init-code.com/zungu/app/vt_aceptar_cita.php?id_cita=" + idCita + "&id_veterinario=" + valueID;
        Log.d("STR", _url);
        new Citas_detalle.RetrieveFeedTask().execute();

    }
    public void reagendarCita(View v){

        //DESCOMENTAR
        Intent i = new Intent(Citas_detalle.this, Reagendar_cita.class);
        i.putExtra("id_cita", idCita);
        startActivity(i);

        //showMsg("test");

        /*
        Intent i = new Intent(context, Citas_detalle.class);
        i.putExtra("id_cita", _listaIdCita.get(pos));
        context.startActivity(i);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        int valueID = sharedpreferences.getInt("idu", 0);

        _url = "http://hyperion.init-code.com/zungu/app/vt_aceptar_cita.php?id_cita=" + idCita + "&id_veterinario=" + valueID;
        Log.d("STR", _url);
        new Citas_detalle.RetrieveFeedTask().execute();
        */

    }
    public void goMenu(View v){
        Intent i = new Intent(Citas_detalle.this, Menu.class);
        startActivity(i);
    }
    public void goNotificaciones(View v){
        Intent i = new Intent(Citas_detalle.this, Notificaciones.class);
        startActivity(i);
    }

    public void goCitas(View v){
        Intent i = new Intent(Citas_detalle.this, Citas.class);
        startActivity(i);
    }

    public void goBack(View v){
        finish();
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

                TextView txtNombre = (TextView) findViewById(R.id.txtNombre);
                TextView txtFecha = (TextView) findViewById(R.id.txtFecha);
                TextView txtHora = (TextView) findViewById(R.id.txtHora);
                TextView txtMotivo = (TextView) findViewById(R.id.txtMotivo);
                TextView txtFormaPago = (TextView) findViewById(R.id.txtFormaPago);
                TextView txtStatus = (TextView) findViewById(R.id.txtStatus);

                TextView txtCorreo = (TextView) findViewById(R.id.txtCorreo);
                TextView txtTelefono = (TextView) findViewById(R.id.txtTelefono);

                final ImageView imgFoto = (ImageView) findViewById(R.id.imgPerfil);

                LinearLayout btnOk = (LinearLayout) findViewById(R.id.btnOk);

                LinearLayout btnReagendar = (LinearLayout) findViewById(R.id.btnReagendar);

                try {

                    JSONObject object = (JSONObject) new JSONTokener(response).nextValue();

                    if(object.getString("status").equals("1")){
                        //btnOk
                        //btnOk.setVisibility(View.INVISIBLE);
                        btnOk.setVisibility(View.GONE);
                        btnReagendar.setVisibility(View.GONE);
                    }
                    //btnReagendar.setVisibility(View.GONE);


                    txtNombre.setText(object.getString("nombre") + " " + object.getString("apellido"));
                    txtFecha.setText(object.getString("fecha_dias"));
                    txtHora.setText(object.getString("hora"));

                    txtCorreo.setText(object.getString("correo"));
                    txtTelefono.setText(object.getString("telefono"));

                    if(object.getString("motivo").equals("") || object.getString("motivo").length() < 1){
                        if(object.getString("id_motivo").equals("1")){
                            txtMotivo.setText("Revisión Mensual");
                        }
                        if(object.getString("id_motivo").equals("2")){
                            txtMotivo.setText("Servicio de estética");
                        }
                        if(object.getString("id_motivo").equals("3")){
                            txtMotivo.setText("Vacunación");
                        }
                        if(object.getString("id_motivo").equals("4")){
                            txtMotivo.setText("Cirugía mayor");
                        }
                        if(object.getString("id_motivo").equals("5")){
                            txtMotivo.setText(object.getString("motivo"));
                        }
                    }else{
                        txtMotivo.setText(object.getString("motivo"));
                    }



                    txtFormaPago.setText(object.getString("nombre"));
                    txtStatus.setText("Pagado " + object.getString("nombre"));


                    String _url = "http://hyperion.init-code.com/zungu/imagen_perfil/" + object.getString("foto");

                    Picasso.with(imgFoto.getContext()).load(_url)
                            .into(imgFoto, new Callback() {
                                @Override
                                public void onSuccess() {
                                    Bitmap imageBitmap = ((BitmapDrawable) imgFoto.getDrawable()).getBitmap();
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(imgFoto.getContext().getResources(), imageBitmap);
                                    circularBitmapDrawable.setCircular(true);
                                    imgFoto.setImageDrawable(circularBitmapDrawable);
                                }
                                @Override
                                public void onError() {
                                    //imgMascota.setImageResource(R.drawable.default_image);
                                }
                            });
                    /*
                    Picasso.with(imgPerfil.getContext()).load(_url).fit().centerCrop().into(imgPerfil);

                    Bitmap imageBitmap = ((BitmapDrawable) imgPerfil.getDrawable()).getBitmap();
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(imgPerfil.getContext().getResources(), imageBitmap);
                    circularBitmapDrawable.setCircular(true);
                    imgPerfil.setImageDrawable(circularBitmapDrawable);

                    /*
                    //Picasso.with(imgPerfil.getContext()).load(_url).fit().centerCrop().into(imgPerfil);
                    Picasso.with(imgPerfil.getContext()).load(_url)
                            .into(imgPerfil, new Callback() {
                                @Override
                                public void onSuccess() {
                                    Bitmap imageBitmap = ((BitmapDrawable) imgPerfil.getDrawable()).getBitmap();
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(imgPerfil.getContext().getResources(), imageBitmap);
                                    circularBitmapDrawable.setCircular(true);
                                    imgPerfil.setImageDrawable(circularBitmapDrawable);
                                }
                                @Override
                                public void onError() {

                                }
                            });

*/
/*
String _url = "http://hyperion.init-code.com/zungu/image_producto/" + foto;
                    if(foto.length() > 3){
                        Log.d("foto", _url);
                        Picasso.with(imgProducto.getContext()).load(_url).fit().centerCrop().into(imgProducto);
                    }

                    lblNombreCuenta.setText(object.getString("nombre_cuenta"));
                    lblInstitucionBancaria.setText(object.getString("institucion_bancaria"));
                    lblClabeInterbancaria.setText(object.getString("clabe_interbancaria"));
                    */


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            Log.i("INFO", response);
        }
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
                showMsg("Has aceptado la cita.");
                //DESCOMENTAR
                //Intent i = new Intent(Citas_detalle.this, Citas_solicitudes.class);
                //startActivity(i);
                /*
                showMsg("Se ha agregado la tarjeta.");

                try {

                    JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
                    int ID = object.getInt("id");
                    CharSequence text;

                    if(ID == 0){
                        text = "Usuario o password no válido.";
                    } else {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putInt(idu, ID);
                        editor.commit();
                        int value = sharedpreferences.getInt("idu", 0);
                        Log.i("IDU", Integer.toString(value));

                        text = "Bienvenido a Zungu veterinarios";
                    }

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                */
            }
            Log.i("INFO", response);
        }
    }
    class RetrieveFeedTaskEdit extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
        }

        protected String doInBackground(Void... urls) {
            try {
                Log.i("INFO url: ", _urlEditar);
                URL url = new URL(_urlEditar);
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
                /*
                EditText txtNombreProducto = (EditText) findViewById(R.id.txtNombreProducto);
                EditText txtNumeroUnidades = (EditText) findViewById(R.id.txtNumeroUnidades);
                EditText txtPrecioCompra = (EditText) findViewById(R.id.txtPrecioCompra);
                EditText txtPrecioVenta = (EditText) findViewById(R.id.txtPrecioVenta);
                EditText txtDescripcionProducto = (EditText) findViewById(R.id.txtDescripcionProducto);
                CheckBox checkBoxMostrar = (CheckBox) findViewById(R.id.checkBoxMostrar);
                ImageView imgProducto = (ImageView) findViewById(R.id.imgProducto);
*/

                try {
                    JSONObject object = (JSONObject) new JSONTokener(response).nextValue();


/*
                    txtNombreProducto.setText(object.getString("nombre"));
                    txtNumeroUnidades.setText(object.getString("numero_unidades"));
                    txtPrecioCompra.setText(object.getString("precio_compra"));
                    txtPrecioVenta.setText(object.getString("precio_venta"));
                    txtDescripcionProducto.setText(object.getString("descripcion"));
                    String foto = object.getString("foto");

                    */

                } catch (JSONException e) {
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

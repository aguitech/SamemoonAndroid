package com.samemoon.app;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Agregar_cita extends AppCompatActivity {

    private String _url;

    //final CharSequence[] items = {"Servicios Veterinarios", "Servicios Pet Friendly"};
    CharSequence[] items;
    public ArrayList<String> _clientes = new ArrayList<String>();
    public  ArrayList<Integer> _ids_cliente = new ArrayList<Integer>();

    public ArrayList<String> _mascotas = new ArrayList<String>();
    public  ArrayList<Integer> _ids_mascotas = new ArrayList<Integer>();

    public ArrayList<String> _servicios = new ArrayList<String>();
    public  ArrayList<Integer> _ids_servicios = new ArrayList<Integer>();

    public ArrayList<String> _veterinarios = new ArrayList<String>();
    public  ArrayList<Integer> _ids_veterinarios = new ArrayList<Integer>();

    public static final String idu = "idu";
    public static final String MyPREFERENCES = "MyPrefs";

    private int valueID;
    private String _urlEditar;
    private String _urlMascota;
    private String _urlServicios;
    private String _urlVeterinario;
    private String _urlNotificaciones;

    Date date_seleccionada;
    Date date_hoy;

    public int selImagen = 0;
    public int selCliente = 0;
    public int selMascota = 0;
    public int selServicio = 0;
    public int selVeterinario = 0;

    SimpleDateFormat df;
    long restr;

    SharedPreferences sharedpreferences;

    Button btnGuardar;

    String fechaValor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_cita);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        valueID = sharedpreferences.getInt("idu", 0);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);

        _urlEditar = "http://hyperion.init-code.com/zungu/app/vt_obtener_clientes.php?id_veterinario=" + valueID;
        new Agregar_cita.RetrieveFeedTaskEdit().execute();

       _urlServicios = "http://hyperion.init-code.com/zungu/app/vt_obtener_servicios.php?id_veterinario=" + valueID;
        new Agregar_cita.RetrieveFeedTaskServicio().execute();

        _urlVeterinario = "http://hyperion.init-code.com/zungu/app/vt_get_redveterinarios2.php?idv=" + valueID;
        new Agregar_cita.RetrieveFeedTaskVeterinario().execute();

        _urlNotificaciones = "http://hyperion.init-code.com/zungu/app/vt_get_numero_notificaciones.php?idv=" + valueID;
        new Agregar_cita.RetrieveFeedTaskNotificaciones().execute();

        df = new SimpleDateFormat("dd/MM/yyyy");
        String restringido = (DateFormat.format("dd/MM/yyyy", new Date()).toString());
        try {
            restr = df.parse(restringido.toString()).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        final Button btnMesDia = (Button) findViewById(R.id.btnMesDia);
        final Button btnHora = (Button) findViewById(R.id.btnHora);

        final Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);
        final int hour = c.get(Calendar.HOUR_OF_DAY);
        final int minute1 = c.get(Calendar.MINUTE);
        final Calendar v = Calendar.getInstance();
        final int year2 = v.get(Calendar.YEAR);
        final int month2 = v.get(Calendar.MONTH);
        final int day2 = v.get(Calendar.DAY_OF_MONTH);
        final int hour2 = v.get(Calendar.HOUR_OF_DAY);
        final int minute2 = v.get(Calendar.MINUTE);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                //fechaValor = null;
                fechaValor = "";
                btnMesDia.setText("Mes/Día");
            } else {
                fechaValor = extras.getString("valor_fecha");
                //showMsg(fechaValor);
                btnMesDia.setText(fechaValor);

            }
        }


        btnMesDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datepick = new DatePickerDialog(Agregar_cita.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        // Date results here
                        //showMsg(String.valueOf(day));
                        /*
                        showMsg(String.valueOf(year));
                        showMsg(String.valueOf(month));
                        showMsg(String.valueOf(dayOfMonth));
                        */



                        Date fecha_hoy = new Date();
                        //System.out.println(fecha_hoy);
                        String valor_fecha = new SimpleDateFormat("yyyy-MM-dd").format(fecha_hoy);

                        String fecha_completa = String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(dayOfMonth);


                        //String dtStart = "2010-10-15T09:27:37Z";
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

                        String dtFin = fecha_completa;
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
                            btnMesDia.setText(String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(dayOfMonth));
                        }else if(date_seleccionada.compareTo(date_hoy)<0){
                            //ES MENOR
                            //showMsg("Test 2");
                            btnMesDia.setText(valor_fecha);
                        }else{
                            //ES IGUAL
                            //showMsg("Test 3");
                            btnMesDia.setText(String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(dayOfMonth));
                        }


                        /*
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date convertedCurrentDate = sdf.parse("2013-09-18");

                        //SimpleDateFormat.parse(String);

                        String date=sdf.format(convertedCurrentDate );
                        System.out.println(date);
                        */


                        //DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);


                        //DateFormat formato = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        //DateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                        //Date valor_fecha_final = formato.parse(valor_fecha);

                        //Date valor_fecha_final = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(valor_fecha);
                        //Date valor_fecha_final = new SimpleDateFormat("yyyy-MM-dd").parse(valor_fecha);

                        //Date valor_fecha_final = valor_fecha;
/*
                        if(valor_fecha_final.compareTo(fecha_completa_final)){

                        }
*/

                        Log.d("res", valor_fecha);





                        //btnMesDia.setText(String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(dayOfMonth));



                        TimePickerDialog timepick = new TimePickerDialog(Agregar_cita.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                String minuto = "";

                                // Time results here
                                //showMsg(String.valueOf(hourOfDay));
                                //showMsg(String.valueOf(minute));

                                //if(minute.getText().toString().length() < 1){
                                if(String.valueOf(minute).toString().length() == 1){
                                    //showMsg(String.valueOf(minute));
                                    minuto = "0" + String.valueOf(minute).toString();
                                }else{
                                    minuto = String.valueOf(minute);
                                }

                                //btnHora.setText(String.valueOf(hourOfDay) + ":" + String.valueOf(minute));
                                //btnHora.setText(String.valueOf(hourOfDay) + ":" + minute);
                                btnHora.setText(String.valueOf(hourOfDay) + ":" + minuto);

                            }
                        }, hour, minute1, true);
                        timepick.setTitle("Selecciona Hora");
                        timepick.show();
                    }
                } ,year,month,day);
                datepick.getDatePicker().setMinDate(restr);
                datepick.setTitle("Selecciona Fecha");
                datepick.show();
            }
        });

        btnHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timepick = new TimePickerDialog(Agregar_cita.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        String minuto = "";

                        // Time results here
                        //showMsg(String.valueOf(hourOfDay));
                        //showMsg(String.valueOf(minute));

                        //if(minute.getText().toString().length() < 1){
                        if(String.valueOf(minute).toString().length() == 1){
                            //showMsg(String.valueOf(minute));
                            minuto = "0" + String.valueOf(minute).toString();
                        }else{
                            minuto = String.valueOf(minute);
                        }

                        //btnHora.setText(String.valueOf(hourOfDay) + ":" + String.valueOf(minute));
                        //btnHora.setText(String.valueOf(hourOfDay) + ":" + minute);
                        btnHora.setText(String.valueOf(hourOfDay) + ":" + minuto);

                    }
                }, hour, minute1, true);
                timepick.setTitle("Selecciona Hora");
                timepick.show();
            }
        });

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

                    _clientes.clear();
                    _ids_cliente.clear();

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject jsonobject = arr.getJSONObject(i);
                        //Log.d("nombre",jsonobject.getString("nombre"));

                        //_clientes.add(jsonobject.getString("nombre_usuario") + " - " + jsonobject.getString("nombre"));
                        _clientes.add(jsonobject.getString("nombre_usuario"));
                        //_ids_cliente.add(jsonobject.getInt("id_mascota"));
                        _ids_cliente.add(jsonobject.getInt("id_usuario"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Log.i("INFO", response);
        }
    }

    class RetrieveFeedTaskMascota extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
        }

        protected String doInBackground(Void... urls) {
            try {
                Log.i("INFO url: ", _urlMascota);
                URL url = new URL(_urlMascota);
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

                    _mascotas.clear();
                    _ids_mascotas.clear();

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject jsonobject = arr.getJSONObject(i);
                        Log.d("nombre",jsonobject.getString("nombre"));

                        //_clientes.add(jsonobject.getString("nombre_usuario") + " - " + jsonobject.getString("nombre"));
                        //_mascotas.add(jsonobject.getString("nombre_usuario") + " - " + jsonobject.getString("nombre"));
                        _mascotas.add(jsonobject.getString("nombre"));
                        //_ids_cliente.add(jsonobject.getInt("id_mascota"));
                        _ids_mascotas.add(jsonobject.getInt("id_mascota"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Log.i("INFO", response);
        }
    }

    class RetrieveFeedTaskServicio extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
        }

        protected String doInBackground(Void... urls) {
            try {
                Log.i("INFO url: ", _urlServicios);
                URL url = new URL(_urlServicios);
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

                    _servicios.clear();
                    _ids_servicios.clear();

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject jsonobject = arr.getJSONObject(i);
                        Log.d("nombre",jsonobject.getString("nombre"));

                        _servicios.add(jsonobject.getString("nombre") + " - " + jsonobject.getString("duracion") + " min.");
                        _ids_servicios.add(jsonobject.getInt("id_servicio_veterinario"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Log.i("INFO", response);
        }
    }

    class RetrieveFeedTaskVeterinario extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
        }

        protected String doInBackground(Void... urls) {
            try {
                Log.i("INFO url: ", _urlVeterinario);
                URL url = new URL(_urlVeterinario);
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

                    _veterinarios.clear();
                    _ids_veterinarios.clear();

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject jsonobject = arr.getJSONObject(i);
                        Log.d("nombre",jsonobject.getString("nombre"));

                        _veterinarios.add(jsonobject.getString("nombre"));
                        _ids_veterinarios.add(jsonobject.getInt("id_veterinario"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Log.i("INFO", response);
        }
    }



    public void showClientList(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        items = _clientes.toArray(new CharSequence[_clientes.size()]);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Button btnCliente = (Button)findViewById(R.id.btnCliente);
                btnCliente.setText(items[item]);
                selCliente = _ids_cliente.get(item); //En la variable selCliente esta guardado el id del cliente.
                Log.d("id_cliente", Integer.toString(selCliente));

                //_urlMascota = "http://hyperion.init-code.com/zungu/app/vt_obtener_clientes.php?id_veterinario=" + valueID + "&id_usuario=" + selCliente;
                //_urlMascota = "http://hyperion.init-code.com/zungu/app/vt_obtener_mascotas.php?id_veterinario=" + valueID + "&id_usuario=" + selCliente;

                Button btnMascota = (Button)findViewById(R.id.btnMascota);
                btnMascota.setText("Mascota");
                selMascota = 0; //En la variable selCliente esta guardado el id del cliente.

                _urlMascota = "http://hyperion.init-code.com/zungu/app/vt_obtener_mascotas.php?id_veterinario=" + valueID + "&id_usuario=" + selCliente;
                Log.d("url_mascota", _urlMascota);
                new Agregar_cita.RetrieveFeedTaskMascota().execute();

            }
        });

        AlertDialog alert = builder.create();

        ListView listView = alert.getListView();
        listView.setDivider(new ColorDrawable(Color.GRAY)); // set color
        listView.setDividerHeight(1);
        listView.setOverscrollFooter(new ColorDrawable(Color.TRANSPARENT));

        alert.show();


    }
    public void showMascotaList(View v){


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        items = _mascotas.toArray(new CharSequence[_mascotas.size()]);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Button btnMascota = (Button)findViewById(R.id.btnMascota);
                btnMascota.setText(items[item]);
                selMascota = _ids_mascotas.get(item); //En la variable selCliente esta guardado el id del cliente.
                Log.d("id_mascota", Integer.toString(selMascota));
            }
        });

        AlertDialog alert = builder.create();

        ListView listView = alert.getListView();
        listView.setDivider(new ColorDrawable(Color.GRAY)); // set color
        listView.setDividerHeight(1);
        listView.setOverscrollFooter(new ColorDrawable(Color.TRANSPARENT));

        alert.show();
    }

    public void showVeterinarioList(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        items = _veterinarios.toArray(new CharSequence[_veterinarios.size()]);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Button btnCliente = (Button)findViewById(R.id.btnVeterinario);
                btnCliente.setText(items[item]);
                selVeterinario = _ids_veterinarios.get(item); //En la variable selCliente esta guardado el id del cliente.
                Log.d("id_veterinario", Integer.toString(selVeterinario));
            }
        });

        AlertDialog alert = builder.create();

        ListView listView = alert.getListView();
        listView.setDivider(new ColorDrawable(Color.GRAY)); // set color
        listView.setDividerHeight(1);
        listView.setOverscrollFooter(new ColorDrawable(Color.TRANSPARENT));

        alert.show();

    }

    public void showServicioList(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        items = _servicios.toArray(new CharSequence[_servicios.size()]);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Button btnCliente = (Button)findViewById(R.id.btnServicio);
                btnCliente.setText(items[item]);
                selServicio = _ids_servicios.get(item); //En la variable selCliente esta guardado el id del cliente.
                Log.d("id_servicio", Integer.toString(selServicio));
            }
        });

        AlertDialog alert = builder.create();

        ListView listView = alert.getListView();
        listView.setDivider(new ColorDrawable(Color.GRAY)); // set color
        listView.setDividerHeight(1);
        listView.setOverscrollFooter(new ColorDrawable(Color.TRANSPARENT));

        alert.show();
    }

    private void showMsg(CharSequence text){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }



    public void goMenu(View v){
        Intent i = new Intent(Agregar_cita.this, Menu.class);
        startActivity(i);
    }
    public void goNotificaciones(View v){
        Intent i = new Intent(Agregar_cita.this, Notificaciones.class);
        startActivity(i);
    }

    public void goCitas(View v){
        Intent i = new Intent(Agregar_cita.this, Citas.class);
        startActivity(i);
    }

    public void changeDate(View v){

    }

    public void confirmar(View v){
        EditText txtMotivoCita = (EditText)findViewById(R.id.txtMotivoCita);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        int valueID = sharedpreferences.getInt("idu", 0);

        //EditText txtPrecioVenta = (EditText) findViewById(R.id.txtPrecioVenta);

        Button btnMesDia = (Button) findViewById(R.id.btnMesDia);
        Button btnHora = (Button) findViewById(R.id.btnHora);
        Button btnCliente = (Button) findViewById(R.id.btnCliente);


        //showMsg(btnMesDia.getText());
        //showMsg(btnHora.getText());
        //showMsg(btnCliente.getText());

        if(selCliente == 0){
            //showMsg("vale 0");
        }else{
            //showMsg("diferente de 0");
        }


        _url = "http://samemoon/cobradores/ios_agregar_evento.php?id_usuario=" + Integer.toString(selCliente) + "&idvh=" + Integer.toString(selVeterinario) + "&id_servicio=" + Integer.toString(selServicio) + "&motivo="+ URLEncoder.encode(txtMotivoCita.getText().toString()) + "&fecha=" + URLEncoder.encode(btnMesDia.getText().toString()) + "&hora=" + URLEncoder.encode(btnHora.getText().toString()) + "&id_veterinario=" + String.valueOf(valueID) + "&id_mascota=" + Integer.toString(selMascota);
        btnGuardar.setEnabled(false);
        new Agregar_cita.RetrieveFeedTask().execute();


        /*
        DESCOMENTAR VALIDACION

        if(txtMotivoCita.getText().toString().length() < 1 || selVeterinario == 0 || selServicio == 0 || selCliente == 0 || btnHora.getText().toString().equals("Horario disponible") || btnMesDia.getText().toString().equals("Mes/Día")){
            showMsg("Todos los campos son necesarios.");
        } else {
            //_url = "http://hyperion.init-code.com/zungu/app/vt_agregar_servicio.php?tipo=" + Integer.toString(tipo) + "&nombre="+ URLEncoder.encode(txtNombreServicio.getText().toString()) + "&costo=" + URLEncoder.encode(txtCostoServicio.getText().toString()) + "&duracion=" + URLEncoder.encode(txtDuracionServicio.getText().toString()) + "&descripcion=" + URLEncoder.encode(txtDescripcionServicio.getText().toString()) + "&capacidad=" + URLEncoder.encode(txtCapacidadServicio.getText().toString()) + "&id_veterinario=" + String.valueOf(valueID);
            //_url = "http://hyperion.init-code.com/zungu/app/vt_agregar_cita.php?id_usuario=" + Integer.toString(selCliente) + "&idvh=" + Integer.toString(selVeterinario) + "&id_servicio=" + Integer.toString(selServicio) + "&motivo="+ URLEncoder.encode(txtMotivoCita.getText().toString()) + "&fecha=" + URLEncoder.encode(btnMesDia.getText().toString()) + "&hora=" + URLEncoder.encode(btnHora.getText().toString()) + "&id_veterinario=" + String.valueOf(valueID);
            _url = "http://hyperion.init-code.com/zungu/app/vt_agregar_cita.php?id_usuario=" + Integer.toString(selCliente) + "&idvh=" + Integer.toString(selVeterinario) + "&id_servicio=" + Integer.toString(selServicio) + "&motivo="+ URLEncoder.encode(txtMotivoCita.getText().toString()) + "&fecha=" + URLEncoder.encode(btnMesDia.getText().toString()) + "&hora=" + URLEncoder.encode(btnHora.getText().toString()) + "&id_veterinario=" + String.valueOf(valueID) + "&id_mascota=" + Integer.toString(selMascota);
            btnGuardar.setEnabled(false);
            new Agregar_cita.RetrieveFeedTask().execute();
        }
        */

    }

    public void goBack(View v){
        finish();
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
                    JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
                    int ID = object.getInt("id_cita");
                    //String NOMBRE = object.getString("nombre");
                    //CharSequence text;

                    if(ID == 0){
                        btnGuardar.setEnabled(true);
                        showMsg("Por favor, ingrese otra fecha u hora.");
                        createSimpleDialog();
                    } else {
                        btnGuardar.setEnabled(true);
                        //Descomentar
                        //Intent i = new Intent(Agregar_cita.this, Cita_generada_exitosamente.class);
                        Intent i = new Intent(Agregar_cita.this, Cita_generada_exitosamente.class);
                        startActivity(i);

                        showMsg("Se ha agendado la cita");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.i("INFO", response);
        }
    }

    public AlertDialog createSimpleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Cupo lleno")
                .setMessage("Por favor, ingrese otra fecha u hora.")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

        return builder.create();
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

                        //numeroNotificaciones.setText(jsonobject.getString("numero_notificaciones"));

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

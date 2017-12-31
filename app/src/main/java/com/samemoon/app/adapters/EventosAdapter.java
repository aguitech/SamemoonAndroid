package com.samemoon.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.samemoon.app.Detalle_evento;
import com.samemoon.app.Lista_eventos;
import com.samemoon.app.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by hectoraguilar on 07/03/17.
 */

public class EventosAdapter extends BaseAdapter {



    //_mascotasAdapter = new EventosAdapter(valueID, mActivity, listaTituloEvento, listaFechaEvento, listaHoraEvento, listaIdEvento);
    ArrayList<String> _listaTituloEvento;
    ArrayList<String> _listaFechaEvento;
    ArrayList<String> _listaHoraEvento;
    ArrayList<String> _listaIdEvento;
    Context context;
    public String _url;
    public String _urlGo;
    public int _valueID;

    private static LayoutInflater inflater=null;

    public EventosAdapter(int valueID, Lista_eventos mainActivity, ArrayList<String> listaTituloEvento, ArrayList<String> listaFechaEvento, ArrayList<String> listaHoraEvento, ArrayList<String> listaIdEvento){
        _listaIdEvento = listaIdEvento;
        _listaTituloEvento = listaTituloEvento;
        _listaFechaEvento = listaFechaEvento;
        _listaHoraEvento = listaHoraEvento;
        _valueID = valueID;

        context = mainActivity;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return _listaIdEvento.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class Holder{
        TextView evento;
        TextView fecha;
        TextView hora;

    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        final Holder holder = new Holder();
        final View rowView;
        rowView = inflater.inflate(R.layout.list_eventos, null);
        final int pos = i;



        holder.evento = (TextView) rowView.findViewById(R.id.txtEvento);
        holder.fecha = (TextView) rowView.findViewById(R.id.fecha);
        holder.hora = (TextView) rowView.findViewById(R.id.hora);


        holder.evento.setText(_listaTituloEvento.get(i));
        holder.fecha.setText(_listaFechaEvento.get(i));
        holder.hora.setText(_listaHoraEvento.get(i));
        /*

        holder.agregarVeterinario.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                _urlGo = "http://hyperion.init-code.com/zungu/app/vt_agregar_id_veterinario.php?idu=" + Integer.toString(_valueID) + "&idv=" + _listaIdVeterinarios.get(pos);
                Log.d("urlgo",_urlGo);
                new AgregarVeterinariosAdapter.RetrieveFeedTask().execute();
                Toast.makeText(holder.nombreVeterinario.getContext(), "Veterinario agregado con éxito.", Toast.LENGTH_LONG).show();
            }
        });
        */

        rowView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                //_urlGo = "http://hyperion.init-code.com/zungu/app/vt_agregar_id_veterinario.php?idu=" + Integer.toString(_valueID) + "&idv=" + _listaIdVeterinarios.get(pos);
                //Log.d("urlgo",_urlGo);
                //new AgregarVeterinariosAdapter.RetrieveFeedTask().execute();

                //Toast.makeText(holder.nombreVeterinario.getContext(), "Veterinario agregado con éxito.", Toast.LENGTH_LONG).show();
                //Intent i = new Intent(context, Detalle_veterinario.class);
                //context.startActivity(i);


                //Intent intent = new Intent(context, Detalle_veterinario.class);
                //intent.putExtra("idveterinario", i);
                //context.startActivity(intent);

                Log.d("click", String.valueOf(i));
                Log.d("click", _listaIdEvento.get(i));

                //Intent intent = new Intent(context, Detalle_cliente.class);
                //Intent intent = new Intent(context, Detalle_contrato.class);
                Intent intent = new Intent(context, Detalle_evento.class);
                //intent.putExtra("idveterinario", _listaIdVeterinarios.get(i));
                //intent.putExtra("idcliente", _listaIdVeterinarios.get(i));
                intent.putExtra("idevento", _listaIdEvento.get(i));
                context.startActivity(intent);

            }
        });

        /*
        holder.detalleVeterinario.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                //_urlGo = "http://hyperion.init-code.com/zungu/app/vt_agregar_id_veterinario.php?idu=" + Integer.toString(_valueID) + "&idv=" + _listaIdVeterinarios.get(pos);
                //Log.d("urlgo",_urlGo);
                //new AgregarVeterinariosAdapter.RetrieveFeedTask().execute();

                //Toast.makeText(holder.nombreVeterinario.getContext(), "Veterinario agregado con éxito.", Toast.LENGTH_LONG).show();
                //Intent i = new Intent(context, Detalle_veterinario.class);
                //context.startActivity(i);


                //Intent intent = new Intent(context, Detalle_veterinario.class);
                //intent.putExtra("idveterinario", i);
                //context.startActivity(intent);

                Log.d("click", String.valueOf(i));
                Log.d("click", _listaIdVeterinarios.get(i));

                //Intent intent = new Intent(context, Detalle_cliente.class);
                Intent intent = new Intent(context, Detalle_contrato.class);
                //intent.putExtra("idveterinario", _listaIdVeterinarios.get(i));
                //intent.putExtra("idcliente", _listaIdVeterinarios.get(i));
                intent.putExtra("idcontrato", _listaIdVeterinarios.get(i));
                context.startActivity(intent);

            }
        });

        holder.nombreVeterinario.setText(_listaNombreVeterinarios.get(i));
        _url = "http://hyperion.init-code.com/zungu/imagen_establecimiento/" + _listaImagenVeterinarios.get(i);
        Log.d("url", _url);
        Log.d("entro", "sii");

        Picasso.with(holder.imagenVeterinario.getContext()).load(_url)
                .into(holder.imagenVeterinario, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap imageBitmap = ((BitmapDrawable) holder.imagenVeterinario.getDrawable()).getBitmap();
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), imageBitmap);
                        circularBitmapDrawable.setCircular(true);
                        holder.imagenVeterinario.setImageDrawable(circularBitmapDrawable);
                    }
                    @Override
                    public void onError() {

                    }
                });

        */
        return rowView;
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
        }

        protected String doInBackground(Void... urls) {
            try {
                Log.i("INFO url: ", _urlGo);
                URL url = new URL(_urlGo);
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
                DESCOMENTAR
                Intent i = new Intent(context, Principal.class);
                context.startActivity(i);
                */
            }
            Log.i("INFO", response);
        }
    }
}

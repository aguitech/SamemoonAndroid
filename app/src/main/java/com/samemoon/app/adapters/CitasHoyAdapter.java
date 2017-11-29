package com.samemoon.app.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.samemoon.app.Citas;
import com.samemoon.app.Citas_detalle;
import com.samemoon.app.EditarCita;
import com.samemoon.app.R;

/**
 * Created by hectoraguilar on 11/11/16.
 */

public class CitasHoyAdapter extends BaseAdapter {
    ArrayList<String> _listaIdCita;
    ArrayList<String> _listaFechaDias;
    ArrayList<String> _listaHora;
    ArrayList<String> _listaMotivo;
    ArrayList<String> _listaIdMotivo;
    ArrayList<String> _listaIdUsuario;
    ArrayList<String> _listaNombreUsuario;

    String _urlGo;
    Context context;

    private static LayoutInflater inflater=null;
    public CitasHoyAdapter(Citas mainActivity, ArrayList<String> listaIdCita, ArrayList<String> listaFechaDias, ArrayList<String> listaHora, ArrayList<String> listaMotivo, ArrayList<String> listaIdMotivo, ArrayList<String> listaIdUsuario, ArrayList<String> listaNombreUsuario) {

        /*, ArrayList<String> , ArrayList<String> , ArrayList<String> */
        _listaIdCita = listaIdCita;
        _listaFechaDias = listaFechaDias;
        _listaHora = listaHora;
        _listaMotivo = listaMotivo;
        _listaIdMotivo = listaIdMotivo;
        _listaIdUsuario = listaIdUsuario;
        _listaNombreUsuario = listaNombreUsuario;
        context = mainActivity;

        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return _listaIdCita.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView txtCliente;
        TextView txtHora;
        ImageView btnEditar;
        ImageView btnVer;
    }

    public void reset(){
        Log.d("clickn","entro");
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        //final SolicitudesAdapter.Holder holder = new SolicitudesAdapter.Holder();
        //final Holder holder = new Holder();
        final Holder holder = new Holder();
        String _url;
        final int pos = position;
        final View rowView;
        //rowView = inflater.inflate(R.layout.list_servicio, null);
        rowView = inflater.inflate(R.layout.list_citas, null);

        holder.txtCliente = (TextView)rowView.findViewById(R.id.txtCliente);
        holder.txtHora = (TextView)rowView.findViewById(R.id.txtHora);
        holder.btnEditar = (ImageView) rowView.findViewById(R.id.btnEditar);
        holder.btnVer = (ImageView) rowView.findViewById(R.id.btnVer);

        boolean wrapInScrollView = true;
        holder.txtCliente.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                //confirm(holder.imgMascota.getContext(), position, "Eliminar mascota: " + _listaNombreMascota.get(position));

                Log.d("id_producto", _listaIdCita.get(pos));

                Intent i = new Intent(context, Citas_detalle.class);
                i.putExtra("id_cita", _listaIdCita.get(pos));
                context.startActivity(i);
                /*
                Intent i = new Intent(context, Citas_detalle.class);
                i.putExtra("id_cita",_listaIdCita.get(pos));
                context.startActivity(i);
                /*
                Intent i = new Intent(context, Citas_detalle.class);
                //i.putExtra("id_cita", _listaIdCita.get(position));
                context.startActivity(i);

                new MaterialDialog.Builder(context)
                        .title(_listaFechaDias.get(position))
                        .contentGravity(GravityEnum.CENTER)
                        .content("Nombre del cliente" + "\n" + _listaNombreUsuario.get(position) + "\nFecha y hora solicitada:\n" + _listaFechaDias.get(position) + "\n" + _listaHora.get(position)  + "Contenidohola contenido" + R.drawable.animal_notifiaciones)
                        //.customView(R.layout.detalle_cita, wrapInScrollView)
                        //.customView(R.layout.detalle_cita, true)
                        //.icon(R.drawable.lapiz)
                        .positiveText("Positivo Str")
                        .negativeText("Negative Str")
                        .show();
                */
            }
        });
        holder.btnEditar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                //confirm(holder.imgMascota.getContext(), position, "Eliminar mascota: " + _listaNombreMascota.get(position));

                Log.d("id_producto", _listaIdCita.get(pos));

                Intent i = new Intent(context, EditarCita.class);
                i.putExtra("id_cita", _listaIdCita.get(pos));
                context.startActivity(i);
            }
        });
        holder.btnVer.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                //confirm(holder.imgMascota.getContext(), position, "Eliminar mascota: " + _listaNombreMascota.get(position));

                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(context);
                dialogo1.setTitle("Importante");
                dialogo1.setMessage("¿ Estas seguro que desea borrarlo ?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        _urlGo = "http://hyperion.init-code.com/zungu/app/vt_get_solicitudes_citas_dia.php?delete="  + _listaIdCita.get(pos);
                        new CitasHoyAdapter.RetrieveFeedTask().execute();

                        _listaFechaDias.remove(pos);
                        _listaHora.remove(pos);
                        _listaIdCita.remove(pos);
                        _listaIdMotivo.remove(pos);
                        _listaIdUsuario.remove(pos);
                        _listaNombreUsuario.remove(pos);
                        _listaMotivo.remove(pos);

                        notifyDataSetChanged();
                        Toast.makeText(context, "Eliminado", Toast.LENGTH_SHORT).show();
                    }
                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                    }
                });
                dialogo1.show();


            }
        });
        /*
        new MaterialDialog.Builder(context)
                        .title(_listaFechaDias.get(position))
                        .content("Nombre del cliente" + "\n" + _listaNombreUsuario.get(position) + "\nFecha y hora solicitada:\n" + _listaFechaDias.get(position) + "\n" + _listaHora.get(position)  + "Contenidohola contenido")
                        .customView(R.layout.custom_view, wrapInScrollView)
                        .positiveText("Positivo Str")
                        .negativeText("Negative Str")
                        .show();


        holder.imgEliminar = (ImageView)rowView.findViewById(R.id.imgEliminar);
        holder.imgDetalle = (ImageView)rowView.findViewById(R.id.imgDetalle);
        holder.imgEliminar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                //confirm(holder.imgMascota.getContext(), position, "Eliminar mascota: " + _listaNombreMascota.get(position));
                Intent i = new Intent(context, Editar_servicio.class);
                i.putExtra("id_servicio", _listaImgMascotas.get(position));
                context.startActivity(i);

                Log.d("click", "detalle");
            }
        });
        holder.imgDetalle.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                //Intent i = new Intent(); 

            }
        });
        */


        // Log.d("entro carga", "entro");
        //Log.d("fecha dias", _listaFechaDias.get(position));
        //Juan Carlos ha agendado una cita para “su mascota” el 01/01/2016.
        //holder.txtDescripcion.setText(_listaNombreUsuario.get(position) + " ha agendado una cita para \"su mascota\" el " + _listaFechaDias.get(position));
        holder.txtCliente.setText(_listaNombreUsuario.get(position));
        //holder.txtHora.setText(_listaFechaDias.get(position));
        //holder.txtHora.setText(_listaHora.get(position) + " horas");
        holder.txtHora.setText(_listaHora.get(position));

        /*
        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //click fila
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
               /* Intent i = new Intent(context, Principal.class);
                context.startActivity(i);*/
            }
            Log.i("INFO", response);
        }
    }
}

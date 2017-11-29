package com.samemoon.app.adapters;

import android.content.Context;
import android.content.DialogInterface;
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

import com.samemoon.app.Notificaciones;
import com.samemoon.app.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by giovanniaranda on 01/11/16.
 */

public class NotificacionesAdapter extends BaseAdapter {

    ArrayList<String> _listaContenidoNotificaciones;
    ArrayList<String> _listaImagenNotificaciones;
    ArrayList<String> _listIdNotificaciones;
    int _idv;

    public String _urlGo;

    Context context;

    private static LayoutInflater inflater=null;
    public NotificacionesAdapter(Notificaciones mainActivity, int idv, ArrayList<String> listaContenidoNotificaciones, ArrayList<String> listaImagenNotificaciones, ArrayList<String> listIdNotificaciones){
        _listaContenidoNotificaciones = listaContenidoNotificaciones;
        _listaImagenNotificaciones = listaImagenNotificaciones;
        _listIdNotificaciones = listIdNotificaciones;
        _idv = idv;

        context = mainActivity;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return _listaContenidoNotificaciones.size();
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
        TextView contenidoNotificacion;
        ImageView imagenNotificacion;
        TextView fechaNotificacion;
        ImageView imgEliminar;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        final Holder holder = new Holder();
        String _url;
        final View rootView;
        rootView = inflater.inflate(R.layout.list_notificaciones,null);

        holder.contenidoNotificacion = (TextView) rootView.findViewById(R.id.contenidoNotificacion);
        holder.fechaNotificacion = (TextView) rootView.findViewById(R.id.fechaNotificacion);
        holder.imagenNotificacion = (ImageView) rootView.findViewById(R.id.imagenNotificacion);
        holder.imgEliminar = (ImageView) rootView.findViewById(R.id.imgEliminar);


        holder.contenidoNotificacion.setText(_listaContenidoNotificaciones.get(i));
        holder.fechaNotificacion.setText(_listIdNotificaciones.get(i));

        final Integer _pos = i;


        holder.imgEliminar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                //confirm(holder.imgMascota.getContext(), position, "Eliminar mascota: " + _listaNombreMascota.get(position));
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(context);
                dialogo1.setTitle("Importante");
                dialogo1.setMessage("¿ Estas seguro que desea borrarlo ?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        /*
                        _urlGo = "http://hyperion.init-code.com/zungu/app/vt_get_mascotas_adopcion.php?delete="  + _listaIdAdopta.get(position);
                        Log.d("urlgo",_urlGo);
                        new MascotasAdopcionAdapter.RetrieveFeedTask().execute();
                        */
                        //Toast.makeText(holder.txtNombreCliente.getContext(), "Mascota eliminada", Toast.LENGTH_LONG).show();



                        //_urlGo = "http://hyperion.init-code.com/zungu/app/vt_eliminar_notificacion.php?id="  + _listIdNotificaciones.get(_pos) + "&idv=" + String.valueOf(_idv);
                        _urlGo = "http://hyperion.init-code.com/zungu/app/vt_eliminar_notificacion.php?id="  + _listaImagenNotificaciones.get(_pos) + "&idv=" + String.valueOf(_idv);
                        Log.d("urlgo",_urlGo);
                        new NotificacionesAdapter.RetrieveFeedTask().execute();
                        //Log.d("valor", String.valueOf(id));

                        //notifyDataSetChanged();
                        Toast.makeText(context, "Eliminado", Toast.LENGTH_SHORT).show();

                        _listaContenidoNotificaciones.remove(i);
                        _listaImagenNotificaciones.remove(i);
                        _listIdNotificaciones.remove(i);

                        notifyDataSetChanged();
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
        holder.imgEliminar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                confirm(holder.imgEliminar.getContext(), i, "Eliminar mascota: " + _listIdNotificaciones.get(i));
                _listaContenidoNotificaciones.remove(i);
                _listaImagenNotificaciones.remove(i);
                _listIdNotificaciones.remove(i);

                notifyDataSetChanged();
            }
        });
        */

        return rootView;
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
                //Intent i = new Intent(context, Principal.class);
                //context.startActivity(i);
            }
            Log.i("INFO", response);
        }
    }

}

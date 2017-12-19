package com.samemoon.app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.samemoon.app.adapters.PagosAdapter;
import com.samemoon.app.classes.Request;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hectoraguilar on 07/03/17.
 */

public class Detalle_evento extends AppCompatActivity {
    ListView lv;
    private String _urlGet;
    private String _url;
    public static final String idu = "idu";
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    private int valueID = 0;

    public static ArrayList<String> listaNombreVeterinarios = new ArrayList<String>();
    public static ArrayList<String> listaImagenVeterinarios = new ArrayList<String>();
    public static ArrayList<String> listaIdVeterinario = new ArrayList<String>();

    public Detalle_evento mActivity = this;
    public PagosAdapter _mascotasAdapter;

    public Detalle_evento _activity = this;
    RecyclerView lvMascotas;

    private String _urlNotificaciones;

    String idString;

    int TAKE_PHOTO_CODE = 1888;

    private ImageView imageView;

    //int TAKE_PHOTO_CODE = 0;
    public static int count = 0;


    private Button tomarFoto;
    private Button subirFoto;
    private ImageView verFoto;
    private  static final int TAKE_PICTURE=1;
    private String bmpPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_evento);

        //lv = (ListView) findViewById(R.id.list_pagos);

        //this.imageView = (ImageView)this.findViewById(R.id.imgFotoEvento);
        this.imageView = (ImageView)this.findViewById(R.id.imgFotoEvento);

        showMsg("test");

        //String idString;
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            idString= null;

        } else {
            //idString= extras.getString("idcliente");
            idString= extras.getString("idcontrato");
            Log.d("id_vet", idString);

        }


        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        valueID = sharedpreferences.getInt("idu", 0);

        /*
        lvMascotas = (RecyclerView) findViewById(R.id.lvVeterinarios);

        //_urlGet = "http://hyperion.init-code.com/zungu/app/vt_principal.php?id_editar=" + Integer.toString(valueID);
        //_urlGet = "http://hyperion.init-code.com/zungu/app/vt_principal.php?id_editar=" + idString;

        _urlGet = "http://hyperion.init-code.com/zungu/app/vt_principal.php?id_editar=" + idString + "&idv=" + valueID + "&accion=true";
        new Detalle_veterinario.RetrieveFeedTaskGet().execute();

        _urlNotificaciones = "http://hyperion.init-code.com/zungu/app/vt_get_numero_notificaciones.php?idv=" + valueID;
        new Detalle_veterinario.RetrieveFeedTaskNotificaciones().execute();
        */
        //_urlGet = "http://hyperion.init-code.com/zungu/app/vt_principal.php?id_editar=" + idString + "&idv=" + valueID + "&accion=true";
        //_urlGet = "http://thekrakensolutions.com/cobradores/android_get_cliente.php?id_editar=" + idString + "&idv=" + valueID + "&accion=true";


        /** RECUPERAR INFORMACION Y RESULTADOS DEL EVENTO

        _urlGet = "http://thekrakensolutions.com/cobradores/android_get_contrato.php?id_editar=" + idString + "&idv=" + valueID + "&accion=true";
        new Detalle_evento.RetrieveFeedTaskGet().execute();

         */

        if( ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                        5);
            }
        }

        // Here, we are making a folder named picFolder to store
        // pics taken by the camera using this application.
        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        File newdir = new File(dir);
        newdir.mkdirs();

        //this.imageView = (ImageView)this.findViewById(R.id.imgFotoEvento);
        ImageView fotoSeleccionada = (ImageView) findViewById(R.id.imgFotoEvento);
        Button capture = (Button) findViewById(R.id.btnCapture);
        capture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Here, the counter will be incremented each time, and the
                // picture taken by camera will be stored as 1.jpg,2.jpg
                // and likewise.
                count++;
                String file = dir+count+".jpg";
                File newfile = new File(file);
                try {
                    newfile.createNewFile();
                }
                catch (IOException e)
                {
                }

                Uri outputFileUri = Uri.fromFile(newfile);

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                //startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                //startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);


            }
        });
        /**
        _url = "http://thekrakensolutions.com/cobradores/android_get_contratos.php?id=" + Integer.toString(valueID);
        Log.d("url_veterinarios", _url);
        new Detalle_contrato.RetrieveFeedTask().execute();
        */

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera
            } else {
                // Your app will not have this permission. Turn off all functions
                // that require this permission or it will force close like your
                // original question
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {

            Log.d("CameraDemo", "Pic saved");

            //Bitmap bitmap = (Bitmap) data.getExtras().get("data");


            if(data != null)
            {
                showMsg("test 2");
                Uri selectedImageUri = data.getData();
                String filestring = selectedImageUri.getPath();

                Bitmap bm = (Bitmap) data.getExtras().get("data");
                Bitmap bmpPhoto = bm;
                bmpPhotoPath = filestring;
                imageView.setImageBitmap(bmpPhoto);
                //removePhoto.setVisibility(View.VISIBLE);

            }else{
                showMsg("test 1");


            }

            /*
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
			if (bitmap != null) {
				//mImageView.setImageBitmap(bitmap);
                imageView.setImageBitmap(bitmap);

				try {
					//sendPhoto(bitmap);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			*/

            /*
            File imgFile = new  File("/storage/emulated/0/picFolder/1.jpg");

            if(imgFile.exists()){

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                ImageView myImage = (ImageView) findViewById(R.id.imgFotoEvento);

                myImage.setImageBitmap(myBitmap);

            }
            */
            /*
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
            */

            /*
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);



            data.getExtras().get("data");
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
            */

            /*
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
            */
            /*
imgFotoEvento

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.imgFotoEvento);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            */
        }
    }
    public void cambiarFoto(View v){
        int MY_PERMISSIONS_REQUEST_READ_AND_WRITE_EXTERNAL_STORAGE = 1;

        if((ContextCompat.checkSelfPermission(Detalle_evento.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(Detalle_evento.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))

        {

            ActivityCompat.requestPermissions
                    (Detalle_evento.this, new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },MY_PERMISSIONS_REQUEST_READ_AND_WRITE_EXTERNAL_STORAGE);

        } else {
            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, 1);
        }
    }

    /*
    private void sendPhoto(Bitmap bitmap) throws Exception {
        new UploadTask().execute(bitmap);
    }


    private class UploadTask extends AsyncTask<Bitmap, Void, Void> {

        protected Void doInBackground(Bitmap... bitmaps) {
            if (bitmaps[0] == null)
                return null;
            setProgress(0);

            Bitmap bitmap = bitmaps[0];
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); // convert Bitmap to ByteArrayOutputStream
            InputStream in = new ByteArrayInputStream(stream.toByteArray()); // convert ByteArrayOutputStream to ByteArrayInputStream

            DefaultHttpClient httpclient = new DefaultHttpClient();
            try {
                HttpPost httppost = new HttpPost(
                        "http://192.168.8.84:8003/savetofile.php"); // server

                MultipartEntity reqEntity = new MultipartEntity();
                reqEntity.addPart("myFile",
                        System.currentTimeMillis() + ".jpg", in);
                httppost.setEntity(reqEntity);

                //Log.i(TAG, "request " + httppost.getRequestLine());
                HttpResponse response = null;
                try {
                    response = httpclient.execute(httppost);
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    if (response != null)
                        //Log.i(TAG, "response " + response.getStatusLine().toString());
                } finally {

                }
            } finally {

            }

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //Toast.makeText(Detalle_evento.this, R.string.uploaded, Toast.LENGTH_LONG).show();
        }
    }
    */

    private void showMsg(CharSequence text){
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
                Log.i("INFO url Contrato: ", _urlGet);
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
                /*
                TextView lblNombre = (TextView) findViewById(R.id.lblNombre);
                TextView lblDireccion = (TextView) findViewById(R.id.lblDireccion);
                ImageView foto = (ImageView) findViewById(R.id.imgFoto);
                */

                TextView lblNombreVo = (TextView) findViewById(R.id.txtNombreA);
                TextView lblEmailVo = (TextView) findViewById(R.id.txtEmailA);
                TextView lblCelVo = (TextView) findViewById(R.id.txtCelA);
                //TextView lblCedVo = (TextView) findViewById(R.id.txtCedA);
                TextView txtDireccion = (TextView) findViewById(R.id.txtDireccion);

                final ImageView fotoVeterinario = (ImageView) findViewById(R.id.imgVeterinario);

                try {

                    JSONObject object = (JSONObject) new JSONTokener(response).nextValue();


                    String _nombre_vo = object.getString("numero_cliente") + " - " + object.getString("nombre") + " " + object.getString("apaterno") + " " + object.getString("amaterno");

                    //String _telefono_vo = object.getString("telefono_casa");
                    String _cedula_vo = object.getString("numero_cliente");
                    String _email_vo = object.getString("fecha_nacimiento");
                    //String _imagen_vo = object.getString("sexo");
                    String _imagen_vo = object.getString("imagen");


                    showMsg("tesst2");

                    showMsg(_email_vo);
                    //showMsg(_telefono_vo);


                    /*
                    {"id_cliente":"1","cliente":"","numero_cliente":"0","fecha_nacimiento":"0000-00-00","sexo":"mkl","imagen":"",":"klmkl","":"mkl","":"mklm","":"klm","":"klmkl","telefono_casa":"","telefono_celular":"","telefono_oficina":"","":"","":"","ocupacion":"","direccion_trabajo":"","nombre_pareja":"","ocupacion_pareja":"","telefono_pareja":"","complexion":"","estatura":"","tez":"","edad_rango":"","cabello":"","color_cabello":"","tipo_identificacion":"","numero_identificacion":"","nombre_referencia_1":"","direccion_referencia_1":"","telefono_referencia_1":"","parentesco_referencia_1":"","anios_conocerce_referencia_1":"","nombre_referencia_2":"","direccion_referencia_2":"","telefono_referencia_2":"","parentesco_referencia_2":"","anios_conocerce_referencia_2":"","maps_localizacion":"","imagen_plano_localizacion":"","fachada_casa":"","a_lado_casa":"","enfrente_casa":"","autorizacion_contratos":"","id_creador":"0","id_empresa":"0"}

                    {"id_cliente":"1","cliente":"","numero_cliente":"0","nombre":"mklmklmklm","apaterno":"klmkl","amaterno":"mklm","fecha_nacimiento":"0000-00-00","sexo":"mkl","imagen":"","calle":"mklm","numero_exterior":"klm","numero_interior":"klmkl","colonia":"mkl","delegacion_municipio":"mkl","estado":"mklm","codigo_postal":"klm","pais":"klmkl","telefono_casa":"","telefono_celular":"","telefono_oficina":"","entre_calle":"","y_calle":"","ocupacion":"","direccion_trabajo":"","nombre_pareja":"","ocupacion_pareja":"","telefono_pareja":"","complexion":"","estatura":"","tez":"","edad_rango":"","cabello":"","color_cabello":"","tipo_identificacion":"","numero_identificacion":"","nombre_referencia_1":"","direccion_referencia_1":"","telefono_referencia_1":"","parentesco_referencia_1":"","anios_conocerce_referencia_1":"","nombre_referencia_2":"","direccion_referencia_2":"","telefono_referencia_2":"","parentesco_referencia_2":"","anios_conocerce_referencia_2":"","maps_localizacion":"","imagen_plano_localizacion":"","fachada_casa":"","a_lado_casa":"","enfrente_casa":"","autorizacion_contratos":"","id_creador":"0","id_empresa":"0"}

                    String _telefono_vo = object.getString("telefono_veterinario");
                    String _cedula_vo = object.getString("cedula_veterinario");
                    String _email_vo = object.getString("email_veterinario");
                    String _imagen_vo = object.getString("imagen_veterinario");
                    */




                    if(_nombre_vo.length() > 3)
                        lblNombreVo.setText(_nombre_vo);

                    if(_email_vo.length() > 3)
                        lblEmailVo.setText(_email_vo);

                    /*
                    if(_telefono_vo.length() > 3)
                        lblCelVo.setText(_telefono_vo);

                    */
                    /*
                    if(_cedula_vo.length() > 3)
                        lblCedVo.setText(_cedula_vo);
                        */


                    //DIRECCION
                    //String txtDireccion_ = object.getString("calle") + " " + object.getString("numero_exterior") + " " + object.getString("numero_interior")  + " , Colonia " + object.getString("colonia")  + " , Delegación/Municipio " + object.getString("delegacion_municipio")  + " , Estado " + object.getString("estado")  + " , C.P. " + object.getString("codigo_postal")  + " , País " + object.getString("pais")  + " , entre calle " + object.getString("entre_calle")  + " y calle " + object.getString("y_calle")  + " " + object.getString("amaterno")  + " " + object.getString("amaterno")  + " " + object.getString("amaterno")  + " " + object.getString("amaterno")  + " " + object.getString("amaterno")  + " " + object.getString("amaterno")  + " " + object.getString("amaterno")  + " " + object.getString("amaterno")  + " " + object.getString("amaterno")  + " " + object.getString("amaterno")  + " " + object.getString("amaterno")  + " " + object.getString("amaterno")  + " " + object.getString("amaterno")  + " " + object.getString("amaterno")  + " " + object.getString("amaterno")  + " " + object.getString("amaterno")  + " " + object.getString("amaterno");
                    //String txtDireccion_ = object.getString("calle") + " " + object.getString("numero_exterior") + " " + object.getString("numero_interior")  + " , Colonia " + object.getString("colonia")  + " , Delegación/Municipio " + object.getString("delegacion_municipio")  + " , Estado " + object.getString("estado")  + " , C.P. " + object.getString("codigo_postal")  + " , País " + object.getString("pais")  + " , entre calle " + object.getString("entre_calle")  + " y calle " + object.getString("y_calle");
                    String txtDireccion_ = object.getString("calle") + " " + object.getString("numero_exterior") + " " + object.getString("numero_interior")  + " , Colonia " + object.getString("colonia")  + " , Delegación/Municipio " + object.getString("poblacion")  + " , Estado " + object.getString("estado")  + " , C.P. " + object.getString("codigo_postal")  + " , País " + object.getString("pais");

                    if(txtDireccion_.length() > 3)
                        txtDireccion.setText(txtDireccion_);


                    Log.d("INFO", _nombre_vo);


                    if(_imagen_vo.length() > 3){
                        String _urlFoto = "http://thekrakensolutions.com/administrativos/images/clientes/" + _imagen_vo;
                        //Picasso.with(fotoVeterinario.getContext()).load(_urlFoto).fit().centerCrop().into(fotoVeterinario);

                        Picasso.with(fotoVeterinario.getContext()).load(_urlFoto)
                                .into(fotoVeterinario, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        Bitmap imageBitmap = ((BitmapDrawable) fotoVeterinario.getDrawable()).getBitmap();
                                        RoundedBitmapDrawable circularBitmapDrawable =
                                                RoundedBitmapDrawableFactory.create(fotoVeterinario.getContext().getResources(), imageBitmap);
                                        circularBitmapDrawable.setCircular(true);
                                        fotoVeterinario.setImageDrawable(circularBitmapDrawable);
                                    }
                                    @Override
                                    public void onError() {

                                    }
                                });
                    }




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
                Log.i("INFO url: GUARDAR ", _url);
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
                try {
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;

                    showMsg("Se ha agregado la foto");
                    /*
                    showMsg("Se ha agregado la mascota");

                    finish();

                    EditText txtNombre = (EditText)findViewById(R.id.txtNombre);

                    txtNombre.setText("");
                    */

                    JSONObject object = null;
                    try {
                        object = (JSONObject) new JSONTokener(response).nextValue();
                        int ID = object.getInt("id_evento");
                        CharSequence text;


                        if(ID > 0){

                            uploadImage(ID);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Log.i("INFO", response);
        }
    }

    private String hashMapToUrl(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
    private class Upload extends AsyncTask<Void,Void,String>{
        private Bitmap image;
        private int ID;

        public Upload(Bitmap image,int ID){
            this.image = image;
            this.ID = ID;
        }

        @Override
        protected String doInBackground(Void... params) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);

            String encodeImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

            HashMap<String,String> detail = new HashMap<>();
            detail.put("id", Integer.toString(ID));
            detail.put("image", encodeImage);

            try{
                String dataToSend = hashMapToUrl(detail);

                //String response = Request.post("http://hyperion.init-code.com/zungu/saveImageAdopta.php",dataToSend);
                //String response = Request.post("http://hyperion.init-code.com/zungu/saveImageAdopta.php",dataToSend);
                String response = Request.post("http://aguitech.com/samemoon/cobradores/app_guardar_foto_android.php",dataToSend);
                return response;

            }catch (Exception e){
                e.printStackTrace();
                Log.e("ERROR","ERROR  "+e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getApplicationContext(),"La mascota ha sido guardado correctamente",Toast.LENGTH_SHORT).show();

            Intent i = new Intent(Detalle_evento.this, Principal.class);
            startActivity(i);
        }
    }

    public void guardarFotoServidor(View v){
        //Intent i = new Intent(Detalle_contrato.this, Lista_clientes.class);

        //_url = "http://hyperion.init-code.com/zungu/app/vt_agregar_mascota_adopcion.php?estado=" + URLEncoder.encode(valueEstado) + "&id_veterinario=" + String.valueOf(valueID);
        _url = "http://aguitech.com/samemoon/cobradores/app_guardar_foto_android.php?id_usuario=" + String.valueOf(valueID);

        new Detalle_evento.RetrieveFeedTask().execute();
    }



    public void uploadImage(int ID){
        ImageView imageView = (ImageView) findViewById(R.id.imgFotoEvento);
        int maxHeight = 600;
        int maxWidth = 600;

        Bitmap image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        float scale = Math.min(((float)maxHeight / image.getWidth()), ((float)maxWidth / image.getHeight()));

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        image = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
        new Upload(image, ID).execute();
    }

    /*
    public void editarEstablecimiento(View view) {
        Intent i = new Intent(Detalle_veterinario.this, Editar_establecimiento.class);
        startActivity(i);
    }
    */

    public void goBack(View v){
        //Intent i = new Intent(Detalle_contrato.this, Lista_clientes.class);
        Intent i = new Intent(Detalle_evento.this, Lista_contratos.class);
        startActivity(i);
    }
    public void goAgregarPago(View v){
        Intent i = new Intent(Detalle_evento.this, Agregar_pago.class);
        //i.putExtra("idcliente", _listaIdVeterinarios.get(i));
        //i.putExtra("idcliente", idString);
        i.putExtra("idcontrato", idString);
        startActivity(i);
    }
    public void goMenu(View v){
        Intent i = new Intent(Detalle_evento.this, Menu.class);
        startActivity(i);
    }

    /*

    public void goMenu(View v){
        Intent i = new Intent(Detalle_veterinario.this, Menu.class);
        startActivity(i);
    }
    public void goBack(View v){
        Intent i = new Intent(Detalle_veterinario.this, Principal.class);
        startActivity(i);
    }
    public void goNotificaciones(View v){
        Intent i = new Intent(Detalle_veterinario.this, Notificaciones.class);
        startActivity(i);
    }

    public void goAgregar(View v){
        Intent i = new Intent(Detalle_veterinario.this, Agregar_veterinarios.class);
        startActivity(i);
    }

    public void goClientes(View v){
        Intent i = new Intent(Detalle_veterinario.this, Clientes.class);
        startActivity(i);
    }

    public void goMascotas(View v){
        Intent i = new Intent(Detalle_veterinario.this, Mascotas.class);
        startActivity(i);
    }

    public void goServicio(View v){
        Intent i = new Intent(Detalle_veterinario.this, Servicio.class);
        startActivity(i);
    }

    public void goTienda(View v){
        Intent i = new Intent(Detalle_veterinario.this, Tienda.class);
        startActivity(i);
    }

    public void goEditarPerfil(View v){
        Intent i = new Intent(Detalle_veterinario.this, Editar_perfil.class);
        startActivity(i);
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
                TextView lblNombre = (TextView) findViewById(R.id.lblNombre);
                TextView lblDireccion = (TextView) findViewById(R.id.lblDireccion);
                ImageView foto = (ImageView) findViewById(R.id.imgFoto);

                TextView lblNombreVo = (TextView) findViewById(R.id.txtNombreA);
                TextView lblEmailVo = (TextView) findViewById(R.id.txtEmailA);
                TextView lblCelVo = (TextView) findViewById(R.id.txtCelA);
                TextView lblCedVo = (TextView) findViewById(R.id.txtCedA);
                final ImageView fotoVeterinario = (ImageView) findViewById(R.id.imgVeterinario);

                try {

                    JSONObject object = (JSONObject) new JSONTokener(response).nextValue();


                    String _nombre_vo = object.getString("nombre_veterinario");
                    String _telefono_vo = object.getString("telefono_veterinario");
                    String _cedula_vo = object.getString("cedula_veterinario");
                    String _email_vo = object.getString("email_veterinario");
                    String _imagen_vo = object.getString("imagen_veterinario");

                    if(_nombre_vo.length() > 3)
                        lblNombreVo.setText(_nombre_vo);

                    if(_email_vo.length() > 3)
                        lblEmailVo.setText(_email_vo);

                    if(_telefono_vo.length() > 3)
                        lblCelVo.setText(_telefono_vo);

                    if(_cedula_vo.length() > 3)
                        lblCedVo.setText(_cedula_vo);

                    if(_imagen_vo.length() > 3){
                        String _urlFoto = "http://hyperion.init-code.com/zungu/imagen_establecimiento/" + _imagen_vo;
                        //Picasso.with(fotoVeterinario.getContext()).load(_urlFoto).fit().centerCrop().into(fotoVeterinario);

                        Picasso.with(fotoVeterinario.getContext()).load(_urlFoto)
                                .into(fotoVeterinario, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        Bitmap imageBitmap = ((BitmapDrawable) fotoVeterinario.getDrawable()).getBitmap();
                                        RoundedBitmapDrawable circularBitmapDrawable =
                                                RoundedBitmapDrawableFactory.create(fotoVeterinario.getContext().getResources(), imageBitmap);
                                        circularBitmapDrawable.setCircular(true);
                                        fotoVeterinario.setImageDrawable(circularBitmapDrawable);
                                    }
                                    @Override
                                    public void onError() {

                                    }
                                });
                    }


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
    */
}

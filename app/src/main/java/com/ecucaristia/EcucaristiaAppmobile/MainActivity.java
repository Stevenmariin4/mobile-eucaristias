package com.ecucaristia.EcucaristiaAppmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ecucaristia.EcucaristiaAppmobile.activity.Enviroment;
import com.ecucaristia.EcucaristiaAppmobile.activity.ListEuca;
import com.ecucaristia.EcucaristiaAppmobile.adapter.AdapterListIg;
import com.ecucaristia.EcucaristiaAppmobile.models.modelListIglesias;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private ListView listview;
    private AdapterListIg adapter;
    private RequestQueue requestQueue;
    private JsonObjectRequest mStringRequest;
    public ArrayList<modelListIglesias> model = new ArrayList<>();
    private ProgressDialog progress;
    private static final String FILE_NAME = "log.txt";
    public Enviroment env = new Enviroment();
    public modelListIglesias m = new modelListIglesias();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Permisos();
        adapter = new AdapterListIg(this, model);
        listview = (ListView) findViewById(R.id.listaIglesias);
        getData();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    modelListIglesias model = (modelListIglesias) adapter.getItem(position);
                    Intent intent = new Intent(MainActivity.this, ListEuca.class);
                    intent.putExtra("id", model.getId());
                    startActivity(intent);
                    Log.e("ModelCLick", model.getNombre() + "    " + model.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public void getData() {
        String url = env.getIglesias();
        requestQueue = Volley.newRequestQueue(this);
        try {
            progress = ProgressDialog.show(this, "Cargando Espere un momento",
                    "Cargando", true);
            mStringRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Response", String.valueOf(response));
                    try {
                        JSONArray datos = response.getJSONArray("body");
                        for (int i = 0; i < datos.length(); i++) {
                            JSONObject objeto = datos.getJSONObject(i);
                            String nombre = objeto.getString("Iglesia");
                            int id = objeto.getInt("idIglesia");
                            m = new modelListIglesias();
                            m.setNombre(nombre);
                            m.setId(id);
                            model.add(m);
                        }
                        //adapter = new AdapterListIg(getApplicationContext(), model);
                        listview.setAdapter((ListAdapter) adapter);
                        progress.dismiss();
                    } catch (JSONException e) {

                        progress.dismiss();
                        Toast.makeText(getApplicationContext(), "Error al obtener la lista de iglesias", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "Error Al enviar solicitud de iglesias " + error, Toast.LENGTH_SHORT).show();
                    Log.e("error", String.valueOf(error));

                }
            });

            requestQueue.add(mStringRequest);
        } catch (Exception e) {
            Log.e("Exception", String.valueOf(e));
        }


    }

    public void Permisos() {
        int permissionNetwork = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
        if (permissionNetwork != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 225);
        } else {

        }
        int permissionInternet = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        if (permissionInternet != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 225);
        }else {

        }


    }

    public void escritura() {
        String baseDir = Environment.getExternalStorageDirectory() + "Log.txt";
        File fich = new File(baseDir);
        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        String separadores = "*******************";
        FileOutputStream fileOutputStream = null;
        if (!fich.exists()) {
            try {
                fich.createNewFile();
                Log.d("TAG1", "Fichero Salvado en: " + baseDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("TAG1", "Fichero Salvado en: " + baseDir);
        }
    }


}

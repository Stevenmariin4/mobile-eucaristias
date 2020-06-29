package com.ecucaristia.EcucaristiaAppmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.ecucaristia.EcucaristiaAppmobile.activity.ListEuca;
import com.ecucaristia.EcucaristiaAppmobile.adapter.AdapterListIg;
import com.ecucaristia.EcucaristiaAppmobile.models.modelListIglesias;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView listview;
    private AdapterListIg adapter;
    private RequestQueue requestQueue;
    private JsonObjectRequest mStringRequest;
    public ArrayList<modelListIglesias> model = new ArrayList<>();
    private ProgressDialog progress;

    public modelListIglesias m = new modelListIglesias();
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getData();
        adapter = new AdapterListIg(this, model);
        listview = (ListView) findViewById(R.id.listaIglesias);
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
        String url = "https://apiecucaristica.herokuapp.com/home/iglesias";
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
                        Toast.makeText(getApplicationContext(), "Error al obtener la lista de iglesias", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("error", String.valueOf(error));

                }
            });

            requestQueue.add(mStringRequest);
        } catch (Exception e) {
            Log.e("Exception", String.valueOf(e));
        }


    }


}

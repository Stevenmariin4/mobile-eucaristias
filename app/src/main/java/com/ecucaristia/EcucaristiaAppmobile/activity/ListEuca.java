package com.ecucaristia.EcucaristiaAppmobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ecucaristia.EcucaristiaAppmobile.MainActivity;
import com.ecucaristia.EcucaristiaAppmobile.R;
import com.ecucaristia.EcucaristiaAppmobile.adapter.AdapterListE;
import com.ecucaristia.EcucaristiaAppmobile.models.modelListEucaristias;
import com.ecucaristia.EcucaristiaAppmobile.models.modelListIglesias;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListEuca extends AppCompatActivity {
    private ListView listview;
    private AdapterListE adapter;
    private RequestQueue requestQueue;
    private JsonObjectRequest mStringRequest;
    public int idIglesia;
    public ArrayList<modelListEucaristias> model = new ArrayList<>();
    public modelListEucaristias m = new modelListEucaristias();
    public Enviroment env = new Enviroment();
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_euca);
        Bundle extras = getIntent().getExtras();
        idIglesia = extras.getInt("id");
        adapter = new AdapterListE(this, model);
        listview = (ListView) findViewById(R.id.lv_lisE);
        getData(idIglesia);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    modelListEucaristias model = (modelListEucaristias) adapter.getItem(position);
                    Intent intent = new Intent(ListEuca.this, EscannerCoder.class);
                    intent.putExtra("id", String.valueOf(model.getId()));
                    intent.putExtra("idIglesia", idIglesia);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getData(int id) {
        String url = env.getListReservas();
        requestQueue = Volley.newRequestQueue(this);
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("id", id);
        JSONObject jsonObj = new JSONObject(params);
        try {
            progress = ProgressDialog.show(this, "Cargando Espere un momento",
                    "Cargando", true);
            mStringRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Response", String.valueOf(response));
                    try {
                        JSONArray datos = response.getJSONArray("body");
                        for (int i = 0; i < datos.length(); i++) {
                            JSONObject objeto = datos.getJSONObject(i);
                            String fechaCompleta = objeto.getString("fechaCompleta");
                            String day = objeto.getString("dia");
                            int id = objeto.getInt("idAgenda");
                            m = new modelListEucaristias();
                            m.setFecha(fechaCompleta);
                            m.setDia(day);
                            m.setId(id);
                            model.add(m);
                        }
                        //adapter = new AdapterListIg(getApplicationContext(), model);
                        listview.setAdapter((ListAdapter) adapter);
                        progress.dismiss();
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "Error al obtener la lista de eucaristias", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), (CharSequence) error,Toast.LENGTH_SHORT).show();
                    Log.e("error", String.valueOf(error));

                }
            });

            requestQueue.add(mStringRequest);
        } catch (Exception e) {
            Log.e("Exception", String.valueOf(e));
        }


    }
}

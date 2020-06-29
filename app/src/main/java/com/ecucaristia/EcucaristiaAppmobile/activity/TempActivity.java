package com.ecucaristia.EcucaristiaAppmobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ecucaristia.EcucaristiaAppmobile.MainActivity;
import com.ecucaristia.EcucaristiaAppmobile.R;
import com.ecucaristia.EcucaristiaAppmobile.models.modelListEucaristias;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TempActivity extends AppCompatActivity {
    public String id;
    EditText temperatura;
    TextView names;
    TextView lastnames;
    public String Nombre_Completo;
    Button btnEnviar;
    private RequestQueue requestQueue;
    private JsonObjectRequest mStringRequest;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dialog_temp);
        Bundle extras = getIntent().getExtras();
        id = extras.getString("id");
        temperatura = (EditText) findViewById(R.id.edTemp);
        names = (TextView) findViewById(R.id.namesTemp);
        lastnames = (TextView) findViewById(R.id.lastNamesTemp);
        names.setText(extras.getString("name"));
        lastnames.setText(extras.getString("apellido"));
        Nombre_Completo = extras.getString("name") + " " + extras.getString("apellido");

        btnEnviar = (Button) findViewById(R.id.btnEnviar);
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("Temperatura", String.valueOf(temperatura.getText()));
                String temp = String.valueOf(temperatura.getText());
                if (temp.equals("")) {
                    Toast.makeText(getApplicationContext(), "Debe ingresar la temperatura", Toast.LENGTH_SHORT).show();
                } else {
                    SetgetData(id, temp);
                }

            }
        });


    }

    public void SetgetData(String id, String tem) {
        String url = "https://apiecucaristica.herokuapp.com/home/update/reserva";
        requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        params.put("temperatura", tem);
        params.put("names", Nombre_Completo);

        JSONObject jsonObj = new JSONObject(params);
        try {
            progress = ProgressDialog.show(this, "Cargando Espere un momento",
                    "Cargando", true);
            mStringRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Response", String.valueOf(response));
                    try {
                        int datos = response.getInt("status");
                        if (datos == 200) {
                            progress.dismiss();
                            createSimpleDialog();

                        } else {
                            Toast.makeText(getApplicationContext(), "Error al actualizar temperatura", Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                        }
                        //adapter = new AdapterListIg(getApplicationContext(), model);

                    } catch (JSONException e) {
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

    public void createSimpleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("TEMPERATURA")
                .setMessage("ACTUALIZADA")
                .setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent homepage = new Intent(TempActivity.this, EscannerCoder.class);
                                startActivity(homepage);
                            }
                        });

        builder.show();
    }
}

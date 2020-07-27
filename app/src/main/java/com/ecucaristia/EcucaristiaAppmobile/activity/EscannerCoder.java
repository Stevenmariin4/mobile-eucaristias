package com.ecucaristia.EcucaristiaAppmobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import com.ecucaristia.EcucaristiaAppmobile.dataPerson;
import com.ecucaristia.EcucaristiaAppmobile.decoderPerson;
import com.ecucaristia.EcucaristiaAppmobile.models.modelListIglesias;
import com.journeyapps.barcodescanner.CaptureActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class EscannerCoder extends AppCompatActivity {
    private ZXingScannerView scannerView;
    public static TextView resulttextview;
    public static TextView lastNames;
    Button btnScanner;
    Button btnSalir;
    private decoderPerson decoderPer;
    dataPerson DataPersonas;
    private RequestQueue requestQueue;
    private ProgressDialog progress;
    private JsonObjectRequest mStringRequest;
    public String id;
    public int idIglesia;
    public String identification;
    public String idReserva;
    public String Nombres;
    public String Apellido;
    public Enviroment env = new Enviroment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escanner_coder);
        Bundle extras = getIntent().getExtras();
        id = extras.getString("id");
        idIglesia = extras.getInt("idIglesia");
        resulttextview = findViewById(R.id.txScanner);
        lastNames = findViewById(R.id.lastNames);
        DataPersonas = new dataPerson();
        btnScanner = findViewById(R.id.btnScanner);
        btnSalir = findViewById(R.id.btnSalir);
        decoderPer = new decoderPerson();
        btnScanner.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CaptureActivity.class);
                intent.setAction("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "PDF_417");
                startActivityForResult(intent, 0);

            }
        });
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homepage = new Intent(EscannerCoder.this, MainActivity.class);
                startActivity(homepage);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String pfd417 = (String) intent.getStringExtra("SCAN_RESULT");
                DataPersonas = decoderPer.decoderIdentification(pfd417);
                identification = DataPersonas.NoIdentity;
                resulttextview.setText(DataPersonas.Names);
                Nombres = DataPersonas.Names;
                Apellido = DataPersonas.LastNames;
                lastNames.setText(DataPersonas.LastNames);
                getReserva();

                // Handle successful scan
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
                Toast.makeText(this, "Fallo al obtener documento", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void getReserva() {
        String url = env.getReservas();
        requestQueue = Volley.newRequestQueue(this);
        requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("idAgenda", id);
        params.put("document", identification);
        JSONObject jsonObj = new JSONObject(params);
        if (identification.equals("")) {
            Toast.makeText(getApplicationContext(), "Error al obtener datos de usuario", Toast.LENGTH_SHORT).show();
        }
        try {
            progress = ProgressDialog.show(this, "Cargando Espere un momento",
                    "Cargando", true);
            mStringRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Response body", String.valueOf(response));
                    try {
                        JSONArray datos = response.getJSONArray("body");
                        if (datos.length() == 0) {
                            createSimpleDialog();
                            progress.dismiss();
                        } else {
                            progress.dismiss();
                            for (int i = 0; i < datos.length(); i++) {
                                JSONObject objeto = datos.getJSONObject(i);
                                Log.e("onResponse: ", String.valueOf(objeto.getString("idReserva")));
                                idReserva = objeto.getString("idReserva");
                            }

                            Intent intent = new Intent(EscannerCoder.this, TempActivity.class);
                            intent.putExtra("id", idReserva);
                            intent.putExtra("name", Nombres);
                            intent.putExtra("apellido", Apellido);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        progress.dismiss();
                        Toast.makeText(getApplicationContext(), "Error al obtener la reserva", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), (CharSequence) error, Toast.LENGTH_SHORT).show();
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

        builder.setTitle("Usuario")
                .setMessage("No Se Encuentra En Agenda")
                .setPositiveButton("Agendar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                agendar();
                            }
                        })
                .setNegativeButton("CANCELAR",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                startActivity(getIntent());
                            }
                        });

        builder.show();
    }

    public void agendar() {
        Intent viewIntent =
                new Intent("android.intent.action.VIEW",
                        Uri.parse("http://voyeucaristia.teknosoft.co:8080/inicio?key=" + idIglesia));
        startActivity(viewIntent);
    }


}

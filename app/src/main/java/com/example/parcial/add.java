package com.example.parcial;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class add extends AppCompatActivity {

    private Button btnBuscar;
    private EditText txtBuscar;
    private RequestQueue queue;

    //txt
    private TextView tvCancion;
    private TextView tvArtista;
    private TextView tvAlbum;
    private TextView tvDireccion;
    private TextView info;

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        queue = Volley.newRequestQueue(this);

        txtBuscar = (EditText) findViewById(R.id.edtBuscar);
        tvCancion = (TextView) findViewById(R.id.tvCancion);
        tvArtista = (TextView) findViewById(R.id.tvArtista);
        tvAlbum = (TextView) findViewById(R.id.tvAlbum);
        tvDireccion = (TextView) findViewById(R.id.tvDuracion);
        info = (TextView) findViewById(R.id.info);
        btnBuscar = (Button)findViewById(R.id.btnBuscar);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Buscar();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.insert_music:
                addMusic();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addMusic(){
        //con el metodo addMusic() paso el nombre de la cancion al MainActivity, para eso valido que no se pasen datos nulos

        if(tvCancion.getText() != ""){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("NewMusic", tvCancion.getText());
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(), "not", Toast.LENGTH_LONG).show();
        }
    }

    public void Buscar(){

        //la url del parcial que dejo el profesor aparece con http pero al probarlo me daba error, investigando encontre una solucion
        //y es cambiar el http por https, no quiere decir que sea la solucion  para todos los casos, pero por lo menos este si lo soluciono.
        //tambien hice unas modificaciones en el manifest, leer los comentarios
        //
        //La variable BuscarCancion es con la cual capturo el texto que el usuario escribe en el EditText
        //por lo tanto lo concateno en la url para realizar la busqueda de la cancion, esta peticion me retorna la cancion buscada en la primera
        //pocision del json por lo que parseo el json de respuesta y capturo solo el de la primera posicion porque es hay donde esta la cancion
        //buscada

        String BuscarCancion = txtBuscar.getText().toString();

        String Url = "https://ws.audioscrobbler.com/2.0/?method=track.search&track="+BuscarCancion+"&api_key=b284db959637031077380e7e2c6f2775&format=json";

        JsonObjectRequest request  = new JsonObjectRequest(Request.Method.GET, Url, (String) null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //code of return the api
                    try {

                        JSONObject jsonObjectResults = response.getJSONObject("results");
                        JSONObject jsonObjectTrackmatches = jsonObjectResults.getJSONObject("trackmatches");
                        JSONArray jsonArrayTrack = jsonObjectTrackmatches.getJSONArray("track");

                        JSONObject jsonObjectMusic = jsonArrayTrack.optJSONObject(0);
                        info.setText("Informacion");
                        tvCancion.setText(jsonObjectMusic.getString("name"));
                        tvArtista.setText(jsonObjectMusic.getString("artist"));
                        tvAlbum.setText(jsonObjectMusic.getString("name"));
                        tvDireccion.setText("0");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //code in error
                Log.d(LOG_TAG, "errooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
                error.printStackTrace();
            }
        });
        this.queue.add(request);
    }

}

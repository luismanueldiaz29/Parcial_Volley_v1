package com.example.parcial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.parcial.Music;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private androidx.recyclerview.widget.RecyclerView RecyclerView;
    private AdapterMusic adapterMusic;
    private ArrayList<Music> musics;
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    //no se porque pero por alguna razon no me funcionaba la url cuando solo estaba con http,
    // tuve que cambiar http por https y funciono
    private static final String USGS_REQUEST_URL = "https://ws.audioscrobbler.com/2.0/?method=chart.gettoptracks&api_key=b284db959637031077380e7e2c6f2775&format=json";
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        musics = new ArrayList<Music>();

        //con esta condicional capturo el dato que me pasa la activity add
        //y lo primero que hago es que valido que no me pasen datos nulos,
        //si no son datos nulos entonces agregan la nueva cancion a la playlist
        //que solo seria agregarlo al ArrayList llamada musics que en el metodo GetData()
        //lo agregar en el RecyclerView junto con los demas datos que son consultados
        //desde la api
        if(getIntent().getStringExtra("NewMusic") != null){
            String name = getIntent().getStringExtra("NewMusic");
            Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG ).show();
            musics.add(new Music(name, "0"));
        }

        queue = Volley.newRequestQueue(this);
        GetData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.insert_music:
                Toast.makeText(getApplicationContext(), "Add music", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(this, add.class);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void GetData(){
        //la url que le paso al JsonObjectRequest le cambie el http por https porque inicialmente me daba error
        // pero investigando encontre esa solucion, no creo que funcione para todos los casos pero por lo menos para este si,
        // tambien hice unas modificaciones en el manifest que son importantes, leer los comentarios que hay deje.
        //
        //con volley hago una peticion de tipo GET y este me debe retornar una respuesta que la capturo con
        //un JsonObjectRequest y luego parseo el objeto tipo json para poder pasar esos datos en el ArrayList
        //que se llama musics el cual lo paso al adaptador que me retorna el estilo que luego se muestra en el RecyclerView

        JsonObjectRequest request  = new JsonObjectRequest(Request.Method.GET, USGS_REQUEST_URL, (String) null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //code of return the api
                    try {
                        RecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
                        RecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


                        JSONObject jsonObjectContacts = response.getJSONObject("tracks");
                        JSONArray jsonArrayTrack = jsonObjectContacts.getJSONArray("track");

                        for(int i=0; i < jsonArrayTrack.length(); i++){

                            JSONObject JsonObjectMusic = jsonArrayTrack.getJSONObject(i);
                            String name = JsonObjectMusic.getString("name");
                            String duration = JsonObjectMusic.getString("duration");

                            musics.add(new Music(name, duration));
                        }

                        adapterMusic = new AdapterMusic(musics);
                        RecyclerView.setAdapter(adapterMusic);

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

    public ArrayList<Music> Musics(){
        ArrayList<Music> musics = new ArrayList<>();
        for (int i =0; i<20; i++) {
            Music music = new Music("lala", "nono");
            musics.add(music);
        }
        return musics;
    }
}

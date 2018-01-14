package ep.rest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtikliListActivity extends AppCompatActivity implements Callback<List<Artikel>> {
    private static final String TAG = MainActivity.class.getCanonicalName();

    private SwipeRefreshLayout container;
    private Button button1,button2;
    private ListView list;
    private ArtikelAdapter adapter;
    private Stranka stranka;
    private String geslo;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artikel_list);
        list = (ListView) findViewById(R.id.items);

        final Intent intent = getIntent();
        stranka = (Stranka) intent.getSerializableExtra("ep.rest.stranka");
        geslo = (String) intent.getSerializableExtra("geslo");
        adapter = new ArtikelAdapter(this);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Artikel artikel = adapter.getItem(i);
                if (artikel != null) {
                    final Intent intent = new Intent(ArtikliListActivity.this, ArtikelDetailActivity.class);
                    intent.putExtra("ep.rest.id", artikel.idartikel);
                    intent.putExtra("ep.rest.stranka", stranka);
                    intent.putExtra("geslo", geslo);
                    startActivity(intent);
                }
            }
        });

        container = (SwipeRefreshLayout) findViewById(R.id.container);
        container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ArtikliService.getInstance().getAll().enqueue(ArtikliListActivity.this);
            }
        });

        button1 = (Button) findViewById(R.id.odjava);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(ArtikliListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        button2 = (Button) findViewById(R.id.profil);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(ArtikliListActivity.this, ProfilActivity.class);
                intent.putExtra("ep.rest.stranka",stranka);
                intent.putExtra("geslo",geslo);
                startActivity(intent);
            }

        });

        ArtikliService.getInstance().getAll().enqueue(ArtikliListActivity.this);
    }

    @Override
    public void onResponse(Call<List<Artikel>> call, Response<List<Artikel>> response) {
        final List<Artikel> hits = response.body();

        if (response.isSuccessful()) {
            Log.i(TAG, "Hits: " + hits.size());
            adapter.clear();
            adapter.addAll(hits);
        } else {
            String errorMessage;
            try {
                errorMessage = "An error occurred: " + response.errorBody().string();
            } catch (IOException e) {
                errorMessage = "An error occurred: error while decoding the error message.";
            }
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            Log.e(TAG, errorMessage);
        }
        container.setRefreshing(false);
    }

    @Override
    public void onFailure(Call<List<Artikel>> call, Throwable t) {
        Log.w(TAG, "Error: " + t.getMessage(), t);
        container.setRefreshing(false);
    }
}

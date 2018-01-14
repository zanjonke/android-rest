package ep.rest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtikelDetailActivity extends AppCompatActivity implements Callback<Artikel> {
    private static final String TAG = ArtikelDetailActivity.class.getCanonicalName();
    private Stranka stranka;
    private String geslo;
    private Artikel artikel;
    private TextView tvArtikelDetail;
    private CollapsingToolbarLayout toolbarLayout;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artikel_detail);
        //final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_layout);
        //setSupportActionBar(toolbar);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        tvArtikelDetail = (TextView) findViewById(R.id.tv_artikel_detail);

        stranka = (Stranka) getIntent().getSerializableExtra("ep.rest.stranka");
        geslo = (String) getIntent().getSerializableExtra("geslo");

        final int id = getIntent().getIntExtra("ep.rest.id", 0);
        if (id > 0) {
            ArtikliService.getInstance().get(id).enqueue(this);
        }

        button = (Button) findViewById(R.id.nazaj);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(ArtikelDetailActivity.this, ArtikliListActivity.class);
                intent.putExtra("ep.rest.stranka", stranka);
                intent.putExtra("geslo", geslo);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResponse(Call<Artikel> call, Response<Artikel> response) {
        artikel = response.body();
        Log.i(TAG, "Got result: " + artikel);

        if (response.isSuccessful()) {
            tvArtikelDetail.setText(artikel.opis);
            toolbarLayout.setTitle(artikel.naziv);
        } else {
            String errorMessage;
            try {
                errorMessage = "An error occurred: " + response.errorBody().string();
            } catch (IOException e) {
                errorMessage = "An error occurred: error while decoding the error message.";
            }
            Log.e(TAG, errorMessage);
            tvArtikelDetail.setText(errorMessage);
        }
    }

    @Override
    public void onFailure(Call<Artikel> call, Throwable t) {
        Log.w(TAG, "Error: " + t.getMessage(), t);
    }
}

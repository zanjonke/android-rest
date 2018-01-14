package ep.rest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtikelFormActivity extends AppCompatActivity
        implements View.OnClickListener, Callback<Void> {
    private static final String TAG = ArtikelFormActivity.class.getCanonicalName();

    private EditText naziv, opis, cena, idartikel, aktiviran;
    private Button button;

    private Artikel artikel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artikel_form);

        naziv = (EditText) findViewById(R.id.etTitle);
        cena = (EditText) findViewById(R.id.etPrice);
        opis = (EditText) findViewById(R.id.etDescription);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);

        final Intent intent = getIntent();
        artikel = (Artikel) intent.getSerializableExtra("ep.rest.artikel");
        if (artikel != null) {
            naziv.setText(artikel.naziv);
            cena.setText(String.valueOf(artikel.cena));
            opis.setText(artikel.opis);

        }
    }

    @Override
    public void onClick(View view) {
        final String artikelNaziv = naziv.getText().toString().trim();
        final String artikelOpis = opis.getText().toString().trim();
        final String artikelCena = cena.getText().toString().trim();
        final int artikelAktiviran = 0;

        if (artikel == null) { // dodajanje
            ArtikliService.getInstance().insert(artikelNaziv, artikelOpis, Double.parseDouble(artikelCena),
                    artikelAktiviran).enqueue(this);
        } else { // urejanje
            ArtikliService.getInstance().update(artikel.idartikel, artikelNaziv, artikelOpis, Double.parseDouble(artikelCena),
                    artikelAktiviran).enqueue(this);
        }
    }

    @Override
    public void onResponse(Call<Void> call, Response<Void> response) {
        final Headers headers = response.headers();

        if (response.isSuccessful()) {
            final int id;
            if (artikel == null) {
                Log.i(TAG, "Insertion completed.");
                // Preberemo Location iz zaglavja
                final String[] parts = headers.get("Location").split("/");
                id = Integer.parseInt(parts[parts.length - 1]);
            } else {
                Log.i(TAG, "Editing saved.");
                id = artikel.idartikel;
            }
            final Intent intent = new Intent(this, ArtikelDetailActivity.class);
            intent.putExtra("ep.rest.id", id);
            startActivity(intent);
        } else {
            String errorMessage;
            try {
                errorMessage = "An error occurred: " + response.errorBody().string();
            } catch (IOException e) {
                errorMessage = "An error occurred: error while decoding the error message.";
            }
            Log.e(TAG, errorMessage);
        }
    }

    @Override
    public void onFailure(Call<Void> call, Throwable t) {
        Log.w(TAG, "Error: " + t.getMessage(), t);
    }
}

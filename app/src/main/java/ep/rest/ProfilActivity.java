package ep.rest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Zhan on 13/01/2018.
 */

public class ProfilActivity extends AppCompatActivity {
    private static final String TAG = ProfilActivity.class.getCanonicalName();

    private EditText ime, priimek, email, naslov, telefon, geslo;
    private Button button1, button2;
    private Stranka stranka;
    private String gesloS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        final Intent intent = getIntent();
        stranka = (Stranka) intent.getSerializableExtra("ep.rest.stranka");
        gesloS = (String) intent.getSerializableExtra("geslo");
        ime = (EditText) findViewById(R.id.imeEt);
        priimek = (EditText) findViewById(R.id.priimekEt);
        email = (EditText) findViewById(R.id.emailEt);
        naslov = (EditText) findViewById(R.id.naslovEt);
        telefon = (EditText) findViewById(R.id.telefonEt);
        geslo = (EditText) findViewById(R.id.gesloEt);

        if(stranka != null){
            ime.setText(stranka.ime);
            priimek.setText(stranka.priimek);
            email.setText(stranka.email);
            naslov.setText(stranka.naslov);
            telefon.setText(stranka.telefon);
            geslo.setText(gesloS);
        }
        button1 = (Button) findViewById(R.id.posodobi);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String uporabnik_email = email.getText().toString().trim();
                final String id = stranka.idstranka;
                final String imeS = ime.getText().toString().trim();
                final String priimekS = priimek.getText().toString().trim();
                final String emailS = email.getText().toString().trim();
                gesloS = geslo.getText().toString().trim();
                final String naslovS = naslov.getText().toString().trim();
                final String telefonS = telefon.getText().toString().trim();
                StrankaService.getInstance().update(id, imeS, priimekS, emailS, gesloS, naslovS, telefonS, stranka.aktiviran).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()){
                            System.out.println("Posodabljanje uspesno!!");
                            stranka.ime = imeS;
                            stranka.priimek = priimekS;
                            stranka.email = emailS;
                            stranka.naslov = naslovS;
                            stranka.telefon = telefonS;
                            stranka.idstranka = id;
                            final Intent intent = new Intent(ProfilActivity.this, ArtikliListActivity.class);
                            intent.putExtra("ep.rest.stranka", stranka);
                            intent.putExtra("geslo", gesloS);
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
                        System.out.println("POSODABLJANJE NEUSPEŠNO: " + t.getMessage());
                    }
                });
            }
        });

        button2 = (Button) findViewById(R.id.nazaj);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(ProfilActivity.this, ArtikliListActivity.class);
                intent.putExtra("ep.rest.stranka", stranka);
                intent.putExtra("geslo", gesloS);
                startActivity(intent);
            }
        });

    }

}

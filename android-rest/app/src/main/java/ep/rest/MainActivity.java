package ep.rest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import javax.xml.transform.Result;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getCanonicalName();

    private Button login;
    private EditText email;
    private EditText geslo;
    private String gesloS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = (EditText) findViewById(R.id.email);
        geslo = (EditText) findViewById(R.id.geslo);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uporabnik_email = email.getText().toString().trim();
                final String uporabnik_geslo = geslo.getText().toString().trim();
                gesloS = uporabnik_geslo;
                LoginService.getInstance().login(uporabnik_email, uporabnik_geslo).enqueue(new Callback<Stranka>() {
                    @Override
                    public void onResponse(Call<Stranka> call, Response<Stranka> response) {
                        Stranka s = response.body();
                        if(s == null){
                            final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                            dialog.setMessage("Napacen email ali geslo!");
                            dialog.setPositiveButton("Ok",null);
                            dialog.create().show();
                        } else {
                            if(s.tip == 0){
                                final Intent intent = new Intent(MainActivity.this, ArtikliListActivity.class);
                                intent.putExtra("ep.rest.stranka",s);
                                intent.putExtra("geslo",gesloS);
                                startActivity(intent);
                            } else {
                                final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                                dialog.setMessage("Niste stranka!");
                                dialog.create().show();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<Stranka> call, Throwable t) {
                        System.out.println("NAPAKA: " + t.getMessage());
                    }
                });
            }
        });

    }

}

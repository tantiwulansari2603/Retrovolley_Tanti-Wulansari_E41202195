package com.example.retrovolley;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class VolleyActivity extends AppCompatActivity {
    private Button btnCloseVolley;
    private Button btnRefreshVolley;
    private ListView lvUserVolley;
    private String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);
        btnCloseVolley = findViewById(R.id.btnCloseVolley);
        btnRefreshVolley = findViewById(R.id.btnRefreshVolley);
        lvUserVolley = findViewById(R.id.lv_userVolley);

        handleSSLHandshake();

        setTitle(R.string.volley);
        getUserFromAPI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.retrofit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(this, AddUserActivity.class);
                intent.putExtra("typeConnection", "voley");
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void actionRefresh(View view) {
        getUserFromAPI();
    }

    public void actionClose(View view){
        finish();
    }

    private void getUserFromAPI() {
        Gson gson = new Gson();
        String URL = "https://192.168.1.70/volley/User_Registration.php";
        ProgressDialog proDialog = new ProgressDialog(this);
        proDialog.setTitle(getString(R.string.volley));
        proDialog.setMessage(getString(R.string.isWait));
        proDialog.show();

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        proDialog.dismiss();
                        UserResponse userResponse = gson.fromJson(response.toString(), UserResponse.class);
                        if (userResponse.getCode() == 200) {
                            UserAdapter adapter = new UserAdapter(getApplicationContext(), userResponse.getUser_list());
                            lvUserVolley.setAdapter(adapter);
                            lvUserVolley.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Toast.makeText(getApplicationContext(), userResponse.getUser_list().get(i).getUser_fullname(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    proDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Volley Error : " +error.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error : "+ error.getMessage());
                }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

    /**
     * Enables https connections
     */
    @SuppressLint("TrulyRandom")
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }
}
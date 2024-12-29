package com.example.managerproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class loginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // ربط عناصر الواجهة
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        progressBar = findViewById(R.id.progress_bar);

        // مستمع زر تسجيل الدخول
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                if (!email.isEmpty() && !password.isEmpty()) {
                    loginUser(email, password);
                } else {
                    Toast.makeText(loginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loginUser(String email, String password) {
        String url = "http://192.168.1.106/mobile/login.php"; // استبدل بـ URL الخاص بك

        progressBar.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        loginButton.setEnabled(true);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                String role = jsonResponse.getString("role");
                                String name = jsonResponse.optString("name", "User");
                                String profileImage = jsonResponse.optString("profile_image", "");

                                // توجيه المستخدم بناءً على الدور
                                switch (role) {
                                    case "Admin":
                                        Intent adminIntent = new Intent(loginActivity.this, adminDashboard.class);
                                        adminIntent.putExtra("name", name);
                                        adminIntent.putExtra("profile_image", profileImage);
                                        startActivity(adminIntent);
                                        break;
                                    case "Manager":
                                        Intent managerIntent = new Intent(loginActivity.this, ManagerPage.class);
                                        managerIntent.putExtra("name", name);
                                        managerIntent.putExtra("profile_image", profileImage);
                                        startActivity(managerIntent);
                                        break;
                                    case "Employee":
                                        Intent employeeIntent = new Intent(loginActivity.this, Employee.class);
                                        employeeIntent.putExtra("name", name);
                                        employeeIntent.putExtra("profile_image", profileImage);
                                        startActivity(employeeIntent);
                                        break;
                                }

                                Toast.makeText(loginActivity.this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(loginActivity.this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(loginActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        loginButton.setEnabled(true);
                        Log.e("Volley Error", error.toString());
                        Toast.makeText(loginActivity.this, "Login failed, please try again", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        queue.add(stringRequest);
    }
}

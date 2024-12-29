package com.example.managerproject;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // ربط عناصر الواجهة
        ImageView profileImageView = findViewById(R.id.profile_image);
        TextView userNameTextView = findViewById(R.id.user_name);
        TextView userEmailTextView = findViewById(R.id.user_email);

        // URL الخاص بملف PHP
        String url = "http://192.168.1.106/mobile/get_user.php?user_id=1";

        // إنشاء طلب باستخدام مكتبة Volley
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {
                                JSONObject user = response.getJSONObject("user");

                                // الحصول على البيانات من الاستجابة
                                String firstName = user.getString("first_name");
                                String lastName = user.getString("last_name");
                                String email = user.getString("email");
                                String profileImage = "http://192.168.1.106/mobile/images/dalia.png" + user.getString("profile_image");

                                // عرض البيانات في واجهة المستخدم
                                userNameTextView.setText(firstName + " " + lastName);
                                userEmailTextView.setText(email);

                                // تحميل الصورة باستخدام Glide
                                Glide.with(Profile.this)
                                        .load(profileImage)
                                        .placeholder(R.drawable.ic_profile) // صورة افتراضية أثناء التحميل
                                        .into(profileImageView);
                            } else {
                                userNameTextView.setText("Error fetching data");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        // إضافة الطلب إلى قائمة الطلبات
        queue.add(jsonObjectRequest);
    }
}

package com.example.managerproject;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class adminDashboard extends AppCompatActivity {

    private TextView employeesCountTextView, projectsCountTextView, reportsCountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // ربط عناصر الواجهة
        employeesCountTextView = findViewById(R.id.employees_count);
        projectsCountTextView = findViewById(R.id.projects_count);
        reportsCountTextView = findViewById(R.id.reports_count);

        // جلب البيانات من قاعدة البيانات
        fetchDashboardData();
    }

    private void fetchDashboardData() {
        // رابط API الذي يعيد البيانات
        String url = "http://localhost/mobile/admin.php";

        // إنشاء طلب JSON
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // استخراج البيانات من JSON
                            int totalEmployees = response.getInt("total_employees");
                            int totalProjects = response.getInt("total_projects");
                            int totalReports = response.getInt("total_reports");

                            // تحديث واجهة المستخدم
                            employeesCountTextView.setText(String.valueOf(totalEmployees));
                            projectsCountTextView.setText(String.valueOf(totalProjects));
                            reportsCountTextView.setText(String.valueOf(totalReports));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(adminDashboard.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // معالجة الأخطاء
                        Toast.makeText(adminDashboard.this, "Error fetching data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // إضافة الطلب إلى قائمة الطلبات
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}

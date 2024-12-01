package com.dev.jahid.proyash.post;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.dev.jahid.proyash.R;
import com.dev.jahid.proyash.authentication.LoginActivity;
import com.dev.jahid.proyash.databinding.FragmentAddBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class AddFragment extends Fragment {

    private FragmentAddBinding binding;
    FirebaseDatabase postDatabase;
    DatabaseReference postReference;
    private String key = "", postPersonName = "",postGroup = "",postPatientType = "",postLocation = "",postDescription = "",phoneNumber1 = "",phoneNumber2 = "",date = "",email = "";
    private static final String ONESIGNAL_REST_API_ID = "YWRlZDAyNTUtYjA1My00Yjg5LWI5M2MtODhjOTYwYWY4MTc4";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.postGroup.setAdapter(new ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.blood_group_list)));

        binding.btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        binding.btnPostSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!initData()) return;
                ProgressDialog progressDialog = new ProgressDialog(requireContext());
                progressDialog.setTitle("তথ্য আপডেট করা হচ্ছে...");
                progressDialog.setMessage("অনুগ্রহ করে অপেক্ষা করুন");
                progressDialog.setCancelable(false);
                progressDialog.show();

                SimpleDateFormat formater = new SimpleDateFormat("dd/mm/yyyy hh:mm a", Locale.ENGLISH);
                Date now = new Date();
                date = formater.format(now);

                postDatabase = FirebaseDatabase.getInstance();
                postReference = postDatabase.getReference("PostData");
                key = postReference.push().getKey();

                SharedPreferences userSharedPrefs = getActivity().getSharedPreferences("com.dev.jahid.proyash.userdata", Context.MODE_PRIVATE);
                email = userSharedPrefs.getString("userEmail","");

                PostModel postModel = new PostModel(key,postPersonName,postGroup,postPatientType,postLocation,postDescription,phoneNumber1,phoneNumber2,date,email,System.currentTimeMillis());
//                Map<String, Object> postDataMap = new HashMap<>();
//                postDataMap.put("postData",postModel);
//                postDataMap.put("createdAt",System.currentTimeMillis());

                postReference.child(key).setValue(postModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                        // Get the data (e.g., title, message)
                        String title = postGroup + " রক্ত প্রয়োজন";
                        String body = postLocation+"";
//                        NotificationSender notificationSender = new NotificationSender(PostAddActivity.this);
//                        notificationSender.sendNotification(title,body);
                        sendNotification(title,body);
                        getActivity().onBackPressed();
                        Toast.makeText(requireContext(), "পোস্ট করা হয়েছে", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                        Toast.makeText(requireContext(), "পোস্ট করা ব্যার্থ হয়েছে", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void sendNotification(String title, String body) {
        OkHttpClient client = new OkHttpClient();

        Log.d("OneSignalTest", title + body);

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        String json = "{"
                + "\"app_id\": \"5ed362d2-3e1a-446b-b45c-0fe5aa95be1c\","
                + "\"included_segments\": [\"All\"],"
                + "\"headings\": {\"en\": \"" + title + "\"},"
                + "\"contents\": {\"en\": \"" + body + "\"},"
                + "\"android_small_icon\": \"proyash_icon\"" // Set your logo here
                + "}";

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                RequestBody requestBody = RequestBody.create(mediaType, json);
                Request request = new Request.Builder()
                        .url("https://onesignal.com/api/v1/notifications")
                        .post(requestBody)
                        .addHeader("Content-Type", "application/json; charset=utf-8")
                        .addHeader("Authorization", ONESIGNAL_REST_API_ID) // replace with your REST API key
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    Log.d("OneSignal", "Notification response: " + response.body().string());
                } catch (Exception e) {
                    Log.e("OneSignal", "Error sending notification", e);
                }
            }
        });
    }


    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("প্রয়াস ২০");
        builder.setIcon(R.drawable.proyash_logo);
        builder.setMessage("অনুগ্রহ করে লগইন করুন!");

        builder.setPositiveButton("লগইন", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(requireActivity(),LoginActivity.class));
            }
        });
        builder.setNegativeButton("পরে", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNeutralButton("সাইন আপ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(requireActivity(),LoginActivity.class));
            }
        });
        builder.create();
        builder.show();
    }

    private boolean isLoggedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    private boolean initData() {
        postPersonName = getActivity().getSharedPreferences("com.dev.jahid.proyash.userdata", Context.MODE_PRIVATE).getString("userName","");
        if (postPersonName.isEmpty()){
            return false;
        }

        postLocation = binding.postLocation.getText().toString();
        if (postLocation.isEmpty()) {
            binding.postLocationLayout.setError("রক্তদানের লোকেশন সিলেক্ট করুন");
            return false;
        }

        postPatientType = binding.postPatientType.getText().toString();
        if (postPatientType.isEmpty()) {
            binding.postLocationLayout.setError("রক্তদানের স্থান নির্বাচন করুন");
            return false;
        }

        phoneNumber1 = binding.postPhone1.getText().toString();
        if (phoneNumber1.isEmpty()) {
            binding.postLocationLayout.setError("মোবাইল নম্বর প্রবেশ করুন");
            return false;
        }

        postGroup = binding.postGroup.getText().toString();
        postDescription = binding.postDescription.getText().toString();
        phoneNumber2 = binding.postPhone2.getText().toString();

        return true;
    }
}
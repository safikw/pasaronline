package com.android.pasaronline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    TextView login;
    EditText nama, email, password, telp;
    Button btnRegister,btnReisterKios;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        nama = findViewById(R.id.etNama);
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        telp = findViewById(R.id.etTelp);
        btnRegister = findViewById(R.id.btnRegister);
        login = findViewById(R.id.tvLogin);
        //btnReisterKios = findViewById(R.id.btnRegisterKios);

        btnRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                checkField(nama);
                checkField(email);
                checkField(password);
                checkField(telp);

                if (valid){
                    //mulai user registrasi
                    fAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser user = fAuth.getCurrentUser();
                            Toast.makeText(RegisterActivity.this, "Akun dibuat", Toast.LENGTH_SHORT).show();
                            DocumentReference df = fStore.collection("Users").document(user.getUid());
                            Map<String,Object> userInfo = new HashMap<>();
                            userInfo.put("Name", nama.getText().toString());
                            userInfo.put("Email", email.getText().toString());
                            userInfo.put("Telp", telp.getText().toString());
                            //
                            userInfo.put("isUser", "1");
                            df.set(userInfo);

                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, "Gagal Membuat Akun", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
    }

    private boolean checkField(EditText textField) {
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }else {
            valid = true;
        }

        return valid;
    }

}
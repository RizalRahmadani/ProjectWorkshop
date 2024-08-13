package rzl.belajar.projectworkshop.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import rzl.belajar.projectworkshop.data.User;
import rzl.belajar.projectworkshop.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding bindingRegister;

    private FirebaseDatabase database;
//    private DatabaseReference reference;
    private FirebaseAuth auth;

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindingRegister = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(bindingRegister.getRoot());

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        bindingRegister.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        bindingRegister.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });





    }


    private void saveData(){
//        reference = database.getReference("users");

        String name = bindingRegister.edtName.getText().toString();
        String email = bindingRegister.edtEmail.getText().toString();
        String password = bindingRegister.edtPassword.getText().toString();
        String konfirmPassword = bindingRegister.edtConfirmPassword.getText().toString();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || konfirmPassword.isEmpty()){
            Toast.makeText(this, "Lengkapi data anda!", Toast.LENGTH_SHORT).show();
        }else if(!email.matches(emailPattern)){
            Toast.makeText(this, "Email anda tidak valid!", Toast.LENGTH_SHORT).show();
        }else if (password.length() < 8){
            Toast.makeText(this, "Password harus lebih 8 karakter!", Toast.LENGTH_SHORT).show();
        }else if(!password.equals(konfirmPassword)){
            Toast.makeText(this, "Password tidak sama!", Toast.LENGTH_SHORT).show();
        }else{
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    DatabaseReference mDatabaseRef = database.getReference().child("users").child(auth.getCurrentUser().getUid());
                    User users = new User(auth.getCurrentUser().getUid(), name, email);
                    mDatabaseRef.setValue(users).addOnCompleteListener(task1 ->{
                        if (task1.isSuccessful()){
                            Toast.makeText(this, "Register Berhasil", Toast.LENGTH_SHORT).show();
                            auth.signOut();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        }else{
                            Toast.makeText(this, "Register gagal!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(this, "Terjadi kesalhan", Toast.LENGTH_SHORT).show();
                }

            });

        }

    }




}
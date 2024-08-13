package rzl.belajar.projectworkshop.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import rzl.belajar.projectworkshop.R;
import rzl.belajar.projectworkshop.databinding.ActivityTaskDetailBinding;

public class TaskDetailActivity extends AppCompatActivity {

    private ActivityTaskDetailBinding binding;

    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private String userId;
    private String taskId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTaskDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
        } else {
            finish();
            return;
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("tasks");

        taskId = getIntent().getStringExtra("taskId");
        String taskName = getIntent().getStringExtra("taskName");
        String deadline = getIntent().getStringExtra("deadline");
        String category = getIntent().getStringExtra("category");
        String description = getIntent().getStringExtra("description");

        taskName = taskName.toLowerCase();
        taskName = taskName.substring(0, 1).toUpperCase() + taskName.substring(1);

        binding.tvNameTask.setText(taskName);
        binding.tvDate.setText(deadline);
        binding.tvKategori.setText(category);
        binding.tvDescription.setText(description);

        binding.btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaskDetailActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        String finalTaskName = taskName;
        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaskDetailActivity.this, EditTaskActivity.class);
                intent.putExtra("taskId", taskId);
                intent.putExtra("taskName", finalTaskName);
                intent.putExtra("deadline", deadline);
                intent.putExtra("category", category);
                intent.putExtra("description", description);
                startActivity(intent);
            }
        });

        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTask();
            }
        });

    }

    private void deleteTask() {
        if (userId != null && taskId != null) {
            databaseReference.child(userId).child(taskId).removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Intent intent = new Intent(TaskDetailActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                    });
        }
    }
}
package com.example.myapplication;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.databinding.ActivitySignupBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;
    private FirebaseAuth auth;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        binding.btnSignup.setOnClickListener(v -> createAccount());
    }

    private void createAccount() {
        String email = binding.etEmail.getText().toString().trim();
        String pw    = binding.etPassword.getText().toString();

        if (!validate(email, pw)) return;

        toggleLoading(true);

        auth.createUserWithEmailAndPassword(email, pw)
                .addOnSuccessListener(task -> {
                    auth.getCurrentUser().sendEmailVerification();   // 인증 메일 전송
                    toggleLoading(false);
                    Toast.makeText(this,
                            "가입 완료! 이메일을 확인하세요.", Toast.LENGTH_LONG).show();
                    finish();   // LoginActivity 로 복귀
                })
                .addOnFailureListener(e -> {
                    toggleLoading(false);
                    String msg = (e instanceof FirebaseAuthUserCollisionException) ?
                            "이미 가입된 이메일입니다" :
                            "가입 실패: " + e.getLocalizedMessage();
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                });
    }

    private boolean validate(String email, String pw) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailLayout.setError("이메일 형식이 아닙니다"); return false;
        } else binding.emailLayout.setError(null);

        if (pw.length() < 6) {
            binding.passwordLayout.setError("6자 이상 입력"); return false;
        } else binding.passwordLayout.setError(null);

        return true;
    }

    private void toggleLoading(boolean show) {
        binding.progress.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.btnSignup.setEnabled(!show);
    }
}

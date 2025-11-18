package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {

    private com.example.myapplication.databinding.ActivityLoginBinding binding;
    private FirebaseAuth auth;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        binding = com.example.myapplication.databinding.ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        auth.signOut();// ← 로컬 토큰 삭제.  매번 새 로그인 필요
        // 이미 로그인 돼 있으면 바로 메인
        //if (auth.getCurrentUser() != null) {
        //    goHome(); return;
        //}

        binding.btnLogin.setOnClickListener(v -> signIn());
        binding.btnGoSignup.setOnClickListener(
                v -> startActivity(new Intent(this, SignupActivity.class)));
    }

    private void signIn() {
        String email = binding.etEmail.getText().toString().trim();
        String pw    = binding.etPassword.getText().toString();

        if (!validate(email, pw)) return;

        toggleLoading(true);

        auth.signInWithEmailAndPassword(email, pw)
                .addOnSuccessListener(task -> {
                    if (auth.getCurrentUser().isEmailVerified()) {
                        goHome();                       // 지도 화면
                    } else {
                        Toast.makeText(this,
                                "메일 인증 후 다시 로그인하세요", Toast.LENGTH_LONG).show();
                        auth.signOut();                 // 세션 제거
                    }
                    toggleLoading(false);
                })
                .addOnFailureListener(e -> { /* 기존 오류 처리 */ });

    }

    private boolean validate(String email, String pw) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailLayout.setError("이메일 형식이 아닙니다"); return false;
        } else binding.emailLayout.setError(null);

        if (pw.length() < 6) {
            binding.passwordLayout.setError("비밀번호는 6자 이상"); return false;
        } else binding.passwordLayout.setError(null);

        return true;
    }

    private void showError(Exception e) {
        String msg = (e instanceof FirebaseAuthInvalidUserException) ?
                "존재하지 않는 계정" :
                (e instanceof FirebaseAuthInvalidCredentialsException) ?
                        "이메일 또는 비밀번호 오류" :
                        "로그인 실패: " + e.getLocalizedMessage();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void toggleLoading(boolean show) {
        binding.progress.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.btnLogin.setEnabled(!show);
    }

    private void goHome() {
        startActivity(new Intent(this, MapsActivity.class));
        finish();
    }
}

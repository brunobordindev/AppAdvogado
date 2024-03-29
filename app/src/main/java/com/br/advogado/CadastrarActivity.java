package com.br.advogado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import config.ConfiguracaoFirebase;

public class CadastrarActivity extends AppCompatActivity {

    EditText edtEmail, edtSenha;
    Button btnCriarConta;
    FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        edtEmail = findViewById(R.id.edit_email);
        edtSenha = findViewById(R.id.edit_senha);
        btnCriarConta = findViewById(R.id.btnCriarConta);

        autenticacao= ConfiguracaoFirebase.getFirebaseAutenticacao();

        btnCriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = edtEmail.getText().toString();
                String senha = edtSenha.getText().toString();

                if(email.isEmpty() || senha.isEmpty()){

                    Toast.makeText(CadastrarActivity.this, "Os campos email e senha são obrigatórios", Toast.LENGTH_SHORT)
                            .show();
                }else{

                    autenticacao.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){

                                Toast.makeText(CadastrarActivity.this,
                                        "Criado com sucesso!",
                                        Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(getBaseContext(), HomeActivity.class));

                            } else {
                                // Em caso de erro, mostrar as mensagens correspondentes
                                String erroExcecao = "";

                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthWeakPasswordException e) {
                                    erroExcecao = "Digite uma senha mais forte!";
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    erroExcecao = "Por favor, digite um e-mail válido!";
                                } catch (FirebaseAuthUserCollisionException e) {
                                    erroExcecao = "E-mail já cadastrado!";
                                } catch (Exception e) {
                                    erroExcecao = "ao cadastrar usuário: " + e.getMessage();
                                }

                                // Montagem da mensagem em caso de erro
                                Toast.makeText(CadastrarActivity.this,
                                        "Erro: " + erroExcecao,
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                }
            }
        });

    }
}
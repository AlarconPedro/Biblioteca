package br.edu.alfaumuarama.aula08_bandodedados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import br.edu.alfaumuarama.aula08_bandodedados.db.Aluno;
import br.edu.alfaumuarama.aula08_bandodedados.db.TbAluno;

public class CadAlunoActivity extends AppCompatActivity {

    EditText txtNomeCad, txtRACad, txtCidadeCad;
    Button btnSalvar, btnCancelar, btnExcluir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_aluno);

        txtNomeCad = findViewById(R.id.txtNomeCad);
        txtRACad = findViewById(R.id.txtRACad);
        txtCidadeCad = findViewById(R.id.txtCidadeCad);
        btnSalvar = findViewById(R.id.btnSalvar);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnExcluir = findViewById(R.id.btnExcluir);

        //pegar o caminho que abriu esta tela
        Intent caminhoTela = getIntent();
        if (caminhoTela != null) {
            //pegando os parametros que vieram no caminho de tela
            Bundle params = caminhoTela.getExtras();

            //so entra se existir parametros
            if (params != null) {
                txtNomeCad.setText(params.getString("nome"));
                txtRACad.setText(String.valueOf(params.getInt("ra")));
                txtCidadeCad.setText(params.getString("cidade"));
                txtRACad.setEnabled(false);
            }
        }

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voltar();
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvar();
            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                excluir();
            }
        });
    }

    private void voltar() {
        //chamando o evento do botao voltar do Android
        onBackPressed();
    }

    private void salvar() {
        //criando a classe aluno com os dados dos campos da tela
        Aluno aluno = new Aluno();
        aluno.nome = txtNomeCad.getText().toString();
        aluno.RA = Integer.parseInt(txtRACad.getText().toString());
        aluno.cidade = txtCidadeCad.getText().toString();

        //salvando os dados da classe aluno no banco de dados
        TbAluno tbAluno = new TbAluno(this);
        tbAluno.salvar(this, aluno);

        voltar();
    }

    private void excluir() {
        //excluindo o aluno no banco de dados pelo RA
        TbAluno tbAluno = new TbAluno(this);
        tbAluno.excluir(this, txtRACad.getText().toString());

        voltar();
    }
}
package br.edu.alfaumuarama.aula08_bandodedados.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BancoDeDados {
    private SQLiteDatabase meuBanco;

    //criando um Singleton, para garantir que esta classe seja instanciada somente uma vez
    private static final BancoDeDados bancoDeDados = new BancoDeDados();

    public static BancoDeDados getInstance() {
        return bancoDeDados;
    }

    public SQLiteDatabase getMeuBanco() {
        return meuBanco;
    }

    private void criarBanco(Context context) {
        //cria o banco de dados, caso nao exista, e abre a conexao
        meuBanco = context.openOrCreateDatabase("meuBanco.db", Context.MODE_PRIVATE, null);
    }

    public void abrirBanco(Context context) {
        if (meuBanco == null) {
            //se o meuBanco nao estiver instanciado, chama o criarBanco
            criarBanco(context);
        }
        else {
            if (!meuBanco.isOpen()) { //if (meuBanco.isOpen() == false)
                //se a conexao estiver fechada, chama o criarBanco
                criarBanco(context);
            }
        }
    }

    public void fecharBanco() {
        //verifico se o banco esta instanciado na memoria
        if (meuBanco != null) {
            //verifica se o banco esta com a conexao aberta
            if (meuBanco.isOpen()) {
                //fecho a conexao do banco de dados
                meuBanco.close();
            }
        }
    }

    public void executarSQL(Context context, String sSQL) {
        try {
            //abrindo a conexao com o banco antes de executar o comando SQL
            abrirBanco(context);

            //executar o comando SQL passado por parametro
            meuBanco.execSQL(sSQL);

            //fechando a conexao com o banco depois de executar o comando SQL
            fecharBanco();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void adicionarNovaColuna(Context context, String tabela, String coluna, String tipoDados) {
        //verifica se a coluna ja existe
        if (existeColunaNaTabela(context, tabela, coluna) == false) {
            //se a coluna nao existir, adiciona a nova coluna na tabela
            executarSQL(context, "ALTER TABLE " + tabela + " ADD COLUMN " + coluna + " " + tipoDados);
        }
    }

    private boolean existeColunaNaTabela(Context context, String tabela, String coluna) {
        try {
            //abrindo a conexao com o banco antes de executar o comando SQL
            abrirBanco(context);

            Cursor cursor = meuBanco.rawQuery("SELECT * FROM " + tabela + " LIMIT 0", null);

            int indiceColuina = cursor.getColumnIndex(coluna);

            //fechando a conexao com o banco depois de executar o comando SQL
            fecharBanco();

            return (indiceColuina != -1);
        }
        catch (Exception ex) {
            return false;
        }
    }
}
package br.edu.alfaumuarama.aula08_bandodedados.db;

import android.content.Context;
import android.database.Cursor;
import java.util.ArrayList;

public class TbAluno {

    //construtor da classe TbAluno
    public TbAluno(Context context) {
        //sql responsavel pela criacao da tabela, caso a mesma ainda nao exista
        String sSQL = "CREATE TABLE IF NOT EXISTS TbAluno (" +
            " id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " nome TEXT, ra INTEGER, cidade TEXT, uf TEXT)";

        BancoDeDados.getInstance().executarSQL(context, sSQL);

        BancoDeDados.getInstance().adicionarNovaColuna(context, "TbAluno", "uf", "TEXT");
    }

    private String addAspas(String texto) {
        return "'" + texto + "'";
    }

    private void inserir(Context context, Aluno aluno) {
        String sSQL =
                "INSERT INTO TbAluno (nome, ra, cidade, uf) VALUES " +
                "(" +
                    addAspas(aluno.nome) + ", " +
                    aluno.RA + ", " +
                    addAspas(aluno.cidade) + ", " +
                    addAspas(aluno.uf) +
                ")";

        BancoDeDados.getInstance().executarSQL(context, sSQL);
    }

    private void alterar(Context context, Aluno aluno) {
        String sSQL =
                "UPDATE TbAluno SET " +
                "  nome = " + addAspas(aluno.nome) + ", " +
                "  ra = " + aluno.RA + ", " +
                "  cidade = " + addAspas(aluno.cidade) + ", " +
                "  uf = " + addAspas(aluno.uf) + " " +
                "WHERE ra = " + aluno.RA;

        BancoDeDados.getInstance().executarSQL(context, sSQL);
    }

    public void salvar(Context context, Aluno aluno) {
        //buscar se existe algum aluno cadastrado com o RA recebido por parametro
        ArrayList<Aluno> listaAluno = buscar(context,"", aluno.RA, "");

        //verifica se encontrou algum aluno com este RA
        if (listaAluno.size() > 0)
            alterar(context, aluno); //se encontrou, faz o UPDATE do aluno
        else
            inserir(context, aluno); //senao encontrou, faz o INSERT do aluno
    }

    public void excluir(Context context, String ra) {
        String sSQL = "DELETE FROM TbAluno WHERE ra = " + ra;
        BancoDeDados.getInstance().executarSQL(context, sSQL);
    }

    public ArrayList<Aluno> buscar(Context context, String nome, int ra, String cidade) {
        /*
        Cursor cursor = BancoDeDados.getInstance().getMeuBanco()
                .rawQuery("SELECT * FROM TbAluno WHERE ra = " + ra, null);
        */
        String condicaoSQL = "";

        if (ra > 0) {
            condicaoSQL = "ra = " + ra;
        }

        //abrindo a conexao com o banco antes de executar a busca
        BancoDeDados.getInstance().abrirBanco(context);

        Cursor cursor = BancoDeDados.getInstance().getMeuBanco().query(
            "TbAluno", //nome da tabela
            new String[] { "nome", "ra", "cidade", "uf" }, //colunas retornadas pela busca
            condicaoSQL, //condicao do WHERE
            null, //parametros da condicao do WHERE
            null, //os campos do Group By
            null, //condicao do having
            "ra", //order by do SQL
            null //limite de registros
        );

        return retornaLista(cursor);
    }

    private ArrayList<Aluno> retornaLista(Cursor cursor) {
        ArrayList<Aluno> listaRetorno = new ArrayList<>();

        //verificando se existem dados retornados pelo SQL
        if (cursor.getCount() > 0) {
            //movendo o cursor para o primeiro registro
            cursor.moveToFirst();

            //buscando o indice das colunas da tabela TbAluno
            int campoNome = cursor.getColumnIndex("nome");
            int campoRA = cursor.getColumnIndex("ra");
            int campoCidade = cursor.getColumnIndex("cidade");
            int campoUF = cursor.getColumnIndex("uf");

            for (int i = 0; i < cursor.getCount(); i++) {
                //populando a classe aluno com os dados do aluno do banco de dados
                Aluno aluno = new Aluno();
                aluno.nome = cursor.getString(campoNome);
                aluno.RA = cursor.getInt(campoRA);
                aluno.cidade = cursor.getString(campoCidade);
                aluno.uf = cursor.getString(campoUF);

                //adicionando o aluno na lista de retorno
                listaRetorno.add(aluno);

                //move o cursor do banco de dados para o proximo registro
                cursor.moveToNext();
            }
        }

        //fechando a conexao com o banco depois de executar a busca
        BancoDeDados.getInstance().fecharBanco();

        return listaRetorno;
    }
}

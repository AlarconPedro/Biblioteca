package br.edu.alfaumuarama.aula08_bandodedados.db;

import java.util.HashMap;

public class Aluno {
    public String nome;
    public int RA;
    public String cidade;
    public String uf;

    //metodo para converter os dados da classe em um HashMap
    public HashMap<String,String> toHashMap() {
        HashMap<String,String> item = new HashMap<>();
        item.put("nome", nome);
        item.put("ra", String.valueOf(RA));
        item.put("cidade", cidade);
        item.put("uf", uf);

        return item;
    }
}

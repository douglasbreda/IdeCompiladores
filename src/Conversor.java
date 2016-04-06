
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author dougl
 */
public class Conversor {

    int[][] tabela = null;
    
    public Conversor() {
        
    }
    
    public int[][] RetornaMatriz(){
        try {
            FileReader arq = new FileReader("arquivo.txt");
            BufferedReader lerArq = new BufferedReader(arq);
            List<Dado> dados = new ArrayList<>();

            Scanner ler = new Scanner(arq);

//            String linha = lerArq.readLine();
            while (ler.hasNext()) {
//                int test = linha.indexOf("{");
//                int test2 = linha.indexOf("}");
                dados.add(new Dado(ler.nextInt(), ler.nextInt()));
//                linha = lerArq.readLine();
            }

            tabela = new int[dados.size()][2];

            for (int i = 0; i < dados.size(); i++) {
                tabela[i][0] = dados.get(i).valor1;
                tabela[i][1] = dados.get(i).valor2;
            }

            for (int i = 0; i < dados.size(); i++) {
                System.out.print(tabela[i][0] + " ");
                System.out.println(tabela[i][1]);
            }

        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n",
                    e.getMessage());
        }
        System.out.println();
        
        return tabela;
    }

}

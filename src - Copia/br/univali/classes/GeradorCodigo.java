
package br.univali.classes;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dougl
 */
public class GeradorCodigo {

    private List<String> lstData = new ArrayList<>();
    private List<String> lstText = new ArrayList<>();
            
    
    public GeradorCodigo() {
    }
    
    //Cria o arquivo com os dados para interpretação de assembly no bipide
    public void CriarArquivoAssembly(List<Simbolos> pListaSimbolos){
        
        pListaSimbolos.stream().filter(simb -> !simb.EhVetor && !simb.EhFuncao).forEach(simbolo ->{
            AdicionarInstrucaoData(simbolo);
            AdicionarInstrucaoText(simbolo);
        });
        
        try {
            GravarArquivo();
        } catch (IOException ex) {
            Logger.getLogger(GeradorCodigo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void GravarArquivo() throws IOException
    {
        FileWriter oArquivo = new FileWriter("assembly.txt");
        PrintWriter oGravar = new PrintWriter(oArquivo);
        
        oGravar.println(".data");
        
        for (String data : lstData) {
            oGravar.println(data);
        }
        
        oGravar.println(".text");
        
        for (String text : lstText) {
            oGravar.println(text);
        }
            oGravar.close();
    }
    
    ///Adiciona uma instrução ao bloco .data
    private void AdicionarInstrucaoData(Simbolos pSimbolo) {
        if (!pSimbolo.Tipo.equals("Comando")) {
            String sInstrucao = pSimbolo.Nome + pSimbolo.Escopo + " : " + pSimbolo.Valor;

            lstData.add(sInstrucao);
        }
    }
    
    //Adiciona uma instrução no bloco .text
    private void AdicionarInstrucaoText(Simbolos pSimbolo) {

        if (pSimbolo.Inicializada) {
            EscreverComandosArquivos(pSimbolo);
        }
    }
    
    //Escreve os comandos no arquivo
    private void EscreverComandosArquivos(Simbolos pSimbolo) {
        if (pSimbolo.Tipo.equals("Comando")) {
            AdicionarEntradaSaida(pSimbolo);
        }
    }
    
    //Adiciona os comandos:
    //LDI   x
    //STO   y
    private String ComandoLoadStorage(Simbolos pSimbolo){
        StringBuilder sbComando = new StringBuilder();
        sbComando.append("LD    ").append(pSimbolo.Valor).append("\n");
        sbComando.append("STO   ").append(pSimbolo.Nome).append(pSimbolo.Escopo).append("\n");
        
        return sbComando.toString();
    }
    
    ///Adiciona comandos de entrada e saida
    private void AdicionarEntradaSaida(Simbolos pSimbolo) {
        if (pSimbolo.Nome.equals("Write")) {
            ComandoWrite(pSimbolo.Valor);
        } else if (pSimbolo.Nome.equals("Read")) {
            ComandoRead(pSimbolo.Valor);
        }
    }
    
    //Define o comando de Write
    private void ComandoWrite(Object pValor)
    {
        StringBuilder sbComando = new StringBuilder();
        sbComando.append("LD    $in_port");
        sbComando.append("\n");
        sbComando.append("STO    ").append(pValor).append("\n");
                
        lstText.add(sbComando.toString());
    }
    
    //Define o comando Read
    private void ComandoRead(Object pValor)
    {
        StringBuilder sbComando = new StringBuilder();
        sbComando.append("LD    $out_port\n");
        sbComando.append("STO    ").append(pValor);
                
        lstText.add(sbComando.toString());
    }
            
}

package br.univali.classes;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dougl
 */
public class GeradorCodigo {

    private List<String> lstData = new ArrayList<>();
    private List<String> lstText = new ArrayList<>();
    private List<String> lstSimboloData = new ArrayList<>();
    private int iEspacoMemoria = 1000;
    private StringBuilder sbTemp = new StringBuilder();
    private List<String> lstOperador = new ArrayList<>();
    
    ///Atribui os valores apenas temporariamente quando há operações com vetores
    public boolean bAtribuirTemporariamente = false;
    
    public GeradorCodigo() {
    }
    //Cria os comandos temporários quando há operações com vetores
    public void CriarValoresTemp(){
        
    }
    
    
    //Atribui os valores simples de LD/LDI e STO
    public void AtribuirText(String pVarStorage,Object pValor, boolean pOperacaoComVetor){
        StringBuilder sbComando = new StringBuilder();
        if (!pOperacaoComVetor) {
            if (PossuiValor(pValor.toString())) {
                if (pValor.toString().matches("[0-9]+")) {
                    sbComando.append("LDI\t\t").append(pValor);
                } else {
                    sbComando.append("LD\t\t").append(pValor);
                }
            } else {
                if (pVarStorage.matches("[0-9]+")) {
                    sbComando.append("LDI\t\t").append(pVarStorage);
                } else {
                    sbComando.append("LD\t\t").append(pVarStorage);
                }
            }
            
            PreencherListaText(sbComando);
        }
    }
    
    private void PreencherListaText(StringBuilder pSbComando){
        
        if (!bAtribuirTemporariamente) {
            lstText.add(pSbComando.toString());
        } else {
            sbTemp.append(pSbComando).append("\n");
        }
    }
    
    private boolean PossuiValor(String pComando){
        return !pComando.equals("");
    }
   
    public void VerificarAdicionarData(Simbolos pSimbolo, String pEscopo){
        
        if (!lstSimboloData.contains(pSimbolo.Nome)) {
            AdicionarInstrucaoData(pSimbolo, pEscopo);
        }
    }
    
    public void AdicionarStorage(String pSto){
        if (!pSto.contains("Global")) {
            lstText.add("STO\t\t" + pSto + "_" + "Global");
        } else {
            lstText.add("STO\t\t" + pSto);
        }
    }

    public void GravarArquivo() throws IOException {
        FileWriter oArquivo = new FileWriter("assembly.asm");
        PrintWriter oGravar = new PrintWriter(oArquivo);

        oGravar.println(".data");

        lstData.stream().forEach((data) -> {
            oGravar.println(data);
        });

        oGravar.println(".text");

        lstText.add("HLT\t\t0");
        lstText.stream().forEach((text) -> {
            oGravar.println(text);
        });
        
        iEspacoMemoria = 1000;
        //oGravar.println("HLT\t\t0");
        oGravar.close();
    }

    ///Adiciona uma instrução ao bloco .data
    private void AdicionarInstrucaoData(Simbolos pSimbolo, String pEscopo) {
        if (!pSimbolo.Tipo.equals("Comando") && !lstSimboloData.contains(pSimbolo.Nome) ) {
            String sInstrucao = "";
            if (!pSimbolo.EhVetor) {
                sInstrucao = pSimbolo.Nome + "_" + pEscopo + " : " + 0;
            }
//            } else {
//                sInstrucao = EscreverValoresVetor(pSimbolo);
//            }
            lstSimboloData.add(pSimbolo.Nome);
            lstData.add(sInstrucao);
        }
    }

    //Escreve os valores dos vetores
    public void EscreverValoresVetor(Simbolos pSimbolo) {
        
        String sRetorno = pSimbolo.Nome + "_" + pSimbolo.Escopo + ": ";

        for (int i = 0; i < Integer.parseInt(pSimbolo.Valor.toString()); i++) {
            sRetorno += "0,";
        }

        sRetorno = sRetorno.substring(0, sRetorno.length() - 1);

        lstData.add(sRetorno);

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

    public void AtribuirOperacao(String pOperacao, Object pValor){
        StringBuilder sbComando = new StringBuilder();
    
        if (pValor != null) {
            if (pValor.toString().matches("[0-9]+")) {
                sbComando.append(pOperacao).append("I").append("\t").append(pValor);
            } else {
                sbComando.append(pOperacao).append("\t\t").append(pValor);
            }
        } else {
            sbTemp.append(pOperacao).append("\t\t").append(iEspacoMemoria).append("\n");
            sbTemp.append("STO\t\t").append(iEspacoMemoria);
        }

        PreencherListaText(sbComando);
    }
    
    private String AdicionarInstrucaoVetor(String pVetor, String pEscopo) {
        StringBuilder sbComando = new StringBuilder();
        String sIndice = pVetor.substring(pVetor.indexOf("[") + 1, pVetor.indexOf("]"));
        pVetor = pVetor.replace("=", "");
        String sVariavel = pVetor.substring(0, pVetor.indexOf("["));

        sbComando.append("LDI\t\t").append(sIndice).append("\n");
        sbComando.append("STO\t\t$indr").append("\n");
        sbComando.append("LDV\t\t").append(sVariavel).append("_").append(pEscopo).append("\n");

        return sbComando.toString();
    }

    public void AdicionarEntradaSaida(Simbolos pSimbolos){
        AdicionarEntradaSaida(pSimbolos, "");
    }
    
    ///Adiciona comandos de entrada e saida
    public void AdicionarEntradaSaida(Simbolos pSimbolo, String pVetorAtual) {
        switch (pSimbolo.Nome) {
            case "Write":
                ComandoWrite(pSimbolo, pVetorAtual);
                break;
            case "Read":
                ComandoRead(pSimbolo, pVetorAtual);
                break;
        }
    }

    //Define o comando de Write
    private void ComandoWrite(Simbolos pSimbolo, String pVetorAtual) {
        StringBuilder sbComando = new StringBuilder();
        
        if (!PossuiValor(pVetorAtual)) {
            if (pSimbolo.Valor.toString().matches("[0-9]+") || pSimbolo.Valor.toString().contains("\"")) {
                sbComando.append("LDI\t\t").append(pSimbolo.Valor).append("\n");
            } else {
                sbComando.append("LD\t\t").append(pSimbolo.Valor).append("_").append(pSimbolo.Escopo).append("\n");
            }
            sbComando.append("STO\t\t$out_port");
            
        } else {
//            sbComando.append("STOV\t").append(pVetorAtual).append("_").append(pSimbolo.Escopo).append("\n");
            sbComando.append("STO\t\t$out_port");
        }

        lstText.add(sbComando.toString());
    }

    //Define o comando Read
    private void ComandoRead(Simbolos pSimbolo, String pVetorAtual) {
        StringBuilder sbComando = new StringBuilder();
        sbComando.append("LD\t\t$in_port\n");
        
        if (!PossuiValor(pVetorAtual)) {
            if (pSimbolo.Valor.toString().matches("[0-9]+")) {
                sbComando.append("STO\t\t").append(pSimbolo.Valor).append("\n");
            } else {
                sbComando.append("STO\t\t").append(pSimbolo.Valor).append("_").append(pSimbolo.Escopo).append("\n");
            }
        } else {
            sbComando.append("STOV\t").append(pVetorAtual).append("_").append(pSimbolo.Escopo).append("\n");
        }

        lstText.add(sbComando.toString());
    }

    //Retorna a string gerada
    public String BuscarAssembly() {

        StringBuilder sbAssembly = new StringBuilder();
        sbAssembly.append(".data").append("\n");
        for (String data : lstData) {
            sbAssembly.append(data);
            sbAssembly.append("\n");
        }
        sbAssembly.append(".text").append("\n");

        for (String text : lstText) {
            sbAssembly.append(text);
            sbAssembly.append("\n");
        }

        return sbAssembly.toString();
    }
    
    //Inicia a escrita do assembly carregando o índice e armazenando na posição 1000
    public void IniciarAssemblyVetor(String pIndice, boolean pAtribuicao, String pVetorAtual, String pEhComando){
        StringBuilder sbComando = new StringBuilder();
        
        if (!pAtribuicao) {
            if (pIndice.matches("[0-9]+")) 
                sbComando.append("LDI\t\t").append(pIndice);
            else
                sbComando.append("LD\t\t").append(pIndice);
            
            sbComando.append("\n");
        }else{
            sbComando.append("LDI\t\t").append(pIndice);
            sbComando.append("\n");
        } 
        if (!pAtribuicao) {
            sbComando.append("STO\t\t").append(iEspacoMemoria);
        } else {
            sbComando.append("STO\t\t$indr").append("\n");
            iEspacoMemoria--;
        }

        if (PossuiValor(pVetorAtual) && pAtribuicao && pEhComando.equals("Write")) {
            sbComando.append("LDV\t\t").append(pVetorAtual + "_" + "Global");
        }
        
        
            

        lstText.add(sbComando.toString());
        iEspacoMemoria++;
    }
    
    //Finali o vetor
    public void FinalizarAssemblyVetor(String pVariavel){
        
        StringBuilder sbComando = new StringBuilder();
        
         if(lstOperador.size() > 0){
            for (String operador : lstOperador) {
                sbComando.append(operador);
            }
        }
         
        sbComando.append("STO\t\t").append(iEspacoMemoria);
        sbComando.append("\n");
        
        sbComando.append("LD\t\t").append(--iEspacoMemoria);
        sbComando.append("\n");
        sbComando.append("STO\t\t$indr");
        sbComando.append("\n");
        sbComando.append("LD\t\t").append(++iEspacoMemoria);
        sbComando.append("\n");
        sbComando.append("STOV\t").append(pVariavel);
        
        lstText.add(sbComando.toString());
    }
    
    ///Seta a lista dos operadores
    public void SetListOperador(List<String> pLista){
        this.lstOperador = pLista;
    }
}

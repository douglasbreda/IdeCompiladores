package br.univali.classes;

import br.univali.arquivos.SemanticError;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import sun.misc.Queue;

/**
 *
 * @author dougl
 */
public class TabelaSimbolos {

    //Contém a tabela de simbolos
    private List<Simbolos> lstSimbolos;

    private List<String> lstErros;

    //Declara o símbolo que a ser adicionado 
    private Simbolos oSimbolo = null;

    //Controla a chave primária da tabela
    private static int iPk = 0;

    private static int iContadorEscopo = 0;

    private String sModificador = "private";
    
    private static int iNumWarnings = 0;

    //Define o escopo da variável
    private Stack<String> pilhaEscopo;

    public TabelaSimbolos() {
        lstSimbolos = new ArrayList<>();
        lstErros = new ArrayList<>();
        pilhaEscopo = new Stack<>();
        pilhaEscopo.push("Global");
        iNumWarnings = 0;
    }

    //Primeiramente cria a variável para depois decidir o nome
    public void CriarVariavelTipo(String pTipo, int pPosicao, int pId) {

        oSimbolo = new Simbolos();
        oSimbolo.Tipo = pTipo;
        oSimbolo.Posicao = pPosicao;
        oSimbolo.Id = pId;
        oSimbolo.Pk += iPk++;
        oSimbolo.Modificador = sModificador;
    }

    //Adiciona um tipo a lista de símbolos
    public void AdicionarNomeVariavel(String pNome) throws SemanticError {

        if (oSimbolo != null) {
            oSimbolo.Nome = pNome;
            AdicionarSimbolo();
        }else{
            if(lstSimbolos.stream().filter(simbolo -> simbolo.Nome.equals(pNome) && simbolo.Escopo.equals("Global")).collect(Collectors.toList()).isEmpty())
                throw new SemanticError("A variável " + pNome + " não foi declarada." );
            else{
                lstSimbolos.stream().filter(simbolo -> simbolo.Nome.equals(pNome) && simbolo.Escopo.equals("Global")).findFirst().get().Usada = true;
            }
        }
    }
    
    //Verifica se ainda não existe a váriavel e outras validações caso necessário
    private boolean ValidarInclusao() {
        return lstSimbolos.stream().filter(simbolo -> simbolo.Nome.equals(oSimbolo.Nome) && simbolo.Escopo.equals(oSimbolo.Escopo)).collect(Collectors.toList()).isEmpty();
    }

    //Adiciona um vetor a lista
    public void CriarVetor(String pTipo, int pPos, int pId) throws SemanticError {
        //CriarVariavelTipo(pTipo, pPos, pId);

        oSimbolo.EhVetor = true;

        //AdicionarSimbolo();
    }

    //Define o escopo padrão, no caso global
    public void DefinirEscopo() {
        DefinirEscopo("Global");
    }

    public void DefinirModificador(String pModificador) {
        if (oSimbolo != null) {
            oSimbolo.Modificador = pModificador;
        }else{
            this.sModificador = pModificador;
        }
    }

    //Define o escopo atual passado por parâmetro
    public void DefinirEscopo(String pEscopo) {
        if (!pEscopo.equals("Global")) {
            if(!pilhaEscopo.peek().equals("Global"))
                pilhaEscopo.pop();
            
            pilhaEscopo.push(pEscopo + ++iContadorEscopo);

        }
    }

    //Remove o da pilha, a menos que seja o global que é o padrão
    private void RemoverEscopo() {
        if (!pilhaEscopo.peek().equals("Global")) {
            pilhaEscopo.pop();
        }
    }

    //Método para adicionar um simbolo a tabela
    private void AdicionarSimbolo() throws SemanticError {
        oSimbolo.Escopo = pilhaEscopo.peek();

        if (ValidarInclusao()) {
            lstSimbolos.add(oSimbolo);
            //RemoverEscopo();
            oSimbolo = null;
        } else {
            throw new SemanticError("A variável '" + oSimbolo.Nome + "' foi declarada mais de uma vez", oSimbolo.Posicao);
        }
    }

    //Verifica se uma variável que está sendo atribuida já foi declarada
    public void VerificarVariaveis(String pNome) throws SemanticError {
        if (oSimbolo == null) {
            if (lstSimbolos.stream().filter(simbolo -> simbolo.Nome.equals(pNome) && simbolo.Escopo.equals(pilhaEscopo.peek())).collect(Collectors.toList()).isEmpty()) {
                throw new SemanticError("A variável '" + pNome + "' não foi declarada");
            } else {
                oSimbolo = lstSimbolos.stream().filter(simbolo -> simbolo.Nome.equals(pNome) && simbolo.Escopo.equals(pilhaEscopo.peek())).findFirst().get();
                oSimbolo.Usada = true;
            }
        }else{
            oSimbolo = lstSimbolos.stream().filter(simbolo -> simbolo.Nome.equals(pNome) && simbolo.Escopo.equals(pilhaEscopo.peek())).findFirst().get();
            if(!oSimbolo.Inicializada)
                throw new SemanticError("A variável '" + oSimbolo.Nome + "' deve ser inicializada");
        }
    }

    //Busca as variáveis que foram declaradas porém nunca utilizadas
    public String BuscarVariaveisNaoUtilizadas() {
        StringBuilder sbMensagemRetorno = new StringBuilder();
        for (Simbolos simbolo : lstSimbolos) {
            if (!simbolo.Usada && !simbolo.EhFuncao && !simbolo.Tipo.equals("Comando")) {
                iNumWarnings++;
                sbMensagemRetorno.append("A variável '").append(simbolo.Nome).append("' nunca é utilizada.").append("\n");
            }
        }

        return sbMensagemRetorno.toString();
    }

    //Seta o símbolo como utilizado quando tem uma valor atribuído a ele
    public void AtribuirValorVariavel() throws SemanticError {
        if (oSimbolo != null ) {
//            if(oSimbolo.Inicializada)
//                oSimbolo.Usada = true;
//            else
            if(!oSimbolo.Inicializada)
                throw new SemanticError("A variável '" + oSimbolo.Nome + "' deve ser inicializada");
        }
//        }else{
//            lstSimbolos.get(lstSimbolos.size() - 1).Usada = true;
//        }
    }
    
    

    ///Retorna a lista de simbolos para a criação da tabela 
    public List<Simbolos> GetListaSimbolos() {
        return this.lstSimbolos;
    }
    
    //Define se o que foi adicionado é uma função, se o simbolo estiver nulo pega o último que foi adicionado
    //pq né, senão dá pau
    public void DefinirFuncao() throws SemanticError{
        if(oSimbolo != null){
            oSimbolo.EhFuncao = true;
            AdicionarSimbolo();
        }
        else
            lstSimbolos.get(lstSimbolos.size() - 1).EhFuncao = true;
    }
    
    //Inicia separadamente o comando read
    public void AdicionarComandoRead(int pPosicao, int pId) throws SemanticError{
        IniciarSimbolosDeComandos("Read", pPosicao, pId);
    }
    
    //Inicia separadamente o comando write
    public void AdicionarComandoWrite(int pPosicao, int pId) throws SemanticError{
        IniciarSimbolosDeComandos("Write", pPosicao, pId);
    }
    
    //Inicia o símbolo e adiciona na lista para comandos como Read e Write
    private void IniciarSimbolosDeComandos(String pNomeComando, int pPosicao, int pId) throws SemanticError
    {
        oSimbolo = new Simbolos();
        oSimbolo.Nome = pNomeComando;
        oSimbolo.Escopo = pilhaEscopo.peek();
        oSimbolo.Inicializada = true;
        oSimbolo.Id = pId;
        oSimbolo.Posicao = pPosicao;
        oSimbolo.Pk = iPk++;
        oSimbolo.Modificador = this.sModificador;
        oSimbolo.Tipo = "Comando";
        
        AdicionarSimbolo();
    }
    //Remove o último escopo adicionado para sair das funções
    public void RemoverUltimoEscopo(){
        if (!pilhaEscopo.peek().equals("Global")) {
            pilhaEscopo.pop();
            oSimbolo = null;
        }
    }
    
    ///Define se a função tem parâmetros
    public void DefinirParametros(String pNome) throws SemanticError{
        lstSimbolos.get(lstSimbolos.size() - 1 ).Parametros = true;
        oSimbolo.Parametros = true;
        oSimbolo.Nome = pNome;
        
        AdicionarSimbolo();
    }
    
    //Define o momento em que uma variável é inicializada
    public void InicializarVariavel(){
        lstSimbolos.get(lstSimbolos.size() - 1).Inicializada = true;
    }
    
    ///Retorna o número de warnings somente para atualização na tela
    public int GetNumWarnings(){
        return iNumWarnings;
    }
}

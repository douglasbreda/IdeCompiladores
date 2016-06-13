package br.univali.classes;

import br.univali.arquivos.SemanticError;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 *
 * @author dougl
 */
public class TabelaSimbolos {

    //Contém a tabela de simbolos
    private List<Simbolos> lstSimbolos;

    private List<String> lstErros;

    public GeradorCodigo oGeradorCodigo = null;

    //Declara o símbolo que a ser adicionado 
    private Simbolos oSimbolo = null;

    //Controla a chave primária da tabela
    private static int iPk = 0;

    private static int iContadorEscopo = 0;

    private String sModificador = "private";

    private static int iNumWarnings = 0;

    //Guarda o nome do simbolo que terá seu valor atribuído
    //pois estava pegando sempre o último e se fosse atribuído em ordem alternada eram atribuídos valores errados
    private String sSimboloAtual = "";

    //Define o escopo da variável
    private Stack<String> pilhaEscopo;

    //Define qual a variável será armazenada (STO)
    private String sVarSTO = "";

    ///A variável que está sendo atribuida
    private String sVariavelSetar = "";

    //Define os operadores
    private String sOperacao = "";

    //Finaliza uma expressão para determinar o seu fim
    private boolean bFinalizarExpressao = false;

    private boolean bAtribuindo = false;

    private boolean bJaDeclarado = false;
    
    private String sIndiceVetor = "";
    
    private Object oValorAtual = null;
    
    private boolean bOperacaoComVetor = false;
    
    private String sVetorAtual = "";
    
    private String sEscopoTemp = "";

    public TabelaSimbolos() {
        lstSimbolos = new ArrayList<>();
        lstErros = new ArrayList<>();
        pilhaEscopo = new Stack<>();
        pilhaEscopo.push("Global");
        iNumWarnings = 0;
        oGeradorCodigo = new GeradorCodigo();
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
    
    //#2
    //Adiciona um tipo a lista de símbolos
    public void AdicionarNomeVariavel(String pNome) throws SemanticError {

        if (oSimbolo != null) {
            oSimbolo.Nome = pNome;
            sSimboloAtual = pNome;
            sEscopoTemp = oSimbolo.Escopo.equals("") ? pilhaEscopo.peek() : oSimbolo.Escopo;
            oGeradorCodigo.VerificarAdicionarData(oSimbolo, sEscopoTemp);
            bJaDeclarado = true;
            AdicionarSimbolo();
        } else {
            if (lstSimbolos.stream().filter(simbolo -> simbolo.Nome.equals(pNome) && simbolo.Escopo.equals("Global")).collect(Collectors.toList()).isEmpty()) {
                throw new SemanticError("A variável " + pNome + " não foi declarada.");
            } else {
                lstSimbolos.stream().filter(simbolo -> simbolo.Nome.equals(pNome) && simbolo.Escopo.equals("Global")).findFirst().get().Usada = true;
                Simbolos oSimbTemp = lstSimbolos.stream().filter(simbolo -> simbolo.Nome.equals(pNome) && simbolo.Escopo.equals("Global")).findFirst().get();
                sSimboloAtual = pNome;
                sEscopoTemp = !oSimbTemp.Escopo.equals("") ? pilhaEscopo.peek() : oSimbTemp.Escopo;
                oValorAtual = oSimbTemp.Valor;
                oGeradorCodigo.VerificarAdicionarData(oSimbTemp, sEscopoTemp);
                bJaDeclarado = true;
                
                if(bAtribuindo && bOperacaoComVetor && (sSimboloAtual == null ? sVetorAtual != null : !sSimboloAtual.equals(sVetorAtual)))
                    oGeradorCodigo.AtribuirText(sSimboloAtual + "_" + sEscopoTemp, "", false);
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
        } else {
            this.sModificador = pModificador;
        }
    }

    //Define o escopo atual passado por parâmetro
    public void DefinirEscopo(String pEscopo) {
        if (!pEscopo.equals("Global")) {
            if (!pilhaEscopo.peek().equals("Global")) {
                pilhaEscopo.pop();
            }

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
            //oGeradorCodigo.CriarArquivoAssembly(oSimbolo, true);
            //RemoverEscopo();
            oSimbolo = null;
        } else {
            throw new SemanticError("A variável '" + oSimbolo.Nome + "' foi declarada mais de uma vez", oSimbolo.Posicao);
        }
    }

    //#12
    //Verifica se uma variável que está sendo atribuida já foi declarada
    public void VerificarVariaveis(String pNome) throws SemanticError {
        if (oSimbolo == null) {
            if (lstSimbolos.stream().filter(simbolo -> simbolo.Nome.equals(pNome) && simbolo.Escopo.equals(pilhaEscopo.peek())).collect(Collectors.toList()).isEmpty()) {
                throw new SemanticError("A variável '" + pNome + "' não foi declarada");
            } else {

                lstSimbolos.stream().filter(simbolo -> simbolo.Nome.equals(pNome) && simbolo.Escopo.equals(pilhaEscopo.peek())).findFirst().get().Usada = true;
                Simbolos oSimboloAtribuicao = lstSimbolos.stream().filter(simbolo -> simbolo.Nome.equals(pNome) && simbolo.Escopo.equals(pilhaEscopo.peek())).findFirst().get();
                this.sSimboloAtual = oSimboloAtribuicao.Nome;
                this.oValorAtual = oSimboloAtribuicao.Valor;
                if (!bAtribuindo) {
                    sVariavelSetar = sVarSTO = sSimboloAtual + "_" + oSimboloAtribuicao.Escopo;
                }
            }

        } else {
            Simbolos oSimboloAtribuicao = lstSimbolos.stream().filter(simbolo -> simbolo.Nome.equals(pNome) && simbolo.Escopo.equals(pilhaEscopo.peek())).findFirst().get();
            if (!oSimboloAtribuicao.Inicializada) {
                throw new SemanticError("A variável '" + oSimboloAtribuicao.Nome + "' deve ser inicializada");
            } else {
                sVariavelSetar = oSimbolo.Nome;
                this.sSimboloAtual = oSimbolo.Nome;
            }
            oSimbolo = null;
                    
        }
    }

    //Busca as variáveis que foram declaradas porém nunca utilizadas
    public String BuscarVariaveisNaoUtilizadas() {
        StringBuilder sbMensagemRetorno = new StringBuilder();
        for (Simbolos simbolo : lstSimbolos) {
            if (!simbolo.Usada && !simbolo.EhFuncao && !simbolo.Tipo.equals("Comando") && !simbolo.EhHistorico) {
                iNumWarnings++;
                sbMensagemRetorno.append("A variável '").append(simbolo.Nome).append("' nunca é utilizada.").append("\n");
            }
        }

        return sbMensagemRetorno.toString();
    }

    //#5
    //Seta o símbolo como utilizado quando tem uma valor atribuído a ele
    public void AtribuirValorVariavel(Object pValor) throws SemanticError {
        if (oSimbolo != null) {
            if (!oSimbolo.Inicializada) {
                throw new SemanticError("A variável '" + oSimbolo.Nome + "' deve ser inicializada");
            } else {
                oSimbolo.Valor = pValor;
                sSimboloAtual = oSimbolo.Nome + "_" + oSimbolo.Escopo;
            }
        } else if (pValor != null) {
            if (lstSimbolos.size() > 0) {
                Simbolos oSimboloAtual = null;
                if (!"".equals(sSimboloAtual) && !sSimboloAtual.contains("[")) {
                    lstSimbolos.stream().filter(simbolo -> simbolo.Nome.equals(sSimboloAtual) && simbolo.Escopo.equals(pilhaEscopo.peek())).findFirst().get().Valor = pValor;
                    oSimboloAtual = lstSimbolos.stream().filter(simbolo -> simbolo.Nome.equals(sSimboloAtual) && simbolo.Escopo.equals(pilhaEscopo.peek())).findFirst().get();
                } else {
                    lstSimbolos.get(lstSimbolos.size() - 1).Valor = pValor;
                    oSimboloAtual = lstSimbolos.get(lstSimbolos.size() - 1);
                }
            }
        }
        if (bAtribuindo ) {
            if(!bOperacaoComVetor)
                oGeradorCodigo.AtribuirText(sSimboloAtual, sVarSTO, bOperacaoComVetor);
            else if(!sSimboloAtual.equals(sVetorAtual))
                oGeradorCodigo.AtribuirText(sVarSTO, pValor, false);
            
        } else if (!bJaDeclarado) {
            oGeradorCodigo.AtribuirText(sVarSTO, pValor, bOperacaoComVetor);
        }

        if (!sOperacao.equals("")) {
            oGeradorCodigo.AtribuirOperacao(sOperacao, pValor);
        }

    }

    //#29
    //Define que uma atribuição está iniciada
    public void IniciarAtribuicao() {
        bAtribuindo = true;
    }

    //#34
    //Finalização da atribuição
    public void FinalizarExpressao() {
        if (!bOperacaoComVetor)
            oGeradorCodigo.AdicionarStorage(sVariavelSetar);
        else
            oGeradorCodigo.FinalizarAssemblyVetor(sVetorAtual + "_" + sEscopoTemp);
            
            
        bAtribuindo = false;
        sVarSTO = "";
        bJaDeclarado = false;
    }

    //#22
    //Define o tamanho do vetor
    public void DefinirTamanhoVetor(Object pValor) throws SemanticError {

        lstSimbolos.get(lstSimbolos.size() - 1).Valor = pValor;

        Simbolos oSimboloAtual = lstSimbolos.get(lstSimbolos.size() - 1);

        oGeradorCodigo.VerificarAdicionarData(oSimboloAtual, oSimboloAtual.Escopo);

    }

    ///Retorna a lista de simbolos para a criação da tabela 
    public List<Simbolos> GetListaSimbolos() {
        return this.lstSimbolos;
    }

    //Define se o que foi adicionado é uma função, se o simbolo estiver nulo pega o último que foi adicionado
    //pq né, senão dá pau
    public void DefinirFuncao() throws SemanticError {
        if (oSimbolo != null) {
            oSimbolo.EhFuncao = true;
            AdicionarSimbolo();
        } else {
            lstSimbolos.get(lstSimbolos.size() - 1).EhFuncao = true;
        }
    }

    //Inicia separadamente o comando read
    public void AdicionarComandoRead(int pPosicao, int pId) throws SemanticError {
        IniciarSimbolosDeComandos("Read", pPosicao, pId);
    }

    //Inicia separadamente o comando write
    public void AdicionarComandoWrite(int pPosicao, int pId) throws SemanticError {
        IniciarSimbolosDeComandos("Write", pPosicao, pId);
    }

    //Inicia o símbolo e adiciona na lista para comandos como Read e Write
    private void IniciarSimbolosDeComandos(String pNomeComando, int pPosicao, int pId) throws SemanticError {
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
    public void RemoverUltimoEscopo() {
        if (!pilhaEscopo.peek().equals("Global")) {
            pilhaEscopo.pop();
            oSimbolo = null;
        }
    }

    ///Define se a função tem parâmetros
    public void DefinirParametros(String pNome) throws SemanticError {
        lstSimbolos.get(lstSimbolos.size() - 1).Parametros = true;
        oSimbolo.Parametros = true;
        oSimbolo.Nome = pNome;

        AdicionarSimbolo();
    }

    //#21
    //Define o momento em que uma variável é inicializada
    public void InicializarVariavel() {
        lstSimbolos.get(lstSimbolos.size() - 1).Inicializada = true;
        Simbolos oSimbVariavel = lstSimbolos.get(lstSimbolos.size() - 1);

        sVariavelSetar = oSimbVariavel.Nome + "_" + oSimbVariavel.Escopo;

    }

    ///Retorna o número de warnings somente para atualização na tela
    public int GetNumWarnings() {
        return iNumWarnings;
    }

    //Define na coluna o valor o que foi atribuido entre parenteses nos comandos Write e Read
    public void AtribuirValReadWrite(Object pValor) {
        if (lstSimbolos.get(lstSimbolos.size() - 1).Tipo.equals("Comando")) {
            lstSimbolos.get(lstSimbolos.size() - 1).Valor = pValor;
            oGeradorCodigo.AdicionarEntradaSaida(lstSimbolos.get(lstSimbolos.size() - 1));
        }
    }

    //#24, #25, #26, #27, #28, #29, #30, #31, #32
    //Retorna o comando de acordo com a operação
    public void DefinirOperacao(String pOperador) {

        switch (pOperador) {
            case "+":
                sOperacao = "ADD";
                break;
            case "-":
                sOperacao = "SUB";
                break;
            case "|":
                sOperacao = "ORI";
                break;
            case "&":
                sOperacao = "AND";
                break;
            case "^":
                sOperacao = "XOR";
                break;
            case "<<":
                sOperacao = "SLL";
                break;
            case ">>":
                sOperacao = "SRL";
                break;
            case "~":
                sOperacao = "NOT";
                break;
        }
    }
    
    //#35 cria e inicializa as variáveis que são vetores
    public void AdicionarValoresVetor(){
        Simbolos oSimboloVetor = lstSimbolos.get(lstSimbolos.size() - 1);
        oGeradorCodigo.EscreverValoresVetor(oSimboloVetor);
        sVetorAtual = oSimboloVetor.Nome;
        oSimbolo = null;
    }
    
    //#36
    //Definição do índice do vetor
    public void DefinirIndiceVetor(String pIndice){
        sIndiceVetor = pIndice;
    }
    
    //#37
    //Definição de Assembly do vetor
    public void DefinirAssemblyVetor(){
        bOperacaoComVetor = true;
        oGeradorCodigo.IniciarAssemblyVetor(sIndiceVetor, bAtribuindo, sVetorAtual);
    }
}

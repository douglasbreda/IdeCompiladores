package br.univali.classes;

/**
 *
 * @author dougl
 */
public class Simbolos {
    
    //ID enviado pela merda do GALS
    public int Id = 0;
    //Define a chave primária para controle de tokens já exibidos
    public int Pk = 0;
    //"Chave primária para procurar na tabela em qual escopo tal token está
    public int Fk = 0;
    //Define o nome da variável ou outra estrutura
    public String Nome = "";
    //Define o tipo da variável ou outra estrutura
    public String Tipo = "";
    //Define se a variável foi inicializada
    public boolean Inicializada = false;
    //Define se a variável foi usada
    public boolean Usada = false;
    //Define em qual escopo será utilizado
    public String Escopo = "";
    //Define se possui parâmetros
    public boolean Parametros = false;
    //Não sei o que é
    public int Posicao = -1;
    //Define se é vetor
    public boolean EhVetor = false;
    //Define se é função
    public boolean EhFuncao = false;
    //Define os modificadores Public, private...
    public String Modificador = "";
    //Define o valor da variável
    public Object Valor = null;
    //Controla as variáveis que são de histórico
    public boolean EhHistorico = false;
    //Contém a expressão atribuída a uma variável
    public String Expressao = "";
    //Define qual o operador 
    public String Operador = "";
    
    public Simbolos() 
    {
        
    }
}

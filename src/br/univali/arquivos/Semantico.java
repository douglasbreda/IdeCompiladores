package br.univali.arquivos;


public class Semantico implements Constants
{
    public void executeAction(int action, Token token)	throws SemanticError
    {
        switch(action){
            case 1:
                Ide.tbSimbolos.CriarVariavelTipo(token.getLexeme(), token.getPosition(), token.getId());
                break;
            case 2:
                Ide.tbSimbolos.AdicionarNomeVariavel(token.getLexeme());
                break;
            case 3:
                Ide.tbSimbolos.CriarVetor(token.getLexeme(), token.getPosition(), token.getId());
                break;
            case 4:
                Ide.tbSimbolos.DefinirModificador(token.getLexeme());
                break;
            case 5:
                Ide.tbSimbolos.AtribuirValorVariavel(token.getLexeme());
                break;
            case 6:
                Ide.tbSimbolos.DefinirEscopo("if");
                break;
            case 7:
                Ide.tbSimbolos.DefinirEscopo("else");
                break;
            case 8:
                Ide.tbSimbolos.DefinirEscopo("while");
                break;
            case 9:
                Ide.tbSimbolos.DefinirEscopo("for");
                break;
            case 10:
                Ide.tbSimbolos.DefinirEscopo("dowhile");
                break;
            case 11:
                Ide.tbSimbolos.DefinirEscopo("foreach");
                break;
            case 12:
                Ide.tbSimbolos.VerificarVariaveis(token.getLexeme());
                break;
            case 13:
                Ide.tbSimbolos.DefinirEscopo("função");
                Ide.tbSimbolos.DefinirFuncao();
                break;
            case 14:
                //Ide.tbSimbolos.DefinirEscopo("função");
                break;
            case 15:
                Ide.tbSimbolos.AdicionarComandoRead(token.getPosition(), token.getId());
                break;
            case 16:
                Ide.tbSimbolos.AdicionarComandoWrite(token.getPosition(),token.getId());
                break;
            case 17:
                Ide.tbSimbolos.DefinirEscopo("Read");
                break;
            case 18:
                Ide.tbSimbolos.DefinirEscopo("Write");
                break;
            case 19:
                Ide.tbSimbolos.DefinirParametros(token.getLexeme());
                break;
            case 20:
                Ide.tbSimbolos.RemoverUltimoEscopo();
                break;
            case 21:
                Ide.tbSimbolos.InicializarVariavel();
                break;
            case 22:
                Ide.tbSimbolos.DefinirTamanhoVetor(token.getLexeme());
                break;
                
                
        }
    }	
}

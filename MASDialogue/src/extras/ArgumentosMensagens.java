package extras;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import net.sf.tweety.arg.deductive.examples.DeductiveExample;
import net.sf.tweety.arg.deductive.semantics.DeductiveArgument;
import net.sf.tweety.commons.Formula;
import net.sf.tweety.logics.pl.parser.PlParser;
import net.sf.tweety.logics.pl.reasoner.SimpleReasoner;
import net.sf.tweety.logics.pl.syntax.PlBeliefSet;
import net.sf.tweety.logics.pl.syntax.Proposition;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;


/*
Classe que trata da conversão entre DeductiveArgument para String e vice-versa
*/
public class ArgumentosMensagens {

    private ArrayList<DeductiveArgument> argsDeductive;
    private ArrayList<String> argsString;
    

    public ArgumentosMensagens(String arg){
        converteStringParaArgumento(arg);
    }
    
    

    public ArgumentosMensagens(ArrayList args) {
        if (args != null && args.size()>0 && args.get(0) instanceof DeductiveArgument){
            this.argsDeductive = args;
            prepararArgumentosString(args); 
        }
        else if(args != null && args.size()>0 && args.get(0) instanceof String){
            this.argsString = args;
            argsDeductive = new ArrayList<DeductiveArgument>();
            prepararArgumentosDeductive(args); 
        }
        
    }
    
    
    private void prepararArgumentosDeductive(ArrayList<String> args){
        
        for(int cont = 0; cont < args.size(); cont++){
            
            ArrayList<String> premissas = new ArrayList<String>();
            String temp = "";
            boolean isArgumento = false;
            String argAtual = args.get(cont);
            for(int a=0; a < argAtual.length(); a++){
                if (argAtual.charAt(a) == '<'){
                    isArgumento = true;
                }
                else if (argAtual.charAt(a) == '>'){
                    premissas.add(temp.trim());
                    temp = "";
                    isArgumento = false;
                }
                else if (isArgumento && argAtual.charAt(a)!='{' && argAtual.charAt(a)!='}' && argAtual.charAt(a)!=','){
                    temp += argAtual.charAt(a);
                }
                else if (isArgumento && argAtual.charAt(a) == ','){
                    premissas.add(temp.trim());
                    temp = "";
                }
            }
            
            //criar formulas e criar deductiveArgument            
            PlParser parser = new PlParser();
            
            ArrayList<PropositionalFormula> listaPremissas = new ArrayList<PropositionalFormula>();
            for(int a=0; a<premissas.size()-1;a++){
                try{
                    listaPremissas.add(parser.parseFormula(premissas.get(a)));
                }catch(Exception erro){erro.printStackTrace();}
            }
            

            try{
                PropositionalFormula claim = parser.parseFormula(premissas.get(premissas.size()-1));
                PlBeliefSet candidate = new PlBeliefSet(listaPremissas);
                Set<DeductiveArgument> arguments = new HashSet<DeductiveArgument>();
                SimpleReasoner reasoner = new SimpleReasoner();
                if(reasoner.query(candidate, claim)){
                    arguments.add(new DeductiveArgument(candidate,claim));
                }
                argsDeductive.addAll(arguments);
            }catch(Exception erro){erro.printStackTrace();}
                      
        }
        
        
    }

    private void prepararArgumentosString(ArrayList<DeductiveArgument> args) {
        
        this.argsString = new ArrayList();
        
        for (int cont = 0; cont < args.size(); cont++) {
            DeductiveArgument tempArg = args.get(cont);
            
            argsString.add(tempArg.toString());
        }
    }
    
    
    public void converteStringParaArgumento(String arg){
            ArrayList<String> premissas = new ArrayList<String>();
            String temp = "";
            boolean isArgumento = false;
            String argAtual = arg;
            for(int a=0; a < argAtual.length(); a++){
                if (argAtual.charAt(a) == '<'){
                    isArgumento = true;
                }
                else if (argAtual.charAt(a) == '>'){
                    premissas.add(temp.trim());
                    temp = "";
                    isArgumento = false;
                }
                else if (isArgumento && argAtual.charAt(a)!='{' && argAtual.charAt(a)!='}' && argAtual.charAt(a)!=','){
                    temp += argAtual.charAt(a);
                }
                else if (isArgumento && argAtual.charAt(a) == ','){
                    premissas.add(temp.trim());
                    temp = "";
                }
            }
            
            PlParser parser = new PlParser();
            
            ArrayList<PropositionalFormula> listaPremissas = new ArrayList<PropositionalFormula>();
            for(int a=0; a<premissas.size()-1;a++){
                try{
                    listaPremissas.add(parser.parseFormula(premissas.get(a)));
                }catch(Exception erro){erro.printStackTrace();}
            }
            
            try{
                PropositionalFormula claim = parser.parseFormula(premissas.get(premissas.size()-1));
                PlBeliefSet candidate = new PlBeliefSet(listaPremissas);
                Set<DeductiveArgument> arguments = new HashSet<DeductiveArgument>();
                SimpleReasoner reasoner = new SimpleReasoner();
                if(reasoner.query(candidate, claim)){
                    DeductiveArgument d = new DeductiveArgument(candidate,claim);
                    arguments.add(d);
                    argsString = new ArrayList<String>();
                    argsDeductive = new ArrayList<DeductiveArgument>();
                    argsString.add(d.toString());
                    argsDeductive.add(d);
                }
                
            }catch(Exception erro){
                erro.printStackTrace();
            }
        
    }
    
    
    
    public ArrayList<String> getArgumentosString(){
        return argsString;
    }
    
    public ArrayList<DeductiveArgument> getArgumentosDeductive(){
        return argsDeductive;
    }
    
    public String getArgumentoString(int posicao){
        return argsString.get(posicao);
    }
    public DeductiveArgument getArgumentoDeductive(int posicao){
        return argsDeductive.get(posicao);
    }
    
    
    public static DeductiveArgument getArgumentoInicial(String claim){
        PlParser parser = new PlParser();
        Set<PropositionalFormula> listaPremissas = new HashSet<PropositionalFormula>();
        PropositionalFormula c = null;
        try{
            c = parser.parseFormula(claim);
            listaPremissas.add(c);
        }
        catch(Exception erro){
            erro.printStackTrace();
        }
        DeductiveArgument inicio = new DeductiveArgument(listaPremissas,c);
        return inicio;
    }
}

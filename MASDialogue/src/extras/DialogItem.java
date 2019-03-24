
package extras;

import jade.core.AID;
import java.util.ArrayList;
import net.sf.tweety.arg.deductive.semantics.DeductiveArgument;

/*
Um item na tabela de diálogo
<ID>   <AGENTE EMISSOR>   <ARGUMENTO>   <LISTA DE ATAQUES:nao utilizado nesta versao>
*/
public class DialogItem {
    private int id;
    private AID agEmissor;
    private DeductiveArgument argumento;
    private ArrayList<Integer> ataques;
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AID getAgEmissor() {
        return agEmissor;
    }

    public void setAgEmissor(AID agEmissor) {
        this.agEmissor = agEmissor;
    }

    public DeductiveArgument getArgumento() {
        return argumento;
    }

    public void setArgumento(DeductiveArgument argumento) {
        this.argumento = argumento;
    }

    public ArrayList<Integer> getAtaques() {
        return ataques;
    }

    public void setAtaques(ArrayList<Integer> ataques) {
        this.ataques = ataques;
    }
    
    public void addAtaques(int ataque){
        this.ataques.add(ataque);
    }
    
    public void showItem(){
        System.out.println(id + "    |     " + agEmissor.getLocalName() + "   |    " + argumento);
    }
    
}

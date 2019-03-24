package agentes.argumentacao;

import agentes.AgenteArgumentativo;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.tweety.arg.deductive.semantics.DeductiveArgument;
import net.sf.tweety.logics.pl.parser.PlParser;
import net.sf.tweety.logics.pl.sat.Sat4jSolver;
import net.sf.tweety.logics.pl.sat.SatSolver;
import net.sf.tweety.logics.pl.syntax.Negation;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;

/*
Comportamento chamado quando novos contra-argumentos devem ser encontrados
 */
public class CounterArgumentsBehaviour extends CyclicBehaviour {

    private MessageTemplate msgTemplate;

    public CounterArgumentsBehaviour(Agent ag) {
        super(ag);
        msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.REQUEST_WHENEVER);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive(msgTemplate);
        if (msg != null) {
            AgenteArgumentativo ag = (AgenteArgumentativo) myAgent;
            
            if (ag.getA() != null) {
                //System.out.println(myAgent.getLocalName() + "<-" + msg.getSender().getLocalName() + ": contra-atacar (REQUEST_WHENEVER): " + ag.getA());
                SatSolver.setDefaultSolver(new Sat4jSolver());
                ArrayList<PropositionalFormula> formulas = new ArrayList<PropositionalFormula>();
                formulas.addAll(ag.getA().getSupport());
                formulas.add(ag.getA().getClaim());
                
                for (int cont = 0; cont < formulas.size(); cont++) {
                    Set<DeductiveArgument> contra = ag.getK().getDeductiveArguments((new Negation(formulas.get(cont))).trim());
                    if (contra != null) {
                        for (DeductiveArgument item : contra) {
                            DeductiveArgument temp = new DeductiveArgument(item.getSupport(), item.getClaim().trim());
                            ag.getS().add(temp);
                        }
                    }
                }
                //System.out.println(myAgent.getLocalName() + ": Contra-argumentos atuais: " + ag.getS());
            }
            else{
                //System.out.println(myAgent.getLocalName() + "<-" + msg.getSender().getLocalName() + ": contra-atacar (REQUEST_WHENEVER): " + msg.getContent());
                try{
                    SatSolver.setDefaultSolver(new Sat4jSolver());
                    Set<DeductiveArgument> contra = ag.getK().getDeductiveArguments(new Negation(new PlParser().parseFormula(msg.getContent())));
                    if (contra != null) {
                        for (DeductiveArgument item : contra) {
                            ag.getS().add(item);
                        }
                    }
                }
                catch(Exception erro){
                    erro.printStackTrace();
                }
                //System.out.println(myAgent.getLocalName() + ":Contra-argumentos atuais: " + ag.getS());
            }
        } else {
            block();
        }
    }
}

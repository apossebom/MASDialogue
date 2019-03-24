package agentes.argumentacao;

import agentes.AgenteArgumentativo;
import extras.ArgumentosMensagens;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.tweety.arg.deductive.semantics.DeductiveArgument;

/*
Comportamento que atualiza o argumento atual sendo discutido
 */
public class CurrentArgumentBehaviour extends CyclicBehaviour {

    private MessageTemplate msgTemplate;

    public CurrentArgumentBehaviour(Agent ag) {
        super(ag);
        msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM_REF);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive(msgTemplate);
        if (msg != null) {
            //System.out.println(myAgent.getLocalName() + "<-" + msg.getSender().getLocalName() + ": Um novo argumento esta sendo  discutido. " + msg.getContent());

            ArgumentosMensagens am = new ArgumentosMensagens(msg.getContent());
            ((AgenteArgumentativo)myAgent).setA(am.getArgumentoDeductive(0));
        } else {
            block();
        }
    }
}

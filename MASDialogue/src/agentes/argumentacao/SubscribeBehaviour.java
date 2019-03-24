package agentes.argumentacao;

import agentes.AgenteArgumentativo;
import extras.ArgumentosMensagens;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.tweety.arg.deductive.semantics.DeductiveArgument;

/*
Comportamento responsável por inscrição na lista de falas.
 */
public class SubscribeBehaviour extends CyclicBehaviour {

    private MessageTemplate msgTemplate;

    public SubscribeBehaviour(Agent ag) {
        super(ag);
        msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.REQUEST_WHEN);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive(msgTemplate);
        if (msg != null) {
            //System.out.println(myAgent.getLocalName() + "<-" + msg.getSender().getLocalName() + ": tem argumentos para enviar? Resposta seria true/false.");

            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.SUBSCRIBE);
            try {
                Set<DeductiveArgument> temp = ((AgenteArgumentativo) myAgent).getS();
                if (!temp.isEmpty()) {
                    reply.setContent("true");
                } else {
                    reply.setContent("false");
                }

            } catch (Exception ex) {
                Logger.getLogger(AgenteArgumentativo.class.getName()).log(Level.SEVERE, null, ex);
            }
            myAgent.send(reply);
            //System.out.println("RESPONDEU " + reply.getContent());
        } else {
            block();
        }
    }

}

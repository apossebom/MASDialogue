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
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.tweety.arg.deductive.semantics.DeductiveArgument;

/*
Comportamento que trata de enviar todos os argumentos na base S e limpar a base
 */
public class SendArgumentsBehaviour extends CyclicBehaviour {

    private MessageTemplate msgTemplate;

    public SendArgumentsBehaviour(Agent ag) {
        super(ag);
        msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.CFP);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive(msgTemplate);
        if (msg != null) {
            //System.out.println(myAgent.getLocalName() + "<-" +  msg.getSender().getLocalName() + ": envie seus argumentos (CFP)(base S). Resposta com Propose");

            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.PROPOSE);
            try {
                ArrayList<DeductiveArgument> temp = new ArrayList<DeductiveArgument>();
                temp.addAll(((AgenteArgumentativo) myAgent).getS());
                ArgumentosMensagens am = new ArgumentosMensagens(temp);
                
                reply.setContentObject(am.getArgumentosString());
                myAgent.send(reply);
            ((AgenteArgumentativo) myAgent).getS().clear();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else {
            block();
        }
    }

}

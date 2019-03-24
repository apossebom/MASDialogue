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
import net.sf.tweety.logics.pl.parser.PlParser;

/*
Comportamento que solicita o aprendizado de alguma fórmula
 */
public class LearnFormulaBehaviour extends CyclicBehaviour {

    private MessageTemplate msgTemplate;

    public LearnFormulaBehaviour(Agent ag) {
        super(ag);
        msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.PROPAGATE);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive(msgTemplate);
        if (msg != null) {
            try {
                //System.out.println(myAgent.getLocalName() + "<-" + msg.getSender().getLocalName() + ": aprender uma fórmula");
                String temp = (String) msg.getContentObject();
                PlParser parser = new PlParser();
                ((AgenteArgumentativo) myAgent).getK().add(parser.parseFormula(temp));
                System.out.println(myAgent.getLocalName() + ": K=" + ((AgenteArgumentativo) myAgent).getK());
            } catch (Exception erro) {
                erro.printStackTrace();
            }
        } else {
            block();
        }
    }

}


package agentes.mediacao;



import agentes.AgenteMediador;
import extras.ArgumentosMensagens;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.ArrayList;


/*
Comportamento que trata da solicitação de argumentos a um agente argumentativo
*/
public class RequestArgsBehaviour extends SimpleBehaviour{
    
    private boolean terminou;
    private AgenteMediador med;
    private MessageTemplate msgTemplate;
    
    public RequestArgsBehaviour(AgenteMediador ag){
        terminou = false;
        this.med = ag;
        msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive(msgTemplate);
        if (msg != null) {
            try{
                //System.out.println("      " + msg.getSender().getLocalName() + " : " +  msg.getContentObject().toString());
                
                
                ArrayList<String> aaa = (ArrayList<String>)msg.getContentObject();
                ArgumentosMensagens am = new ArgumentosMensagens(aaa);

                med.getAgenda().addAll(am.getArgumentosDeductive());                
            }
            catch(Exception erro){
                erro.printStackTrace();
            }
            
            
            terminou = true;
        }
        else{
            block();
        }
        
        
    }

    @Override
    public boolean done() {
        return terminou;
    }
    
}

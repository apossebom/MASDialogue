
package agentes.mediacao;


import agentes.mediacao.*;
import agentes.AgenteMediador;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.ArrayList;
import masdialogue.Principal;


/*
Comportamento que recebe as Subscriptions (agentes que possuem argumentos para serem enviados)
A != null ou vazio.
Resposta sempre será "true" ou "false".
*/
public class SubscriptionBehaviour extends SimpleBehaviour{
    private boolean terminou;
    private ArrayList<String> agentesResposta;
    private MessageTemplate msgTemplate;
    private AgenteMediador med;

    
    public SubscriptionBehaviour(AgenteMediador m){
        terminou = false;
        agentesResposta = new ArrayList<String>();
        msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE);
        this.med = m;
    }
    
    @Override
    public void action() {
        ACLMessage msg = myAgent.receive(msgTemplate);
        if(msg!=null){
            try{
                if (!agentesResposta.contains(msg.getSender().getLocalName())){
                    agentesResposta.add(msg.getSender().getLocalName());
                    boolean resposta = Boolean.parseBoolean(msg.getContent());
                    if(resposta == true){
                        //System.out.println("Agente " + msg.getSender().getLocalName() + ": quer falar");
                        if(!med.getWB().contains(msg.getSender())){
                            med.getWB().add(msg.getSender());
                            //System.out.println("Agente " + msg.getSender().getLocalName() + " foi inscrito com sucesso na lista");
                        }
                        else{
                            //System.out.println("Agente " + msg.getSender().getLocalName() + " já esta inscrito na lista");
                        }
                    }
                    else{
                        //System.out.println("Agente " + msg.getSender().getLocalName() + " não quer falar");
                    }
                }
                else{
                    //System.out.println("Agente " + msg.getSender().getLocalName() + " já havia respondido antes. Ignora nova resposta.");
                }
                if(Principal.agentesArgumentativos.length == agentesResposta.size()){
                    //System.out.println("Todos os agentes já responderam à pergunta de quem quer falar " + med.getWB());
                    terminou = true;
                }
            }
            catch(Exception erro){
                erro.printStackTrace();
            }

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

package masdialogue;

import agentes.AgenteMediador;
import extras.MeuEnvironment;
import jade.core.AID;
import jade.core.Profile;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.leap.Properties;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.gateway.JadeGateway;

public class Principal {

    //definição do framework
    public static String agentesArgumentativos[] = {"Maria", "José"};
    public static String agentesArgumentativosBases[] = {"baseAgente1.txt","baseAgente2.txt"};
    
    public static String assuntos[] = {"a"};
    

    public static void main(String[] args) {
        MeuEnvironment m = new MeuEnvironment();

        ContainerController cc = m.getInstance();
        //m.exibirGUIdoProcesso();


        
        /*
        Criar agentes argumentativos e inicializá-los
        */
        
        for (int cont=0; cont < agentesArgumentativos.length; cont++){
            String agentArg = agentesArgumentativos[cont];
            

            AgentController ag1;
            if (cc != null) {
                try {
                    String nomeDoAgente = agentArg;
                    String classeDoAgente = "agentes.AgenteArgumentativo";
                    Object argumentos[] = new Object[1];
                    argumentos[0] = agentesArgumentativosBases[cont];
                    ag1 = cc.createNewAgent(nomeDoAgente, classeDoAgente, argumentos);
                    ag1.start();
                } catch (Exception erro) {
                    erro.printStackTrace();
                }
            }
        }
        
        
        
        /*
        Criar agente mediador e inicializá-lo
        */
        
        AgenteMediador mediador = new AgenteMediador();
        mediador.run();

        
    }

}


package extras;


import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;

/*
Classe responsável por criar o Container principal para que os agentes
argumentativos possam ser inseridos
*/
public class MeuEnvironment {
    private static jade.core.Runtime rt;
    private static jade.core.Profile p;
    private static jade.wrapper.AgentContainer cc;
    
    public static String host = "localhost";
    public static String porta = "1099";
    public static String nomeMainContainer = "ContainerPrincipal";
    
    
    public static jade.wrapper.ContainerController getInstance(){
        
        if (cc == null){

            //Obter uma instância do JADE Runtime
            //importar a classe jade.core.Runtime para não confundir com Runtime do Java
            rt = jade.core.Runtime.instance();
            rt.setCloseVM(true);

            //Criar um Container para o agente
            p = new jade.core.ProfileImpl();
            p.setParameter(jade.core.Profile.MAIN_PORT, porta);
            p.setParameter(jade.core.Profile.MAIN_HOST, host);
            p.setParameter(jade.core.Profile.CONTAINER_NAME, nomeMainContainer);
            cc = rt.createMainContainer(p);
            
        }
        return cc;
    }
    
    public static jade.core.Runtime getRuntime(){
        return rt;
    }
    
    public static jade.core.Profile getProfile(){
        return p;
    }
    
    
    
    
    public static void exibirGUIdoProcesso(){
        ProfileImpl pContainer = new ProfileImpl();
        pContainer.setParameter(jade.core.Profile.MAIN_PORT, porta);
        pContainer.setParameter(jade.core.Profile.MAIN_HOST, host);

        jade.wrapper.AgentContainer cont = rt.createAgentContainer(pContainer);
        try{
            AgentController rma = cc.createNewAgent("rma", "jade.tools.rma.rma", new Object[0]);
            rma.start();
        }
        catch(Exception erro){
            erro.printStackTrace();
        }
    }
            
            
    
}

package agentes;


import agentes.mediacao.RequestArgsBehaviour;
import agentes.mediacao.SubscriptionBehaviour;
import extras.DialogItem;
import jade.core.AID;
import jade.core.Profile;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.leap.Properties;
import jade.wrapper.gateway.JadeGateway;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import masdialogue.Principal;
import net.sf.tweety.arg.deductive.semantics.DeductiveArgument;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;


public class AgenteMediador {
    
    //Propriedades do Mediador
    private ArrayList<DialogItem> DT;
    private ArrayList<AID> listaAgentesArgumentativos;
    private ArrayList<AID> WB;
    private ArrayList<DeductiveArgument> AGENDA;
    
    //Métodos get/set usados
    public ArrayList<AID> getWB(){
        return WB;
    }
    public ArrayList<DeductiveArgument> getAgenda(){
        return AGENDA;
    }
    
    //Agente Jade (GatewayAgent) criado para se comunicar com os argumentativos
    private JadeGateway jadeGateway;
    
    public AgenteMediador(){
        /*
        ETAPAS DO DIÁLOGO
        1) Configurar todo o diálogo (tabela de diálogos, agentes argumentativos, WB, AGENDA
        
        2) Informar argumento inicial e solicitar por ataques
        
        3) Pedir por inscrições
        
        REPETIR ENQUANTO WB for diferente de vazia/nula
        
            4) Pede os argumentos do agente na primeira posição (e remove este agente da lista)
        
            5) Armazena os argumentos recebidos na AGENDA
        
            PARA CADA ARGUMENTO NA AGENDA
            
                6) Informar argumento atual ao grupo
        
                7) Pedir por ataques
        
                8) Pedir por inscrições
        */
        
        
        //1. Configurar todo o diálogo
        DT = new ArrayList<DialogItem>(); 
        WB = new ArrayList<AID>();
        AGENDA = new ArrayList<DeductiveArgument>();
        
        this.listaAgentesArgumentativos = new ArrayList<AID>();
        for(String item : Principal.agentesArgumentativos){
            this.listaAgentesArgumentativos.add(new AID(item, AID.ISLOCALNAME));
        }
        
        //Cria o GatewayAgent
        Properties pp = new Properties();
        pp.setProperty(Profile.MAIN_HOST, "localhost");
        pp.setProperty(Profile.MAIN_PORT, "1099");

        jadeGateway.init(null, pp);
    }
    
    
    
    public void run(){
        /*
        Processo de mediação do diálogo
        */
        
        try{
            for(String assunto : Principal.assuntos){
                
                //2) Informar argumento inicial e solicitar por ataques
                informarArgumentoInicioDialogo(assunto);
                
                
                
                //3) Solicitar por inscrição
                solicitarInscricoes();
                
                
                
                //REPETIR ENQUANTO WB for diferente de vazia/nula
                while(!WB.isEmpty()){
                    
                    //4) Pede os argumentos do agente na primeira posição (e remove este agente da lista)
                    //5) Armazena os argumentos recebidos na AGENDA
                    AID agAtual = WB.get(0);
                    pedirArgumentos(agAtual);
                    
                    
                    
                    // PARA CADA ARGUMENTO NA AGENDA
                    for(int cont=0; cont < AGENDA.size(); cont++){
                        
                        
                        DeductiveArgument argAtual = AGENDA.get(cont);
                        
                        //Validar argumento para verificar se ele ja foi discutido anteriormente
                        boolean validar = validarArgumento(argAtual);
                        
                        if (validar){
                        
                            System.out.println("Argumento atual sendo discutido: " + argAtual);

                            //6) Informar argumento atual ao grupo
                            informarArgumento(argAtual, agAtual);
                        
                            //7) Pedir por ataques
                            solicitarAtaques();
                        
                            //8) Pedir por inscrições
                            solicitarInscricoes();
                            
                        }
                        else{
                            System.out.println("O argumento " + argAtual + " ja foi informado anteriormente");
                        }
                        
                        
                    }
                    
                    
                    
                    
                    //todos os argumentos enviados pelo agente já foram discutidos
                    AGENDA.clear();
                    
                    exibirDT();
                    
                }
                
                
                System.out.println("FIM do diálogo sobre " + assunto );
            }
            
            
        } 
        catch (Exception erro) {
            erro.printStackTrace();
        }
        
        
    }
    
    
    /*
    Ação: Informar argumento inicial aos mediadores
    */
    public void informarArgumentoInicioDialogo(String formula){
        try{
            JadeGateway.execute(new OneShotBehaviour() {
                public void action() {
                    ACLMessage msg = new ACLMessage(ACLMessage.REQUEST_WHENEVER);
                    msg.setContent(formula);
                    for (AID ag : listaAgentesArgumentativos) {
                        msg.addReceiver(ag);
                    }
                    myAgent.send(msg);
                }
            });
        }
        catch(Exception erro){
            erro.printStackTrace();
        }
    }
    
    
    /*
    Ação: solicitar por inscrições em WB
    */
    public void solicitarInscricoes(){
        SubscriptionBehaviour t = new SubscriptionBehaviour(this);
        try{
            JadeGateway.execute(new OneShotBehaviour() {
                public void action() {
                    ACLMessage msg = new ACLMessage(ACLMessage.REQUEST_WHEN);
                    msg.setContent("");
                    for (AID ag : listaAgentesArgumentativos) {
                        msg.addReceiver(ag);
                    }
                    myAgent.send(msg);
                }
            });
            JadeGateway.execute(t);
        }
        catch(Exception erro){
            erro.printStackTrace();
        }
  
    }
    
    
    /*
    Ação: pedir por argumentos ao primeiro agente inscrito em WB
    */
    public void pedirArgumentos(AID agenteAtual){
        AID agentWB = agenteAtual;
        WB.remove(0);
        
        
        try{
            JadeGateway.execute(new OneShotBehaviour() {
                public void action() {
                    ACLMessage msg = new ACLMessage(ACLMessage.CFP);
                    msg.setContent("");
                    msg.addReceiver(agentWB);
                    myAgent.send(msg);
                }
            });
            
            //System.out.println("WB[0] " + agentWB.getLocalName() + " enviando argumentos");
            RequestArgsBehaviour t = new RequestArgsBehaviour(this);
            JadeGateway.execute(t);
            //System.out.println("Argumentos recebidos " + agentWB.getLocalName() + ": " + AGENDA);
            
        }
        catch(Exception erro){
            erro.printStackTrace();
        }
                    
    }
    
    
    /*
    Ação: informar um argumento a ser discutido ao grupo
    Preenche este argumento na DT
    */
    public void informarArgumento(DeductiveArgument argAtual, AID agArg){
        
            DialogItem it = new DialogItem();
            it.setAgEmissor(agArg);
            it.setArgumento(argAtual);
            it.setId(DT.size());
            DT.add(it);


            //6) Informar argumento atual ao grupo

            try{
                JadeGateway.execute(new OneShotBehaviour() {
                    public void action() {
                        ACLMessage msg = new ACLMessage(ACLMessage.INFORM_REF);
                        for(AID a : listaAgentesArgumentativos){
                            msg.addReceiver(a);
                        }
                        msg.setContent(argAtual.toString());
                        myAgent.send(msg);
                    }
                });
            }
            catch(Exception erro){
                erro.printStackTrace();
            }
            
    }
    
    
    /*
    Validar se o argumento pode ou não ser incluído em DT
    */
    public boolean validarArgumento(DeductiveArgument argAtual){
        boolean resultado = true;

        Set<PropositionalFormula> cjto1 = new HashSet();
        cjto1.addAll(argAtual.getSupport());
        cjto1.add(argAtual.getClaim());
        
        for(DialogItem item : DT){
            Set<PropositionalFormula> cjto2 = new HashSet();
            cjto2.addAll(item.getArgumento().getSupport());
            cjto2.add(item.getArgumento().getClaim());
            if(cjto1.equals(cjto2)){
                resultado = false;
                break;
            }
        }
        
        return resultado;
    }
    
    
    /*
    Ação: Solicitar ataque ao argumento atual
    */
    public void solicitarAtaques(){
        try{
            JadeGateway.execute(new OneShotBehaviour() {
                public void action() {
                     //System.out.println("****** 7- Pedir por ataques *******");
                    ACLMessage msg = new ACLMessage(ACLMessage.REQUEST_WHENEVER);
                    for(AID a : listaAgentesArgumentativos){
                        msg.addReceiver(a);
                    }
                    msg.setContent(null);
                    myAgent.send(msg);
                }
            });
        }
        catch(Exception erro){
            erro.printStackTrace();
        }
        
    }
    
    
    /*
    Exibir a Tabela de Diálogos
    */
    public void exibirDT(){
        System.out.println("------------------------------------------");
        System.out.println("TABELA DE DIALOGO");
        System.out.println("id\tagente\targumento");
        for(DialogItem item : DT){
            System.out.println(item.getId() + "\t" + item.getAgEmissor().getLocalName() + "\t"+item.getArgumento());
        }
        System.out.println("------------------------------------------");
        
    }
    
}

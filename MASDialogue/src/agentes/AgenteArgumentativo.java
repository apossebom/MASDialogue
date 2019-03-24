package agentes;

import agentes.argumentacao.CounterArgumentsBehaviour;
import agentes.argumentacao.CurrentArgumentBehaviour;
import agentes.argumentacao.LearnFormulaBehaviour;
import agentes.argumentacao.SendArgumentsBehaviour;
import agentes.argumentacao.SubscribeBehaviour;
import extras.ArgumentosMensagens;
import extras.BaseDeConhecimentos;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.wrapper.StaleProxyException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.tweety.arg.deductive.semantics.DeductiveArgument;
import net.sf.tweety.arg.deductive.syntax.DeductiveKnowledgeBase;
import net.sf.tweety.logics.pl.parser.PlParser;
import net.sf.tweety.logics.pl.sat.Sat4jSolver;
import net.sf.tweety.logics.pl.sat.SatSolver;
import net.sf.tweety.logics.pl.syntax.Negation;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;

public class AgenteArgumentativo extends Agent {

    /*
        Atributos de um agente argumentativo:
        AG = {ag1, ..., agn} para AG sendo um conjunto finito com n>0
        agi= <K, S, A> onde agi \in AG.
     */
    private Set<DeductiveArgument> S;
    private DeductiveArgument A;
    private DeductiveKnowledgeBase K;

    /*
        Métodos get/set para os atributos da classe
     */
    public Set<DeductiveArgument> getS() {
        return S;
    }

    public DeductiveArgument getA() {
        return A;
    }

    public DeductiveKnowledgeBase getK() {
        return K;
    }

    public void setS(Set<DeductiveArgument> x) {
        S.addAll(x);
    }

    public void setA(DeductiveArgument x) {
        A = x;
    }

    public void setK(DeductiveKnowledgeBase x) {
        K = x;
    }

    private void exibirBaseDeConhecimentos(){
        System.out.println("Agente: " + getLocalName() + ": " + K.toString());
    }
    
    /*
        Método setup() é o início da execução do agente.
     */
    public void setup() {
        
        //Registrar no DF como argumentativo
        /*
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName( getAID() );
        ServiceDescription sd  = new ServiceDescription();
        sd.setType( "arguer" );
        sd.setName( getLocalName() );
        dfd.addServices(sd);
        try {  
            DFService.register( this, dfd );  
        }
        catch (Exception erro) {
            erro.printStackTrace(); 
        }
        */
        
        
        //Obter o nome da base de conhecimentos:
        Object argumentos[] = getArguments();
        BaseDeConhecimentos b1 = null;
        if (argumentos != null) {
            b1 = new BaseDeConhecimentos((String) argumentos[0]);
        } else {
            try {
                getContainerController().kill();
            } catch (StaleProxyException ex) {
                Logger.getLogger(AgenteArgumentativo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        
        //inicializar K e S
        K = b1.getBaseDeConhecimentos();
        S = new HashSet<DeductiveArgument>();

        
        /*
        Atribuir todos os Behaviours dos agentes argumentativos
        */
        
        //Quando solicitado, envia seus argumentos na base S
        SendArgumentsBehaviour sab = new SendArgumentsBehaviour(this);
        addBehaviour(sab);
        
        //Quando solicitado, contra-argumenta o argumento em A (ou argumento inicial)
        CounterArgumentsBehaviour cab = new CounterArgumentsBehaviour(this);
        addBehaviour(cab);
        
        //Quando solicitado, é informado de um novo argumento sendo discutido (A)
        CurrentArgumentBehaviour curab = new CurrentArgumentBehaviour(this);
        addBehaviour(curab);
        
        //Quando solicitado, aprende alguma nova fórmula
        //LearnFormulaBehaviour lfb = new LearnFormulaBehaviour(this);
        //addBehaviour(lfb);
        
        //Quando solicitado, informa se deseja ou não emitir seus argumentos
        SubscribeBehaviour sb = new SubscribeBehaviour((this));
        addBehaviour(sb);
        
    }
    
}

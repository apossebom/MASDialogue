package extras;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

import net.sf.tweety.arg.deductive.syntax.DeductiveKnowledgeBase;
import net.sf.tweety.logics.pl.parser.PlParser;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;

/*
Classe que abre o arquivo com os conhecimentos dos agentes e transforma em fórmulas
*/
public class BaseDeConhecimentos {

    private ArrayList<String> formulas;

    public BaseDeConhecimentos(String nomeArquivo) {

        formulas = new ArrayList<String>();

        try {
            // cria um objeto do novo arquivo
            File f = new File("bases/"+ nomeArquivo);

            //  diz que será feito a leitura dele
            FileReader arq = new FileReader(f);
            BufferedReader lerArq = new BufferedReader(arq);

            //usa escanear o arquivo usando o delimitador de , 
            Scanner scanner = new Scanner(arq).useDelimiter(",");

            //adiciona todos na formula
            while (scanner.hasNext()) {
                formulas.add(scanner.next());
            }

            arq.close();
        } catch (Exception e) {
            System.err.println("Erro na abertura do arquivo: "+e.getMessage());

        }

    }

    // novo metodo ,com a base de dados dedutiveis, a gente cria o obter base de conhecimentos 
    public DeductiveKnowledgeBase getBaseDeConhecimentos() {
        try {
            DeductiveKnowledgeBase kb = new DeductiveKnowledgeBase();
            PlParser parser = new PlParser();

            for (int cont = 0; cont < formulas.size(); cont++) {
                PropositionalFormula p1 = parser.parseFormula(formulas.get(cont));
                kb.add(p1);
            }
            return kb;
        } catch (Exception erro) {
            System.out.println("Erro: " + erro.getMessage());
        }
        return null;
    }

}

import java.util.*;
import java.io.*;

// to run simply do a: new BC(ask,tell) and then bc.execute()
// ask is a propositional symbol
// and tell is a knowledge base
// ask : p
// tell : p=>q;q=>r;r;

class BC{
    // create variables
    public static String tell;
    public static String ask;
    public static ArrayList<String> agenda;
    public static ArrayList<String> facts;
    public static ArrayList<String> clauses;
    public static ArrayList<String> entailed;

    public BC(String a, String t){
        // initialize variables
        agenda  = new ArrayList<String>();
        clauses  = new ArrayList<String>();
        entailed  = new ArrayList<String>();
        facts  = new ArrayList<String>();
        tell = t;
        ask = a;
        init(tell);
    }


    // metodo inicializa os valores para o encadeamento para tras
    public static void init(String tell){
        agenda.add(ask);
        // divide o conjunto de regras em sentencas
        String[] sentences = tell.split(";");
        for (int i=0;i<sentences.length;i++){
            if (!sentences[i].contains("=>"))
                // adiciona os fatos
                facts.add(sentences[i]);
            else{
                // adiciona as premissas
                clauses.add(sentences[i]);
            }
        }
    }

    // metodo que chama o metodo principal bcentails() e retorna o output para o iengine
    public String execute(){
        String output = "";

        if (bcentails()){
            // se for retornado true eh colocado na cauda
            output = "SIM: ";
            // para cada simbolo na cauda em reverso
            for (int i=entailed.size()-1;i>=0;i--){
                if (i==0)
                    output += entailed.get(i);
                else
                    // sem ponto e virgula no final
                    output += entailed.get(i)+", ";

            }
        }
        else{
            output = "NÃO";
        }
        return output;
    }

    //  Algoritmo de encadeamento para tras
    public boolean bcentails(){
        // enquanto a lista de simbolos nao esta vazia
        while(!agenda.isEmpty()){
            // pega simbolo atual
            String q = agenda.remove(agenda.size()-1);
            // adiciona para a cauda
            entailed.add(q);

            // checa se o elemento eh um fato
            if (!facts.contains(q)){
                // cria array para guardar novos simbolos a serem processados
                ArrayList<String> p = new ArrayList<String>();
                for(int i=0;i<clauses.size();i++){
                    // por cada clausula
                    if (conclusionContains(clauses.get(i),q)){
                        // que contem o simbolo como sendo sua conclusao

                        ArrayList<String> temp = getPremises(clauses.get(i));
                        for(int j=0;j<temp.size();j++){
                            // adiciona simbolos a um array temporario
                            p.add(temp.get(j));
                        }
                    }
                }
                // nenhum simbolo foi gerado e como não é um fato
                // entao esse simbolo e eventualmente o objetivo nao eh indução do conjunto de regras
                if (p.size()==0){
                    return false;
                }
                else{
                    // se tiver simbolos restantes, adicione para a agenda
                    for(int i=0;i<p.size();i++){
                        if (!entailed.contains(p.get(i)))
                            agenda.add(p.get(i));
                    }


                }
            }

        }
        return true;
    }

    // metodo que retorna os conjuntos contidos numa clausula
    public static ArrayList<String> getPremises(String clause){
        // pega a premissa
        String premise = clause.split("=>")[0];
        ArrayList<String> temp = new ArrayList<String>();
        String[] conjuncts = premise.split("&");
        // para cada conjunto:
        for(int i=0;i<conjuncts.length;i++){
            if (!agenda.contains(conjuncts[i]))
                temp.add(conjuncts[i]);
        }
        return temp;
    }


    // metodo checa se c aparece na conclusao da clausula
    // input : clausula, c
    // output : true se c esta na conclusao da clausula
    public static boolean conclusionContains(String clause, String c){
        String conclusion = clause.split("=>")[1];
        if (conclusion.equals(c))
            return true;
        else
            return false;

    }

}
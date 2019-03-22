import java.util.*;
import java.io.*;

// to run simply do a: new FC(ask,tell) and then fc.execute()
// ask is a propositional symbol
// and tell is a knowledge base
// ask : r
// tell : p=>q;q=>r;p;q;

    class FC<main> {
        public static String tell;
        public static String ask;
        public static ArrayList<String> agenda;

        public static ArrayList<String> facts;
        public static ArrayList<String> clauses;
        public static ArrayList<Integer> count;
        public static ArrayList<String> entailed;


        public FC(String a, String t){
            // initialize variables
            agenda  = new ArrayList<String>();
            clauses  = new ArrayList<String>();
            entailed  = new ArrayList<String>();
            facts  = new ArrayList<String>();
            count  = new ArrayList<Integer>();
            tell = t;
            ask = a;
            init(tell);
        }

        // metodo que chama o metodo principal fcentails() e retorna o output para o iengine
        public String execute(){
            String output = "";
            if (fcentails()){
                // se for retornado true eh colocado na cauda
                output = "SIM: ";
                // para cada simbolo na cauda
                for (int i=0;i<entailed.size();i++){
                    output += entailed.get(i)+", ";
                }
                output += ask;
            }
            else{
                output = "NÃO";
            }
            return output;
        }

        // Algoritmo de encadeamento para frente
        public boolean fcentails(){
        // Em loop enquanto tem fatos nao processados
            while(!agenda.isEmpty()){
                // pega o primeiro item e processa
                String p = agenda.remove(0);
                // adiciona para a cauda
                entailed.add(p);
                // Para cada clausula...
                for (int i=0;i<clauses.size();i++){
                    // ... que contem p na sua premissa
                    if (premiseContains(clauses.get(i),p)){
                        Integer j = count.get(i);
                        // reduza o count : elementos desconhecido em cada premissa
                        count.set(i,--j);
                        // todos os elementos da premissa agora sao conhecidos
                        if (count.get(i)==0){
                            // a conclusao foi provada e colocada em agenda
                            String head = clauses.get(i).split("=>")[1];
                            // verifica se a inferencia foi provada
                            if (head.equals(ask))
                                return true;
                            agenda.add(head);
                        }
                    }
                }
            }
            // nao pode ser provada
            return false;
        }




        // metodo atribui os valores iniciais para o encadeamento para frente
        // recebe em String as regras e separa simbolos e clausulas
        public static void init(String tell){
            String[] sentences = tell.split(";");
            for (int i=0;i<sentences.length;i++){

                if (!sentences[i].contains("=>"))
                    // adiciona fatos
                    agenda.add(sentences[i]);
                else{
                    // adiciona sentencas
                    clauses.add(sentences[i]);
                    count.add(sentences[i].split("&").length);
                }
            }
        }


        // metodo que checa se p aparece na premissa da clausula
        // input : clausula, p
        // output : true se p esta na premisa da clausula
        public static boolean premiseContains(String clause, String p){
            String premise = clause.split("=>")[0];
            String[] conjuncts = premise.split("&");
            // checa se p esta na premisa
            if (conjuncts.length==1)
                return premise.equals(p);
            else
                return Arrays.asList(conjuncts).contains(p);
        }

        public static void main(String args[]) {
            String pq = "p=>q;q=>r;p;q;";
            String test = "p&q=>r;p;q;";
            String h  = "C=>M;" +
                        "A&D=>E" +
                        "L=>H;"  +
                        "B&C=>G;" +
                        "A&B=>C;" +
                        "G&D=>H&I;" +
                        "C=>D;" +
                        "E&K=>H;" +
                        "A;B;";

            FC fc = new FC("r", test);

            System.out.println(fc.execute());

        }
    }

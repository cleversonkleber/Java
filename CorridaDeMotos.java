import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CorridaDeMotos {
    private static int qtdCompetidores;
    private static int qtdCorrrida;
    public static List<TaskRunning> corridaList1 =  new ArrayList<>();

    public static void main(String[] args) {
       qtdCompetidores = 10;
       qtdCorrrida = 10;
       criarCompetidores();
       podio();
       tabelaPontos();
    }

    private static void criarCompetidores(){
            IntStream.rangeClosed(1, qtdCompetidores)
            .forEach(index -> {
                new Thread(new TaskRunning("Competidor #"+index));
            });
    }

    private static Map<Object, Long> somaPontos(){
        Map<Object, Long> somaPontos = corridaList1.stream().collect(
                Collectors.groupingBy(TaskRunning::getNome,
                        Collectors.summingLong(TaskRunning::getPontos)
                )
        );
        return somaPontos;
    }
    private static void podio(){
        Map<Object, Long> somaPontos1 = somaPontos().entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(3)
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue,(e1,e2)->e1, LinkedHashMap::new));
        System.out.println("================ Podio ===========================");
        somaPontos1.forEach((k,v)->System.out.println(("\t"+k+" com "+v+" pontos")));
    }

    private static void tabelaPontos(){
        Map<Object, Long> somapontos;
        somapontos = somaPontos().entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue,(e1,e2)->e1, LinkedHashMap::new));
        System.out.println("\n============ Tabela de pontos ===================");
        somapontos.forEach((k,v)->System.out.println(("\t"+k+" com "+v+" pontos")));

    }


    static class TaskRunning implements Runnable{
        private String nome;
        private Integer pontos;
        private Thread t;
        private static Corrida corrida = new Corrida();
        public TaskRunning(String nome){
            this.setNome(nome);
            t = new Thread(this,nome);
            t.start();
//            try {
////                t.join();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
        }


        @Override
        public void run() {

            IntStream.rangeClosed(1, qtdCorrrida)
                    .forEach(index -> {
                        this.setPontos(corrida.getPoint());
                        corridaList1.add(this);
                        corrida.linhaDeChegada();
                        corrida.contrLeCorrida();
//                        System.out.printf("T%s > %s%n", Thread.currentThread().getName(), index);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });

        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public Integer getPontos() {
            return pontos;
        }

        public void setPontos(Integer pontos) {
            this.pontos = pontos;
        }
    }

    public static class Corrida {
        private int point=10;
       public  void linhaDeChegada(){
           synchronized (this){
               point--;
           }
        }
        public int getPoint() {
            return point;
        }
        public void contrLeCorrida(){
            if(point == 0) {
                point =qtdCompetidores;
            }
        }

    }
    }






import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static Scanner in;
    public static Graph grafo;
    public static ArrayList<String[]> conteudoDoArquivo;

    public static void main(String[] args) {
        in = new Scanner(System.in);
        conteudoDoArquivo = new ArrayList<>();

        System.out.println("Digite o arquivo .txt que deseja fazer a leitura: ");
        String arquivo = in.nextLine();
        Path filePath = Paths.get(arquivo);

        try (BufferedReader reader = Files.newBufferedReader(filePath, Charset.defaultCharset())) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                try {
                    if (line.equals("}")) {
                        break;
                    }
                    conteudoDoArquivo.add(line.split(" -> "));

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Houve algum erro na leitura do arquivo!");
        }

        criaGrafo();
        int[] resultado = maximoMinions(grafo.graphSize());
        int numeroDeMinions = resultado[0];
        int menorTempo = resultado[1];

        for (int i = numeroDeMinions; i > 0; i--) {
            int tempoSimulacao = rodaSimulacao(i);
            if (tempoSimulacao > menorTempo) {
                break;
            }
            menorTempo = tempoSimulacao;
            numeroDeMinions = i;
        }

        System.out.println("Numero ideal de minions: " + numeroDeMinions);
        System.out.println("Tempo de treinamento: " + menorTempo);
    }

    public static int rodaSimulacao(int minionsTeste) {
        int tempo = 0;
        criaGrafo();
        grafo.addPodemSerDesmanchados();
        while (grafo.concluido()) {
            minionsTeste = grafo.addSendoDesmanchado(minionsTeste);
            int menorTempo = grafo.menorTempo();
            tempo += menorTempo;
            minionsTeste += grafo.avancaTempo(menorTempo);
        }
        return tempo;
    }

    public static int[] maximoMinions(int minionsTeste) {
        int tempo = 0;
        int totalMinions = minionsTeste;
        int minimoMinions = minionsTeste;
        criaGrafo();
        grafo.addPodemSerDesmanchados();
        while (grafo.concluido()) {
            minionsTeste = grafo.addSendoDesmanchado(minionsTeste);
            if (minionsTeste < minimoMinions) {
                minimoMinions = minionsTeste;
            }
            int menorTempo = grafo.menorTempo();
            tempo += menorTempo;
            minionsTeste += grafo.avancaTempo(menorTempo);
        }
        return new int[]{totalMinions - minimoMinions, tempo};
    }

    public static void criaGrafo() {
        grafo = new Graph();
        for (String[] line : conteudoDoArquivo) {
            grafo.add(line[0], Integer.parseInt(line[0].split("_")[1]), null, line[1]);
            grafo.add(line[1], Integer.parseInt(line[1].split("_")[1]), line[0], null);
        }
    }
}

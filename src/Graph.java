import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class Graph {
    private final HashMap<String, Nodo> nodos;
    private final LinkedList<Nodo> podemSerDesmanchados;
    private final LinkedList<Nodo> sendoDesmanchados;

    public Graph() {
        nodos = new HashMap<>();
        podemSerDesmanchados = new LinkedList<>();
        sendoDesmanchados = new LinkedList<>();
    }

    public void add(String nome, int tempo, String nomeRequisito, String nomeDependencia) {
        if (nodos.containsKey(nome)) {
            if (nomeRequisito != null) {
                nodos.get(nome).addRequisito(nodos.get(nomeRequisito));
            }
            if (nomeDependencia != null) {
                if(nodos.get(nomeDependencia) == null){
                    add(nomeDependencia, Integer.parseInt(nomeDependencia.split("_")[1]), null, null);
                }
                nodos.get(nome).addDependencia(nodos.get(nomeDependencia));
            }
            return;
        }
        Nodo nodo = new Nodo(nome, tempo);
        if (nomeRequisito != null) {
            nodo.addRequisito(nodos.get(nomeRequisito));
        }
        if (nomeDependencia != null) {
            if(nodos.get(nomeDependencia) == null){
                add(nomeDependencia, Integer.parseInt(nomeDependencia.split("_")[1]), null, null);
            }
            nodo.addDependencia(nodos.get(nomeDependencia));
        }
        nodos.put(nome, nodo);
    }

    public void addPodemSerDesmanchadosDependentes(Nodo nodo) {
        for(Nodo dependente: nodo.getNodosDependentes()) {
            if (dependente.podeSerDesmanchado()) {
                podemSerDesmanchados.add(dependente);
            }
        }
    }

    public void addPodemSerDesmanchados() {
        for(Iterator<Map.Entry<String, Nodo>> it = nodos.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, Nodo> entry = it.next();
            if (nodos.get(entry.getKey()).podeSerDesmanchado()) {
                podemSerDesmanchados.add(nodos.get(entry.getKey()));
                it.remove();
            }
        }
    }

    public int addSendoDesmanchado(int minionsDisponiveis) {
        int cont = 0;
        for (int i = 0; i < minionsDisponiveis; i++) {
            if (podemSerDesmanchados.size() == 0) {
                break;
            }
            int menor = 0;
            for (int j = 1; j < podemSerDesmanchados.size(); j++) {
                if (podemSerDesmanchados.get(menor).getNome().compareTo(podemSerDesmanchados.get(j).getNome()) > 0) {
                    menor = j;
                }
            }
            sendoDesmanchados.add(podemSerDesmanchados.get(menor));
            podemSerDesmanchados.remove(menor);
            cont++;
        }
        return minionsDisponiveis - cont;
    }

    public int menorTempo() {
        if (sendoDesmanchados.size() == 0) {
            return 0;

        }
        int menorTempo = sendoDesmanchados.get(0).getTempo();
        for (int i = 1; i < sendoDesmanchados.size(); i++) {
            if (menorTempo > sendoDesmanchados.get(i).getTempo()) {
                menorTempo = sendoDesmanchados.get(i).getTempo();
            }
        }
        return menorTempo;
    }

    public int avancaTempo(int menorTempo) {
        int minionsLivres = 0;
        for (int i = 0; i < sendoDesmanchados.size(); i++) {
            sendoDesmanchados.get(i).diminuiTempo(menorTempo);
            if (sendoDesmanchados.get(i).getTempo() == 0) {
                sendoDesmanchados.get(i).setDesmanchado(true);
                addPodemSerDesmanchadosDependentes(sendoDesmanchados.get(i));
                sendoDesmanchados.remove(i);
                minionsLivres++;
                i--;
            }
        }
        return minionsLivres;
    }

    public boolean concluido() {
        return sendoDesmanchados.size() != 0 || podemSerDesmanchados.size() != 0;
    }

    public int graphSize(){
        return nodos.size();
    }
}

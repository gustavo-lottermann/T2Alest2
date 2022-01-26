import java.util.ArrayList;

public class Nodo {
    private final String nome;
    private int tempo;
    private boolean desmanchado;
    private final ArrayList<Nodo> nodosRequisito;
    private final ArrayList<Nodo> nodosDependentes;

    public Nodo(String nome, int tempo) {
        this.nome = nome;
        this.tempo = tempo;
        this.desmanchado = false;
        this.nodosRequisito = new ArrayList<>();
        this.nodosDependentes = new ArrayList<>();
    }

    public void addRequisito(Nodo nodoRequisito) {
        nodosRequisito.add(nodoRequisito);
    }

    public void addDependencia(Nodo nodoDependente) {
        nodosDependentes.add(nodoDependente);
    }

    public ArrayList<Nodo> getNodosDependentes(){
        return nodosDependentes;
    }

    public String getNome() {
        return nome;
    }

    public int getTempo() {
        return tempo;
    }

    public void diminuiTempo(int tempo) {
        this.tempo -= tempo;
    }

    public boolean isDesmanchado() {
        return desmanchado;
    }

    public boolean podeSerDesmanchado() {
        for (Nodo nodo : nodosRequisito) {
            if (!nodo.isDesmanchado()) {
                return false;
            }
        }
        return true;
    }

    public void setDesmanchado(boolean desmanchado) {
        this.desmanchado = desmanchado;
    }
}

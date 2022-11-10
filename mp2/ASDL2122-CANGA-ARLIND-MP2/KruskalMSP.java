package it.unicam.cs.asdl2122.mp2;

import java.util.HashSet;
import java.util.Set;

//TODO completare gli import necessari

//ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 * 
 * Classe singoletto che implementa l'algoritmo di Kruskal per trovare un
 * Minimum Spanning Tree di un grafo non orientato, pesato e con pesi non
 * negativi. L'algoritmo implementato si avvale della classe
 * {@code ForestDisjointSets<GraphNode<L>>} per gestire una collezione di
 * insiemi disgiunti di nodi del grafo.
 * 
 * @author Luca Tesei (template) **ARLIND CANGA, arlind.canga@studenti.unicam.it** (implementazione)
 * 
 * @param <L>
 *                tipo delle etichette dei nodi del grafo
 *
 */
public class KruskalMSP<L> {

    /*
     * Struttura dati per rappresentare gli insiemi disgiunti utilizzata
     * dall'algoritmo di Kruskal.
     */
    private ForestDisjointSets<GraphNode<L>> disjointSets;

    // TODO inserire eventuali variabili istanza o classi interne necessarie

    /**
     * Costruisce un calcolatore di un albero di copertura minimo che usa
     * l'algoritmo di Kruskal su un grafo non orientato e pesato.
     */
    public KruskalMSP() {
        this.disjointSets = new ForestDisjointSets<GraphNode<L>>();
    }

    /**
     * Utilizza l'algoritmo goloso di Kruskal per trovare un albero di copertura
     * minimo in un grafo non orientato e pesato, con pesi degli archi non
     * negativi. L'albero restituito non è radicato, quindi è rappresentato
     * semplicemente con un sottoinsieme degli archi del grafo.
     * 
     * @param g
     *              un grafo non orientato, pesato, con pesi non negativi
     * @return l'insieme degli archi del grafo g che costituiscono l'albero di
     *         copertura minimo trovato
     * @throw NullPointerException se il grafo g è null
     * @throw IllegalArgumentException se il grafo g è orientato, non pesato o
     *        con pesi negativi
     */
    public Set<GraphEdge<L>> computeMSP(Graph<L> g) {
        if (g == null){
            throw new NullPointerException("Grafo nullo");
        }
        if (g.isDirected()){
            throw new IllegalArgumentException("Questo algoritmo lavora solo con grafi non orientati");
        }
        //la inizializzo sempre in modo che non rimane sporca
        disjointSets = new ForestDisjointSets<GraphNode<L>>();
        for (GraphNode<L> node : g.getNodes()){
            disjointSets.makeSet(node);
        }
        Set<GraphEdge<L>> edges = g.getEdges();
        //Creo un array di archi
        GraphEdge<L> [] orderEdges = new GraphEdge[edges.size()];
        int arrIndex = 0;
        //Popolo l'array di archi
        for (GraphEdge<L> edge : edges){
            if (edge.getWeight() < 0 || Double.isNaN(edge.getWeight())){
                throw new IllegalArgumentException("Albero non pesato o peso negativo");
            }
            orderEdges[arrIndex] = edge;
            arrIndex++;
        }
        //ordino gli archi in base al weight
        this.sort(orderEdges);
        //creo il set degli archi da restituire
        Set<GraphEdge<L>> ret = new HashSet<GraphEdge<L>>();
        for (int i = 0; i < orderEdges.length; i++){
            GraphEdge<L> edge = orderEdges[i];
            if (!this.isCyclic(edge.getNode1(), edge.getNode2())){
                ret.add(edge);
                disjointSets.union(edge.getNode1(), edge.getNode2());
            }
        }
        return ret;
    }

    //Arlind Canga - Metodo utile all'ordinamento di un array di archi
    //utilizza la logica dell'insertion sort
    private void sort(GraphEdge<L> arr[])
    {
        int n = arr.length;
        for (int i = 1; i < n; ++i) {
            GraphEdge<L> edge = arr[i];
            int j = i - 1;

            while (j >= 0 && arr[j].getWeight() > edge.getWeight()) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = edge;
        }
    }

    //Metodo utile per controllare se, dati due set, creerebbero un ciclo
    //se il parent di due nodi è lo stesso non posso creare un arco tra loro
    //perchè mi troverei a creare un ciclo
    private boolean isCyclic (GraphNode<L> e1, GraphNode<L> e2){
        return this.disjointSets.findSet(e1) == this.disjointSets.findSet(e2);
    }


}

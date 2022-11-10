package it.unicam.cs.asdl2122.mp2;

import java.util.HashSet;
import java.util.Set;

//TODO completare gli import necessari

//ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 * Classe singoletto che realizza un calcolatore delle componenti connesse di un
 * grafo non orientato utilizzando una struttura dati efficiente (fornita dalla
 * classe {@ForestDisjointSets<GraphNode<L>>}) per gestire insiemi disgiunti di
 * nodi del grafo che sono, alla fine del calcolo, le componenti connesse.
 * 
 * @author Luca Tesei (template) **ARLIND CANGA, arlind.canga@studenti.unicam.it** (implementazione)
 *
 * @param <L>
 *                il tipo delle etichette dei nodi del grafo
 */
public class UndirectedGraphConnectedComponentsComputer<L> {

    /*
     * Struttura dati per gli insiemi disgiunti.
     */
    private ForestDisjointSets<GraphNode<L>> f;

    /**
     * Crea un calcolatore di componenti connesse.
     */
    public UndirectedGraphConnectedComponentsComputer() {
        this.f = new ForestDisjointSets<GraphNode<L>>();
    }

    /**
     * Calcola le componenti connesse di un grafo non orientato utilizzando una
     * collezione di insiemi disgiunti.
     * 
     * @param g
     *              un grafo non orientato
     * @return un insieme di componenti connesse, ognuna rappresentata da un
     *         insieme di nodi del grafo
     * @throws NullPointerException
     *                                      se il grafo passato è nullo
     * @throws IllegalArgumentException
     *                                      se il grafo passato è orientato
     */
    public Set<Set<GraphNode<L>>> computeConnectedComponents(Graph<L> g) {
        if (g == null){
            throw new NullPointerException("Grafo nullo");
        }
        if (g.isDirected()){
            throw new IllegalArgumentException("Questo algoritmo lavora solo con grafi non orientati");
        }
        this.f = new ForestDisjointSets<GraphNode<L>>();
        //prendo i nodi e creo insiemi disgiunti singoletti
        for (GraphNode<L> node : g.getNodes()){
            f.makeSet(node);
        }
        Set<GraphEdge<L>> edges = g.getEdges();
        //utilizzo gli archi per fare le union sull'insieme disgiunto
        for (GraphEdge<L> edge : edges){
            f.union(edge.getNode1(), edge.getNode2());
        }
        // creo l'insieme degli insiemi
        Set<Set<GraphNode<L>>> result = new HashSet<>();
        //prendo i rappresentanti dell'insieme disgiunto
        Set<GraphNode<L>> reps = f.getCurrentRepresentatives();
        for (GraphNode<L> rep : reps){
            //per ogni rapresentative questa chiamata mi restituirà un set di oggetti che lo hanno compe padre
            result.add(f.getCurrentElementsOfSetContaining(rep));
        }
        return result;
    }
}

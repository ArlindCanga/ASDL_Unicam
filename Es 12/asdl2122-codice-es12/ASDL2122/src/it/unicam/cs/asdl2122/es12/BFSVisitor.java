package it.unicam.cs.asdl2122.es12;

import java.util.LinkedList;

/**
 * Classe singoletto che fornisce lo schema generico di visita Breadth-First di
 * un grafo rappresentato da un oggetto di tipo Graph<L>.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 * @param <L> le etichette dei nodi del grafo
 */
public class BFSVisitor<L> {

    /**
     * Esegue la visita in ampiezza di un certo grafo a partire da un nodo
     * sorgente. Setta i valori seguenti valori associati ai nodi: distanza
     * intera, predecessore. La distanza indica il numero minimo di archi che si
     * devono percorrere dal nodo sorgente per raggiungere il nodo e il
     * predecessore rappresenta il padre del nodo in un albero di copertura del
     * grafo. Ogni volta che un nodo viene visitato viene eseguito il metodo
     * visitNode sul nodo. In questa classe il metodo non fa niente, basta
     * creare una sottoclasse e ridefinire il metodo per eseguire azioni
     * particolari.
     * 
     * @param g
     *                   il grafo da visitare.
     * @param source
     *                   il nodo sorgente.
     * @throws NullPointerException
     *                                      se almeno un valore passato è null
     * @throws IllegalArgumentException
     *                                      se il nodo sorgente non appartiene
     *                                      al grafo dato
     */
    public void BFSVisit(Graph<L> g, GraphNode<L> source) {
        if(g == null || source == null) throw new NullPointerException("Grafo o nodo nulli");
        if(!g.containsNode(source)) throw new IllegalArgumentException("Nodo non appartenente al grafo");

        LinkedList<GraphNode<L>> queue = new LinkedList<GraphNode<L>>();
        for(GraphNode<L> node : g.getNodes()){
            if(node.equals(source)) continue;

            node.setColor(node.COLOR_WHITE);
            node.setIntegerDistance(Integer.MAX_VALUE);
            node.setPrevious(null);
        }

        source.setColor(source.COLOR_GREY);
        queue.add(source);

        while(queue.size() != 0){
            GraphNode<L> u = queue.poll();

            for(GraphNode<L> adj : g.getAdjacentNodesOf(u)){
                if(adj.getColor() == adj.COLOR_WHITE){
                    adj.setColor(adj.COLOR_GREY);
                    adj.setIntegerDistance(u.getIntegerDistance() + 1);
                    adj.setPrevious(u);
                    queue.add(adj);
                }
            }

            u.setColor(u.COLOR_BLACK);
            this.visitNode(u);
        }
    }

    /**
     * Questo metodo, che di default non fa niente, viene chiamato su tutti i
     * nodi visitati durante la BFS quando i nodi passano da grigio a nero.
     * Ridefinire il metodo in una sottoclasse per effettuare azioni specifiche.
     * 
     * @param n
     *              il nodo visitato
     */
    public void visitNode(GraphNode<L> n) {
        /*
         * In questa classe questo metodo non fa niente. Esso può essere
         * ridefinito in una sottoclasse per fare azioni particolari.
         */
    }

}

package it.unicam.cs.asdl2122.mp2;

//TODO completare gli import necessari

//ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe singoletto che implementa l'algoritmo di Prim per trovare un Minimum
 * Spanning Tree di un grafo non orientato, pesato e con pesi non negativi.
 * 
 * L'algoritmo richiede l'uso di una coda di min priorità tra i nodi che può
 * essere realizzata con una semplice ArrayList (non c'è bisogno di ottimizzare
 * le operazioni di inserimento, di estrazione del minimo, o di decremento della
 * priorità).
 * 
 * Si possono usare i colori dei nodi per registrare la scoperta e la visita
 * effettuata dei nodi.
 * 
 * @author Luca Tesei (template) **ARLIND CANGA, arlind.canga@studenti.unicam.it** (implementazione)
 * 
 * @param <L>
 *                tipo delle etichette dei nodi del grafo
 *
 */
public class PrimMSP<L> {

    private List<GraphNode<L>> priorityQueue;
    private List<Boolean> visited;
    private List<Double> weight;
    private static double infinito = Double.POSITIVE_INFINITY;


    /*
     * In particolare: si deve usare una coda con priorità che può semplicemente
     * essere realizzata con una List<GraphNode<L>> e si deve mantenere un
     * insieme dei nodi già visitati
     */

    /**
     * Crea un nuovo algoritmo e inizializza la coda di priorità con una coda
     * vuota.
     */
    public PrimMSP() {
        this.priorityQueue = new ArrayList<GraphNode<L>>();
        this.visited = new ArrayList<Boolean>();
        this.weight = new ArrayList<Double>();
    }

    /**
     * Utilizza l'algoritmo goloso di Prim per trovare un albero di copertura
     * minimo in un grafo non orientato e pesato, con pesi degli archi non
     * negativi. Dopo l'esecuzione del metodo nei nodi del grafo il campo
     * previous deve contenere un puntatore a un nodo in accordo all'albero di
     * copertura minimo calcolato, la cui radice è il nodo sorgente passato.
     * 
     * @param g
     *              un grafo non orientato, pesato, con pesi non negativi
     * @param s
     *              il nodo del grafo g sorgente, cioè da cui parte il calcolo
     *              dell'albero di copertura minimo. Tale nodo sarà la radice
     *              dell'albero di copertura trovato
     * 
     * @throw NullPointerException se il grafo g o il nodo sorgente s sono nulli
     * @throw IllegalArgumentException se il nodo sorgente s non esiste in g
     * @throw IllegalArgumentException se il grafo g è orientato, non pesato o
     *        con pesi negativi
     */
    public void computeMSP(Graph<L> g, GraphNode<L> s) {
        if (g == null || s == null){
            throw new NullPointerException("Parametri nulli");
        }
        if (!g.getNodes().contains(s)){
            throw new IllegalArgumentException("Nodo non presente nel grafo");
        }
        if (g.isDirected()){
            throw new IllegalArgumentException("Grafi orientato");
        }
        if (!this.checkGraph(g)){
            throw new IllegalArgumentException("Il grafo non è pesato o ha pesi negativi");
        }
        //prendo tutti i nodi e li aggiungo alla lista
        for (GraphNode<L> node : g.getNodes()){
            this.priorityQueue.add(node);
        }

        //setto visited a false e i valori dei pesi iniziali ad infinito
        for (int i = 0; i < this.priorityQueue.size(); i++){
            this.visited.add(false);
            this.weight.add(infinito);
        }
        this.PRIM(g, s);

    }

    //Arlind Canga, metodi di comodo

    //Il codice di questo metodo lo ritengo molto confusionale, purtroppo lo devo lasciare cosi altrimenti non riesco
    //a consegnare in tempo
    private void PRIM (Graph<L> g, GraphNode<L> s){
        //creo un arco fittizio con peso infinito
        GraphNode<String> a = new GraphNode<String>("a");
        GraphNode<String> b = new GraphNode<String>("b");
        GraphEdge<String> testEdge = new GraphEdge<String>(a,b,false,infinito);
        GraphEdge<L> minEdge = null;
        //mi prendo l'arco valido con peso minore del nodo s
        for (GraphEdge<L> edge : g.getEdgesOf(s)){
            if (edge.getWeight() < testEdge.getWeight()){
                GraphNode<L> notmyNode = null;
                if (edge.getNode1() == s){
                    notmyNode = edge.getNode2();
                } else {
                    notmyNode = edge.getNode1();
                }
                if (!this.isVisited((notmyNode))){
                    testEdge.setWeight(edge.getWeight());
                    minEdge = edge;
                }
            }
        }
        //setto il nodo come visitato (ho controllato se ci siano o meno archi)
        this.visited.set(this.getIndexofNode(s), true);
        if (minEdge == null){
            return;
        }
        if (s.getPrevious()!= null && !(this.minWeight(g, s, s.getPrevious()) == s)){
            this.weight.set(this.getIndexofNode(s), minEdge.getWeight());
            if (minEdge.getNode1() == s){
                this.priorityQueue.get(this.getIndexofNode(minEdge.getNode2())).setPrevious(s);
                // Altrimenti vuol dire che nodo2 == s
            } else {
                this.priorityQueue.get(this.getIndexofNode(minEdge.getNode1())).setPrevious(s);
            }
            return;
        }
        //se ho trovato un arco
        if (testEdge.getWeight() != infinito){
            //se ho trovato l'arco memorizzo il suo peso
            this.weight.set(this.getIndexofNode(s), minEdge.getWeight());
            //se edge.node1 == s setto node1 come previous di node 2
            if (minEdge.getNode1() == s){
                this.priorityQueue.get(this.getIndexofNode(minEdge.getNode2())).setPrevious(s);
                // Altrimenti vuol dire che nodo2 == s
            } else {
                this.priorityQueue.get(this.getIndexofNode(minEdge.getNode1())).setPrevious(s);
            }
            if (!this.isVisited(minEdge.getNode1())){
                this.PRIM(g, minEdge.getNode1());
            }
            if (!this.isVisited(minEdge.getNode2())){
                this.PRIM(g, minEdge.getNode2());
            }
            //se il nodo che ho appena elaborato ha figli non visitati li vado a visitare
            if (hasChildren(g, s)){
                this.PRIM(g, s);
            }
        }
    }

    private boolean checkGraph(Graph<L> g){
        // Verifico i pesi degli archi
        for (GraphEdge<L> edge : g.getEdges()) {
            if (edge.getWeight() < 0 || Double.isNaN(edge.getWeight())) {
                return false;
            }
        }
        return true;
    }

    private boolean isVisited(GraphNode<L> node){
        if (this.visited.get(this.getIndexofNode(node))){
            return true;
        }
        return false;
    }

    private int getIndexofNode(GraphNode<L> node){
        for (int i = 0; i < this.priorityQueue.size(); i++){
            if (this.priorityQueue.get(i) == node){
                return i;
            }
        }
        return 0;
    }

    private boolean hasChildren (Graph<L> g, GraphNode<L> s){
        for (GraphEdge<L> edge : g.getEdgesOf(s)){
            GraphNode<L> notmyNode = null;
            if (edge.getNode1() == s){
                notmyNode = edge.getNode2();
            } else {
                notmyNode = edge.getNode1();
            }
            if (!this.isVisited((notmyNode))){
                return true;
            }
        }
        return false;
    }

    //dati due nodi ritorna quello con pesi minori
    private GraphNode<L> minWeight (Graph<L> g, GraphNode<L> node1, GraphNode<L> node2){
        GraphNode<String> a = new GraphNode<String>("a");
        GraphNode<String> b = new GraphNode<String>("b");
        GraphEdge<String> testEdge = new GraphEdge<String>(a,b,false,infinito);
        //mi prendo l'arco valido con peso minore del nodo s
        for (GraphEdge<L> edge : g.getEdgesOf(node1)){
            if (edge.getWeight() < testEdge.getWeight() && edge.getNode1() != node2 && edge.getNode2()!= node2){
                GraphNode<L> myNode = null;
                if (edge.getNode1() == node1){
                    myNode = edge.getNode2();
                } else {
                    myNode = edge.getNode1();
                }
                if (!this.isVisited((myNode))){
                    testEdge.setWeight(edge.getWeight());
                }
            }
        }
        GraphEdge<String> testEdge2 = new GraphEdge<String>(a,b,false,infinito);
        for (GraphEdge<L> edge : g.getEdgesOf(node2)){
            if (edge.getWeight() < testEdge2.getWeight() && edge.getNode1() != node1 && edge.getNode2()!= node1){
                GraphNode<L> myNode = null;
                if (edge.getNode1() == node2){
                    myNode = edge.getNode2();
                } else {
                    myNode = edge.getNode1();
                }
                if (!this.isVisited((myNode))){
                    testEdge2.setWeight(edge.getWeight());
                }
            }
        }
        if (testEdge.getWeight() >= testEdge2.getWeight()){
            return node2;
        }else{
            return node1;
        }
    }
}

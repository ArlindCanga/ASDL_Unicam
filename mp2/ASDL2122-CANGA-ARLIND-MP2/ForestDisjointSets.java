package it.unicam.cs.asdl2122.mp2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

//TODO completare gli import necessari

//ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 * Implementazione dell'interfaccia <code>DisjointSets<E></code> tramite una
 * foresta di alberi ognuno dei quali rappresenta un insieme disgiunto. Si
 * vedano le istruzioni o il libro di testo Cormen et al. (terza edizione)
 * Capitolo 21 Sezione 3.
 * 
 * @author Luca Tesei (template) ** ARLIND CANGA, arlind.canga@studenti.unicam.it ** (implementazione)
 *
 * @param <E>
 *                il tipo degli elementi degli insiemi disgiunti
 */
public class ForestDisjointSets<E> implements DisjointSets<E> {

    /*
     * Mappa che associa ad ogni elemento inserito il corrispondente nodo di un
     * albero della foresta. La variabile è protected unicamente per permettere
     * i test JUnit.
     */
    protected Map<E, Node<E>> currentElements;
    
    /*
     * Classe interna statica che rappresenta i nodi degli alberi della foresta.
     * Gli specificatori sono tutti protected unicamente per permettere i test
     * JUnit.
     */
    protected static class Node<E> {
        /*
         * L'elemento associato a questo nodo
         */
        protected E item;

        /*
         * Il parent di questo nodo nell'albero corrispondente. Nel caso in cui
         * il nodo sia la radice allora questo puntatore punta al nodo stesso.
         */
        protected Node<E> parent;

        /*
         * Il rango del nodo definito come limite superiore all'altezza del
         * (sotto)albero di cui questo nodo è radice.
         */
        protected int rank;

        /**
         * Costruisce un nodo radice con parent che punta a se stesso e rango
         * zero.
         * 
         * @param item
         *                 l'elemento conservato in questo nodo
         * 
         */
        public Node(E item) {
            this.item = item;
            this.parent = this;
            this.rank = 0;
        }

    }

    /**
     * Costruisce una foresta vuota di insiemi disgiunti rappresentati da
     * alberi.
     */
    public ForestDisjointSets() {
        this.currentElements = new HashMap<E, Node<E>>();
    }

    @Override
    public boolean isPresent(E e) {
        if (e == null){
            throw new NullPointerException("Parametro nullo");
        }
        if (this.currentElements.containsKey(e)){
            return true;
        }
        return false;
    }

    /*
     * Crea un albero della foresta consistente di un solo nodo di rango zero il
     * cui parent è se stesso.
     */
    @Override
    public void makeSet(E e) {
        if (this.isPresent(e)){
            throw new IllegalArgumentException("Elemento già presente");
        }
        Node<E> node = new Node<E>(e);
        this.currentElements.put(e, node);
    }

    /*
     * L'implementazione del find-set deve realizzare l'euristica
     * "compressione del cammino". Si vedano le istruzioni o il libro di testo
     * Cormen et al. (terza edizione) Capitolo 21 Sezione 3.
     */
    @Override
    public E findSet(E e) {
        //Mi fa anche il controllo e==null
        if (!this.isPresent(e)){
            return null;
        }
        Node<E> node = this.currentElements.get(e);
        //Se il parent non è se stesso chiamo ricorsivamente il metodo finche avrò il padre del set
        if (node.item != node.parent.item){
            //Essendo la chiamata ricorsiva il parent si aggiornerà per tutti i nodi
            E newParent = findSet(node.parent.item);
            node.parent = this.currentElements.get(newParent);
        }
        return node.parent.item;
    }

    /*
     * L'implementazione dell'unione deve realizzare l'euristica
     * "unione per rango". Si vedano le istruzioni o il libro di testo Cormen et
     * al. (terza edizione) Capitolo 21 Sezione 3. In particolare, il
     * rappresentante dell'unione dovrà essere il rappresentante dell'insieme il
     * cui corrispondente albero ha radice con rango più alto. Nel caso in cui
     * il rango della radice dell'albero di cui fa parte e1 sia uguale al rango
     * della radice dell'albero di cui fa parte e2 il rappresentante dell'unione
     * sarà il rappresentante dell'insieme di cui fa parte e2.
     */
    @Override
    public void union(E e1, E e2) {
        if (e1 == null || e2 == null){
            throw new NullPointerException("Parametri nulli");
        }
        if (!(this.isPresent(e1) && this.isPresent(e2))){
            throw new IllegalArgumentException("Elementi non presenti");
        }
        //prendo i parent di e1 ed e2 per fare valutare l'union
        Node<E> node1 = this.currentElements.get(e1).parent;
        Node<E> node2 = this.currentElements.get(e2).parent;
        //controllo che non facciano parte dello stesso insieme
        if (node1 != node2){
            //controllo il rango per capire quale nodo diventerà il nuovo rappresentative
            if (node1.rank > node2.rank){
                node2.parent = node1;
            } else {
                if (node1.rank == node2.rank){
                    node2.rank++;
                }
                node1.parent = node2;
            }
        }
    }

    @Override
    public Set<E> getCurrentRepresentatives() {
        Set<E> refs = new HashSet<E>();
        for (E key : this.currentElements.keySet()){
            //findSet trova correttamente ogni parent
            //HashSet non ammette dupplicati
            refs.add(this.findSet(key));
        }
        return refs;
    }

    @Override
    public Set<E> getCurrentElementsOfSetContaining(E e) {
        if (e == null){
            throw new NullPointerException("Argomento nullo");
        }
        if (!this.isPresent(e)){
            throw new IllegalArgumentException("Elemento non presente in nessun insieme");
        }
        Set<E> refs = new HashSet<E>();
        E parentGroup = this.findSet(e);
        //scorro tutta la Map e aggiungo al set solamente se e.parent == key.parent
        for (E key : this.currentElements.keySet()){
            if (parentGroup == this.currentElements.get(key).parent.item){
                refs.add(key);
            }
        }
        return refs;
    }

    @Override
    public void clear() {
        this.currentElements = new HashMap<E, Node<E>>();
    }

}

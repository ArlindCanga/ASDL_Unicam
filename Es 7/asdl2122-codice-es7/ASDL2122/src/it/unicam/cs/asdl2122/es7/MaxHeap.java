package it.unicam.cs.asdl2122.es7;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Classe che implementa uno heap binario che può contenere elementi non nulli
 * possibilmente ripetuti.
 * 
 * @author Template: Luca Tesei, Implementation: collettiva
 *
 * @param <E>
 *                il tipo degli elementi dello heap, che devono avere un
 *                ordinamento naturale.
 */
public class MaxHeap<E extends Comparable<E>> {

    /*
     * L'array che serve come base per lo heap
     */
    private ArrayList<E> heap;

    /**
     * Costruisce uno heap vuoto.
     */
    public MaxHeap() {
        this.heap = new ArrayList<E>();
    }

    /**
     * Restituisce il numero di elementi nello heap.
     * 
     * @return il numero di elementi nello heap
     */
    public int size() {
        return this.heap.size();
    }

    /**
     * Determina se lo heap è vuoto.
     * 
     * @return true se lo heap è vuoto.
     */
    public boolean isEmpty() {
        return this.heap.isEmpty();
    }

    /**
     * Costruisce uno heap a partire da una lista di elementi.
     * 
     * @param list
     *                 lista di elementi
     * @throws NullPointerException
     *                                  se la lista è nulla
     */
    public MaxHeap(List<E> list) {
        if (list == null){
            throw new NullPointerException("Lista nulla");
        }
        this.heap = new ArrayList<E>();
        for(E element: list){
            this.insert(element);
        }
    }

    /**
     * Inserisce un elemento nello heap
     * 
     * @param el
     *               l'elemento da inserire
     * @throws NullPointerException
     *                                  se l'elemento è null
     * 
     */
    public void insert(E el) {
        if (el == null){
            throw new NullPointerException("Elemento nullo");
        }
        int index = this.heap.size();
        int parentIndex = this.parentIndex(index);
        this.heap.add(el);
        if (index > 0){
            int tmp;
            while((Integer)this.heap.get(this.parentIndex(index)) < (Integer) el) {
                tmp = parentIndex;
                this.heap.set(parentIndex, this.heap.get(index));
                this.heap.set(index, this.heap.get(tmp));
                index = parentIndex;
                parentIndex = this.parentIndex(parentIndex);
            }
        }
    }

    /*
     * Funzione di comodo per calcolare l'indice del figlio sinistro del nodo in
     * posizione i. Si noti che la posizione 0 è significativa e contiene sempre
     * la radice dello heap.
     */
    private int leftIndex(int i) {
        return 2*i + 1;
    }

    /*
     * Funzione di comodo per calcolare l'indice del figlio destro del nodo in
     * posizione i. Si noti che la posizione 0 è significativa e contiene sempre
     * la radice dello heap.
     */
    private int rightIndex(int i) {
        return 2*i + 2;
    }

    /*
     * Funzione di comodo per calcolare l'indice del genitore del nodo in
     * posizione i. Si noti che la posizione 0 è significativa e contiene sempre
     * la radice dello heap.
     */
    private int parentIndex(int i) {
        return (i - 1) / 2;
    }

    /**
     * Ritorna l'elemento massimo senza toglierlo.
     * 
     * @return l'elemento massimo dello heap oppure null se lo heap è vuoto
     */
    public E getMax() {
        return this.heap.get(0);
    }

    /**
     * Estrae l'elemento massimo dallo heap. Dopo la chiamata tale elemento non
     * è più presente nello heap.
     * 
     * @return l'elemento massimo di questo heap oppure null se lo heap è vuoto
     */
    public E extractMax() {
        int lastIndex = this.heap.size() - 1;
        E element = this.heap.get(lastIndex);
        this.heap.set(0, element);
        this.heapify(0);
        return element;
    }

    /*
     * Ricostituisce uno heap a partire dal nodo in posizione i assumendo che i
     * suoi sottoalberi sinistro e destro (se esistono) siano heap.
     */
    private void heapify(int i) {
        int maxIndex = -1;
        //indici dei figli dell'elemento
        int leftIndex = this.leftIndex(i);
        int rightIndex = this.rightIndex(i);
        //trovo l'elemento con priorità minima tra element e i suoi figli
        if(leftIndex < this.heap.size() && this.heap.get(leftIndex).compareTo(this.heap.get(i)) >  0){
            maxIndex = leftIndex;
        }else{
            maxIndex = i;
        }
        if(rightIndex < this.heap.size() && this.heap.get(rightIndex).compareTo(this.heap.get(i)) >  0){
            maxIndex = rightIndex;
        }
        //se element ha priorità maggiore di uno dei suoi figli lo faccio scendere
        if(maxIndex != i){
            int tmp = maxIndex;
            this.heap.set(i, this.heap.get(maxIndex));
            this.heap.set(i, this.heap.get(tmp));
            this.heapify(maxIndex);
        }
    }
    
    /**
     * Only for JUnit testing purposes.
     * 
     * @return the arraylist representing this max heap
     */
    protected ArrayList<E> getHeap() {
        return this.heap;
    }
}

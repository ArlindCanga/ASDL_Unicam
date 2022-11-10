package it.unicam.cs.asdl2122.mp1;

import java.util.*;


/**
 * Un multinsieme è un insieme in cui gli elementi hanno una molteplicità (cioè un numero di
 * volte che occorrono nell'insieme). Se un elemento ha molteplicità zero
 * allora non appartiene all'insieme. Per risolvere al meglio il problema della molteplicità
 * ho deciso di utilizzare delle liste concatenate tramite nodi dove ogni nodo appunta la molteplicità
 * di un elemento.
 * 
 * @author Luca Tesei (template) **Arlind Canga, arlind.canga@studenti.unicam.it** (implementazione)
 *
 * @param <E>
 *                il tipo degli elementi del multiset
 */
public class MyMultiset<E> implements Multiset<E> {

    private int size;

    private Node<E> head;
    private Node<E> tail;


    // AC - Classe interna Node per gestire gli elementi e le occorrenze

    private static class Node<E> {
        private E item;
        private int occurrencies; //utilizzo questo campo per segnare le occorrenze
        private Node<E> next;


        Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
            this.occurrencies = 1;
        }

        Node(E item, Node<E> next, int count) {
            this.item = item;
            this.next = next;
            this.occurrencies = count;
        }
    }

    // AC - Metodo utile per la ricerca di un elemento
    private Node<E> searchNode(Node<E> element){
        //Non metto i controlli sul parametro perchè li faccio prima di questa chiamata
        if (this.head != null){
            Node<E> searchedNode = this.head;
            if (searchedNode.item == element.item){
                return searchedNode;
            }
            while (searchedNode.next != null){
                searchedNode = searchedNode.next;
                if (searchedNode.item == element.item){
                    return searchedNode;
                }
            }
        }
        return null;
    }
    //AC Ridefinisco Iterator per gestire il Fail-safe
    // Non è static perchè deve poter accedere a MyMultiset.this.size
    private class Itr implements Iterator<E> {
        private Node<E> lastReturned;
        private int repeat;
        private int numeroModificheAtteso;

        private Itr(){
            this.lastReturned = null;
            this.numeroModificheAtteso = MyMultiset.this.size;
        }

        @Override
        public boolean hasNext() {
            if (this.lastReturned == null)
                // sono all'inizio dell'iterazione
                return MyMultiset.this.head != null;
            else
                // almeno un next è stato fatto
                if (lastReturned.next != null){
                    return true;
                } else {
                    if (this.repeat > 0){
                        return true;
                    } else {
                        return false;
                    }
                }
        }

        @Override
        public E next() {
            // controllo concorrenza
            if (this.numeroModificheAtteso != MyMultiset.this.size) {
                throw new ConcurrentModificationException(
                        "Lista modificata durante l'iterazione");
            }
            // controllo hasNext()
            if (!hasNext())
                throw new NoSuchElementException(
                        "Richiesta di next quando hasNext è falso");
            // c'è sicuramente un elemento di cui fare next
            // aggiorno lastReturned e restituisco l'elemento next
            if (this.lastReturned == null) {
                // sono all’inizio e la lista non è vuota
                this.lastReturned = MyMultiset.this.head;
                this.repeat = MyMultiset.this.head.occurrencies;
            } else {
                if (this.repeat == 0){
                    lastReturned = lastReturned.next;
                    this.repeat = lastReturned.occurrencies;
                }
                // non sono all’inizio, ma c’è ancora qualcuno
            }
            this.repeat = this.repeat -1;
            return lastReturned.item;
        }
    }

    /**
     * Crea un multiset vuoto.
     */
    public MyMultiset() {
        this.size = 0;
        this.head = null;
        this.tail = null;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public int count(Object element) {
        if (element == null){
            throw new NullPointerException("Il parametro passato è nullo");
        }
        if (this.head != null){ // Se il multiset non è vuoto controllo che l'elemento esista
            Node<E> nodetosearch = this.searchNode(new Node<E>((E) element, null));
            if (nodetosearch != null){
                return nodetosearch.occurrencies;
            }
        }
        return 0;
    }

    //non riutilizzo il metodo add(element) per questioni di performance
    @Override
    public int add(E element, int occurrences) {
        if(element == null){
            throw new NullPointerException("Valori nulli non ammessi");
        }
        if (occurrences < 0 || occurrences > Integer.MAX_VALUE){
            throw new IllegalArgumentException("Numero di occorrenze non valido");
        }
        Node<E> newNode = new Node<E>((E) element, null, occurrences);
        int occ = 0;
        if (this.head == null){ //se il Multiset è vuoto creo il primo nodo
            this.head = newNode;
            this.tail = newNode;
        } else {
            Node<E> nodetosearch = this.searchNode(newNode);
            if (nodetosearch != null){ //controllo se il Nodo già esiste
                occ = nodetosearch.occurrencies;
                if (((nodetosearch.occurrencies + (long)occurrences) > Integer.MAX_VALUE)) {
                    throw new IllegalArgumentException("Numero di occorrenze non valido");
                }
                nodetosearch.occurrencies = nodetosearch.occurrencies + occurrences;
            } else {
                this.tail.next = newNode;
                this.tail = newNode;
            }
        }
        this.size = this.size + occurrences;
        return occ;
    }

    @Override
    public void add(E element) {
        if(element == null){
            throw new NullPointerException("Valori nulli non ammessi");
        }
        Node<E> newNode = new Node<E>((E) element, null);
        if (this.head == null){
            this.head = newNode;
            this.tail = newNode;
        } else {
            Node<E> nodetosearch = this.searchNode(newNode);
            if (nodetosearch != null){
                if (nodetosearch.occurrencies == Integer.MAX_VALUE) {
                    throw new IllegalArgumentException("Numero di occorrenze non valido");
                }
                nodetosearch.occurrencies = nodetosearch.occurrencies + 1;
            } else {
                this.tail.next = newNode;
                this.tail = newNode;
            }
        }
        this.size = this.size + 1;
    }

    // Come per l'add, non ho sfruttato remove(element) per non scorrere il multiset 'occurences' volte
    @Override
    public int remove(Object element, int occurrences) {
        if(element == null){
            throw new NullPointerException("Valori nulli non ammessi");
        }
        if (occurrences < 0){
            throw new IllegalArgumentException("Numero di occorrenze non valido");
        }
        Node<E> newNode = new Node<E>((E) element, null, occurrences);
        Node<E> nodetosearch = this.searchNode(newNode);
        int ret = 0;
        if (nodetosearch != null){
            ret = nodetosearch.occurrencies;
            if ((nodetosearch.occurrencies - occurrences) <= 0){ //controllo se devo eliminare del tutto il nodo
                if (nodetosearch.item == this.head.item){ //controllo se è l'elemento head
                    if (nodetosearch.item == this.tail.item){ //controllo se oltre head è anche tail
                        this.head = null;
                        this.tail = null;
                    } else {
                        this.head = nodetosearch.next;
                    }
                } else {
                    Node<E> workNode = this.head;
                    if (nodetosearch.item == this.tail.item){ //controllo se è l'ultimo elemento
                        while (workNode.next != this.tail){
                            workNode = workNode.next;
                        }
                        workNode.next = null;
                        this.tail = workNode;
                    } else {
                        while (workNode.next != nodetosearch){
                            workNode = workNode.next;
                        }
                        workNode.next = nodetosearch.next;
                    }
                }
            } else {
                nodetosearch.occurrencies = nodetosearch.occurrencies - occurrences;
            }
        }
        this.size = this.size - occurrences;
        return ret;
    }

    @Override
    public boolean remove(Object element) {
        if(element == null){
            throw new NullPointerException("Valori nulli non ammessi");
        }
        Node<E> newNode = new Node<E>((E) element, null);
        Node<E> nodetosearch = this.searchNode(newNode);
        boolean ret = false;
        if (nodetosearch != null){
            if ((nodetosearch.occurrencies - 1) <= 0){ //controllo se devo eliminare del tutto il nodo
                if (nodetosearch.item == this.head.item){ //controllo se è l'elemento head
                    if (nodetosearch.item == this.tail.item){ //controllo se oltre head è anche tail
                        this.head = null;
                        this.tail = null;
                    } else {
                        this.head = nodetosearch.next;
                    }
                } else {
                    Node<E> workNode = this.head;
                    if (nodetosearch.item == this.tail.item){ //controllo se è l'ultimo elemento
                        while (workNode.next != this.tail){
                            workNode = workNode.next;
                        }
                        workNode.next = null;
                        this.tail = workNode;
                    } else {
                        while (workNode.next != nodetosearch){
                            workNode = workNode.next;
                        }
                        workNode.next = nodetosearch.next;
                    }
                }
            } else {
                nodetosearch.occurrencies = nodetosearch.occurrencies - 1;
            }
            ret = true;
            this.size = this.size - 1;
        }
        return ret;
    }

    @Override
    public int setCount(E element, int count) {
        if(element == null){
            throw new NullPointerException("Valori nulli non ammessi");
        }
        if (count < 0){
            throw new IllegalArgumentException("Numero di occorrenze non valido");
        }
        Node<E> newNode = new Node<E>((E) element, null);
        Node<E> nodetosearch = this.searchNode(newNode);
        int ret = 0;
        if (nodetosearch != null){
            ret = nodetosearch.occurrencies;
            if (nodetosearch.occurrencies > count){
                this.remove(element, (nodetosearch.occurrencies - count));
            } else {
                this.add(element, (count - nodetosearch.occurrencies));
            }
        } else {
            this.add(element, count);
        }
        return ret;
    }

    @Override
    public Set<E> elementSet() {
        Set<E> elementSet = new HashSet<E>();
        if (this.head != null){
            Node<E> newNode = this.head;
            while (newNode != null){
                elementSet.add(newNode.item);
                newNode = newNode.next;
            }
        }
        return elementSet;
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override
    public boolean contains(Object element) {
        if(element == null){
            throw new NullPointerException("Valori nulli non ammessi");
        }
        Node<E> newNode = new Node<E>((E) element, null);
        Node<E> nodetosearch = this.searchNode(newNode);
        return  nodetosearch != null;
    }

    @Override
    public void clear() {
        this.head = null;
        this.tail = null;
        this.size = 0;
        System.gc();  //Garbage Collector, elimino dalla memoria tutti gli oggetti non referenziati
    }

    @Override
    public boolean isEmpty() {
        if (this.size > 0){
            return false;
        }
        return true;
    }

    /*
     * Due multinsiemi sono uguali se e solo se contengono esattamente gli
     * stessi elementi (utilizzando l'equals della classe E) con le stesse
     * molteplicità.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            throw new NullPointerException("Valori nulli non ammessi");
        }
        MyMultiset<E> otherSet = (MyMultiset<E>) obj;
        if (this.size != otherSet.size){
            return false;
        }
        Node<E> actualNode = this.head;
        while (actualNode != null){
            Node<E> searchedNode = otherSet.searchNode(actualNode);
            if(searchedNode == null || actualNode.occurrencies != searchedNode.occurrencies){
                return false;
            }
            actualNode = actualNode.next;
        }
        return true;
    }

    /*
     * Da ridefinire in accordo con la ridefinizione di equals.
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        Node<E> searchedNode = this.head;
        while (searchedNode.next != null){
            result = prime * (result + searchedNode.item.hashCode() + searchedNode.occurrencies);
            searchedNode = searchedNode.next;
        }
        return result;
    }

}

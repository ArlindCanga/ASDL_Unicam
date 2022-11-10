package it.unicam.cs.asdl2122.es6;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Lista concatenata singola che non accetta valori null, ma permette elementi
 * duplicati. Le seguenti operazioni non sono supportate:
 * 
 * <ul>
 * <li>ListIterator<E> listIterator()</li>
 * <li>ListIterator<E> listIterator(int index)</li>
 * <li>List<E> subList(int fromIndex, int toIndex)</li>
 * <li>T[] toArray(T[] a)</li>
 * <li>boolean containsAll(Collection<?> c)</li>
 * <li>addAll(Collection<? extends E> c)</li>
 * <li>boolean addAll(int index, Collection<? extends E> c)</li>
 * <li>boolean removeAll(Collection<?> c)</li>
 * <li>boolean retainAll(Collection<?> c)</li>
 * </ul>
 * 
 * L'iteratore restituito dal metodo {@code Iterator<E> iterator()} è fail-fast,
 * cioè se c'è una modifica strutturale alla lista durante l'uso dell'iteratore
 * allora lancia una {@code ConcurrentMopdificationException} appena possibile,
 * cioè alla prima chiamata del metodo {@code next()}.
 * 
 * @author Luca Tesei
 *
 * @param <E>
 *                il tipo degli elementi della lista
 */
public class ASDL2122SingleLinkedList<E> implements List<E> {

    private int size;

    private Node<E> head;

    private Node<E> tail;

    private int numeroModifiche;

    /**
     * Crea una lista vuota.
     */
    public ASDL2122SingleLinkedList() {
        this.size = 0;
        this.head = null;
        this.tail = null;
        this.numeroModifiche = 0;
    }

    /*
     * Classe per i nodi della lista concatenata. E' dichiarata static perché
     * gli oggetti della classe Node<E> non hanno bisogno di accedere ai campi
     * della classe principale per funzionare.
     */
    private static class Node<E> {
        private E item;

        private Node<E> next;

        /*
         * Crea un nodo "singolo" equivalente a una lista con un solo elemento.
         */
        Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
        }

    }

    /*
     * Classe che realizza un iteratore per ASDL2122SingleLinkedList.
     * L'iteratore deve essere fail-fast, cioè deve lanciare una eccezione
     * ConcurrentModificationException se a una chiamata di next() si "accorge"
     * che la lista è stata cambiata rispetto a quando l'iteratore è stato
     * creato.
     * 
     * La classe è non-static perché l'oggetto iteratore, per funzionare
     * correttamente, ha bisogno di accedere ai campi dell'oggetto della classe
     * principale presso cui è stato creato.
     */
    private class Itr implements Iterator<E> {

        private Node<E> lastReturned;

        private int numeroModificheAtteso;

        private Itr() {
            // All'inizio non è stato fatto nessun next
            this.lastReturned = null;
            this.numeroModificheAtteso = ASDL2122SingleLinkedList.this.numeroModifiche;
        }

        @Override
        public boolean hasNext() {
            if (this.lastReturned == null)
                // sono all'inizio dell'iterazione
                return ASDL2122SingleLinkedList.this.head != null;
            else
                // almeno un next è stato fatto
                return lastReturned.next != null;

        }

        @Override
        public E next() {
            // controllo concorrenza
            if (this.numeroModificheAtteso != ASDL2122SingleLinkedList.this.numeroModifiche) {
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
                this.lastReturned = ASDL2122SingleLinkedList.this.head;
                return ASDL2122SingleLinkedList.this.head.item;
            } else {
                // non sono all’inizio, ma c’è ancora qualcuno
                lastReturned = lastReturned.next;
                return lastReturned.item;
            }

        }

    }

    /*
     * Una lista concatenata è uguale a un'altra lista se questa è una lista
     * concatenata e contiene gli stessi elementi nello stesso ordine.
     * 
     * Si noti che si poteva anche ridefinire il metodo equals in modo da
     * accettare qualsiasi oggetto che implementi List<E> senza richiedere che
     * sia un oggetto di questa classe:
     * 
     * obj instanceof List
     * 
     * In quel caso si può fare il cast a List<?>:
     * 
     * List<?> other = (List<?>) obj;
     * 
     * e usando l'iteratore si possono tranquillamente controllare tutti gli
     * elementi (come è stato fatto anche qui):
     * 
     * Iterator<E> thisIterator = this.iterator();
     * 
     * Iterator<?> otherIterator = other.iterator();
     * 
     * ...
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (!(obj instanceof ASDL2122SingleLinkedList))
            return false;
        ASDL2122SingleLinkedList<?> other = (ASDL2122SingleLinkedList<?>) obj;
        // Controllo se entrambe liste vuote
        if (head == null) {
            if (other.head != null)
                return false;
            else
                return true;
        }
        // Liste non vuote, scorro gli elementi di entrambe
        Iterator<E> thisIterator = this.iterator();
        Iterator<?> otherIterator = other.iterator();
        while (thisIterator.hasNext() && otherIterator.hasNext()) {
            E o1 = thisIterator.next();
            // uso il polimorfismo di Object perché non conosco il tipo ?
            Object o2 = otherIterator.next();
            // il metodo equals che si usa è quello della classe E
            if (!o1.equals(o2))
                return false;
        }
        // Controllo che entrambe le liste siano terminate
        return !(thisIterator.hasNext() || otherIterator.hasNext());
    }

    /*
     * L'hashcode è calcolato usando gli hashcode di tutti gli elementi della
     * lista.
     */
    @Override
    public int hashCode() {
        int hashCode = 1;
        // implicitamente, col for-each, uso l'iterator di questa classe
        for (E e : this)
            hashCode = 31 * hashCode + e.hashCode();
        return hashCode;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public boolean contains(Object o) {
        if(o == null){
            throw new NullPointerException("Valori nulli non ammessi");
        }
        Iterator<E> iterator = this.iterator();
        while(iterator.hasNext()){
            E item = iterator.next();
            if(item.equals(o)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        if(e == null){
            throw new NullPointerException("Valori nulli non ammessi");
        }
        Node<E> newNode = new Node(e, null); //New Node
        if(this.isEmpty()){
            //se la lista è vuota salvo il nodo sulla coda e sulla testa
            this.head = newNode;
            this.tail = newNode;
        }else{
            Node<E> item = this.head;
            while (!item.equals(tail)){
                item = item.next;
            }
            item.next = newNode;
            this.tail = newNode;
        }
        this.size++;
        this.numeroModifiche++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if(o == null){
            throw new NullPointerException("Valori nulli non ammessi");
        }
        if(this.isEmpty()){
            return false;
        }
        Node<E> item = this.head;
        while(item.next != null){
            if(item.next.item.equals(o)){
                item.next = item.next.next; //prendo il next di due posizioni dopo
                if(item.next == null){
                    this.tail = item;
                }
                this.numeroModifiche++;
                this.size--;
                return true;
            }
            item = item.next;
        }
        return false;
    }

    @Override
    public void clear() {
        this.head = null;
        this.tail = null;
        this.size = 0;
        this.numeroModifiche++;
    }

    @Override
    public E get(int index) {
        if(index >= this.size || index < 0){
            throw new IndexOutOfBoundsException("Index out of range");
        }
        Node<E> searchedNode = this.head;
        for(int i = 0; i < index; i++){
            searchedNode = searchedNode.next;
        }
        return searchedNode.item;
    }

    @Override
    public E set(int index, E element) {
        if(element == null){
            throw new NullPointerException("Valori nulli non ammessi");
        }
        if(index < 0 || index >= this.size){
            throw new IndexOutOfBoundsException("Index out of range");
        }
        Node<E> searchedNode = this.head;
        for(int i = 0; i < index; i++){
            searchedNode = searchedNode.next;
        }
        E item = searchedNode.item;
        searchedNode.item = element;
        return item;
    }

    @Override
    public void add(int index, E element) {
        if(element == null){
            throw new NullPointerException("Valori nulli non ammessi");
        }
        if(index > this.size || index < 0){
            throw new ArrayIndexOutOfBoundsException("Index out of range");
        }
        if(index == 0){
            Node<E> newNode = new Node(element, this.head);
            this.head = newNode;
        }else{
            Node<E> searchedNode = this.head;
            for(int i = 0; i < index; i++){
                searchedNode = searchedNode.next;
            }
            Node<E> newNode = new Node(element, searchedNode.next);
            searchedNode.next = newNode;
            if(index == this.size){
                this.tail = newNode;
            }
        }
        this.size++;
        this.numeroModifiche++;

    }

    @Override
    public E remove(int index) {
        if(index >= this.size || index < 0){
            throw new ArrayIndexOutOfBoundsException("Index out of range");
        }
        if(index == 0){ //in caso di indice 0 elimino il primo elemento dopo head
            this.head = this.head.next;
        }
        Node<E> searchedNode = this.head;
        for(int i = 0; i < index - 1; i++){
            searchedNode = searchedNode.next;
        }
        E item = searchedNode.next.item;
        searchedNode.next = searchedNode.next.next;
        if(searchedNode.next == null){
            this.tail = searchedNode;
        }
        this.size--;
        this.numeroModifiche++;
        return item;
    }

    @Override
    public int indexOf(Object o) {
        if(o == null){
            throw new NullPointerException("Valri nulli non ammessi");
        }
        Iterator<E> iterator = this.iterator();
        int i = 0;
        while(iterator.hasNext()){
            if(iterator.next().equals(o)){
                return i;
            }
            i++;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if(o == null){
            throw new NullPointerException("Valri nulli non ammessi");
        }
        Iterator<E> iterator = this.iterator();
        int i = 0;
        int index = -1;
        while(iterator.hasNext()){
            if(iterator.next().equals(o)){
                index = i;
            }
            i++;
        }
        return index;
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[this.size];
        for(int i = 0; i < this.size; i++){
            arr[i] = this.get(i);
        }
        return arr;
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }
}

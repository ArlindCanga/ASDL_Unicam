package it.unicam.cs.asdl2122.mp1;

import java.util.HashSet;
import java.util.Set;


/**
 * La classe LinkedListDisjointSets gestisce una collezione di elementi disgiunti
 * seguendo il modello dell'interfaccia DisjointSets.
 * Questa classe è formata da un insieme di Nodi, ognuno dei quali rappresenta un insieme
 * disgiunto.
 *
 * @author Luca Tesei (template) ** Arlind, Canga
 *         arlind.canga@studenti.unicam.it** (implementazione)
 *
 */
public class LinkedListDisjointSets implements DisjointSets {

    private Node<DisjointSetElement> head;
    private Node<DisjointSetElement> tail;


    // AC - Classe interna Node per gli insiemi

    private static class Node<DisjointSetElement> {
        private DisjointSetElement item;
        private Node<DisjointSetElement> next;


        Node(DisjointSetElement item, LinkedListDisjointSets.Node<DisjointSetElement> next) {
            this.item = item;
            this.next = next;
        }

    }


    /**
     * Crea una collezione vuota di insiemi disgiunti.
     */
    public LinkedListDisjointSets() {
        this.head = null;
        this.tail = null;
    }

    /*
     * Nella rappresentazione con liste concatenate un elemento è presente in
     * qualche insieme disgiunto se il puntatore al suo elemento rappresentante
     * (ref1) non è null.
     */
    @Override
    public boolean isPresent(DisjointSetElement e) {
        if (e == null){
            throw new IllegalArgumentException("Argomento nullo");
        }
        if (!(this.head == null)){
            //scorro gli insiemi disgiunti
            Node<DisjointSetElement> currentNode = this.head;
            while (currentNode != null){
                DisjointSetElement currentElement = currentNode.item;
                while(currentElement != null){
                    if (currentElement.equals(e)){
                        return true;
                    }
                    currentElement = currentElement.getRef2();
                }
                currentNode = currentNode.next;
            }
        }
        return false;
    }

    /*
     * Nella rappresentazione con liste concatenate un nuovo insieme disgiunto è
     * rappresentato da una lista concatenata che contiene l'unico elemento. Il
     * rappresentante deve essere l'elemento stesso e la cardinalità deve essere
     * 1.
     */
    @Override
    public void makeSet(DisjointSetElement e) {
        if (e == null){
            throw new NullPointerException("Argomento nullo");
        }
        if (this.isPresent(e)){
            throw new IllegalArgumentException("Elemento già presente");
        }
        e.setRef1(e); //essendo il primo dell'insieme lo setto come rappresentante
        e.setNumber(1);
        Node<DisjointSetElement> currentNode = new Node<DisjointSetElement>(e, null);
        if (this.head == null){
            this.head = currentNode;
            this.tail = currentNode;
        } else {
            this.tail.next = currentNode;
            this.tail = currentNode;
        }
    }

    /*
     * Nella rappresentazione con liste concatenate per trovare il
     * rappresentante di un elemento basta far riferimento al suo puntatore
     * ref1.
     */
    @Override
    public DisjointSetElement findSet(DisjointSetElement e) {
        if (e == null){
            throw new NullPointerException("Argomento nullo");
        }
        if (!(this.head == null)){
            Node<DisjointSetElement> currentNode = this.head;
            while (currentNode != null){
                DisjointSetElement currentElement = currentNode.item;
                while(currentElement != null){
                    if (currentElement.equals(e)){
                        return currentElement.getRef1();
                    }
                    currentElement = currentElement.getRef2();
                }
                currentNode = currentNode.next;
            }
        }
        throw new IllegalArgumentException("Elemento non trovato in nessun insieme");
    }

    /*
     * Dopo l'unione di due insiemi effettivamente disgiunti il rappresentante
     * dell'insieme unito è il rappresentate dell'insieme che aveva il numero
     * maggiore di elementi tra l'insieme di cui faceva parte {@code e1} e
     * l'insieme di cui faceva parte {@code e2}. Nel caso in cui entrambi gli
     * insiemi avevano lo stesso numero di elementi il rappresentante
     * dell'insieme unito è il rappresentante del vecchio insieme di cui faceva
     * parte {@code e1}.
     * 
     * Questo comportamento è la risultante naturale di una strategia che
     * minimizza il numero di operazioni da fare per realizzare l'unione nel
     * caso di rappresentazione con liste concatenate.
     * 
     */
    @Override
    public void union(DisjointSetElement e1, DisjointSetElement e2) {
        if (e1 == null || e2 == null){
            throw new NullPointerException("Argomenti nulli");
        }
        if (!(this.isPresent(e1) && this.isPresent(e2))){
            throw new IllegalArgumentException("Elementi non presenti in nessun insieme");
        }
        Node<DisjointSetElement> currentNode = this.head;
        Node<DisjointSetElement> element1 = null;
        Node<DisjointSetElement> element2 = null;
        DisjointSetElement currentElement;
        //scorro i nodi
        while ((currentNode != null) && (element1 == null || element2 == null)){
            currentElement = currentNode.item;
            //scorro gli insiemi, se trovo un elemento uguale mi segno il nodo
            while((currentElement != null) && (element1 == null || element2 == null)){
                if (currentElement.equals(e1)) {
                    element1 = currentNode;
                }
                if(currentElement.equals(e2)){
                    element2 = currentNode;
                }
                currentElement = currentElement.getRef2();
            }
            currentNode = currentNode.next;
        }
        //se non fanno parte dello stesso insieme  procedo con l'unione
        if (!(element1.item.equals(element2.item))){
            //ordino gli insiemi in base alla loro size, se size e1 = size e2 --> unisco sx <-- sy
            if (element1.item.getNumber() < element2.item.getNumber()){
                Node<DisjointSetElement> element3 = element1;
                element1 = element2;
                element2 = element3;
            }
            //Inizialmente sistemo i Nodi
            Node<DisjointSetElement> wkNode = this.head;
            if (wkNode != element2){
                while (wkNode != null){ //ciclo per prendere il nodo prima del nodo da eliminare
                    if (wkNode.next == element2){
                        if (element2 == this.tail){
                            wkNode.next = null;
                            this.tail = wkNode;
                        } else {
                            wkNode.next = element2.next;
                        }
                        break;
                    }
                    wkNode = wkNode.next;
                }
            } else {
                this.head = wkNode.next;
            }
            //inizio con l'unione vera e propria
            DisjointSetElement e3 = element2.item;
            while (e3 != null){
                e3.setRef1(element1.item.getRef1());  //cambio il rapresentative
                if(e3.getRef2() == null){
                    break;
                }
                e3 = e3.getRef2();
            }
            element1.item.getRef1().setNumber(element1.item.getNumber() + element2.item.getNumber()); //setto size
            //concateno DisjointSetElement
            if (element1.item.getRef2() != null){
                e3.setRef2(element1.item.getRef2());
            }
            element1.item.setRef2(element2.item);
        }

    }

    @Override
    public Set<DisjointSetElement> getCurrentRepresentatives() {
        Set<DisjointSetElement> ref1 = new HashSet<DisjointSetElement>();
        Node<DisjointSetElement> currentNode = this.head;
        while (currentNode != null){
            ref1.add(currentNode.item.getRef1());
            currentNode = currentNode.next;
        }
        return ref1;
    }

    @Override
    public Set<DisjointSetElement> getCurrentElementsOfSetContaining(
            DisjointSetElement e) {
        if (e == null){
            throw new NullPointerException("Argomento nullo");
        }
        if (!this.isPresent(e)){
            throw new IllegalArgumentException("Elemento non presente in nessun insieme");
        }
        Set<DisjointSetElement> ref1 = new HashSet<DisjointSetElement>();
        if (!(this.head == null)){
            //scorro gli insiemi disgiunti
            Node<DisjointSetElement> currentNode = this.head;
            Node<DisjointSetElement> myNode = null;
            while (currentNode != null){
                DisjointSetElement currentElement = currentNode.item;
                while(currentElement != null){
                    if (currentElement.equals(e)){
                        myNode = currentNode; //prendo il Nodo dell'insieme che mi interessa
                        break;
                    }
                    currentElement = currentElement.getRef2();
                }
                if (myNode != null){
                    break;
                }
                currentNode = currentNode.next;
            }
            if(myNode != null){
                DisjointSetElement elem = myNode.item;
                while(elem != null){
                    ref1.add(elem);
                    elem = elem.getRef2();
                }
            }
        }
        return ref1;
    }

    @Override
    public int getCardinalityOfSetContaining(DisjointSetElement e) {
        if (e == null){
            throw new NullPointerException("Argomento nullo");
        }
        if (!this.isPresent(e)){
            throw new IllegalArgumentException("Elemento non presente in nessun insieme");
        }
        return e.getRef1().getNumber();
    }

}

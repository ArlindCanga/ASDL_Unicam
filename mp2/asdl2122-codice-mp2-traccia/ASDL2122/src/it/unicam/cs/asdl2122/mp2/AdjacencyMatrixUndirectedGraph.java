/**
 * 
 */
package it.unicam.cs.asdl2122.mp2;

import java.lang.reflect.Array;
import java.util.*;

// TODO completare gli import necessari

// ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 * Classe che implementa un grafo non orientato tramite matrice di adiacenza.
 * Non sono accettate etichette dei nodi null e non sono accettate etichette
 * duplicate nei nodi (che in quel caso sono lo stesso nodo).
 * 
 * I nodi sono indicizzati da 0 a nodeCoount() - 1 seguendo l'ordine del loro
 * inserimento (0 è l'indice del primo nodo inserito, 1 del secondo e così via)
 * e quindi in ogni istante la matrice di adiacenza ha dimensione nodeCount() *
 * nodeCount(). La matrice, sempre quadrata, deve quindi aumentare di dimensione
 * ad ogni inserimento di un nodo. Per questo non è rappresentata tramite array
 * ma tramite ArrayList.
 * 
 * Gli oggetti GraphNode<L>, cioè i nodi, sono memorizzati in una mappa che
 * associa ad ogni nodo l'indice assegnato in fase di inserimento. Il dominio
 * della mappa rappresenta quindi l'insieme dei nodi.
 * 
 * Gli archi sono memorizzati nella matrice di adiacenza. A differenza della
 * rappresentazione standard con matrice di adiacenza, la posizione i,j della
 * matrice non contiene un flag di presenza, ma è null se i nodi i e j non sono
 * collegati da un arco e contiene un oggetto della classe GraphEdge<L> se lo
 * sono. Tale oggetto rappresenta l'arco. Un oggetto uguale (secondo equals) e
 * con lo stesso peso (se gli archi sono pesati) deve essere presente nella
 * posizione j, i della matrice.
 *
 * Questa classe supporta i metodi di cancellazione di nodi e archi e
 * supporta tutti i metodi che usano indici, utilizzando l'indice assegnato a
 * ogni nodo in fase di inserimento.
 * 
 * @author Luca Tesei (template) **Arlind Canga, arlind.canga@studenti.unicam.it** (implementazione)
 *
 * 
 */
public class AdjacencyMatrixUndirectedGraph<L> extends Graph<L> {
    /*
     * Le seguenti variabili istanza sono protected al solo scopo di agevolare
     * il JUnit testing
     */

    /*
     * Insieme dei nodi e associazione di ogni nodo con il proprio indice nella
     * matrice di adiacenza
     */
    protected Map<GraphNode<L>, Integer> nodesIndex;

    /*
     * Matrice di adiacenza, gli elementi sono null o oggetti della classe
     * GraphEdge<L>. L'uso di ArrayList permette alla matrice di aumentare di
     * dimensione gradualmente ad ogni inserimento di un nuovo nodo e di
     * ridimensionarsi se un nodo viene cancellato.
     */
    protected ArrayList<ArrayList<GraphEdge<L>>> matrix;

    /**
     * Crea un grafo vuoto.
     */
    public AdjacencyMatrixUndirectedGraph() {
        this.matrix = new ArrayList<ArrayList<GraphEdge<L>>>();
        this.nodesIndex = new HashMap<GraphNode<L>, Integer>();
    }

    @Override
    public int nodeCount() {
        //ogni matrix[i] (quindi ogni riga) rappresenta un nodo
        return matrix.size();
    }

    @Override
    public int edgeCount() {
        int res = 0;
        for (int i = 0; i < this.matrix.size(); i++) {
            for (int j = i; j < this.matrix.size(); j++) {
                if (this.matrix.get(i).get(j) != null) {
                    res++;
                }
            }
        }
        return res;
    }

    @Override
    public void clear() {
        //pulisco matrice e Map
        this.nodesIndex = new HashMap<GraphNode<L>, Integer>();
        this.matrix = new ArrayList<ArrayList<GraphEdge<L>>>();
    }

    @Override
    public boolean isDirected() {
        //Stiamo implementando un grafo non orientato
        return false;
    }

    /*
     * Gli indici dei nodi vanno assegnati nell'ordine di inserimento a partire
     * da zero
     */
    @Override
    public boolean addNode(GraphNode<L> node) {
        if (node == null){
            throw new NullPointerException("Nodo Nullo!");
        }
        //controllo se il nodo esiste
        if (this.getNodes().contains(node)){
            return false;
        }
        // Se il nodo non esiste lo vado ad aggiungere, inizialmente sulla map poi sulla matrice
        this.nodesIndex.put(node, this.nodeCount());
        // creo una nuova riga da inserire nella matrice e la inizializzo a null
        ArrayList<GraphEdge<L>> newRow = new ArrayList<GraphEdge<L>>();
        for (int i = 0; i <= this.matrix.size(); i++){ //<= perchè ancora non è nell'arraylist
            newRow.add(null);
        }
        //creo una colonna sulla matrice e la metto a null perchè non ho archi con l'ultimo nodo aggiunto
        for (ArrayList<GraphEdge<L>> row : this.matrix) {
            row.add(null);
        }
        //aggiungo la riga alla matrice
        this.matrix.add(newRow);
        return true;
    }

    /*
     * Gli indici dei nodi vanno assegnati nell'ordine di inserimento a partire
     * da zero
     */
    @Override
    public boolean addNode(L label) {
        if (label == null){
            throw new NullPointerException("Label Nulla!");
        }
        return this.addNode(new GraphNode<L>(label));
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(GraphNode<L> node) {
        if (node == null){
            throw new NullPointerException("Nodo Nullo!");
        }
        if (!this.getNodes().contains(node)){
            throw new IllegalArgumentException("Il nodo non esiste!");
        }
        //rimuovo il nodo, salvo il valore per poter cambiare gli indici degli altri nodi
        int nodeValue = this.nodesIndex.get(node);
        this.nodesIndex.remove(node);
        //cambio gli indici dei nodi con indice maggiore del nodo da eliminare
        for (GraphNode<L> key : this.nodesIndex.keySet()){
            if (this.nodesIndex.get(key) > nodeValue){
                this.nodesIndex.replace(key, (this.nodesIndex.get(key) - 1));
            }
        }
        // rimuovo dalla matrice
        this.matrix.remove(nodeValue);
        for (int i = 0; i < this.matrix.size(); i++){
            this.matrix.get(i).remove(nodeValue);
        }
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(L label) {
        if (label == null){
            throw new NullPointerException("Label Nulla!");
        }
        this.removeNode(new GraphNode<L>(label));
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(int i) {
        if (i < 0 || i > this.matrix.size() - 1){
            throw new IndexOutOfBoundsException ("L'indice passato è errato");
        }
        //cerco il nodo con indice i
        for (GraphNode<L> key : this.nodesIndex.keySet()){
            if (this.nodesIndex.get(key) == i){
                this.removeNode(key);
                break;
            }
        }
    }

    @Override
    public GraphNode<L> getNode(GraphNode<L> node) {
        if (node == null){
            throw new NullPointerException("Nodo Nullo!");
        }
        if (this.getNodes().contains(node)){
            //equals controlla solo i valori percò i nodi potrebbero essere diversi da altri punti di vista
            //quindi per correttezza ritorno il puntatore al nodo originale
            for (GraphNode<L> key : this.nodesIndex.keySet()){
                if (key.equals(node)){
                    return key;
                }
            }
        }
        return null;
    }

    @Override
    public GraphNode<L> getNode(L label) {
        if (label == null){
            throw new NullPointerException("Nodo Nullo!");
        }
        return this.getNode(new GraphNode<L>(label));
    }

    @Override
    public GraphNode<L> getNode(int i) {
        if (i < 0 || i > this.matrix.size() - 1){
            throw new IndexOutOfBoundsException ("L'indice passato è errato");
        }
        for (GraphNode<L> key : this.nodesIndex.keySet()){
            if (this.nodesIndex.get(key) == i){
                return this.getNode(key);
            }
        }
        return null;
    }

    @Override
    public int getNodeIndexOf(GraphNode<L> node) {
        if (node == null){
            throw new NullPointerException("Nodo Nullo!");
        }
        if (!this.getNodes().contains(node)){
            throw new IllegalArgumentException("Il nodo non esiste");
        }
        return this.nodesIndex.get(node);
    }

    @Override
    public int getNodeIndexOf(L label) {
        if (label == null){
            throw new NullPointerException("Label Nulla!");
        }
        return this.getNodeIndexOf(new GraphNode<L>(label));
    }

    @Override
    public Set<GraphNode<L>> getNodes() {
        return this.nodesIndex.keySet();
    }

    @Override
    public boolean addEdge(GraphEdge<L> edge) {
        // Se l' arco specificato è nullo
        if (edge == null)
            throw new NullPointerException("Arco nullo");
        if (edge.isDirected() && !this.isDirected())
            throw new IllegalArgumentException("Arco orientato non ammesso!");
        if (!edge.isDirected() && this.isDirected())
            throw new IllegalArgumentException("L' arco specificato non è orientato mentre questo grafo è orientato!");
        //controllo che i nodi dell'arco esistono
        if ((this.nodesIndex.get(edge.getNode1()) == null) || (this.nodesIndex.get(edge.getNode2()) == null)){
            throw new IllegalArgumentException("Uno dei due nodi non esiste!");
        }
        int indexNode1 = this.nodesIndex.get(edge.getNode1());
        int indexNode2 = this.nodesIndex.get(edge.getNode2());
        //controllo se l'arco è già presente,
        //controllo il null per evitare l'exception NullPointer
        if (this.matrix.get(indexNode1).get(indexNode2) == null
                || !this.matrix.get(indexNode1).get(indexNode2).equals(edge)){
            //inserisco l'arco su matrix[index1][index2]
            this.matrix.get(indexNode1).set(indexNode2, edge);
            //inserisco l'arco su matrix[index2][index1] perchè non so in che ordine mi arrivano gli indici
            this.matrix.get(indexNode2).set(indexNode1, edge);
            return true;
        }
        return false;
    }

    @Override
    public boolean addEdge(GraphNode<L> node1, GraphNode<L> node2) {
        if (node1 == null || node2 == null){
            throw new NullPointerException("Uno dei due nodi è nullo!");
        }
        return this.addEdge(new GraphEdge<L>(node1, node2, this.isDirected()));
    }

    @Override
    public boolean addWeightedEdge(GraphNode<L> node1, GraphNode<L> node2,
            double weight) {
        if (node1 == null || node2 == null){
            throw new NullPointerException("Uno dei due nodi è nullo!");
        }
        return this.addEdge(new GraphEdge<L>(node1, node2, this.isDirected(), weight));
    }

    @Override
    public boolean addEdge(L label1, L label2) {
        if (label1 == null || label2 == null){
            throw new NullPointerException("Label nulla!");
        }
        GraphNode<L> node1 = new GraphNode<L>(label1);
        GraphNode<L> node2 = new GraphNode<L>(label2);
        return this.addEdge(node1, node2);
    }

    @Override
    public boolean addWeightedEdge(L label1, L label2, double weight) {
        if (label1 == null || label2 == null){
            throw new NullPointerException("Label nulla!");
        }
        GraphNode<L> node1 = new GraphNode<L>(label1);
        GraphNode<L> node2 = new GraphNode<L>(label2);
        return addWeightedEdge(node1, node2, weight);
    }

    @Override
    public boolean addEdge(int i, int j) {
        //controllo se gli indici sono nel range giusto e se corrispondono ad una valore di una Chiave sulla Map
        if (((i < 0 || i > this.matrix.size() - 1) || (j < 0 || j > this.matrix.size() - 1)) //controllo degli indici
             || !this.nodesIndex.containsValue(i) || !this.nodesIndex.containsValue(j)){ //controllo che siano presenti
            throw new IndexOutOfBoundsException ("L'indice passato è errato");
        }
        GraphNode<L> node1 = null;
        GraphNode<L> node2 = null;
        for (GraphNode<L> key : this.nodesIndex.keySet()){
            if (this.nodesIndex.get(key) == i){
                node1 = key;
            }
            if (this.nodesIndex.get(key) == j){
                node2 = key;
            }
        }
        if (node1 != null && node2 != null){
            return this.addEdge(node1, node2);
        }
        return false;
    }

    @Override
    public boolean addWeightedEdge(int i, int j, double weight) {
        if (((i < 0 || i > this.matrix.size() - 1) || (j < 0 || j > this.matrix.size() - 1)) //controllo degli indici
                || !this.nodesIndex.containsValue(i) || !this.nodesIndex.containsValue(j)){ //controllo che siano presenti
            throw new IndexOutOfBoundsException ("L'indice passato è errato");
        }
        GraphNode<L> node1 = null;
        GraphNode<L> node2 = null;
        for (GraphNode<L> key : this.nodesIndex.keySet()){
            if (this.nodesIndex.get(key) == i){
                node1 = key;
            }
            if (this.nodesIndex.get(key) == j){
                node2 = key;
            }
        }
        if (node1 != null && node2 != null){
            return this.addEdge(new GraphEdge<L>(node1, node2, this.isDirected(), weight));
        }
        return false;
    }

    @Override
    public void removeEdge(GraphEdge<L> edge) {
        if (edge == null){
            throw new NullPointerException("Arco nullo");
        }
        //controllo che i nodi dell'arco esistono
        if ((this.nodesIndex.get(edge.getNode1()) == null) || (this.nodesIndex.get(edge.getNode2()) == null)){
            throw new IllegalArgumentException("Uno dei due nodi non esiste!");
        }
        int indexNode1 = this.nodesIndex.get(edge.getNode1());
        int indexNode2 = this.nodesIndex.get(edge.getNode2());
        //controllo se l'arco è già presente,
        //controllo il null per evitare l'exception NullPointer
        if (this.matrix.get(indexNode1).get(indexNode2) == null
                || !this.matrix.get(indexNode1).get(indexNode2).equals(edge)){
            throw new IllegalArgumentException("Il nodo da rimuovere non esiste");
        }
        this.matrix.get(indexNode1).set(indexNode2, null);
        this.matrix.get(indexNode2).set(indexNode1, null);
    }

    @Override
    public void removeEdge(GraphNode<L> node1, GraphNode<L> node2) {
        if (node1 == null || node2 == null){
            throw new NullPointerException("Argomento nullo");
        }
        //richiamo il metodo removeEdge che accetta un arco come input,
        //i controlli mancanti verranno effettuati dentro al metodo
        this.removeEdge(new GraphEdge<L>(node1, node2, this.isDirected()));
    }

    @Override
    public void removeEdge(L label1, L label2) {
        if (label1 == null || label2 == null){
            throw new NullPointerException("Label nulla!");
        }
        GraphNode<L> node1 = new GraphNode<L>(label1);
        GraphNode<L> node2 = new GraphNode<L>(label2);
        //richiamo il metodo removeEdge che accetta in input due nodi
        //i controlli mancanti verranno effettuati dentro al metodo
        this.removeEdge(node1,node2);
    }

    @Override
    public void removeEdge(int i, int j) {
        if ((i < 0 || i > this.matrix.size() - 1) || (j < 0 || j > this.matrix.size() - 1)){
            throw new IndexOutOfBoundsException ("L'indice passato è errato");
        }
        GraphNode<L> node1 = null;
        GraphNode<L> node2 = null;
        for (GraphNode<L> key : this.nodesIndex.keySet()){
            if (this.nodesIndex.get(key) == i){
                node1 = key;
            }
            if (this.nodesIndex.get(key) == j){
                node2 = key;
            }
        }
        //richiamo il metodo removeEdge che accetta in input due nodi
        //i controlli mancanti verranno effettuati dentro al metodo
        this.removeEdge(node1,node2);
    }

    @Override
    public GraphEdge<L> getEdge(GraphEdge<L> edge) {
        if (edge == null){
            throw new NullPointerException("Arco nullo!");
        }
        if (!(this.getNodes().contains(edge.getNode1()) && this.getNodes().contains(edge.getNode2()))){
            throw new IllegalArgumentException("I nodi dell'arco non sono presenti nel grafo");
        }
        for (int i = 0; i < this.matrix.size(); i++){
            for (int j = i; j < this.matrix.get(i).size(); j++){
                if (this.matrix.get(i).get(j) != null && this.matrix.get(i).get(j).equals(edge)){
                    return this.matrix.get(i).get(j);
                }
            }
        }
        return null;
    }

    @Override
    public GraphEdge<L> getEdge(GraphNode<L> node1, GraphNode<L> node2) {
        if (node1 == null || node2 == null){
            throw new NullPointerException("Uno dei due nodi è nullo");
        }
        if (!(this.getNodes().contains(node1)) && this.getNodes().contains(node2)){
            throw new IllegalArgumentException("Nodo non presente nel grafo");
        }
        return getEdge(new GraphEdge<L>(node1, node2, this.isDirected()));
    }

    @Override
    public GraphEdge<L> getEdge(L label1, L label2) {
        if (label1 == null || label2 == null){
            throw new NullPointerException("Label nulla");
        }
        GraphNode<L> node1 = new GraphNode<L>(label1);
        GraphNode<L> node2 = new GraphNode<L>(label2);
        return getEdge(node1,node2);
    }

    @Override
    public GraphEdge<L> getEdge(int i, int j) {
        if ((i < 0 || i > this.matrix.size() - 1) || (j < 0 || j > this.matrix.size() - 1)){
            throw new IndexOutOfBoundsException ("L'indice passato è errato");
        }
        GraphNode<L> node1 = null;
        GraphNode<L> node2 = null;
        for (GraphNode<L> key : this.nodesIndex.keySet()){
            if (this.nodesIndex.get(key) == i){
                node1 = key;
            }
            if (this.nodesIndex.get(key) == j){
                node2 = key;
            }
        }
        if (node1 != null && node2 != null){
            return this.getEdge(node1,node2);
        }
        return null;
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(GraphNode<L> node) {
        if (node == null){
            throw new NullPointerException("Nodo nullo");
        }
        if (!this.getNodes().contains(node)){
            throw new IllegalArgumentException("Il nodo passato non esiste nel grafo");
        }
        Set<GraphNode<L>> nodiAdiacenti = new HashSet<GraphNode<L>>();
        // creo un array dove memorizzo in ordine di value le key della map
        GraphNode<L> wkArray[] = new GraphNode[this.nodesIndex.size()];
        for (GraphNode<L> key : this.nodesIndex.keySet()){
            wkArray[this.nodesIndex.get(key)] = key;
        }
        //scorro le colonne della riga di node
        for (int i = 0; i < this.nodesIndex.size(); i++){
            if (this.matrix.get(this.nodesIndex.get(node)).get(i) != null){
                nodiAdiacenti.add(wkArray[i]);
            }
        }
        return nodiAdiacenti;
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(L label) {
        if (label == null){
            throw new NullPointerException("Label nulla");
        }
        return this.getAdjacentNodesOf(new GraphNode<L>(label));
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(int i) {
        if (i < 0 || i > this.matrix.size() - 1){
            throw new IndexOutOfBoundsException ("L'indice passato è errato");
        }
        for (GraphNode<L> key : this.nodesIndex.keySet()){
            if (this.nodesIndex.get(key) == i){
                return this.getAdjacentNodesOf(key);
            }
        }
        return null;
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(L label) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(int i) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(GraphNode<L> node) {
        if (node == null){
            throw new NullPointerException("Nodo nullo");
        }
        if (!this.getNodes().contains(node)){
            throw new IllegalArgumentException("Nodo non presente nel grafo");
        }
        Set<GraphEdge<L>> archiAdiacenti = new HashSet<GraphEdge<L>>();
        //scorro le colonne della riga di node
        for (int i = 0; i < this.nodesIndex.size(); i++){
            if (this.matrix.get(this.nodesIndex.get(node)).get(i) != null){
                archiAdiacenti.add(this.matrix.get(this.nodesIndex.get(node)).get(i));
            }
        }
        return archiAdiacenti;
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(L label) {
        if (label == null){
            throw new NullPointerException("Label nulla");
        }
        return this.getEdgesOf(new GraphNode<L>(label));
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(int i) {
        if (i < 0 || i > this.matrix.size() - 1){
            throw new IndexOutOfBoundsException ("L'indice passato è errato");
        }
        for (GraphNode<L> key : this.nodesIndex.keySet()){
            if (this.nodesIndex.get(key) == i){
                return this.getEdgesOf(key);
            }
        }
        return null;
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(L label) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(int i) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getEdges() {
        Set<GraphEdge<L>> archiMatrix = new HashSet<GraphEdge<L>>();
        for (int i = 0; i < this.matrix.size(); i++){
            //faccio partire j = i in modo da prendere solo metà matrice
            //in quanto è simmetrica alla metà che ignoro
            for (int j = i; j < this.matrix.get(i).size(); j++){
                if (this.matrix.get(i).get(j) != null){
                    archiMatrix.add(this.matrix.get(i).get(j));
                }
            }
        }
        return archiMatrix;
    }
}

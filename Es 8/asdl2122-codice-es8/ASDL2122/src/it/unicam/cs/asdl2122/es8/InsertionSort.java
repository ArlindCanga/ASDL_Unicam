package it.unicam.cs.asdl2122.es8;

import java.util.List;

// TODO completare import

/**
 * Implementazione dell'algoritmo di Insertion Sort integrata nel framework di
 * valutazione numerica. L'implementazione è in loco.
 * 
 * @author Template: Luca Tesei, Implementazione: Collettiva
 *
 * @param <E>
 *                Una classe su cui sia definito un ordinamento naturale.
 */
public class InsertionSort<E extends Comparable<E>>
        implements SortingAlgorithm<E> {
    //AC- Variabili inserite da me
    private int size;
    private int cmpCount;

    public SortingAlgorithmResult<E> sort(List<E> l) {
        if(l == null){
            throw new NullPointerException("Lista nulla");
        }
        if(l.size() == 0){
            return new SortingAlgorithmResult<E>(l, 0);
        }
        this.size = l.size();
        int j = 0;
        this.cmpCount = 0;

        for(int i = 1; i < size; i++){
            E value = l.get(i);
            j = i - 1;
            while(j >= 0 && l.get(j).compareTo(value) > 0){
                l.set(j + 1, l.get(j));
                j--;
                this.cmpCount++;
            }
            this.cmpCount++;
            l.set(j + 1, value);
        }

        return new SortingAlgorithmResult<E>(l, this.cmpCount);
    }

    public String getName() {
        return "InsertionSort";
    }
}

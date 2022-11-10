/**
 * 
 */
package it.unicam.cs.asdl2122.es8;

import java.util.List;
import java.util.Random;

//TODO completare import

/**
 * Implementazione del QuickSort con scelta della posizione del pivot scelta
 * randomicamente tra le disponibili. L'implementazione è in loco.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 * @param <E>
 *                il tipo degli elementi della sequenza da ordinare.
 *
 */
public class QuickSortRandom<E extends Comparable<E>>
        implements SortingAlgorithm<E> {

    private static final Random randomGenerator = new Random();
    //AC - Variabili inserite da me
    private int size;
    private int cmpCount;

    @Override
    public SortingAlgorithmResult<E> sort(List<E> l) {
        if (l == null) {
            throw new NullPointerException("Lista vuota");
        }
        if (l.size() == 0) {
            return new SortingAlgorithmResult<E>(l, 0);
        }

        this.size = l.size();

        this.quickSortRandom(l, 0, size - 1);
        return new SortingAlgorithmResult<E>(l, this.cmpCount);
    }

    private void quickSortRandom(List<E> l, int p, int r){
        this.cmpCount++;
        if(p < r){
            int q = this.partition(l, p, r);
            this.quickSortRandom(l, p, q - 1);
            this.quickSortRandom(l, q + 1, r);
        }
    }

    private int partition(List<E> l, int p, int r){
        this.random(l, p, r);
        E elem = l.get(r);
        int i = p - 1;
        for(int j = p; j < r; j++){
            this.cmpCount++;
            if(l.get(j).compareTo(elem) <= 0){
                i++;
                this.swap(l, i, j);
            }
        }
        this.swap(l, i+1, r);
        return i + 1;
    }

    private void swap(List<E> l, int i, int j){
        E tmp = l.get(i);
        l.set(i, l.get(j));
        l.set(j, tmp);
    }

    private void random(List<E> l, int low, int high){
        int p = this.randomGenerator.nextInt(high - low) + low;

        E tmp = l.get(p);
        l.set(p, l.get(high));
        l.set(high, tmp);
    }

    @Override
    public String getName() {
        return "QuickSortRandom";
    }

}

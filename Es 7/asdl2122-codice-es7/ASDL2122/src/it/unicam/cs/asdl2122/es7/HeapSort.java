/**
 * 
 */
package it.unicam.cs.asdl2122.es7;

import java.util.List;

// TODO completare import 

/**
 * Classe che implementa un algoritmo di ordinamento basato su heap.
 * 
 * @author Template: Luca Tesei, Implementation: collettiva
 *
 */
public class HeapSort<E extends Comparable<E>> implements SortingAlgorithm<E> {
    //Ac - variabile di comodo
    private int countCompare;

    @Override
    public SortingAlgorithmResult<E> sort(List<E> l) {
        this.countCompare = 0;
        int size = l.size();
        for(int i = (size / 2) - 1; i >= 0; i--){
            this.heapify(l, size, i);
            this.countCompare++;
        }

        for(int i = size - 1; i > 0; i--){
            E tmp = l.get(0);
            l.set(0, l.get(i));
            l.set(i, tmp);

            this.heapify(l, i, 0);
            this.countCompare++;
        }

        return new SortingAlgorithmResult<E>(l, countCompare);
    }

    @Override
    public String getName() {
        return "HeapSort";
    }

    //Ac - Metodo di comodo
    private void heapify(List<E> list, int n, int i){
        int largest = i;
        int l = 2*i + 1;
        int r = 2*i + 2;

        this.countCompare++;
        if(l < n && list.get(l).compareTo(list.get(largest)) > 0){
            largest = l;
        }

        this.countCompare++;
        if(r < n && list.get(r).compareTo(list.get(largest)) > 0){
            largest = r;
        }

        this.countCompare++;
        if(largest != i){
            E tmp = list.get(i);
            list.set(i, list.get(largest));
            list.set(largest, tmp);

            heapify(list, n, largest);
        }
    }
}

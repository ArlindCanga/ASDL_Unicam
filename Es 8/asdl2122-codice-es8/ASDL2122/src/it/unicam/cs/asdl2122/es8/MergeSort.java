/**
 * 
 */
package it.unicam.cs.asdl2122.es8;

import java.util.List;
// AC
import java.util.ArrayList;

/**
 * Implementazione dell'algoritmo di Merge Sort integrata nel framework di
 * valutazione numerica. Non è richiesta l'implementazione in loco.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 */
public class MergeSort<E extends Comparable<E>> implements SortingAlgorithm<E> {
    //AC- Variabili inserite da me
    private int size;
    private int cmpCount;

    public SortingAlgorithmResult<E> sort(List<E> l) {
        if(l == null){
            throw new NullPointerException("Impossibile ordinare lista nulla");
        }
        if(l.size() == 0){
            return new SortingAlgorithmResult<E>(l, 0);
        }

        this.size = l.size();
        int mid = size / 2;
        this.mergeSort(l, 0, this.size - 1);
        return new SortingAlgorithmResult<E>(l, this.cmpCount);
    }

    private void mergeSort(List<E> l, int left, int right){
        this.cmpCount++;
        if(left < right){
            int mid = (left + right) / 2;
            this.mergeSort(l, left, mid);
            this.mergeSort(l, mid + 1, right);
            this.merge(l, left, mid, right);
        }
    }

    private void merge(List<E> l, int left, int mid, int right){
        int leftSize = mid - left + 1;
        int rightSize = right - mid;
        List<E> leftArr = new ArrayList<E>();
        List<E> rightArr = new ArrayList<E>();

        for(int i = 0; i < leftSize; i++){
            leftArr.add(l.get(left + i));
        }
        for(int j = 0; j < rightSize; j++){
            rightArr.add(l.get(mid + 1 + j));
        }

        int i = 0;
        int j = 0;
        int k = left;

        while(i < leftSize && j < rightSize){
            if(leftArr.get(i).compareTo(rightArr.get(j)) <= 0){
                l.set(k, leftArr.get(i));
                i++;
            }else{
                l.set(k, rightArr.get(j));
                j++;
            }
            this.cmpCount = this.cmpCount + 2;
            k++;
        }
        this.cmpCount++;

        while(i < leftSize){
            l.set(k, leftArr.get(i));
            i++;
            k++;
        }
        while(j < rightSize){
            l.set(k, rightArr.get(j));
            j++;
            k++;
        }
    }

    public String getName() {
        return "MergeSort";
    }
}

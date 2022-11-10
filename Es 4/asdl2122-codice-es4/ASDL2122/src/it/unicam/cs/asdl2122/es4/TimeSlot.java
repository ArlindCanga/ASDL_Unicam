/**
 * 
 */
package it.unicam.cs.asdl2122.es4;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 * Un time slot è un intervallo di tempo continuo che può essere associato ad
 * una prenotazione. Gli oggetti della classe sono immutabili. Non sono ammessi
 * time slot che iniziano e finiscono nello stesso istante.
 * 
 * @author Luca Tesei
 *
 */
public class TimeSlot implements Comparable<TimeSlot> {

    /**
     * Rappresenta la soglia di tolleranza da considerare nella sovrapposizione
     * di due Time Slot. Se si sovrappongono per un numero di minuti minore o
     * uguale a questa soglia allora NON vengono considerati sovrapposti.
     */
    public static final int MINUTES_OF_TOLERANCE_FOR_OVERLAPPING = 5;

    private final GregorianCalendar start;

    private final GregorianCalendar stop;

    /**
     * Crea un time slot tra due istanti di inizio e fine
     * 
     * @param start
     *                  inizio del time slot
     * @param stop
     *                  fine del time slot
     * @throws NullPointerException
     *                                      se uno dei due istanti, start o
     *                                      stop, è null
     * @throws IllegalArgumentException
     *                                      se start è uguale o successivo a
     *                                      stop
     */
    public TimeSlot(GregorianCalendar start, GregorianCalendar stop) {
        if(start == null || stop == null) {
            throw new NullPointerException("Intervallo nullo");
        }
        if(start.after(stop) || start.equals(stop)) {
            throw new IllegalArgumentException("Intervallo non valido");
        }
        this.start = start;
        this.stop = stop;
    }

    /**
     * @return the start
     */
    public GregorianCalendar getStart() {
        return start;
    }

    /**
     * @return the stop
     */
    public GregorianCalendar getStop() {
        return stop;
    }

    /*
     * Un time slot è uguale a un altro se rappresenta esattamente lo stesso
     * intervallo di tempo, cioè se inizia nello stesso istante e termina nello
     * stesso istante.
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(this == obj) {
            return true;
        }
        if(!(obj instanceof TimeSlot)){
            return false;
        }
        TimeSlot objSlot = (TimeSlot) obj;
        if ((this.start.getTimeInMillis() == objSlot.start.getTimeInMillis())
                && (this.stop.getTimeInMillis() == objSlot.stop.getTimeInMillis())) {
            return true;
        }
        return false;
    }

    /*
     * Il codice hash associato a un timeslot viene calcolato a partire dei due
     * istanti di inizio e fine, in accordo con i campi usati per il metodo
     * equals.
     */
    @Override
    public int hashCode() {
        int prime = 47;
        int result = 1;
        result = result * prime + (int) (this.start.getTimeInMillis() ^ (this.start.getTimeInMillis() >>> 32));
        result = result * prime + (int) (this.stop.getTimeInMillis() ^ (this.stop.getTimeInMillis() >>> 32));
        return result;
    }

    /*
     * Un time slot precede un altro se inizia prima. Se due time slot iniziano
     * nello stesso momento quello che finisce prima precede l'altro. Se hanno
     * stesso inizio e stessa fine sono uguali, in compatibilità con equals.
     */
    @Override
    public int compareTo(TimeSlot o) {
        if(o == null) {
            throw new NullPointerException("Impossibile comparare un timeslot null");
        }
        if (this.start.getTimeInMillis() > o.start.getTimeInMillis()){
            return 1;
        }
        if (this.start.getTimeInMillis() < o.start.getTimeInMillis()){
            return -1;
        }
        //testo se lo start è lo stesso
        if (this.start.getTimeInMillis() == o.start.getTimeInMillis()){
            if (this.stop.getTimeInMillis() > o.stop.getTimeInMillis()){
                return 1;
            }
            if (this.stop.getTimeInMillis() < o.stop.getTimeInMillis()){
                return -1;
            }
        }
        return 0;
    }

    /**
     * Determina il numero di minuti di sovrapposizione tra questo timeslot e
     * quello passato.
     * 
     * @param o
     *              il time slot da confrontare con questo
     * @return il numero di minuti di sovrapposizione tra questo time slot e
     *         quello passato, oppure -1 se non c'è sovrapposizione. Se questo
     *         time slot finisce esattamente al millisecondo dove inizia il time
     *         slot <code>o</code> non c'è sovrapposizione, così come se questo
     *         time slot inizia esattamente al millisecondo in cui finisce il
     *         time slot <code>o</code>. In questi ultimi due casi il risultato
     *         deve essere -1 e non 0. Nel caso in cui la sovrapposizione non è
     *         di un numero esatto di minuti, cioè ci sono secondi e
     *         millisecondi che avanzano, il numero dei minuti di
     *         sovrapposizione da restituire deve essere arrotondato per difetto
     * @throws NullPointerException
     *                                      se il time slot passato è nullo
     * @throws IllegalArgumentException
     *                                      se i minuti di sovrapposizione
     *                                      superano Integer.MAX_VALUE
     */
    public int getMinutesOfOverlappingWith(TimeSlot o) {
        if (o == null){
            throw new NullPointerException("TimeSlot passato è nullo");
        }
        long minOverlapped = -1;
        //creo due Timeslot di appoggio per ordinarli
        TimeSlot first;
        TimeSlot second;
        if(this.compareTo(o) <= 0) {
            first = this;
            second = o;
        }else {
            first = o;
            second = this;
        }
        if((first.start.before(second.stop)) && (second.start.before(first.stop))){
            minOverlapped = (first.stop.getTimeInMillis() - second.start.getTimeInMillis());
            if (!(first.stop.before(second.stop))){
                minOverlapped = minOverlapped - (first.stop.getTimeInMillis() - second.stop.getTimeInMillis());
            }
            minOverlapped = minOverlapped / 60000;
        }
        if (minOverlapped > Integer.MAX_VALUE){
            throw new IllegalArgumentException("Minuti di sovrapposizione > " + Integer.MAX_VALUE);
        }
        return (int) minOverlapped;
    }

    /**
     * Determina se questo time slot si sovrappone a un altro time slot dato,
     * considerando la soglia di tolleranza.
     * 
     * @param o
     *              il time slot che viene passato per il controllo di
     *              sovrapposizione
     * @return true se questo time slot si sovrappone per più (strettamente) di
     *         MINUTES_OF_TOLERANCE_FOR_OVERLAPPING minuti a quello passato
     * @throws NullPointerException
     *                                  se il time slot passato è nullo
     */
    public boolean overlapsWith(TimeSlot o) {
        if (this.getMinutesOfOverlappingWith(o) > MINUTES_OF_TOLERANCE_FOR_OVERLAPPING)
            return true;
        return false;
    }

    /*
     * Ridefinisce il modo in cui viene reso un TimeSlot con una String.
     * 
     * Esempio 1, stringa da restituire: "[4/11/2019 11.0 - 4/11/2019 13.0]"
     * 
     * Esempio 2, stringa da restituire: "[10/11/2019 11.15 - 10/11/2019 23.45]"
     * 
     * I secondi e i millisecondi eventuali non vengono scritti.
     */
    @Override
    public String toString() {
        String interval;
        SimpleDateFormat dateFormat = new SimpleDateFormat("d/MM/yyyy HH.m");
        interval = "[" + dateFormat.format(this.start.getTime()) + " - " + dateFormat.format(this.stop.getTime()) + "]";
        return interval;
    }

}

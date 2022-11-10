package it.unicam.cs.asdl2122.es5;

import java.util.*;

// TODO importare eventuali classi o interfacce che servono

/**
 * Un oggetto della classe aula rappresenta una certa aula con le sue facilities
 * e le sue prenotazioni.
 * 
 * @author Template: Luca Tesei, Implementazione: Collettiva
 *
 */
public class Aula implements Comparable<Aula> {
    // Identificativo unico di un'aula
    private final String nome;

    // Location dell'aula
    private final String location;

    // Insieme delle facilities di quest'aula
    private final Set<Facility> facilities;

    // Insieme delle prenotazioni per quest'aula, segue l'ordinamento naturale
    // delle prenotazioni
    private final SortedSet<Prenotazione> prenotazioni;

    /**
     * Costruisce una certa aula con nome e location. Il set delle facilities è
     * vuoto. L'aula non ha inizialmente nessuna prenotazione.
     * 
     * @param nome
     *                     il nome dell'aula
     * @param location
     *                     la location dell'aula
     * 
     * @throws NullPointerException
     *                                  se una qualsiasi delle informazioni
     *                                  richieste è nulla
     */
    public Aula(String nome, String location) {
        if(nome == null || location == null) {
            throw new NullPointerException("Argomenti nulli");
        }
        this.nome = nome;
        this.location = location;
        this.facilities = new HashSet<Facility>();
        this.prenotazioni = new TreeSet<Prenotazione>();
    }

    /**
     * Costruisce una certa aula con nome, location e insieme delle facilities.
     * L'aula non ha inizialmente nessuna prenotazione.
     * 
     * @param nome
     *                       il nome dell'aula
     * @param location
     *                       la location dell'aula
     * @param facilities
     *                       l'insieme delle facilities dell'aula
     * @throws NullPointerException
     *                                  se una qualsiasi delle informazioni
     *                                  richieste è nulla
     */
    public Aula(String nome, String location, Set<Facility> facilities) {
        if(nome == null || location == null || facilities == null){
            throw new NullPointerException("Argomenti nulli");
        }
        this.nome = nome;
        this.location = location;
        this.facilities = facilities;
        this.prenotazioni = new TreeSet<Prenotazione>();
    }

    /*
     * Ridefinire in accordo con equals
     */
    @Override
    public int hashCode() {
        int prime = 31;
        int result = prime + this.nome.hashCode();
        return result;
    }

    /* Due aule sono uguali se e solo se hanno lo stesso nome */
    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(obj == null){
            return false;
        }
        if(!(obj instanceof Aula)){
            return false;
        }
        Aula other = (Aula) obj;
        if(!this.nome.equals(other.nome)){
            return false;
        }
        return true;
    }

    /* L'ordinamento naturale si basa sul nome dell'aula */
    @Override
    public int compareTo(Aula o) {
        if(o == null){
            throw new NullPointerException("Aula nulla");
        }

        return this.nome.compareTo(o.nome);
    }

    /**
     * @return the facilities
     */
    public Set<Facility> getFacilities() {
        return facilities;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @return the prenotazioni
     */
    public SortedSet<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    /**
     * Aggiunge una faciltity a questa aula.
     * 
     * @param f
     *              la facility da aggiungere
     * @return true se la facility non era già presente e quindi è stata
     *         aggiunta, false altrimenti
     * @throws NullPointerException
     *                                  se la facility passata è nulla
     */
    public boolean addFacility(Facility f) {
        if(f == null){
            throw new NullPointerException("Facility nulla");
        }

        if(this.facilities.contains(f)){
            return false;
        }
        this.facilities.add(f);
        return true;
    }

    /**
     * Determina se l'aula è libera in un certo time slot.
     * 
     * @param ts
     *               il time slot da controllare
     * 
     * @return true se l'aula risulta libera per tutto il periodo del time slot
     *         specificato
     * @throws NullPointerException
     *                                  se il time slot passato è nullo
     */
    public boolean isFree(TimeSlot ts) {
        if(ts == null) {
            throw new NullPointerException("Timeslot nullo");
        }
        Iterator<Prenotazione> iterator = this.prenotazioni.iterator();
        Prenotazione pren;
        while (iterator.hasNext()){
            pren = iterator.next();
            if (pren.getTimeSlot().getMinutesOfOverlappingWith(ts) > 0){
                return false;
            }
            if (pren.getTimeSlot().getStart().getTimeInMillis() > ts.getStart().getTimeInMillis()){
                break;
            }
        }
        return true;
    }

    /**
     * Determina se questa aula soddisfa tutte le facilities richieste
     * rappresentate da un certo insieme dato.
     * 
     * @param requestedFacilities
     *                                l'insieme di facilities richieste da
     *                                soddisfare
     * @return true se e solo se tutte le facilities di
     *         {@code requestedFacilities} sono soddisfatte da questa aula.
     * @throws NullPointerException
     *                                  se il set di facility richieste è nullo
     */
    public boolean satisfiesFacilities(Set<Facility> requestedFacilities) {
        if(requestedFacilities == null){
            throw new NullPointerException("Insiemi di facility nullo");
        }
        for(Facility requestedFacility : requestedFacilities){
            boolean satisfies = false;
            for(Facility facility: this.facilities){
                if(facility.satisfies(requestedFacility)){
                    satisfies = true;
                }
            }
            if(!satisfies){
                return false;
            }
        }
        return true;
    }

    /**
     * Prenota l'aula controllando eventuali sovrapposizioni.
     * 
     * @param ts
     * @param docente
     * @param motivo
     * @throws IllegalArgumentException
     *                                      se la prenotazione comporta una
     *                                      sovrapposizione con un'altra
     *                                      prenotazione nella stessa aula.
     * @throws NullPointerException
     *                                      se una qualsiasi delle informazioni
     *                                      richieste è nulla.
     */
    public void addPrenotazione(TimeSlot ts, String docente, String motivo) {
        if(ts == null || docente == null || motivo == null){
            throw new NullPointerException("Argomenti nulli");
        }
        if(!this.isFree(ts)){
            throw new IllegalArgumentException("Aula non libera");
        }
        this.prenotazioni.add(new Prenotazione(this, ts, docente, motivo));
    }

    /**
     * Cancella una prenotazione di questa aula.
     * 
     * @param p
     *              la prenotazione da cancellare
     * @return true se la prenotazione è stata cancellata, false se non era
     *         presente.
     * @throws NullPointerException
     *                                  se la prenotazione passata è null
     */
    public boolean removePrenotazione(Prenotazione p) {
        if(p == null){
            throw new NullPointerException("Prenotazione nulla");
        }
        if(!this.prenotazioni.contains(p)){
            return false;
        }
        this.prenotazioni.remove(p);
        return true;
    }

    /**
     * Rimuove tutte le prenotazioni di questa aula che iniziano prima (o
     * esattamente in) di un punto nel tempo specificato.
     * 
     * @param timePoint
     *                      un certo punto nel tempo
     * @return true se almeno una prenotazione è stata cancellata, false
     *         altrimenti.
     * @throws NullPointerException
     *                                  se il punto nel tempo passato è nullo.
     */
    public boolean removePrenotazioniBefore(GregorianCalendar timePoint) {
        if(timePoint == null){
            throw new NullPointerException("Punto del tempo nullo");
        }
        boolean retVal = false;
        Iterator<Prenotazione> iterator = this.prenotazioni.iterator();
        Prenotazione pren;
        while (iterator.hasNext()){
            pren = iterator.next();
            if (pren.getTimeSlot().getStart().getTimeInMillis() > timePoint.getTimeInMillis()){
                break;
            }
            if (pren.getTimeSlot().getStart().getTimeInMillis() <= timePoint.getTimeInMillis()){
                iterator.remove();
                retVal = true;
            }
        }
        return retVal;
    }
}

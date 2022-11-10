package it.unicam.cs.asdl2122.es2;

import java.util.regex.Pattern;

/**
 * Un oggetto cassaforte con combinazione ha una manopola che può essere
 * impostata su certe posizioni contrassegnate da lettere maiuscole. La
 * serratura si apre solo se le ultime tre lettere impostate sono uguali alla
 * combinazione segreta.
 * 
 * @author Luca Tesei
 */
public class CombinationLock {

    private String combinations;
    private String pwd = "";
    private boolean changed = false;
    private boolean isOpen = false;

    /**
     * Costruisce una cassaforte <b>aperta</b> con una data combinazione
     * 
     * @param aCombination
     *                         la combinazione che deve essere una stringa di 3
     *                         lettere maiuscole dell'alfabeto inglese
     * @throw IllegalArgumentException se la combinazione fornita non è una
     *        stringa di 3 lettere maiuscole dell'alfabeto inglese
     * @throw NullPointerException se la combinazione fornita è nulla
     */
    public CombinationLock(String aCombination) {
        if(!Pattern.matches("[A-Z][A-Z][A-Z]", aCombination)) {
            throw new IllegalArgumentException("Caratteri non validi, inserire lettere maiuscole dalla A alla Z");
        }
        if(aCombination == null) {
            throw new NullPointerException("Inserisci una combinazione");
        }
        this.combinations = aCombination;
        this.changed = false;
        this.isOpen = true;
    }

    /**
     * Imposta la manopola su una certaposizione.
     * 
     * @param aPosition
     *                      un carattere lettera maiuscola su cui viene
     *                      impostata la manopola
     * @throws IllegalArgumentException
     *                                      se il carattere fornito non è una
     *                                      lettera maiuscola dell'alfabeto
     *                                      inglese
     */
    public void setPosition(char aPosition) {
        if(!Character.isLetter(aPosition) || aPosition != Character.toUpperCase(aPosition)) {
            throw new IllegalArgumentException("Carattere non valido");
        }
        this.pwd += aPosition;
        //tengo conto solo degli ultimi 3 caratteri inseriti
        if(this.pwd.length() > 3) {
            this.pwd = this.pwd.substring(this.pwd.length() - 3, this.pwd.length());
        }
        this.changed = true;
    }

    /**
     * Tenta di aprire la serratura considerando come combinazione fornita le
     * ultime tre posizioni impostate. Se l'apertura non va a buon fine le
     * lettere impostate precedentemente non devono essere considerate per i
     * prossimi tentativi di apertura.
     */
    public void open() {
        if (!this.changed){
            this.isOpen = false;
        }
        else {
            this.isOpen = this.pwd.equals(this.combinations);
        }
    }

    /**
     * Determina se la cassaforte è aperta.
     * 
     * @return true se la cassaforte è attualmente aperta, false altrimenti
     */
    public boolean isOpen() {
        return this.isOpen;
    }

    /**
     * Chiude la cassaforte senza modificare la combinazione attuale. Fa in modo
     * che se si prova a riaprire subito senza impostare nessuna nuova posizione
     * della manopola la cassaforte non si apre. Si noti che se la cassaforte
     * era stata aperta con la combinazione giusta le ultime posizioni impostate
     * sono proprio la combinazione attuale.
     */
    public void lock() {
        this.changed = false;
        this.isOpen = false;
    }

    /**
     * Chiude la cassaforte e modifica la combinazione. Funziona solo se la
     * cassaforte è attualmente aperta. Se la cassaforte è attualmente chiusa
     * rimane chiusa e la combinazione non viene cambiata, ma in questo caso le
     * le lettere impostate precedentemente non devono essere considerate per i
     * prossimi tentativi di apertura.
     * 
     * @param aCombination
     *                         la nuova combinazione che deve essere una stringa
     *                         di 3 lettere maiuscole dell'alfabeto inglese
     * @throw IllegalArgumentException se la combinazione fornita non è una
     *        stringa di 3 lettere maiuscole dell'alfabeto inglese
     * @throw NullPointerException se la combinazione fornita è nulla
     */
    public void lockAndChangeCombination(String aCombination) {
        if (this.isOpen){
            if (aCombination == null)
                throw new IllegalArgumentException("Combinazione nulla");
            if(!Pattern.matches("[A-Z][A-Z][A-Z]", aCombination))
                throw new IllegalArgumentException("Combinazione non valida");
            this.combinations = aCombination;
            this.isOpen = false;
        }
    }
}

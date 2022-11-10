/**
 * 
 */
package it.unicam.cs.asdl2122.es4;

/**
 * Una Presence Facility è una facility che può essere presente oppure no. Ad
 * esempio la presenza di un proiettore HDMI oppure la presenza dell'aria
 * condizionata.
 * 
 * @author Template: Luca Tesei, Implementation: Collective
 *
 */
public class PresenceFacility extends Facility {

    /**
     * Costruisce una presence facility.
     * 
     * @param codice
     * @param descrizione
     * @throws NullPointerException
     *                                  se una qualsiasi delle informazioni
     *                                  richieste è nulla.
     */
    public PresenceFacility(String codice, String descrizione) {
        super(codice, descrizione);
        //i controlli li fa direttamente sulla classe super
    }

    /*
     * Una Presence Facility soddisfa una facility solo se la facility passata è
     * una Presence Facility ed ha lo stesso codice.
     * 
     */
    @Override
    public boolean satisfies(Facility o) {
        if(o == null) {
            throw new NullPointerException("Parametro nullo");
        }
        if(!(o instanceof PresenceFacility)) {
            return false;
        }
        PresenceFacility other = (PresenceFacility) o;
        if(!this.getCodice().equals(other.getCodice())) {
            return false;
        }
        return true;
    }

}

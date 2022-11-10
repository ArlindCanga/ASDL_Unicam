package it.unicam.cs.asdl2122.es2;

/**
 * Uno scassinatore è un oggetto che prende una certa cassaforte e trova la
 * combinazione utilizzando la "forza bruta".
 * 
 * @author Luca Tesei
 *
 */
public class Burglar {
    private CombinationLock bruteTest;
    private int totAttempts = 0;
    private String brutePWD = "";

    /**
     * Costruisce uno scassinatore per una certa cassaforte.
     * 
     * @param aCombinationLock
     * @throw NullPointerException se la cassaforte passata è nulla
     */
    public Burglar(CombinationLock aCombinationLock) {
        if(aCombinationLock == null) {
            throw new NullPointerException("Nessuna cassaforte da scassinare");
        }
        this.bruteTest = aCombinationLock;
    }

    /**
     * Forza la cassaforte e restituisce la combinazione.
     * 
     * @return la combinazione della cassaforte forzata.
     */
    public String findCombination() {
        this.totAttempts = 0;
        String combination = "";
        //prendo i caratteri dall'ASCII (A - Z sono 65 - 90)
        for (int i = 65; i < 91; i++) {
            for(int ii = 65; ii < 91; ii++) {
                for(int iii = 65; iii < 91; iii++) {
                    this.totAttempts++;
                    this.bruteTest.setPosition((char) i);
                    this.bruteTest.setPosition((char) ii);
                    this.bruteTest.setPosition((char) iii);
                    this.bruteTest.open(); //testo se la combinazione è giusta
                    if(this.bruteTest.isOpen()) {
                        //memorizzo la combinazione
                        combination += (char) i;
                        combination += (char) ii;
                        combination += (char) iii;
                        return combination;
                    }
                }
            }
        }
        return combination;
    }

    /**
     * Restituisce il numero di tentativi che ci sono voluti per trovare la
     * combinazione. Se la cassaforte non è stata ancora forzata restituisce -1.
     * 
     * @return il numero di tentativi che ci sono voluti per trovare la
     *         combinazione, oppure -1 se la cassaforte non è stata ancora
     *         forzata.
     */
    public long getAttempts() {
        return this.totAttempts;
    }
}

/**
 * 
 */
package it.unicam.cs.asdl2122.es1;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * @author Template: Luca Tesei, Implementation: Collettiva da Esercitazione a
 *         Casa
 *
 */
class EquazioneSecondoGradoModificabileConRisolutoreTest {
    /*
     * Costante piccola per il confronto di due numeri double
     */
    static final double EPSILON = 1.0E-15;

    /**
     * Test method for
     * {@link it.unicam.cs.asdl2021.slides.javaeoop.EquazioneSecondoGradoModificabileConRisolutore#EquazioneSecondoGradoModificabileConRisolutore(double, double, double)}.
     */
    @Test
    final void testEquazioneSecondoGradoModificabileConRisolutore() {
        // controllo che il valore 0 su a lanci l'eccezione
        assertThrows(IllegalArgumentException.class,
                () -> new EquazioneSecondoGradoModificabileConRisolutore(0, 1,
                        1));
        // devo controllare che comunque nel caso normale il costruttore
        // funziona
        new EquazioneSecondoGradoModificabileConRisolutore(1, 1, 1);
    }

    /**
     * Test method for
     * {@link it.unicam.cs.asdl2021.slides.javaeoop.EquazioneSecondoGradoModificabileConRisolutore#getA()}.
     */
    @Test
    final void testGetA() {
        double x = 10;
        EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(
                x, 1, 1);
        // controllo che il valore restituito sia quello che ho messo
        // all'interno
        // dell'oggetto
        assertTrue(x == e1.getA());
        // in generale si dovrebbe usare assertTrue(Math.abs(x -
        // e1.getA())<EPSILON) ma in
        // questo caso il valore che testiamo non ha subito manipolazioni quindi
        // la sua rappresentazione sarà la stessa di quella inserita nel
        // costruttore senza errori di approssimazione

    }

    /**
     * Test method for
     * {@link it.unicam.cs.asdl2021.slides.javaeoop.EquazioneSecondoGradoModificabileConRisolutore#setA(double)}.
     */
    @Test
    final void testSetA() {
        EquazioneSecondoGradoModificabileConRisolutore equaz = new EquazioneSecondoGradoModificabileConRisolutore(1, 1, 1);
        assertThrows(IllegalArgumentException.class, () -> equaz.setA(0));
        equaz.solve();
        equaz.setA(2);
        assertTrue(Math.abs(equaz.getA() - 2) < EPSILON);
        assertFalse(equaz.isSolved());
    }

    /**
     * Test method for
     * {@link it.unicam.cs.asdl2021.slides.javaeoop.EquazioneSecondoGradoModificabileConRisolutore#getB()}.
     */
    @Test
    final void testGetB() {
        EquazioneSecondoGradoModificabileConRisolutore equaz = new EquazioneSecondoGradoModificabileConRisolutore(1, 1, 1);
        equaz.setB(2);
        assertTrue(Math.abs(equaz.getB() - 2) < EPSILON);
    }

    /**
     * Test method for
     * {@link it.unicam.cs.asdl2021.slides.javaeoop.EquazioneSecondoGradoModificabileConRisolutore#setB(double)}.
     */
    @Test
    final void testSetB() {
        EquazioneSecondoGradoModificabileConRisolutore equaz = new EquazioneSecondoGradoModificabileConRisolutore(1, 1, 1);
        equaz.solve();
        equaz.setB(2);
        assertTrue(Math.abs(equaz.getB() - 2) < EPSILON);
        //test attributo solved settato a false dopo set
        assertFalse(equaz.isSolved());
    }

    /**
     * Test method for
     * {@link it.unicam.cs.asdl2021.slides.javaeoop.EquazioneSecondoGradoModificabileConRisolutore#getC()}.
     */
    @Test
    final void testGetC() {
        EquazioneSecondoGradoModificabileConRisolutore equaz = new EquazioneSecondoGradoModificabileConRisolutore(1, 1, 1);
        equaz.setC(2);
        assertTrue(Math.abs(equaz.getC() - 2) < EPSILON);
    }

    /**
     * Test method for
     * {@link it.unicam.cs.asdl2021.slides.javaeoop.EquazioneSecondoGradoModificabileConRisolutore#setC(double)}.
     */
    @Test
    final void testSetC() {
        EquazioneSecondoGradoModificabileConRisolutore equaz = new EquazioneSecondoGradoModificabileConRisolutore(1, 1, 1);
        equaz.solve();
        equaz.setC(2);
        assertTrue(Math.abs(equaz.getC() - 2) < EPSILON);
        assertFalse(equaz.isSolved());
    }

    /**
     * Test method for
     * {@link it.unicam.cs.asdl2021.slides.javaeoop.EquazioneSecondoGradoModificabileConRisolutore#isSolved()}.
     */
    @Test
    final void testIsSolved() {
        EquazioneSecondoGradoModificabileConRisolutore equaz = new EquazioneSecondoGradoModificabileConRisolutore(1, 1, 1);
        //prima della risoluzione
        assertFalse(equaz.isSolved());
        equaz.solve();
        //dopo la risoluzione
        assertTrue(equaz.isSolved());
    }

    /**
     * Test method for
     * {@link it.unicam.cs.asdl2021.slides.javaeoop.EquazioneSecondoGradoModificabileConRisolutore#solve()}.
     */
    @Test
    final void testSolve() {
        EquazioneSecondoGradoModificabileConRisolutore e3 = new EquazioneSecondoGradoModificabileConRisolutore(
                1, 1, 3);
        // controllo semplicemente che la chiamata a solve() non generi errori
        e3.solve();
        // i test con i valori delle soluzioni vanno fatti nel test del metodo
        // getSolution()
    }

    /**
     * Test method for
     * {@link it.unicam.cs.asdl2021.slides.javaeoop.EquazioneSecondoGradoModificabileConRisolutore#getSolution()}.
     */
    @Test
    final void testGetSolution() {
        EquazioneSecondoGradoModificabileConRisolutore equaz = new EquazioneSecondoGradoModificabileConRisolutore(1, 7, -8); //s1 = 1; s2 = -8
        //controllo l'exception
        assertThrows(IllegalStateException.class, () -> equaz.getSolution());
        //risolvo per poi controllare i valori
        equaz.solve();
        SoluzioneEquazioneSecondoGrado val = equaz.getSolution();
        assertTrue(val.getS1() == 1);
        assertTrue(val.getS2() == -8);
    }

}

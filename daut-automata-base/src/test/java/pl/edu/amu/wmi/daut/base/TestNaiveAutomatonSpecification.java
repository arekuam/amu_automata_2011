package pl.edu.amu.wmi.daut.base;

import junit.framework.TestCase;
import java.util.List;

/**
 * Przykładowe testy przykładowej klasy NaiveAutomatonSpecification.
 */
public class TestNaiveAutomatonSpecification extends TestCase {

    /**
     * Test prostego automatu o trzech stanach.
     */
    public final void testSimpleAutomaton() {
        NaiveAutomatonSpecification spec = new NaiveAutomatonSpecification();

        // budowanie

        State s0 = spec.addState();
        State s1 = spec.addState();
        spec.addTransition(s0, s1, new CharTransitionLabel('a'));
        State s2 = spec.addState();
        spec.addTransition(s0, s2, new CharTransitionLabel('b'));
        spec.addTransition(s1, s2, new CharTransitionLabel('c'));

        spec.markAsInitial(s0);
        spec.markAsFinal(s2);

        // testowanie

        State r0 = spec.getInitialState();

        List<OutgoingTransition> r0Outs = spec.allOutgoingTransitions(r0);

        // w ten sposób w JUnicie wyrażamy oczekiwanie, że liczba
        // przejść wychodzących z początkowego stanu powinna być równa 2
        assertEquals(r0Outs.size(), 2);
        assertFalse(spec.isFinal(r0));

        State r1;
        State r2;

        if (((CharTransitionLabel)r0Outs.get(0).getTransitionLabel()).getChar() == 'a') {
            r1 = r0Outs.get(0).getTargetState();
            r2 = r0Outs.get(1).getTargetState();
            assertEquals(((CharTransitionLabel)r0Outs.get(1).getTransitionLabel()).getChar(), 'b');
            assertTrue(((CharTransitionLabel)r0Outs.get(1).getTransitionLabel()).canAcceptCharacter('b'));
            assertFalse(((CharTransitionLabel)r0Outs.get(1).getTransitionLabel()).canAcceptCharacter('c'));
            assertFalse(((CharTransitionLabel)r0Outs.get(1).getTransitionLabel()).canBeEpsilon());
        }
        else {
            // kolejność może być odwrócona
            r1 = r0Outs.get(1).getTargetState();
            r2 = r0Outs.get(0).getTargetState();
            assertEquals(((CharTransitionLabel)r0Outs.get(0).getTransitionLabel()).getChar(), 'b');
        }

        assertFalse(spec.isFinal(r1));
        assertTrue(spec.isFinal(r2));
        assertSame(r0, spec.getInitialState());
        assertNotSame(r0, r1);
        assertNotSame(r0, r2);
        assertNotSame(r1, r2);

        List<State> states = spec.allStates();

        assertEquals(states.size(), 3);
    }
    
    /**
     * Prosty test wyznaczania przecięcia.
     */
    public final void testIntersections() {
        CharTransitionLabel tA1 = new CharTransitionLabel('a');
        CharTransitionLabel tA2 = new CharTransitionLabel('a');
        CharTransitionLabel tB = new CharTransitionLabel('b');
        EmptyTransitionLabel emptyTransition = new EmptyTransitionLabel();

        TransitionLabel intersectedA = tA1.intersect(tA2);
        assertFalse(intersectedA.isEmpty());
        assertTrue(intersectedA.canAcceptCharacter('a'));
        assertFalse(intersectedA.canAcceptCharacter('b'));

        assertTrue(tA1.intersect(tB).isEmpty());
        assertTrue(tB.intersect(tA1).isEmpty());
        assertTrue(emptyTransition.intersect(tA1).isEmpty());
        assertTrue(tA1.intersect(emptyTransition).isEmpty());
        assertTrue(emptyTransition.intersect(emptyTransition).isEmpty());
    }


    public final void testAddLoop() {
        NaiveAutomatonSpecification spec = new NaiveAutomatonSpecification();

        //budowanie

        State s0 = spec.addState();
        spec.addLoop(s0, new CharTransitionLabel('a'));
        spec.markAsInitial(s0);
        spec.markAsFinal(s0);

        //testowanie

        State r0 = spec.getInitialState();

        List<OutgoingTransition> r0Outs = spec.allOutgoingTransitions(r0);

        assertEquals(r0Outs.size(), 1);
        assertTrue(spec.isFinal(r0));

        State r1;

        if (((CharTransitionLabel)r0Outs.get(0).getTransitionLabel()).getChar() == 'a') {
            r1 = r0Outs.get(0).getTargetState();
            assertEquals(((CharTransitionLabel)r0Outs.get(0).getTransitionLabel()).getChar(), 'a');
            assertTrue(((CharTransitionLabel)r0Outs.get(0).getTransitionLabel()).canAcceptCharacter('a'));
            assertFalse(((CharTransitionLabel)r0Outs.get(0).getTransitionLabel()).canBeEpsilon());
        }

        assertTrue(spec.isFinal(r0));
        assertSame(r0, spec.getInitialState());

        List<State> states = spec.allStates();

        assertEquals(states.size(), 1);

    }





}


    /**
     * Testy metody sprawdzającej, czy akceptowany język jest nieskończony.
     */
    public final void testInfinite1() {
        NaiveAutomatonSpecification automat = new NaiveAutomatonSpecification();
        State s0 = automat.addState();
        State s1 = automat.addState();
        State s2 = automat.addState();
        State s3 = automat.addState();
        State s4 = automat.addState();
        automat.addTransition(s0, s1, new CharTransitionLabel('a'));   
        automat.addLoop(s1, new CharTransitionLabel('b'));
        automat.addLoop(s2, new CharTransitionLabel('b'));
        automat.addTransition(s1, s2, new CharTransitionLabel('a'));
        automat.addTransition(s2, s1, new CharTransitionLabel('a'));
        automat.addTransition(s2, s0, new CharTransitionLabel('c'));
        automat.addTransition(s2, s3, new CharTransitionLabel('c'));
        automat.addTransition(s3, s4, new CharTransitionLabel('a'));
        automat.addLoop(s4, new CharTransitionLabel('b'));
        automat.markAsFinal(s2);
        automat.markAsFinal(s1);
        automat.markAsInitial(s0);
        automat.markAsFinal(s4);
        assertTrue(automat.isInfinite());
    }

    public final void testInfinite2() {
        NaiveAutomatonSpecification automat = new NaiveAutomatonSpecification();
        State s0 = automat.addState();
        State s1 = automat.addState();
        State s2 = automat.addState();
        State s3 = automat.addState();
        automat.addTransition(s0, s1, new CharTransitionLabel('a'));
        automat.addTransition(s1, s2, new CharTransitionLabel('a'));
        automat.addTransition(s2, s3, new CharTransitionLabel('a'));
        automat.addTransition(s3, s1, new CharTransitionLabel('a'));
        automat.markAsInitial(s0);
        automat.markAsFinal(s2);
        automat.markAsFinal(s1);
        assertTrue(automat.isInfinite());
    }

    public final void testInfinite3() {
        NaiveAutomatonSpecification automat = new NaiveAutomatonSpecification();
        State s0 = automat.addState();
        State s1 = automat.addState();
        State s2 = automat.addState();
        State s3 = automat.addState();
        State s4 = automat.addState();
        automat.addTransition(s0, s1, new CharTransitionLabel('a'));
        automat.addTransition(s1, s2, new CharTransitionLabel('a'));
        automat.addTransition(s2, s3, new CharTransitionLabel('a'));
        automat.addTransition(s3, s1, new CharTransitionLabel('a'));
        automat.addTransition(s3, s4, new CharTransitionLabel('b'));
        automat.markAsInitial(s0);
        automat.markAsFinal(s2);
        automat.markAsFinal(s1);
        automat.markAsFinal(s4);
        assertTrue(automat.isInfinite());
    }

    public final void testInfinite4() {
        NaiveAutomatonSpecification automat = new NaiveAutomatonSpecification();
        State s0 = automat.addState();
        State s1 = automat.addState();
        State s2 = automat.addState();
        State s3 = automat.addState();
        State s4 = automat.addState();
        State s5 = automat.addState();
        automat.addTransition(s0, s1, new CharTransitionLabel('a'));
        automat.addTransition(s1, s2, new CharTransitionLabel('a'));
        automat.addTransition(s2, s3, new CharTransitionLabel('a'));
        automat.addTransition(s3, s2, new CharTransitionLabel('b'));
        automat.addTransition(s3, s1, new CharTransitionLabel('a'));
        automat.addTransition(s3, s4, new CharTransitionLabel('b'));
        automat.addTransition(s4, s5, new CharTransitionLabel('a'));
        automat.addTransition(s5, s4, new CharTransitionLabel('a'));
        automat.markAsInitial(s0);
        automat.markAsFinal(s1);
        assertTrue(automat.isInfinite());
    }

    public final void testInfinite5() {
        NaiveAutomatonSpecification automat = new NaiveAutomatonSpecification();
        State s0 = automat.addState();
        State s1 = automat.addState();
        State s2 = automat.addState();
        State s3 = automat.addState();
        State s4 = automat.addState();
        automat.addTransition(s0, s1, new CharTransitionLabel('a'));
        automat.addTransition(s1, s2, new CharTransitionLabel('a'));
        automat.addTransition(s2, s3, new CharTransitionLabel('a'));
        automat.addTransition(s3, s1, new CharTransitionLabel('a'));
        automat.addTransition(s3, s4, new CharTransitionLabel('b'));
        automat.markAsInitial(s0);
        automat.markAsFinal(s4);
        assertFalse(automat.isInfinite());
    }

    public final void testInfinite6() {
        NaiveAutomatonSpecification automat = new NaiveAutomatonSpecification();
        State s0 = automat.addState();
        State s1 = automat.addState();
        State s2 = automat.addState();
        State s3 = automat.addState();
        State s4 = automat.addState();
        automat.addTransition(s0, s1, new CharTransitionLabel('a'));
        automat.addTransition(s1, s2, new CharTransitionLabel('a'));
        automat.addTransition(s2, s3, new CharTransitionLabel('a'));
        automat.addTransition(s3, s4, new CharTransitionLabel('a'));
        automat.markAsInitial(s0);
        automat.markAsFinal(s3);
        assertFalse(automat.isInfinite());
    }

    public final void testInfinite7() {
        NaiveAutomatonSpecification automat = new NaiveAutomatonSpecification();
        State s0 = automat.addState();
        State s1 = automat.addState();
        State s2 = automat.addState();
        State s3 = automat.addState();
        automat.addTransition(s1, s2, new CharTransitionLabel('a'));
        automat.addTransition(s2, s3, new CharTransitionLabel('a'));
        automat.addTransition(s3, s1, new CharTransitionLabel('a'));
        automat.markAsInitial(s0);
        automat.markAsFinal(s2);
        automat.markAsFinal(s1);
        assertFalse(automat.isInfinite());
    }

    public final void testInfinite8() {
        NaiveAutomatonSpecification automat = new NaiveAutomatonSpecification();
        State s0 = automat.addState();
        automat.addLoop(s0, new CharTransitionLabel('a'));
        automat.markAsInitial(s0);
        automat.markAsFinal(s0);
        assertTrue(automat.isInfinite());
    }

    public final void testInfinite9() {
        NaiveAutomatonSpecification automat = new NaiveAutomatonSpecification();
        State s0 = automat.addState();
        State s1 = automat.addState();
        automat.addTransition(s0, s1, new CharTransitionLabel('a'));
        automat.addTransition(s1, s0, new CharTransitionLabel('a'));
        automat.markAsInitial(s0);
        automat.markAsFinal(s0);
        assertTrue(automat.isInfinite());
    }
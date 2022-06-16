package de.themonstrouscavalca.alteredstates.helpers;

import de.themonstrouscavalca.alteredstates.transitions.Transition;
import de.themonstrouscavalca.alteredstates.interfaces.INameEvents;
import de.themonstrouscavalca.alteredstates.interfaces.INameStates;

import java.util.HashMap;

public class TransitionToCheckAndActionMap<S extends INameStates, E extends INameEvents, C, X>
        extends HashMap<Transition<S, E, C, X>, EventCheckAndAction<E, C, X>>{
}

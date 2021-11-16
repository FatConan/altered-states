package de.themonstrouscavalca.alteredstates.helpers;

import de.themonstrouscavalca.alteredstates.Transition;
import de.themonstrouscavalca.alteredstates.interfaces.INameEvents;
import de.themonstrouscavalca.alteredstates.interfaces.INameStates;

import java.util.HashMap;

public class TransitionToCheckAndActionMap<S extends INameStates, E extends INameEvents, C, X>
        extends HashMap<Transition<S, E>, EventCheckAndAction<E, C, X>>{
}

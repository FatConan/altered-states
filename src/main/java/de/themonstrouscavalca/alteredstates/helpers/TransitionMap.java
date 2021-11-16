package de.themonstrouscavalca.alteredstates.helpers;

import de.themonstrouscavalca.alteredstates.Transition;
import de.themonstrouscavalca.alteredstates.interfaces.INameEvents;
import de.themonstrouscavalca.alteredstates.interfaces.INameStates;

import java.util.HashMap;

public class TransitionMap<S extends INameStates, E extends INameEvents, C, X>
        extends HashMap<Transition<S, E>, EventCheckAndAction<E, C, X>>{
}

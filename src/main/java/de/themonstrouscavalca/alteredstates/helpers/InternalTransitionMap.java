package de.themonstrouscavalca.alteredstates.helpers;

import de.themonstrouscavalca.alteredstates.InternalTransition;
import de.themonstrouscavalca.alteredstates.interfaces.INameEvents;
import de.themonstrouscavalca.alteredstates.interfaces.INameStates;

import java.util.HashMap;

public class InternalTransitionMap<S extends INameStates, E extends INameEvents, C, X>
        extends HashMap<InternalTransition<S, E>, EventCheckAndAction<E, C, X>>{
}

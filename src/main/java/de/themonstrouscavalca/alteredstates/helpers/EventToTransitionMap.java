package de.themonstrouscavalca.alteredstates.helpers;

import de.themonstrouscavalca.alteredstates.transitions.Transition;
import de.themonstrouscavalca.alteredstates.interfaces.INameEvents;
import de.themonstrouscavalca.alteredstates.interfaces.INameStates;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

/**
 *
 * @param <S> State
 * @param <E> Event
 * @param <C> Context
 * @param <X> EventContext
 */
public class EventToTransitionMap<S extends INameStates, E extends INameEvents, C, X>
        extends HashMap<E, List<Transition<S, E, C, X>>>{

    public void addTransition(Transition<S, E, C, X> transition){
        if(this.containsKey(transition.getEvent())){
            this.get(transition.getEvent()).add(transition);
        }else{
            List<Transition<S, E, C, X>> transitionList = new ArrayList<>();
            transitionList.add(transition);
            this.put(transition.getEvent(), transitionList);
        }
    }
}


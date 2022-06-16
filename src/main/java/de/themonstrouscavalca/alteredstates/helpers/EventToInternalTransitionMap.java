package de.themonstrouscavalca.alteredstates.helpers;

import de.themonstrouscavalca.alteredstates.transitions.InternalTransition;
import de.themonstrouscavalca.alteredstates.interfaces.INameEvents;
import de.themonstrouscavalca.alteredstates.interfaces.INameStates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventToInternalTransitionMap<S extends INameStates, E extends INameEvents>
        extends HashMap<E, List<InternalTransition<S, E>>>{

    public void addTransition(InternalTransition<S, E> transition){
        if(this.containsKey(transition.getEvent())){
            this.get(transition.getEvent()).add(transition);
        }else{
            List<InternalTransition<S, E>> transitionList = new ArrayList<>();
            transitionList.add(transition);
            this.put(transition.getEvent(), transitionList);
        }
    }
}

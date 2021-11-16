package de.themonstrouscavalca.alteredstates.helpers;

import de.themonstrouscavalca.alteredstates.Transition;
import de.themonstrouscavalca.alteredstates.interfaces.INameEvents;
import de.themonstrouscavalca.alteredstates.interfaces.INameStates;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class EventToTransitionMap<S extends INameStates, E extends INameEvents>
        extends HashMap<E, List<Transition<S, E>>>{

    public void addTransition(Transition<S, E> transition){
        if(this.containsKey(transition.getEvent())){
            this.get(transition.getEvent()).add(transition);
        }else{
            List<Transition<S, E>> transitionList = new ArrayList<>();
            transitionList.add(transition);
            this.put(transition.getEvent(), transitionList);
        }
    }
}


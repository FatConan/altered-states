package de.themonstrouscavalca.alteredstates;

import de.themonstrouscavalca.alteredstates.interfaces.INameEvents;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class Events<E extends INameEvents>{
    private List<E> externalEvents;
    private List<E> internalEvents;

    public Events(Set<E> externalEvents, Set<E> internalEvents){
        this.externalEvents = new ArrayList<>(externalEvents);
        this.externalEvents.sort(Comparator.comparing(INameEvents::getName));
        this.internalEvents =  new ArrayList<>(internalEvents);
        this.internalEvents.sort(Comparator.comparing(INameEvents::getName));
    }

    public List<E> getExternalEvents(){
        return externalEvents;
    }

    public List<E> getInternalEvents(){
        return internalEvents;
    }
}

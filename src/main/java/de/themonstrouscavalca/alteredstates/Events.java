package de.themonstrouscavalca.alteredstates;

import java.util.List;

public class Events<E>{
    private List<E> externalEvents;
    private List<E> internalEvents;

    public Events(List<E> externalEvents, List<E> internalEvents){
        this.externalEvents = externalEvents;
        this.internalEvents = internalEvents;
    }

    public List<E> getExternalEvents(){
        return externalEvents;
    }

    public List<E> getInternalEvents(){
        return internalEvents;
    }
}

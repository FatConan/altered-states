package de.themonstrouscavalca.alteredstates.helpers;

import de.themonstrouscavalca.alteredstates.interfaces.ICheckEvents;
import de.themonstrouscavalca.alteredstates.interfaces.INameEvents;
import de.themonstrouscavalca.alteredstates.interfaces.ITakeAction;

public class EventCheckAndAction<E extends INameEvents, C, X>{
    private final ICheckEvents<E, C, X> checker;
    private final ITakeAction<E, C, X> actor;

    public EventCheckAndAction(ICheckEvents<E, C, X> checker, ITakeAction<E, C, X> actor){
        this.checker = checker;
        this.actor = actor;
    }

    public ICheckEvents<E, C, X> getChecker(){
        return checker;
    }

    public ITakeAction<E, C, X> getActor(){
        return actor;
    }
}

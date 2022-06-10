package de.themonstrouscavalca.alteredstates.helpers;

import de.themonstrouscavalca.alteredstates.interfaces.INameStates;

import java.util.Arrays;
import java.util.HashSet;

public class StateCollection<S extends INameStates> extends HashSet<S>{
    @SuppressWarnings("rawtypes")
    public final static StateCollection WILDCARD = new StateCollection<>();
    static {
        WILDCARD.wildcard = true;
    }

    @SuppressWarnings("unchecked")
    public static final <T extends INameStates> StateCollection<T> wildcard() {
        return (StateCollection<T>) WILDCARD;
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <T extends INameStates> StateCollection<T> of(T ... states) {
        StateCollection<T> stateCollection = new StateCollection<>();
        stateCollection.addAll(Arrays.asList(states));
        return stateCollection;
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <T extends INameStates> StateCollection<T> not(T ... states) {
        StateCollection<T> stateCollection = new StateCollection<>();
        stateCollection.addAll(Arrays.asList(states));
        stateCollection.inverse = true;
        return stateCollection;
    }

    private boolean wildcard = false;
    private boolean inverse = false;

    public boolean isWildcard(){
        return wildcard;
    }

    public boolean isInverse(){
        return inverse;
    }

    public boolean matches(S state){
        return (this.isInverse() && !this.contains(state)) || (this.isWildcard() || this.contains(state));
    }
}

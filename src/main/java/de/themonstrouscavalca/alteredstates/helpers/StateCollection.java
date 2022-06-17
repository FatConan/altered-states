package de.themonstrouscavalca.alteredstates.helpers;

import de.themonstrouscavalca.alteredstates.interfaces.INameStates;

import java.util.Arrays;
import java.util.HashSet;

public class StateCollection<S extends INameStates> extends HashSet<S>{
    @SuppressWarnings("rawtypes")
    public static final StateCollection WILDCARD;
    static {
        @SuppressWarnings("rawtypes")
        final StateCollection wildcard = new StateCollection<>();
        wildcard.wildcard = true;
        WILDCARD = wildcard;
    }

    @SuppressWarnings("unchecked")
    public static <T extends INameStates> StateCollection<T> wildcard() {
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
        if(this.isWildcard()){
            //Wild cards match any state, so return true without performing further checks
            return true;
        }
        if(this.isInverse()){
            //This is a NOT group. check that the provided state is NOT in the current set
            return !this.contains(state);
        }
        //Otherwise check if the returned state is within the current set
        return this.contains(state);
    }
}

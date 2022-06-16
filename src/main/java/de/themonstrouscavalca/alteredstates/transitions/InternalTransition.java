package de.themonstrouscavalca.alteredstates.transitions;

import de.themonstrouscavalca.alteredstates.helpers.StateCollection;
import de.themonstrouscavalca.alteredstates.interfaces.INameEvents;
import de.themonstrouscavalca.alteredstates.interfaces.INameStates;

public class InternalTransition<S extends INameStates, E extends INameEvents>{
    private String label;
    private StateCollection<S> states;
    private E event;
    private boolean privileged;

    public static class Builder<S extends INameStates, E extends INameEvents>{
        private String label = "";
        private StateCollection<S> states;
        private E event;
        private boolean privileged = false;

        public Builder<S, E> states(StateCollection<S> states){
            this.states = states;
            return this;
        }
        public Builder<S, E> states(S state){
            this.states = StateCollection.of(state);
            return this;
        }
        public Builder<S, E> label(String label){
            this.label = label;
            return this;
        }
        public Builder<S, E> on(E onEvent){
            this.event = onEvent;
            return this;
        }
        public Builder<S, E> privileged(){
            this.privileged = true;
            return this;
        }
        public InternalTransition<S, E> build(){
            return new InternalTransition<>(this);
        }
    }

    public InternalTransition(Builder<S, E> builder){
        this.states = builder.states;
        this.event = builder.event;
        this.privileged = builder.privileged;
        this.label = builder.label;
    }

    public String getLabel(){
        return label;
    }
    public boolean isPrivileged(){
        return privileged;
    }
    public StateCollection<S> getStates(){
        return states;
    }
    public E getEvent(){
        return event;
    }
}

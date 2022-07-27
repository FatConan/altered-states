package de.themonstrouscavalca.alteredstates.transitions;

import de.themonstrouscavalca.alteredstates.helpers.StateCollection;
import de.themonstrouscavalca.alteredstates.interfaces.INameEvents;
import de.themonstrouscavalca.alteredstates.interfaces.INameStates;

import java.util.Objects;

public class InternalTransition<S extends INameStates, E extends INameEvents>{
    private static final String STATE_ERROR = "This transition has been configured to use a single state, use getState()";
    private static final String STATES_ERROR = "This transition has been configured to use complex from states, use getStates()";

    private InternalTransitionType transitionType;
    private String label;
    private StateCollection<S> states;
    private S state;
    private E event;
    private boolean privileged;

    public static class Builder<S extends INameStates, E extends INameEvents>{
        private boolean multipleStates = false;
        private String label = "";
        private StateCollection<S> states;
        private S state;
        private E event;
        private boolean privileged = false;

        public Builder<S, E> states(StateCollection<S> states){
            this.multipleStates = true;
            this.states = states;
            return this;
        }
        public Builder<S, E> states(S state){
            this.state = state;
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
        this.transitionType = InternalTransitionType.of(builder.multipleStates);
        this.states = builder.states;
        this.state = builder.state;
        this.event = builder.event;
        this.privileged = builder.privileged;
        this.label = builder.label;
    }

    public InternalTransitionType getTransitionType(){
        return transitionType;
    }

    public boolean matchesState(S state){
        return this.getTransitionType().isMultipleStates() ? this.getStates().matches(state) : Objects.equals(this.getState(), state);
    }

    public String getLabel(){
        return label;
    }
    public boolean isPrivileged(){
        return privileged;
    }
    public StateCollection<S> getStates(){
        if(!this.transitionType.isMultipleStates()){
            throw new TransitionConfigurationException(STATE_ERROR, this);
        }
        return states;
    }
    public S getState(){
        if(this.transitionType.isMultipleStates()){
            throw new TransitionConfigurationException(STATES_ERROR, this);
        }
        return this.state;
    }
    public E getEvent(){
        return event;
    }
}

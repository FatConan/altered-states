package de.themonstrouscavalca.alteredstates.transitions;

import de.themonstrouscavalca.alteredstates.helpers.StateCollection;
import de.themonstrouscavalca.alteredstates.interfaces.IGenerateState;
import de.themonstrouscavalca.alteredstates.interfaces.INameEvents;
import de.themonstrouscavalca.alteredstates.interfaces.INameStates;

import java.util.Objects;

/**
 *
 * @param <S> State
 * @param <E> Event
 * @param <C> Context
 * @param <X> EventContext
 */
public class Transition<S extends INameStates, E extends INameEvents, C, X>{
    private static final String FROM_STATES_ERROR = "This transition has been configured to use complex from states, use getFromStates()";
    private static final String FROM_STATE_ERROR = "This transition has been configured to use a simple from state, use getFromState()";
    private static final String STATE_GENERATOR_ERROR = "This transition has been configured to use a dynamic to state generator, use getToState(event, context, eventContext)";
    private final TransitionType transitionType;

    private final String label;
    private final StateCollection<S> fromStates;
    private final S fromState;
    private final IGenerateState<S, E, C, X> toStateGenerator;
    private final S toState;
    private final E event;
    private final boolean privileged;

    public static class Builder<S extends INameStates, E extends INameEvents, C, X>{
        private boolean multipleOrigins = false;
        private boolean withToStateGenerator = false;

        private String label = "";

        private StateCollection<S> fromStates;
        private S fromState;

        private IGenerateState<S, E, C, X> toStateGenerator;
        private S toState;

        private E event;
        private boolean privileged = false;

        public Builder<S, E, C, X> from(StateCollection<S> fromStates){
            this.multipleOrigins = true;
            this.fromStates = fromStates;
            return this;
        }
        public Builder<S, E, C, X> from(S fromState){
            this.fromState = fromState;
            return this;
        }
        public Builder<S, E, C, X> to(S toState){
            this.toState = toState;
            return this;
        }
        public Builder<S, E, C, X> to(IGenerateState<S, E, C, X> toStateGenerator){
            this.withToStateGenerator = true;
            this.toStateGenerator = toStateGenerator;
            return this;
        }
        public Builder<S, E, C, X> label(String label){
            this.label = label;
            return this;
        }
        public Builder<S, E, C, X> on(E onEvent){
            this.event = onEvent;
            return this;
        }
        public Builder<S, E, C, X> privileged(){
            this.privileged = true;
            return this;
        }
        public Transition<S, E, C, X> build(){
            return new Transition<>(this);
        }
    }

    public Transition(Builder<S, E,  C, X> builder){
        this.transitionType = TransitionType.of(builder.multipleOrigins, builder.withToStateGenerator);
        this.fromState = builder.fromState;
        this.fromStates = builder.fromStates;
        this.toStateGenerator = builder.toStateGenerator;
        this.toState = builder.toState;
        this.event = builder.event;
        this.privileged = builder.privileged;
        this.label = builder.label;
    }

    public String getLabel(){
        return label;
    }

    public boolean matchesFromState(S state){
        return this.getTransitionType().isMultipleOrigins() ? this.getFromStates().matches(state) : Objects.equals(this.getFromState(), state);
    }

    public S getFromState(){
        if(this.transitionType.isMultipleOrigins()){
            throw new TransitionConfigurationException(FROM_STATES_ERROR, this);
        }
        return this.fromState;
    }

    public StateCollection<S> getFromStates(){
        if(!this.transitionType.isMultipleOrigins()){
            throw new TransitionConfigurationException(FROM_STATE_ERROR, this);
        }
        return fromStates;
    }

    public IGenerateState<S, E, C, X> getToStateGenerator(){
        if(!this.transitionType.isToGenerator()){
            return (e, c, x) -> this.toState;
        }
        return toStateGenerator;
    }

    public S getToState(E event, C context, X eventContext){
        if(!this.transitionType.isToGenerator()){
            return this.getToState();
        }
        return this.toStateGenerator.generate(event, context, eventContext);
    }

    public S getToState(){
        if(this.transitionType.isToGenerator()){
            throw new TransitionConfigurationException(STATE_GENERATOR_ERROR, this);
        }
        return this.toState;
    }

    public E getEvent(){
        return event;
    }

    public boolean isPrivileged(){
        return privileged;
    }

    public TransitionType getTransitionType(){
        return transitionType;
    }
}

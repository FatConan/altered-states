package de.themonstrouscavalca.alteredstates.transitions;

import de.themonstrouscavalca.alteredstates.helpers.StateCollection;
import de.themonstrouscavalca.alteredstates.interfaces.IGenerateState;
import de.themonstrouscavalca.alteredstates.interfaces.INameEvents;
import de.themonstrouscavalca.alteredstates.interfaces.INameStates;

/**
 *
 * @param <S> State
 * @param <E> Event
 * @param <C> Context
 * @param <X> EventContext
 */
public class Transition<S extends INameStates, E extends INameEvents, C, X>{
    private final String label;
    private final StateCollection<S> fromStates;
    private final IGenerateState<S, E, C, X> toStateGenerator;
    private final E event;
    private final boolean privileged;

    public static class Builder<S extends INameStates, E extends INameEvents, C, X>{
        private String label = "";
        private StateCollection<S> fromStates;
        private IGenerateState<S, E, C, X> toStateGenerator;
        private E event;
        private boolean privileged = false;

        public Builder<S, E, C, X> from(StateCollection<S> fromStates){
            this.fromStates = fromStates;
            return this;
        }
        public Builder<S, E, C, X> from(S fromState){
            this.fromStates = StateCollection.of(fromState);
            return this;
        }
        public Builder<S, E, C, X> to(S toState){
            this.toStateGenerator = (e, c, x) -> toState;
            return this;
        }
        public Builder<S, E, C, X> to(IGenerateState<S, E, C, X> toStateGenerator){
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
        this.fromStates = builder.fromStates;
        this.toStateGenerator = builder.toStateGenerator;
        this.event = builder.event;
        this.privileged = builder.privileged;
        this.label = builder.label;
    }

    public String getLabel(){
        return label;
    }

    public StateCollection<S> getFromStates(){
        return fromStates;
    }

    public IGenerateState<S, E, C, X> getToStateGenerator(){
        return toStateGenerator;
    }

    public S getToState(E event, C context, X eventContext){
        return toStateGenerator.generate(event, context, eventContext);
    }

    public E getEvent(){
        return event;
    }

    public boolean isPrivileged(){
        return privileged;
    }
}

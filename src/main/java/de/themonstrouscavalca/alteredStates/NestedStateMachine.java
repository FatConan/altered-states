package de.themonstrouscavalca.alteredStates;

import de.themonstrouscavalca.alteredStates.interfaces.EventConsumer;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A state machine of statemachines, this can allow us to create state machines in which complex transitions can be handled as
 * "internal" transitions within a state that is itself expressed as another state machine as well as transitions between the
 * stae machines thmeselves.
 * @param <S> State Class
 * @param <E> Event class
 * @param <C> State context class
 * @param <X> Event context class
 */
public class NestedStateMachine<S, E, C, X, T extends StateMachine<S, E, C, X>>{
    private final T initialState;
    private T currentState;
    private final List<T> states;
    private final List<E> events;
    private final List<Transition<T, E>> transitions;
    private final Map<E, List<Transition<T, E>>> eventMap;
    private final Map<Transition<T, E>, EventConsumer<E, X, C>> handlerMap;
    private final C context;

    public NestedStateMachine(T initialState,
                              List<T> states, List<E> events,
                              List<Transition<T, E>> transitions,
                              Map<Transition<T, E>, EventConsumer<E, X, C>> handlerMap,
                              C context){
        this.initialState = initialState;
        this.currentState = initialState;
        this.states = states;
        this.events = events;
        this.transitions = transitions;
        this.handlerMap = handlerMap;
        this.context = context;
        this.eventMap = this.processExternalTransitions();
    }

    private Map<E, List<Transition<T, E>>> processExternalTransitions(){
        Map<E, List<Transition<T, E>>> eventMap = new HashMap<>();
        for(Transition<T, E> transition : this.transitions){
            if(eventMap.containsKey(transition.getEvent())){
                eventMap.get(transition.getEvent()).add(transition);
            }else{
                List<Transition<T, E>> transitionList = new ArrayList<>();
                transitionList.add(transition);
                eventMap.put(transition.getEvent(), transitionList);
            }
        }
        return eventMap;
    }

    public T getInitialState(){
        return initialState;
    }

    public List<T> getStates(){
        return states;
    }

    public List<E> getEvents(){
        return events;
    }

    public List<Transition<T, E>> getTransitions(){
        return transitions;
    }

    public Map<E, List<Transition<T, E>>> getEventMap(){
        return eventMap;
    }

    public Map<Transition<T, E>, EventConsumer<E, X, C>> getHandlerMap(){
        return handlerMap;
    }

    public C getContext(){
        return context;
    }

    public StateChange<T, E, C, X> onEvent(E event, X eventContext){
        T initialState = this.getInitialState();
        boolean externalTransitionFound = false;
        boolean externalTransitionSuccessful = false;
        boolean internalTransitionFound = false;
        boolean internalTransitionSuccessful = false;

        List<Transition<T, E>> transitions = this.eventMap.getOrDefault(event, Collections.emptyList());
        Optional<Transition<T, E>> selectedOpt = transitions.stream()
                .filter(t -> t.getFromState().equals(this.currentState)).findFirst();

        if(selectedOpt.isPresent()){
            externalTransitionFound = true;
            boolean gate = this.handlerMap.get(selectedOpt.get()).process(event, eventContext, this.context);
            if(gate){
                externalTransitionSuccessful = true;
                this.currentState = selectedOpt.get().getToState();
            }
        }

        StateChange<S, E, C, X> internalResult = this.currentState.onEvent(event, eventContext);
        internalTransitionFound = internalResult.transitionFound();
        internalTransitionSuccessful = internalResult.transitionSuccessful();

        return new StateChange<>(
                initialState, this.currentState, event, this.getContext(), eventContext,
                externalTransitionFound, externalTransitionSuccessful,
                internalTransitionFound, internalTransitionSuccessful);
    }

    public StateChange<T, E, C, X> onEvent(E event){
        return this.onEvent(event, null);
    }

    public void setCurrentState(T state){
        this.currentState = state;
    }

    public T getCurrentState(){
        return this.currentState;
    }

    public List<Transition<T, E>> getAvailableTransitions(){
        return this.transitions.stream()
                .filter(t -> t.getFromState().equals(this.currentState))
                .collect(Collectors.toList());
    }
}

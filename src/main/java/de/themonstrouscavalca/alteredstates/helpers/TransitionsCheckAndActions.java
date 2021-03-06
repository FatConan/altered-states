package de.themonstrouscavalca.alteredstates.helpers;

import de.themonstrouscavalca.alteredstates.InternalTransition;
import de.themonstrouscavalca.alteredstates.Transition;
import de.themonstrouscavalca.alteredstates.interfaces.INameEvents;
import de.themonstrouscavalca.alteredstates.interfaces.INameStates;

import java.util.*;
import java.util.stream.Collectors;

public class TransitionsCheckAndActions<S extends INameStates, E extends INameEvents, C, X>{
    private Set<E> events = new LinkedHashSet<>();
    private EventToTransitionMap<S, E> eventMap = new EventToTransitionMap<>();
    private EventToInternalTransitionMap<S, E> internalEventMap = new EventToInternalTransitionMap<>();
    private TransitionToCheckAndActionMap<S, E, C, X> transitionCheckAndActions = new TransitionToCheckAndActionMap<>();
    private InternalTransitionToCheckAndActionMap<S, E, C, X> internalTransitionCheckAndActions = new InternalTransitionToCheckAndActionMap<>();

    public List<Transition<S, E>> getTransitions(){
        List<Transition<S, E>> transitions = new ArrayList<>(this.transitionCheckAndActions.keySet());
        transitions.sort(Comparator.comparing(Transition::getLabel));
        return transitions;
    }

    public List<InternalTransition<S, E>> getInternalTransitions(){
        List<InternalTransition<S, E>> transitions = new ArrayList<>(this.internalTransitionCheckAndActions.keySet());
        transitions.sort(Comparator.comparing(InternalTransition::getLabel));
        return transitions;
    }

    public EventToTransitionMap<S, E> getEventMap(){
        return eventMap;
    }

    public Set<E> getEvents(){
        return events;
    }

    public EventToInternalTransitionMap<S, E> getInternalEventMap(){
        return internalEventMap;
    }

    public TransitionToCheckAndActionMap<S, E, C, X> getTransitionsMap(){
        return this.transitionCheckAndActions;
    }

    public InternalTransitionToCheckAndActionMap<S, E, C, X> getInternalTransitionsMap(){
        return this.internalTransitionCheckAndActions;
    }

    public void addTransition(Transition<S, E> transition, EventCheckAndAction<E, C, X> eventCheckAndAction){
        this.eventMap.addTransition(transition);
        this.events.add(transition.getEvent());
        this.transitionCheckAndActions.put(transition, eventCheckAndAction);
    }

    public void addTransition(InternalTransition<S, E> transition, EventCheckAndAction<E, C, X> eventCheckAndAction){
        this.internalEventMap.addTransition(transition);
        this.events.add(transition.getEvent());
        this.internalTransitionCheckAndActions.put(transition, eventCheckAndAction);
    }

    /*public Optional<Transition<S, E>> getTransitionForEventAndState(E event, S state){
        List<Transition<S, E>> transitionsForEvent = this.eventMap.getOrDefault(event, Collections.emptyList());
        Optional<Transition<S, E>> selectedOpt = transitionsForEvent.stream()
                .filter(t -> t.getFromState().equals(state)).findFirst();
        return selectedOpt;
    }*/

    public List<Transition<S, E>> getTransitionForEventAndState(E event, S state){
        List<Transition<S, E>> transitionsForEvent = this.eventMap.getOrDefault(event, Collections.emptyList());
        List<Transition<S, E>> selected = transitionsForEvent.stream()
                .filter(t -> t.getFromState().equals(state)).collect(Collectors.toList());
        return selected;
    }

    /*public Optional<InternalTransition<S, E>> getInternalTransitionForEventAndState(E event, S state){
        List<InternalTransition<S, E>> transitionsForEvent = this.internalEventMap.getOrDefault(event, Collections.emptyList());
        Optional<InternalTransition<S, E>> selectedOpt = transitionsForEvent.stream()
                .filter(t -> t.getState().equals(state)).findFirst();
        return selectedOpt;
    }*/

    public List<InternalTransition<S, E>> getInternalTransitionForEventAndState(E event, S state){
        List<InternalTransition<S, E>> transitionsForEvent = this.internalEventMap.getOrDefault(event, Collections.emptyList());
        List<InternalTransition<S, E>> selected = transitionsForEvent.stream()
                .filter(t -> t.getState().equals(state)).collect(Collectors.toList());
        return selected;
    }

    public TransitionsCheckAndActions<S, E, C, X> getForState(S state){
        TransitionsCheckAndActions<S, E, C, X> stateSpecific = new TransitionsCheckAndActions<>();
        for(Transition<S, E> t: this.getTransitions().stream()
                .filter(t -> t.getFromState().equals(state))
                .collect(Collectors.toList())){
            stateSpecific.addTransition(t, this.getTransitionsMap().get(t));
        }

        for(InternalTransition<S, E> t: this.getInternalTransitions().stream()
                .filter(t -> t.getState().equals(state))
                .collect(Collectors.toList())){
            stateSpecific.addTransition(t, this.getInternalTransitionsMap().get(t));
        }
        return stateSpecific;
    }

    public EventCheckAndAction<E, C, X> getCheckAndAction(Transition<S, E> transition){
        return this.transitionCheckAndActions.get(transition);
    }

    public EventCheckAndAction<E, C, X> getCheckAndAction(InternalTransition<S, E> transition){
        return this.internalTransitionCheckAndActions.get(transition);
    }
}

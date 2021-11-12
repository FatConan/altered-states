package de.themonstrouscavalca.alteredStates;

import de.themonstrouscavalca.alteredStates.interfaces.EventConsumer;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;

public class StateMachineTest{
    public enum State{
        STATE_1,
        STATE_2,
        STATE_3;
    }
    public enum Event{
        EVENT_1,
        EVENT_2,
        EVENT_3,
        EVENT_4,
        EVENT_5,
        EVENT_6;
    }

    public static class SubMachine extends StateMachine<State, Event, String, String>{
        public SubMachine(State initialState, List<State> states,
                          List<Event> events, List<Transition<State, Event>> transitions,
                          List<InternalTransition<State, Event>> internalTransitions,
                          Map<Transition<State, Event>, EventConsumer<Event, String, String>> handlerMap,
                          Map<InternalTransition<State, Event>, EventConsumer<Event, String, String>> internalHandlerMap,
                          String context){
            super(initialState, states, events, transitions, internalTransitions, handlerMap, internalHandlerMap, context);
        }
    }

    public static class Machine extends NestedStateMachine<State, Event, String, String, SubMachine>{
        public Machine(SubMachine initialState, List<SubMachine> states, List<Event> events,
                       List<Transition<SubMachine, Event>> transitions,
                       Map<Transition<SubMachine, Event>, EventConsumer<Event, String, String>> handlerMap, String context){
            super(initialState, states, events, transitions, handlerMap, context);
        }
    }

    public static class SubMachineBuilder extends StateMachineBuilder<State, Event, String, String, SubMachine>{

        public static SubMachineBuilder builder(){
            return new SubMachineBuilder();
        }

        @Override
        protected SubMachine createInstance(){
            return new SubMachine(this.initialState, this.finalStates, this.finalEvents,
                    this.transitions,
                    this.internalTransitions,
                    this.handlerMap,
                    this.internalHandlerMap,
                    this.context);
        }
    }

    public static class MachineBuilder extends NestedStateMachineBuilder<State, Event, String, String, SubMachine, Machine>{
        public static MachineBuilder builder(){
            return new MachineBuilder();
        }

        @Override
        protected Machine createInstance(){
            return new Machine(this.initialState, this.states, this.events,
                    this.transitions,
                    this.handlerMap,
                    this.context);
        }
    }



    @Test
    public void createBuilderWithStatesAndEvents(){
        StateMachineBuilder<State, Event, String, String, StateMachine<State, Event, String, String>> builder =
                new StateMachineBuilder<State, Event, String, String, StateMachine<State, Event, String, String>>()
                .addTransition(State.STATE_1, State.STATE_2, Event.EVENT_1)
                .addTransition(State.STATE_2, State.STATE_3, Event.EVENT_2)
                .addTransition(State.STATE_3, State.STATE_1, Event.EVENT_3)
                .setContext("TEST")
                .setInitialState(State.STATE_1);

        StateMachine<State, Event, String, String> machine = builder.build();
        assertEquals("Initial state doesn't match", State.STATE_1, machine.getCurrentState());

        StateChange<State, Event, String, String> next  = machine.onEvent(Event.EVENT_1);
        assertEquals("Progression 1 doesn't match", State.STATE_2, next.getToState());
        assertEquals("Progression 1 doesn't match", State.STATE_2, machine.getCurrentState());

        next  = machine.onEvent(Event.EVENT_1);
        assertEquals("Progression 1 doesn't match", State.STATE_2, next.getToState());
        assertEquals("Progression 1 doesn't match", State.STATE_2, machine.getCurrentState());

        next = machine.onEvent(Event.EVENT_2);
        assertEquals("Progression 2 doesn't match", State.STATE_3, next.getToState());
        assertEquals("Progression 2 doesn't match", State.STATE_3, machine.getCurrentState());

        next = machine.onEvent(Event.EVENT_1);
        assertEquals("Progression 2 doesn't match", State.STATE_3, next.getToState());
        assertEquals("Progression 2 doesn't match", State.STATE_3, machine.getCurrentState());

        next = machine.onEvent(Event.EVENT_2);
        assertEquals("Progression 2 doesn't match", State.STATE_3, next.getToState());
        assertEquals("Progression 2 doesn't match", State.STATE_3, machine.getCurrentState());

        next = machine.onEvent(Event.EVENT_3);
        assertEquals("Progression 3 doesn't match", State.STATE_1, next.getToState());
        assertEquals("Progression 3 doesn't match", State.STATE_1, machine.getCurrentState());
    }

    @Test
    public void createBuilderWithSubMachine(){
        SubMachineBuilder builder = new SubMachineBuilder();
        builder.addTransition(State.STATE_1, State.STATE_2, Event.EVENT_1)
                .addTransition(State.STATE_2, State.STATE_3, Event.EVENT_2)
                .addTransition(State.STATE_3, State.STATE_1, Event.EVENT_3)
                .setContext("TEST")
                .setInitialState(State.STATE_1);

        StateMachine<State, Event, String, String> machine = builder.build();
        assertEquals("Initial state doesn't match", State.STATE_1, machine.getCurrentState());

        StateChange<State, Event, String, String> next  = machine.onEvent(Event.EVENT_1);
        assertEquals("Progression 1 doesn't match", State.STATE_2, next.getToState());
        assertEquals("Progression 1 doesn't match", State.STATE_2, machine.getCurrentState());

        next  = machine.onEvent(Event.EVENT_1);
        assertEquals("Progression 1 doesn't match", State.STATE_2, next.getToState());
        assertEquals("Progression 1 doesn't match", State.STATE_2, machine.getCurrentState());

        next = machine.onEvent(Event.EVENT_2);
        assertEquals("Progression 2 doesn't match", State.STATE_3, next.getToState());
        assertEquals("Progression 2 doesn't match", State.STATE_3, machine.getCurrentState());

        next = machine.onEvent(Event.EVENT_1);
        assertEquals("Progression 2 doesn't match", State.STATE_3, next.getToState());
        assertEquals("Progression 2 doesn't match", State.STATE_3, machine.getCurrentState());

        next = machine.onEvent(Event.EVENT_2);
        assertEquals("Progression 2 doesn't match", State.STATE_3, next.getToState());
        assertEquals("Progression 2 doesn't match", State.STATE_3, machine.getCurrentState());

        next = machine.onEvent(Event.EVENT_3);
        assertEquals("Progression 3 doesn't match", State.STATE_1, next.getToState());
        assertEquals("Progression 3 doesn't match", State.STATE_1, machine.getCurrentState());
    }

    @Test
    public void createdNestedBuilder(){
        StateMachine<State, Event, String, String> subMachine1 =
                new StateMachineBuilder<State, Event, String, String, StateMachine<State, Event, String, String>>()
                        .addTransition(State.STATE_1, State.STATE_2, Event.EVENT_1)
                        .addTransition(State.STATE_2, State.STATE_3, Event.EVENT_2)
                        .addTransition(State.STATE_3, State.STATE_1, Event.EVENT_3)
                        .setContext("TEST")
                        .setInitialState(State.STATE_1)
                        .build();

        StateMachine<State, Event, String, String> subMachine2 =
                new StateMachineBuilder<State, Event, String, String, StateMachine<State, Event, String, String>>()
                        .addTransition(State.STATE_1, State.STATE_3, Event.EVENT_3)
                        .setContext("TEST")
                        .setInitialState(State.STATE_1)
                        .build();

        StateMachine<State, Event, String, String> subMachine3 =
                new StateMachineBuilder<State, Event, String, String, StateMachine<State, Event, String, String>>()
                        .addTransition(State.STATE_3, State.STATE_2, Event.EVENT_1)
                        .setContext("TEST")
                        .setInitialState(State.STATE_3)
                        .build();

        NestedStateMachine<State, Event, String, String, StateMachine<State, Event, String, String>> machine =
                new NestedStateMachineBuilder<State, Event, String, String, StateMachine<State, Event, String, String>, NestedStateMachine<State, Event, String, String, StateMachine<State, Event, String, String>>>()
                        .addTransition(subMachine1, subMachine2, Event.EVENT_4)
                        .addTransition(subMachine2, subMachine3, Event.EVENT_5)
                        .addTransition(subMachine3, subMachine1, Event.EVENT_6)
                        .setInitialState(subMachine1)
                        .build();

        assertEquals("Initial state doesn't match", subMachine1, machine.getCurrentState());
        StateChange<StateMachine<State, Event, String, String>, Event, String, String> next  = machine.onEvent(Event.EVENT_1);
        assertEquals("Progression 1 doesn't match", subMachine1, next.getToState());
        assertEquals("Progression 1 doesn't match", subMachine1, machine.getCurrentState());
        assertEquals("Progression 1 doesn't match", State.STATE_2, machine.getCurrentState().getCurrentState());

        next  = machine.onEvent(Event.EVENT_1);
        assertEquals("Progression 1 doesn't match", subMachine1, next.getToState());
        assertEquals("Progression 1 doesn't match", subMachine1, machine.getCurrentState());
        assertEquals("Progression 1 doesn't match", State.STATE_2, machine.getCurrentState().getCurrentState());

        next = machine.onEvent(Event.EVENT_2);
        assertEquals("Progression 1 doesn't match", subMachine1, next.getToState());
        assertEquals("Progression 1 doesn't match", subMachine1, machine.getCurrentState());
        assertEquals("Progression 1 doesn't match", State.STATE_3, machine.getCurrentState().getCurrentState());

        next = machine.onEvent(Event.EVENT_4);
        assertEquals("Progression 2 doesn't match", subMachine2, next.getToState());
        assertEquals("Progression 2 doesn't match", subMachine2, machine.getCurrentState());
        assertEquals("Progression 2 doesn't match", State.STATE_1, machine.getCurrentState().getCurrentState());

        next = machine.onEvent(Event.EVENT_5);
        assertEquals("Progression 2 doesn't match", subMachine3, next.getToState());
        assertEquals("Progression 2 doesn't match", subMachine3, machine.getCurrentState());
        assertEquals("Progression 2 doesn't match", State.STATE_3, machine.getCurrentState().getCurrentState());

        next = machine.onEvent(Event.EVENT_1);
        assertEquals("Progression 3 doesn't match", subMachine3, next.getToState());
        assertEquals("Progression 3 doesn't match", subMachine3, machine.getCurrentState());
        assertEquals("Progression 2 doesn't match", State.STATE_2, machine.getCurrentState().getCurrentState());

        next = machine.onEvent(Event.EVENT_6);
        assertEquals("Progression 3 doesn't match", subMachine1, next.getToState());
        assertEquals("Progression 3 doesn't match", subMachine1, machine.getCurrentState());
        assertEquals("Progression 2 doesn't match", State.STATE_3, machine.getCurrentState().getCurrentState());
    }

    @Test
    public void createMachineBuilder(){
        SubMachine subMachine1 =
                new SubMachineBuilder()
                        .addTransition(State.STATE_1, State.STATE_2, Event.EVENT_1)
                        .addTransition(State.STATE_2, State.STATE_3, Event.EVENT_2)
                        .addTransition(State.STATE_3, State.STATE_1, Event.EVENT_3)
                        .setContext("TEST")
                        .setInitialState(State.STATE_1)
                        .build();

        SubMachine subMachine2 =
                new SubMachineBuilder()
                        .addTransition(State.STATE_1, State.STATE_3, Event.EVENT_3)
                        .setContext("TEST")
                        .setInitialState(State.STATE_1)
                        .build();

        SubMachine subMachine3 =
                new SubMachineBuilder()
                        .addTransition(State.STATE_3, State.STATE_2, Event.EVENT_1)
                        .setContext("TEST")
                        .setInitialState(State.STATE_3)
                        .build();

        Machine machine =
                new MachineBuilder()
                        .addTransition(subMachine1, subMachine2, Event.EVENT_4)
                        .addTransition(subMachine2, subMachine3, Event.EVENT_5)
                        .addTransition(subMachine3, subMachine1, Event.EVENT_6)
                        .setInitialState(subMachine1)
                        .build();

        assertEquals("Initial state doesn't match", subMachine1, machine.getCurrentState());
        StateChange<SubMachine, Event, String, String> next  = machine.onEvent(Event.EVENT_1);
        assertEquals("Progression 1 doesn't match", subMachine1, next.getToState());
        assertEquals("Progression 1 doesn't match", subMachine1, machine.getCurrentState());
        assertEquals("Progression 1 doesn't match", State.STATE_2, machine.getCurrentState().getCurrentState());

        next  = machine.onEvent(Event.EVENT_1);
        assertEquals("Progression 1 doesn't match", subMachine1, next.getToState());
        assertEquals("Progression 1 doesn't match", subMachine1, machine.getCurrentState());
        assertEquals("Progression 1 doesn't match", State.STATE_2, machine.getCurrentState().getCurrentState());

        next = machine.onEvent(Event.EVENT_2);
        assertEquals("Progression 1 doesn't match", subMachine1, next.getToState());
        assertEquals("Progression 1 doesn't match", subMachine1, machine.getCurrentState());
        assertEquals("Progression 1 doesn't match", State.STATE_3, machine.getCurrentState().getCurrentState());

        next = machine.onEvent(Event.EVENT_4);
        assertEquals("Progression 2 doesn't match", subMachine2, next.getToState());
        assertEquals("Progression 2 doesn't match", subMachine2, machine.getCurrentState());
        assertEquals("Progression 2 doesn't match", State.STATE_1, machine.getCurrentState().getCurrentState());

        next = machine.onEvent(Event.EVENT_5);
        assertEquals("Progression 2 doesn't match", subMachine3, next.getToState());
        assertEquals("Progression 2 doesn't match", subMachine3, machine.getCurrentState());
        assertEquals("Progression 2 doesn't match", State.STATE_3, machine.getCurrentState().getCurrentState());

        next = machine.onEvent(Event.EVENT_1);
        assertEquals("Progression 3 doesn't match", subMachine3, next.getToState());
        assertEquals("Progression 3 doesn't match", subMachine3, machine.getCurrentState());
        assertEquals("Progression 2 doesn't match", State.STATE_2, machine.getCurrentState().getCurrentState());

        next = machine.onEvent(Event.EVENT_6);
        assertEquals("Progression 3 doesn't match", subMachine1, next.getToState());
        assertEquals("Progression 3 doesn't match", subMachine1, machine.getCurrentState());
        assertEquals("Progression 2 doesn't match", State.STATE_3, machine.getCurrentState().getCurrentState());
    }
}

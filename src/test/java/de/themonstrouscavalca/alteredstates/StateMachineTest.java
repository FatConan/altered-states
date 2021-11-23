package de.themonstrouscavalca.alteredstates;

import de.themonstrouscavalca.alteredstates.abs.AbstractStateMachineBuilder;
import de.themonstrouscavalca.alteredstates.helpers.GateChecker;
import de.themonstrouscavalca.alteredstates.helpers.TransitionsCheckAndActions;
import de.themonstrouscavalca.alteredstates.impl.StateMachine;
import de.themonstrouscavalca.alteredstates.impl.StateMachineBuilder;
import de.themonstrouscavalca.alteredstates.interfaces.IManageStates;
import de.themonstrouscavalca.alteredstates.interfaces.IMonitorStateChanges;
import de.themonstrouscavalca.alteredstates.interfaces.INameEvents;
import de.themonstrouscavalca.alteredstates.interfaces.INameStates;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StateMachineTest{
    public enum State implements INameStates{
        STATE_1,
        STATE_2,
        STATE_3;

        @Override
        public String getName(){
            return getName();
        }
    }

    public enum Event implements INameEvents{
        EVENT_1,
        EVENT_2,
        EVENT_3,
        EVENT_4,
        EVENT_5,
        EVENT_6;

        @Override
        public String getName(){
            return getName();
        }
    }

    public static class SubMachine extends StateMachine<State, Event, String, String>
            implements IManageStates<State, Event, String, String>{
        public SubMachine(SubMachineBuilder builder){
            super(builder);
        }
    }

    public static class Machine extends StateMachine<SubMachine, Event, String, String>
        implements IManageStates<SubMachine, Event, String, String>{
        public Machine(MachineBuilder builder){
            super(builder);
        }

        public IMonitorStateChanges<SubMachine, Event, String, String> onEvent(Event event, String eventContext){
            IMonitorStateChanges<State, Event, String, String> internal = this.getCurrentState().onEvent(event, eventContext);
            return this.handleEvent(event, eventContext);
        }
    }

    public static class SubMachineBuilder
            extends AbstractStateMachineBuilder<State, Event, String, String, SubMachine>{
        @Override
        protected SubMachine createInstance(){
            return new SubMachine(this);
        }
    }

    public static class MachineBuilder extends AbstractStateMachineBuilder<SubMachine, Event, String, String, Machine>{
        public static MachineBuilder builder(){
            return new MachineBuilder();
        }

        @Override
        protected Machine createInstance(){
            return new Machine(this);
        }
    }

    @Test
    public void createBuilderWithStatesAndEvents(){
        StateMachineBuilder<State, Event, String, String> builder = new StateMachineBuilder<>();
                builder.addTransition(State.STATE_1, State.STATE_2, Event.EVENT_1)
                .addTransition(State.STATE_2, State.STATE_3, Event.EVENT_2)
                .addTransition(State.STATE_3, State.STATE_1, Event.EVENT_3)
                .setContext("TEST")
                .setInitialState(State.STATE_1);

        StateMachine<State, Event, String, String> machine = builder.build();
        assertEquals("Initial state doesn't match", State.STATE_1, machine.getCurrentState());

        IMonitorStateChanges<State, Event, String, String> next  = machine.onEvent(Event.EVENT_1);
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

        IMonitorStateChanges<State, Event, String, String> next  = machine.onEvent(Event.EVENT_1);
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
                new StateMachineBuilder<State, Event, String, String>()
                        .addTransition(State.STATE_1, State.STATE_2, Event.EVENT_1)
                        .addTransition(State.STATE_2, State.STATE_3, Event.EVENT_2)
                        .addTransition(State.STATE_3, State.STATE_1, Event.EVENT_3)
                        .setContext("TEST")
                        .setInitialState(State.STATE_1)
                        .build();

        StateMachine<State, Event, String, String> subMachine2 =
                new StateMachineBuilder<State, Event, String, String>()
                        .addTransition(State.STATE_1, State.STATE_3, Event.EVENT_3)
                        .setContext("TEST")
                        .setInitialState(State.STATE_1)
                        .build();

        StateMachine<State, Event, String, String> subMachine3 =
                new StateMachineBuilder<State, Event, String, String>()
                        .addTransition(State.STATE_3, State.STATE_2, Event.EVENT_1)
                        .setContext("TEST")
                        .setInitialState(State.STATE_3)
                        .build();

        StateMachine<StateMachine<State, Event, String, String>, Event, String, String> machine =
                new StateMachineBuilder<StateMachine<State, Event, String, String>, Event, String, String>()
                        .addTransition(subMachine1, subMachine2, Event.EVENT_4)
                        .addTransition(subMachine2, subMachine3, Event.EVENT_5)
                        .addTransition(subMachine3, subMachine1, Event.EVENT_6)
                        .setInitialState(subMachine1)
                        .build();

        assertEquals("Initial state doesn't match", subMachine1, machine.getCurrentState());
        IMonitorStateChanges<StateMachine<State, Event, String, String>, Event, String, String> next  = machine.onEvent(Event.EVENT_1);
        assertEquals("Progression 1 doesn't match", subMachine1, next.getToState());
        assertEquals("Progression 1 doesn't match", subMachine1, machine.getCurrentState());
        assertEquals("Progression 1 doesn't match", State.STATE_1, machine.getCurrentState().getCurrentState());

        next  = machine.onEvent(Event.EVENT_1);
        assertEquals("Progression 1 doesn't match", subMachine1, next.getToState());
        assertEquals("Progression 1 doesn't match", subMachine1, machine.getCurrentState());
        assertEquals("Progression 1 doesn't match", State.STATE_1, machine.getCurrentState().getCurrentState());

        next = machine.onEvent(Event.EVENT_2);
        assertEquals("Progression 1 doesn't match", subMachine1, next.getToState());
        assertEquals("Progression 1 doesn't match", subMachine1, machine.getCurrentState());
        assertEquals("Progression 1 doesn't match", State.STATE_1, machine.getCurrentState().getCurrentState());

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
        assertEquals("Progression 2 doesn't match", State.STATE_3, machine.getCurrentState().getCurrentState());

        next = machine.onEvent(Event.EVENT_6);
        assertEquals("Progression 3 doesn't match", subMachine1, next.getToState());
        assertEquals("Progression 3 doesn't match", subMachine1, machine.getCurrentState());
        assertEquals("Progression 2 doesn't match", State.STATE_1, machine.getCurrentState().getCurrentState());
    }

    @Test
    public void MachineTransitionChecker(){
        SubMachine subMachine1 =
                new SubMachineBuilder()
                        .addTransition(State.STATE_1, State.STATE_2, Event.EVENT_1, (c) -> true, (c) -> c)
                        .addTransition(State.STATE_2, State.STATE_3, Event.EVENT_2, (c) -> false, (c) -> c)
                        .addTransition(State.STATE_3, State.STATE_1, Event.EVENT_3, (c) -> false, (c) -> c)
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
                        .addTransition(subMachine1, subMachine2, Event.EVENT_4, (c) -> false, (c) -> c)
                        .addTransition(subMachine2, subMachine3, Event.EVENT_5, (c) -> GateChecker.checkGate(true, true, false), (c) -> c)
                        .addTransition(subMachine3, subMachine1, Event.EVENT_6)
                        .setInitialState(subMachine1)
                        .build();

        TransitionsCheckAndActions<SubMachine, Event, String, String> transitions = machine.getTransitionsForState(subMachine1);
        for(Transition<SubMachine, Event> t: transitions.getTransitions()){
            assertEquals(transitions.getCheckAndAction(t).getChecker().check(null), false);
        }
        TransitionsCheckAndActions<State, Event, String, String> subTransitions = machine.getCurrentState().getTransitionsForState(State.STATE_1);
        for(Transition<State, Event> t: subTransitions.getTransitions()){
            assertEquals(subTransitions.getCheckAndAction(t).getChecker().check(null), true);
        }
        TransitionsCheckAndActions<SubMachine, Event, String, String> transitions2 = machine.getTransitionsForState(subMachine2);
        for(Transition<SubMachine, Event> t: transitions2.getTransitions()){
            assertEquals(transitions2.getCheckAndAction(t).getChecker().check(null), false);
        }
    }
}

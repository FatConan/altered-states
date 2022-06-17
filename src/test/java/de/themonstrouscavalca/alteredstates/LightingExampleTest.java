package de.themonstrouscavalca.alteredstates;

import de.themonstrouscavalca.alteredstates.abs.AbstractStateMachineBuilder;
import de.themonstrouscavalca.alteredstates.helpers.StateCollection;
import de.themonstrouscavalca.alteredstates.impl.StateMachine;
import de.themonstrouscavalca.alteredstates.interfaces.IManageStates;
import de.themonstrouscavalca.alteredstates.interfaces.INameEvents;
import de.themonstrouscavalca.alteredstates.interfaces.INameStates;
import de.themonstrouscavalca.alteredstates.transitions.InternalTransition;
import de.themonstrouscavalca.alteredstates.transitions.Transition;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.*;

/* A more "real world" example of what can be done with the state machines */
public class LightingExampleTest{
    private static class LightBulb{
        private String color;
        private int strobeFrequency;
        private boolean on;

        public String getColor(){
            return color;
        }

        public void setColor(String color){
            this.color = color;
        }

        public int getStrobeFrequency(){
            return strobeFrequency;
        }

        public void setStrobeFrequency(int strobeFrequency){
            this.strobeFrequency = strobeFrequency;
        }

        public boolean isOn(){
            return on;
        }

        public void setOn(boolean on){
            this.on = on;
        }
    }

    private enum LightingState implements INameStates{
        SOLID,
        BLINKING,
        STROBING,
        OFF;

        @Override
        public String getName(){
            return getName();
        }
    }

    private enum LightingEvent implements INameEvents{
        SOLID,
        BLINK,
        STROBE,
        OFF,
        CHANGE_COLOR,
        CHANGE_FREQUENCY;

        @Override
        public String getName(){
            return getName();
        }
    }

    private static class LightBulbSettings{
        private String color;
        private int frequency = -1;

        public static LightBulbSettings color(String color){
            LightBulbSettings setting = new LightBulbSettings();
            setting.color = color;
            return setting;
        }

        public static LightBulbSettings frequency(int frequency){
            LightBulbSettings setting = new LightBulbSettings();
            setting.frequency = frequency;
            return setting;
        }

        public String getColor(){
            return color;
        }

        public int getFrequency(){
            return frequency;
        }
    }

    private static class BulbMachine extends StateMachine<LightingState, LightingEvent, LightBulb, LightBulbSettings>
            implements IManageStates<LightingState, LightingEvent, LightBulb, LightBulbSettings>{
        private static class BulbMachineBuilder
                extends AbstractStateMachineBuilder<LightingState, LightingEvent, LightBulb, LightBulbSettings, BulbMachine>{
            @Override
            protected BulbMachine createInstance(){
                return new BulbMachine(this);
            }
        }

        public BulbMachine(BulbMachineBuilder builder){
            super(builder);
        }
    }

    private BulbMachine bulbMachine(LightBulb lightBulb){
        BulbMachine.BulbMachineBuilder bulbBuilder = new BulbMachine.BulbMachineBuilder();
        return bulbBuilder
                .addTransition(StateCollection.not(LightingState.SOLID), LightingState.SOLID, LightingEvent.SOLID,
                        "Illuminate Solid",
                        (contextHolder) -> true,
                        (contextHolder) -> {
                            LightBulb b = contextHolder.getContext();
                            b.setOn(true);
                            b.setStrobeFrequency(0);
                            return contextHolder;
                        })
                .addTransition(StateCollection.not(LightingState.BLINKING), LightingState.BLINKING, LightingEvent.BLINK,
                        "Illuminate Blinking",
                        (contextHolder) -> true,
                        (contextHolder) -> {
                            LightBulb b = contextHolder.getContext();
                            b.setOn(true);
                            b.setStrobeFrequency(20);
                            return contextHolder;
                        })
                .addTransition(StateCollection.not(LightingState.STROBING), LightingState.STROBING, LightingEvent.STROBE,
                        "Illuminate Strobing",
                        (contextHolder) -> true,
                        (contextHolder) -> {
                            LightBulb b = contextHolder.getContext();
                            b.setOn(true);
                            b.setStrobeFrequency(100);
                            return contextHolder;
                        })
                .addTransition(StateCollection.not(LightingState.OFF), LightingState.OFF, LightingEvent.OFF,
                        "Delluminate",
                        (contextHolder) -> true,
                        (contextHolder) -> {
                            LightBulb b = contextHolder.getContext();
                            b.setOn(false);
                            return contextHolder;
                        })
                .addInternalTransition(StateCollection.wildcard(), LightingEvent.CHANGE_COLOR,
                        "Change Color",
                        (contextHolder) -> true,
                        (contextHolder) -> {
                            LightBulb b = contextHolder.getContext();
                            b.setColor(contextHolder.getEventContext().getColor());
                            return contextHolder;
                        })
                .addInternalTransition(StateCollection.of(LightingState.BLINKING, LightingState.STROBING), LightingEvent.CHANGE_FREQUENCY,
                        "Change Frequency",
                        (contextHolder) -> true,
                        (contextHolder) -> {
                            LightBulb b = contextHolder.getContext();
                            b.setStrobeFrequency(contextHolder.getEventContext().getFrequency());
                            return contextHolder;
                        })
                .setContext(lightBulb)
                .setInitialState(LightingState.OFF)
                .build();
    }

    @Test
    public void lightingExample(){
        LightBulb bulb1 = new LightBulb();
        BulbMachine bulbC1Controller = this.bulbMachine(bulb1);
        bulbC1Controller.onEvent(LightingEvent.SOLID);
        bulbC1Controller.onEvent(LightingEvent.CHANGE_COLOR, LightBulbSettings.color("red"));
        bulbC1Controller.onEvent(LightingEvent.CHANGE_FREQUENCY, LightBulbSettings.frequency(1000));

        LightBulb bulb2 = new LightBulb();
        BulbMachine bulbC2Controller = this.bulbMachine(bulb2);
        bulbC2Controller.onEvent(LightingEvent.BLINK);
        bulbC2Controller.onEvent(LightingEvent.CHANGE_COLOR, LightBulbSettings.color("green"));
        bulbC2Controller.onEvent(LightingEvent.CHANGE_FREQUENCY, LightBulbSettings.frequency(500));

        LightBulb bulb3 = new LightBulb();
        BulbMachine bulbC3Controller = this.bulbMachine(bulb3);
        bulbC3Controller.onEvent(LightingEvent.STROBE);
        bulbC3Controller.onEvent(LightingEvent.CHANGE_COLOR, LightBulbSettings.color("yellow"));
        bulbC3Controller.onEvent(LightingEvent.CHANGE_FREQUENCY, LightBulbSettings.frequency(250));
        bulbC3Controller.onEvent(LightingEvent.OFF);

        LightBulb bulb4 = new LightBulb();
        BulbMachine bulbC4Controller = this.bulbMachine(bulb4);
        //From OFF we should allow "Illuminate Solid", "Illuminate Blinking", "Illuminate Strobing", "Change Color"
        assertEquals("Number of available external transitions doesn't match", 3, bulbC4Controller.getAvailableTransitions().size());
        assertEquals("Number of available internal transitions doesn't match", 1, bulbC4Controller.getAvailableInternalTransitions().size());


        Set<String> transitionNames = new HashSet<>(Arrays.asList("Illuminate Solid", "Illuminate Blinking", "Illuminate Strobing"));
        for(Transition<LightingState, LightingEvent, LightBulb, LightBulbSettings> t: bulbC4Controller.getAvailableTransitions()){
            assertTrue("Available transitions don't match", transitionNames.contains(t.getLabel()));
        }
        Set<String> internalTransitionNames = new HashSet<>(Collections.singletonList("Change Color"));
        for(InternalTransition<LightingState, LightingEvent> t: bulbC4Controller.getAvailableInternalTransitions()){
            assertTrue("Available internal transitions don't match", internalTransitionNames.contains(t.getLabel()));
        }

        assertTrue("Bulb1 is not on", bulb1.isOn());
        assertEquals("bulb1 frequency is set", 0, bulb1.getStrobeFrequency());
        assertEquals("bulb1 color isn't set", "red", bulb1.getColor());
        assertEquals("Isn't set to SOLID", bulbC1Controller.getCurrentState(), LightingState.SOLID);

        assertTrue("Bulb2 is not on", bulb2.isOn());
        assertEquals("bulb2 frequency is not set", 500, bulb2.getStrobeFrequency());
        assertEquals("bulb2 color isn't set", "green", bulb2.getColor());
        assertEquals("Isn't set to BLINK", bulbC2Controller.getCurrentState(), LightingState.BLINKING);

        assertFalse("Bulb3 is on", bulb3.isOn());
        assertEquals("bulb1 frequency is set", 250, bulb3.getStrobeFrequency());
        assertEquals("bulb1 color isn't set", "yellow", bulb3.getColor());
        assertEquals("Isn't set to SOLID", bulbC3Controller.getCurrentState(), LightingState.OFF);
    }
}

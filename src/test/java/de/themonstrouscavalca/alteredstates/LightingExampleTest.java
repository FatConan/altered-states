package de.themonstrouscavalca.alteredstates;

import de.themonstrouscavalca.alteredstates.abs.AbstractStateMachineBuilder;
import de.themonstrouscavalca.alteredstates.helpers.StateCollection;
import de.themonstrouscavalca.alteredstates.impl.StateMachine;
import de.themonstrouscavalca.alteredstates.interfaces.IManageStates;
import de.themonstrouscavalca.alteredstates.interfaces.INameEvents;
import de.themonstrouscavalca.alteredstates.interfaces.INameStates;
import org.junit.Test;

import static junit.framework.TestCase.*;

public class LightingExampleTest{
    public static class LightBulb{
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

    public enum LightingState implements INameStates{
        SOLID,
        BLINKING,
        STROBING,
        OFF;

        @Override
        public String getName(){
            return getName();
        }
    }

    public enum LightingEvent implements INameEvents{
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

    public static class LightBulbSettings{
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

    public static class BulbMachineBuilder
            extends AbstractStateMachineBuilder<LightingState, LightingEvent, LightBulb, LightBulbSettings, BulbMachine>{
        @Override
        protected BulbMachine createInstance(){
            return new BulbMachine(this);
        }
    }

    public static class BulbMachine extends StateMachine<LightingState, LightingEvent, LightBulb, LightBulbSettings>
            implements IManageStates<LightingState, LightingEvent, LightBulb, LightBulbSettings>{
        public BulbMachine(BulbMachineBuilder builder){
            super(builder);
        }
    }

    private BulbMachine bulbMachine(LightBulb lightBulb){
        BulbMachineBuilder bulbBuilder = new BulbMachineBuilder();
        return bulbBuilder
                .addTransition(StateCollection.not(LightingState.SOLID), LightingState.SOLID, LightingEvent.SOLID,
                        (contextHolder) -> true,
                        (contextHolder) -> {
                            LightBulb b = contextHolder.getContext();
                            b.setOn(true);
                            b.setStrobeFrequency(0);
                            return contextHolder;
                        })
                .addTransition(StateCollection.not(LightingState.BLINKING), LightingState.BLINKING, LightingEvent.BLINK,
                        (contextHolder) -> true,
                        (contextHolder) -> {
                            LightBulb b = contextHolder.getContext();
                            b.setOn(true);
                            b.setStrobeFrequency(20);
                            return contextHolder;
                        })
                .addTransition(StateCollection.not(LightingState.STROBING), LightingState.STROBING, LightingEvent.STROBE,
                        (contextHolder) -> true,
                        (contextHolder) -> {
                            LightBulb b = contextHolder.getContext();
                            b.setOn(true);
                            b.setStrobeFrequency(100);
                            return contextHolder;
                        })
                .addTransition(StateCollection.not(LightingState.OFF), LightingState.OFF, LightingEvent.OFF,
                        (contextHolder) -> true,
                        (contextHolder) -> {
                            LightBulb b = contextHolder.getContext();
                            b.setOn(false);
                            return contextHolder;
                        })
                .addInternalTransition(StateCollection.wildcard(), LightingEvent.CHANGE_COLOR,
                        (contextHolder) -> true,
                        (contextHolder) -> {
                            LightBulb b = contextHolder.getContext();
                            b.setColor(contextHolder.getEventContext().getColor());
                            return contextHolder;
                        })
                .addInternalTransition(StateCollection.of(LightingState.BLINKING, LightingState.STROBING), LightingEvent.CHANGE_FREQUENCY,
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

package de.themonstrouscavalca.alteredstates;

import de.themonstrouscavalca.alteredstates.abs.AbstractStateMachineBuilder;
import de.themonstrouscavalca.alteredstates.impl.StateMachine;
import de.themonstrouscavalca.alteredstates.interfaces.*;
import de.themonstrouscavalca.alteredstates.transitions.StateChange;

public class DynamicStatesTest{
    private enum StageStatus implements INameStates{
        VOID,
        UNSTARTED,
        IN_PROGRESS,
        COMPLETE;

        @Override
        public String getName(){
            return name();
        }
    }

    private enum ProcessEvent implements INameEvents{
        START_DOCUMENT_REVIEW,
        START_REQUIREMENTS_CREATION,
        START_REQUIREMENTS_REVIEW,
        START_REQUIREMENT_DOCUMENT,
        ADD_FEEDBACK,
        COMPLETE_FEEDBACK,
        UPDATE,
        CLOSE;

        @Override
        public String getName(){
            return null;
        }
    }

    private static class StageNode extends StateMachine<StageStatus, ProcessEvent, Process, ProcessDetails>
            implements IManageStates<StageStatus, ProcessEvent, Process, ProcessDetails>{

        private static class StageNodeBuilder
                extends AbstractStateMachineBuilder<StageStatus, ProcessEvent, Process, ProcessDetails, StageNode>{

            @Override
            protected StageNode createInstance(){
                return new StageNode(this);
            }
        }

        public StageNode(IExpressStateMachines<StageStatus, ProcessEvent, Process, ProcessDetails> builder){
            super(builder);
        }

        public static StageNode VOID = new StageNodeBuilder().setInitialState(StageStatus.VOID).build();
    }

    private static class Process{
    }

    private static class ProcessDetails{
    }

    private static class ProcessNodes extends StateMachine<StageNode, ProcessEvent, Process, ProcessDetails>
            implements IManageStates<StageNode, ProcessEvent, Process, ProcessDetails>{

        private static class ProcessNodeBuilder
                extends AbstractStateMachineBuilder<StageNode, ProcessEvent, Process, ProcessDetails, ProcessNodes>{

            @Override
            protected ProcessNodes createInstance(){
                return new ProcessNodes(this);
            }
        }

        public ProcessNodes(IExpressStateMachines<StageNode, ProcessEvent, Process, ProcessDetails> builder){
            super(builder);
        }

        @Override
        public IMonitorStateChanges<StageNode, ProcessEvent, Process, ProcessDetails> onEvent(ProcessEvent event, ProcessDetails eventContext){
            IMonitorStateChanges<StageStatus, ProcessEvent, Process, ProcessDetails>
                    stageChange = this.getCurrentState().onEvent(event, eventContext);

            //If there was an action transition that should have happened...
            if(stageChange.transitionFound() && stageChange.transitionPermitted()){
                //...but failed to do so...
                if(!stageChange.transitionSuccessful()){
                    //...then return a failed internal state change
                    return new StateChange<StageNode, ProcessEvent, Process, ProcessDetails>(this.getCurrentState(), this.getCurrentState(), event, stageChange.getContextHolder(),
                            false, false, false,
                            true, true, false);
                }
            }

            //Otherwise accept the changes to context and then proceed to try the process transitions
            IMonitorStateChanges<StageNode, ProcessEvent, Process, ProcessDetails>
                    processChange = this.handleEvent(event, eventContext);

            if(stageChange.transitionFound() && stageChange.transitionPermitted() && stageChange.transitionSuccessful()){
                return new StateChange<>(this.getCurrentState(), this.getCurrentState(), event, processChange.getContextHolder(),
                        processChange.transitionFound(), processChange.transitionPermitted(), processChange.transitionSuccessful(),
                        true, true, true);

            }

            return processChange;
        }
    }

    private StageNode stage(Process process, String feedbackStart, String feedbackContinue, String feedbackEnd){
        return new StageNode.StageNodeBuilder()
                .setInitialState(StageStatus.UNSTARTED)
                .setContext(process)
                .addTransition(StageStatus.UNSTARTED, StageStatus.IN_PROGRESS,
                        ProcessEvent.ADD_FEEDBACK,
                        feedbackStart)
                .addInternalTransition(StageStatus.IN_PROGRESS, ProcessEvent.ADD_FEEDBACK, feedbackContinue)
                .addTransition(StageStatus.IN_PROGRESS, StageStatus.COMPLETE,
                        ProcessEvent.COMPLETE_FEEDBACK,
                        feedbackEnd)
                .build();
    }

    private StageNode DOCUMENT_REVIEW(Process process){
        return stage(process, "Begin the initial review", "Contribute to the the review", "Complete the review");
    }
    private StageNode REQUIREMENTS_CREATION(Process process){
        return stage(process, "Create the requirements", "Contribute to the requirements", "Complete the requirement");
    }
    private StageNode REQUIREMENTS_REVIEW(Process process){
        return stage(process, "Create the requirements review", "Contribute to the review", "Complete the review");
    }
    private StageNode REQUIREMENT_DOCUMENT(Process process){
        return stage(process, "Create the final requirements document", "Contribute to the document", "Complete the document");
    }


    /*private ProcessNodes process(Process process){
        ProcessNodes.ProcessNodeBuilder builder = new ProcessNodes.ProcessNodeBuilder();
        return builder
                .setContext(process)
                .setInitialState(StageNode.VOID)
                .addTransition(StageNode.VOID, DOCUMENT_REVIEW(process),
                        ProcessEvent.START_DOCUMENT_REVIEW, )
                .build();

    }*/

}
package de.rwth.i2.attestor.phases.symbolicExecution.utilStrategies;

import de.rwth.i2.attestor.stateSpaceGeneration.PostProcessingStrategy;
import de.rwth.i2.attestor.stateSpaceGeneration.ProgramState;
import de.rwth.i2.attestor.stateSpaceGeneration.StateCanonicalizationStrategy;
import de.rwth.i2.attestor.stateSpaceGeneration.StateSpace;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class AggressivePostProcessingStrategy implements PostProcessingStrategy {

    private StateCanonicalizationStrategy canonicalizationStrategy;
    private int minAbstractionDistance;

    public AggressivePostProcessingStrategy(StateCanonicalizationStrategy canonicalizationStrategy,
                                            int minAbstractionDistance) {

        this.canonicalizationStrategy = canonicalizationStrategy;
        this.minAbstractionDistance = minAbstractionDistance;
    }

    @Override
    public void process(StateSpace stateSpace) {

        if (minAbstractionDistance == 0) {
            return;
        }

        Set<ProgramState> finalStates = stateSpace.getFinalStates();

        if (finalStates.size() == 1) {
            return;
        }

        Map<ProgramState, ProgramState> abstractedStates = new LinkedHashMap<>();
        Map<Integer, Integer> idMap = new LinkedHashMap<>();

        for (ProgramState state : finalStates) {
            ProgramState absState = canonicalizationStrategy.canonicalize(state);
            absState.setStateSpaceId(state.getStateSpaceId());
            ProgramState oldState = abstractedStates.put(absState, absState);
            if (oldState != null) {
                idMap.put(state.getStateSpaceId(), oldState.getStateSpaceId());
            } else {
                idMap.put(state.getStateSpaceId(), absState.getStateSpaceId());
            }
        }

        if (abstractedStates.size() < finalStates.size()) {
            stateSpace.updateFinalStates(abstractedStates.keySet(), idMap);
        }
    }
}

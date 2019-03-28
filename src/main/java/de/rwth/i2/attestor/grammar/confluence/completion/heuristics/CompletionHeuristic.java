package de.rwth.i2.attestor.grammar.confluence.completion.heuristics;

import de.rwth.i2.attestor.grammar.confluence.completion.CompletionState;

/**
 * Interface for all completion heuristics. A completion heuristic returns all possible ways the heuristic can be applied
 * in the current state.
 * A heuristic tries to modify the grammar so it is "closer" to being confluent.
 */
public interface CompletionHeuristic {

    /**
     * @param state The state on which the heuristic should be applied
     * @return A collection of all possible immediate successors the the input state according to the heuristic
     */
    Iterable<CompletionState> applyHeuristic(CompletionState state);
}

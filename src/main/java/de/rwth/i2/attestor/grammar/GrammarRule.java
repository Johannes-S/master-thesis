package de.rwth.i2.attestor.grammar;

import de.rwth.i2.attestor.graph.Nonterminal;
import de.rwth.i2.attestor.graph.SelectorLabel;
import de.rwth.i2.attestor.graph.heap.HeapConfiguration;

import java.util.Collection;

public interface GrammarRule {
    Nonterminal getNonterminal();

    HeapConfiguration getHeapConfiguration();

    /**
     * Can be called for collapsed and non collapsed rules. For non collapsed rules, the morphism in the collapsed heap configuration is null.
     */
    CollapsedHeapConfiguration getCollapsedHeapConfiguration();

    String getGrammarName();

    String getRuleIdentifier();

    int getOriginalRuleIdx();

    default Collection<SelectorLabel> getLocalOutgoingSelectorLabels(int tentacle) {
        HeapConfiguration hc = getCollapsedHeapConfiguration().getOriginal();
        return hc.selectorLabelsOf(hc.externalNodeAt(tentacle));
    }

}

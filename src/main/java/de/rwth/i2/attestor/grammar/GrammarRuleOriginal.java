package de.rwth.i2.attestor.grammar;

import de.rwth.i2.attestor.graph.Nonterminal;
import de.rwth.i2.attestor.graph.heap.HeapConfiguration;

import java.util.*;

public class GrammarRuleOriginal implements GrammarRule {
    private final String grammarName;
    private final int originalRuleIdx;
    private final Nonterminal nonterminal;
    private final HeapConfiguration hc;
    private final List<GrammarRuleCollapsed> collapsedRules;

    /**
     * Creates a new rule with the status CONFLUENCE_GENERATED
     */
    public GrammarRuleOriginal(String grammarName, Nonterminal nonterminal, HeapConfiguration hc, int originalRuleIdx) {
        this.grammarName = grammarName;
        this.originalRuleIdx = originalRuleIdx;
        this.nonterminal = nonterminal;
        this.hc = hc;
        this.collapsedRules = Collections.emptyList();
    }

    GrammarRuleOriginal(String grammarName, int originalRuleIdx, Nonterminal nonterminal, HeapConfiguration hc, List<GrammarRuleCollapsed> collapsedRules) {
        this.grammarName = grammarName;
        this.originalRuleIdx = originalRuleIdx;
        this.nonterminal = nonterminal;
        this.hc = hc;
        this.collapsedRules = collapsedRules;
    }

    public List<GrammarRuleCollapsed> getCollapsedRules() {
        return Collections.unmodifiableList(collapsedRules);
    }

    @Override
    public int getOriginalRuleIdx() {
        return originalRuleIdx;
    }

    @Override
    public Nonterminal getNonterminal() {
        return nonterminal;
    }

    @Override
    public HeapConfiguration getHeapConfiguration() {
        return hc;
    }

    @Override
    public CollapsedHeapConfiguration getCollapsedHeapConfiguration() {
        return new CollapsedHeapConfiguration(hc, hc, null);
    }

    @Override
    public String getRuleIdentifier() {
        return Integer.toString(originalRuleIdx + 1);
    }

    @Override
    public String getGrammarName() {
        return grammarName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(grammarName, originalRuleIdx, -1);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof GrammarRuleOriginal) {
            GrammarRuleOriginal otherRule = (GrammarRuleOriginal) o;
            return getGrammarName() == otherRule.getGrammarName()
                    && getOriginalRuleIdx() == otherRule.getOriginalRuleIdx();
        } else {
            return false;
        }
    }
}

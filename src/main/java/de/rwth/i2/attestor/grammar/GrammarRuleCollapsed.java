package de.rwth.i2.attestor.grammar;

import de.rwth.i2.attestor.graph.Nonterminal;
import de.rwth.i2.attestor.graph.heap.HeapConfiguration;

import java.util.Objects;

public class GrammarRuleCollapsed implements GrammarRule {
    private final GrammarRuleOriginal originalRule;  // Note: this is does not have to be same rule object that contains this rule
    private final CollapsedHeapConfiguration collapsedHeapConfiguration;
    private final int collapsedRuleIdx;

    public GrammarRuleCollapsed(GrammarRuleOriginal originalRule, int collapsedRuleIdx, CollapsedHeapConfiguration cHC) {
        this.originalRule = originalRule;
        this.collapsedHeapConfiguration = cHC;
        this.collapsedRuleIdx = collapsedRuleIdx;
    }

    @Override
    public String getRuleIdentifier() {
        return originalRule.getRuleIdentifier() + "." + Integer.toString(getCollapsedRuleIdx() + 1);
    }

    @Override
    public Nonterminal getNonterminal() {
        return originalRule.getNonterminal();
    }

    @Override
    public HeapConfiguration getHeapConfiguration() {
        return collapsedHeapConfiguration.getCollapsed();
    }

    @Override
    public CollapsedHeapConfiguration getCollapsedHeapConfiguration() {
        return collapsedHeapConfiguration;
    }

    @Override
    public String getGrammarName() {
        return originalRule.getGrammarName();
    }

    @Override
    public int getOriginalRuleIdx() {
        return originalRule.getOriginalRuleIdx();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGrammarName(), getOriginalRuleIdx(), getCollapsedRuleIdx());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof GrammarRuleCollapsed) {
            GrammarRuleCollapsed otherRule = (GrammarRuleCollapsed) o;
            return getGrammarName() == otherRule.getGrammarName()
                    && getOriginalRuleIdx() == otherRule.getOriginalRuleIdx()
                    && getCollapsedRuleIdx() == otherRule.getCollapsedRuleIdx();
        } else {
            return false;
        }
    }

    public int getCollapsedRuleIdx() {
        return collapsedRuleIdx;
    }
}

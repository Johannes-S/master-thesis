package de.rwth.i2.attestor.phases.confluenceCheck;

import de.rwth.i2.attestor.grammar.Grammar;
import de.rwth.i2.attestor.grammar.NamedGrammar;
import de.rwth.i2.attestor.grammar.confluence.CriticalPair;
import de.rwth.i2.attestor.grammar.confluence.CriticalPairFinder;
import de.rwth.i2.attestor.main.AbstractPhase;
import de.rwth.i2.attestor.main.scene.Scene;
import de.rwth.i2.attestor.phases.transformers.GrammarTransformer;

import java.io.IOException;

public class ConfluenceCheckPhase extends AbstractPhase {

    private boolean confluenceCheckEnabled = false;
    private int numberWeaklyJoinable = 0;
    private int numberStronglyJoinable = 0;
    private int numberNotJoinable = 0;

    public ConfluenceCheckPhase(Scene scene) {
        super(scene);
    }

    @Override
    public String getName() {
        return "Check confluence";
    }

    @Override
    public void executePhase() throws IOException {
        confluenceCheckEnabled = scene().options().isConfluenceCheckEnabled();
        if (confluenceCheckEnabled) {
            Grammar grammar = getPhase(GrammarTransformer.class).getGrammar();
            NamedGrammar namedGrammar = new NamedGrammar(grammar, null);
            CriticalPairFinder criticalPairFinder = new CriticalPairFinder(namedGrammar);
            numberStronglyJoinable = 0;
            numberWeaklyJoinable = 0;
            numberNotJoinable = 0;
            for (CriticalPair criticalPair : criticalPairFinder.getCriticalPairs()) {
                switch (criticalPair.getJoinability()) {
                    case NOT_JOINABLE:
                        numberNotJoinable++;
                        break;
                    case WEAKLY_JOINABLE:
                        numberWeaklyJoinable++;
                        break;
                    case STRONGLY_JOINABLE:
                        numberStronglyJoinable++;
                        break;
                }
            }
        }

    }

    @Override
    public void logSummary() {
        if (confluenceCheckEnabled) {
            logSum("Number critical pairs " + (numberNotJoinable + numberStronglyJoinable + numberWeaklyJoinable) +
                    "(" + numberStronglyJoinable + " strongly joinable, " +
                    numberWeaklyJoinable + " weakly joinable, " +
                    numberNotJoinable + " not joinable)");
            if (numberNotJoinable > 0 || numberWeaklyJoinable > 0) {
                logHighlight("The grammar is not confluent");
            } else {
                logHighlight("The grammar is confluent");
            }
        } else {
            logSum("Confluence check is disabled");
        }
    }

    @Override
    public boolean isVerificationPhase() {
        return false;
    }
}

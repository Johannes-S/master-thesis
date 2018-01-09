package de.rwth.i2.attestor.main;

import de.rwth.i2.attestor.LTLFormula;
import de.rwth.i2.attestor.main.phases.transformers.CounterexampleTransformer;
import de.rwth.i2.attestor.main.phases.transformers.ModelCheckingResultsTransformer;
import de.rwth.i2.attestor.main.phases.transformers.StateSpaceTransformer;
import de.rwth.i2.attestor.main.scene.DefaultScene;
import de.rwth.i2.attestor.stateSpaceGeneration.ProgramState;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public abstract class AbstractAttestor {

    protected  static final Logger logger = LogManager.getLogger("Attestor");

    protected final Properties properties = new Properties();
    protected PhaseRegistry registry;
    protected DefaultScene scene = new DefaultScene();

    /**
     * Runs attestor to perform a program analysis.
     *
     * @param args The command line arguments determining communication and analysis customizations.
     * @see <a href="https://github.com/moves-rwth/attestor/wiki/Command-Line-Options">

     * Explanation of all command line options
     * </a>
     */
    public void run(String[] args) {

        printVersion();
        registry = new PhaseRegistry();
        registerPhases(args);
        registry.logExecutionSummary();
        registry.logExecutionTimes();
    }

    protected abstract void registerPhases(String[] args);

    public long getTotalNumberOfStates() {

        return scene.getNumberOfGeneratedStates();
    }

    public int getNumberOfStatesWithoutProcedureCalls() {

        return registry.getMostRecentPhase(StateSpaceTransformer.class)
                .getStateSpace()
                .getStates()
                .size();
    }

    public int getNumberOfFinalStates() {

        return registry.getMostRecentPhase(StateSpaceTransformer.class)
                .getStateSpace()
                .getFinalStates()
                .size();
    }

    public Map<LTLFormula, ProgramState> getCounterexamples() {
        CounterexampleTransformer transformer = registry.getMostRecentPhase(CounterexampleTransformer.class);
        HashMap<LTLFormula, ProgramState> result = new HashMap<>();
        for(LTLFormula formula : transformer.getFormulasWithCounterexamples()) {
            result.put(formula, transformer.getInputOf(formula));
        }
        return result;
    }

    public boolean hasAllLTLSatisfied() {
        return registry.getMostRecentPhase(ModelCheckingResultsTransformer.class).hasAllLTLSatisfied();
    }

    private void printVersion() {

        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream("attestor.properties"));
            logger.log(Level.getLevel("VERSION"), properties.getProperty("artifactId")
                    + " - version " + properties.getProperty("version"));
        } catch (IOException e) {
            logger.fatal("Project version could not be found. Aborting.");
            System.exit(1);
        }
    }
}

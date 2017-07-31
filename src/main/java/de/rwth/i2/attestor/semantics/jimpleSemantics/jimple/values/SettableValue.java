package de.rwth.i2.attestor.semantics.jimpleSemantics.jimple.values;

import de.rwth.i2.attestor.semantics.jimpleSemantics.JimpleExecutable;
import de.rwth.i2.attestor.util.NotSufficientlyMaterializedException;

/**
 * SettableValues are Values to which something can be assigned, i.e. that can
 * appear as the left-hand-side of an assign Stmt, e.g. Locals, Fields etc.
 * 
 * on the other hand ConcreteValues, UndefinedValues, NewExpr etc are examples
 * for Values that are not Settable
 * 
 * @author hannah
 *
 */
public interface SettableValue extends Value {

	/**
	 * Sets a value to a given value that is evaluated in a given executable.
	 * @param executable The executable used to evaluate the given value.
	 * @param concreteRHS The value this value should be set to.
	 * 
	 * @throws NotSufficientlyMaterializedException if the evaluation of originValue
	 * tries to access an abstracted selector
	 * @throws NullPointerDereferenceException if the evaluation of originValue
	 * results in a null pointer derefereniation
	 */
	void setValue(JimpleExecutable executable, ConcreteValue concreteRHS)
			throws NotSufficientlyMaterializedException, NullPointerDereferenceException;

}
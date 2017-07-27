package de.rwth.i2.attestor.grammar.canoncalization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.rwth.i2.attestor.grammar.StackMatcher;
import de.rwth.i2.attestor.grammar.canonicalization.EmbeddingStackChecker;
import de.rwth.i2.attestor.grammar.materialization.StackMaterializer;
import de.rwth.i2.attestor.graph.Nonterminal;
import de.rwth.i2.attestor.graph.SelectorLabel;
import de.rwth.i2.attestor.graph.heap.HeapConfiguration;
import de.rwth.i2.attestor.graph.heap.Matching;
import de.rwth.i2.attestor.graph.heap.internal.InternalHeapConfiguration;
import de.rwth.i2.attestor.graph.heap.matching.EmbeddingChecker;
import de.rwth.i2.attestor.indexedGrammars.BalancedTreeGrammar;
import de.rwth.i2.attestor.indexedGrammars.IndexedNonterminal;
import de.rwth.i2.attestor.indexedGrammars.stack.*;
import de.rwth.i2.attestor.tasks.GeneralSelectorLabel;
import de.rwth.i2.attestor.types.Type;
import gnu.trove.list.array.TIntArrayList;

public class EmbeddingStackCheckerTest {

	EmbeddingStackChecker checker;
	
	@Before
	public void setUp() throws Exception {
		StackMatcher stackMatcher = new StackMatcher( new DefaultStackMaterialization() );
		StackMaterializer stackMaterializer = new StackMaterializer();
		 checker = new EmbeddingStackChecker( stackMatcher, stackMaterializer );
	}

	/**
	 * This test uses graphs without any nonterminals as inputs,
	 * i.e. it doesn't test any logic.
	 */
	@Test
	public void testSimple() {
		HeapConfiguration toAbstract = getSimpleInput();
		HeapConfiguration pattern = getSimpleInput();
		Nonterminal lhs = getInstantiableNonterminal();
		Matching embedding = new EmbeddingChecker( pattern, toAbstract ).getNext();
		
		StackEmbeddingResult res = checker.getStackEmbeddingResult( toAbstract, embedding, lhs );
		
		assertTrue( res.canMatch() );
		assertEquals( getSimpleInput(), res.getMaterializedToAbstract() );
		assertEquals( getInstantiableNonterminal(), res.getInstantiatedLhs() );
	}
	
	/**
	 * This tests verifies that the graphs are not modified, if the stacks match directly
	 */
	@Test
	public void testWithIdenticalStacks(){
		List<StackSymbol> concreteStack = getConcreteStack();
		HeapConfiguration toAbstract = getInputWithStack( concreteStack );
		HeapConfiguration pattern = getInputWithStack( concreteStack );
		Nonterminal lhs = getMatchingNonterminalWithStack( concreteStack );
		Matching embedding = new EmbeddingChecker( pattern, toAbstract ).getNext();
		
		StackEmbeddingResult res = checker.getStackEmbeddingResult( toAbstract, embedding, lhs );
		
		assertTrue( res.canMatch() );
		assertEquals( getInputWithStack( concreteStack ), res.getMaterializedToAbstract() );
		assertEquals( getMatchingNonterminalWithStack( concreteStack), res.getInstantiatedLhs() );
		
	}
	
	/**
	 * This test verifies that materialization is applied correctly in a simple case
	 * (No instantiation, no different abstract symbols)
	 */
	@Test
	public void testOnlyMaterialization(){
		List<StackSymbol> somePrefix = getStackPrefix();
		List<StackSymbol> otherPrefix = getOtherStackPrefix();
		
		List<StackSymbol> toMatch = makeAbstract( somePrefix);
		List<StackSymbol> reference = makeAbstract( otherPrefix );
		HeapConfiguration toAbstract = getInputWithStacks( toMatch, reference );
		
		List<StackSymbol> concreteStack = makeConcrete( somePrefix );		
		HeapConfiguration pattern = getInputWithStack( concreteStack );
		Nonterminal lhs = getMatchingNonterminalWithStack( concreteStack );
		Matching embedding = new EmbeddingChecker(pattern, toAbstract).getNext();
		
		StackEmbeddingResult res = checker.getStackEmbeddingResult( toAbstract, embedding, lhs );
		
		assertTrue( res.canMatch() );
		assertEquals( getInputWithStacks( makeConcrete( somePrefix ), makeConcrete( otherPrefix) ), 
					  res.getMaterializedToAbstract() );
		assertEquals( getMatchingNonterminalWithStack(concreteStack), res.getInstantiatedLhs() );
	}
	
	/**
	 * still requires no instantiation, but uses embeds two nonterminals
	 * which have different abstract symbols (ensures that they are materialized
	 * independently)
	 */
	@Test
	public void testMaterializationWithDifferentAbstractSymbols(){
		List<StackSymbol> somePrefix = getStackPrefix();
		List<StackSymbol> otherPrefix = getOtherStackPrefix();
		
		List<StackSymbol> toMatch1 = makeAbstract( somePrefix);
		List<StackSymbol> reference1 = makeAbstract( otherPrefix );
		
		List<StackSymbol> toMatch2 = makeOtherAbstract( somePrefix );
		List<StackSymbol> reference2 = makeOtherAbstract( otherPrefix );
		HeapConfiguration toAbstract = getInputWithStacks( toMatch1, toMatch2, 
															reference1, reference2 );
		
		List<StackSymbol> concreteStack1 = makeConcrete( somePrefix );
		List<StackSymbol> concreteStack2 = makeOtherConcrete( somePrefix );
		HeapConfiguration pattern = getPatternWithStacks( concreteStack1, concreteStack2 );
		Nonterminal lhs = getMatchingNonterminalWithStack( concreteStack1 );
		Matching embedding = new EmbeddingChecker( pattern, toAbstract ).getNext();
		
		StackEmbeddingResult res = checker.getStackEmbeddingResult( toAbstract, embedding, lhs );
		
		assertTrue( res.canMatch() );
		assertEquals( getInputWithStacks( makeConcrete( somePrefix ), makeOtherConcrete( somePrefix ),
										  makeConcrete( otherPrefix), makeOtherConcrete( otherPrefix ) ), 
					  res.getMaterializedToAbstract() );
		assertEquals( getMatchingNonterminalWithStack(concreteStack1), res.getInstantiatedLhs() );
	}


	private List<StackSymbol> getStackPrefix() {
		StackSymbol s = DefaultStackMaterialization.SYMBOL_s;
		ArrayList<StackSymbol> prefix = new ArrayList<>();
		prefix.add( s );
		prefix.add( s );
		return prefix;
	}
	
	private List<StackSymbol> getOtherStackPrefix() {
		StackSymbol s = DefaultStackMaterialization.SYMBOL_s;
		StackSymbol other = ConcreteStackSymbol.getStackSymbol("other", false);
		
		ArrayList<StackSymbol> prefix = new ArrayList<>();
		prefix.add( s );
		prefix.add( other );
		return prefix;
	}
	
	private List<StackSymbol> makeConcrete( List<StackSymbol> prefix ) {
		StackSymbol bottom = DefaultStackMaterialization.SYMBOL_Z;
		return addSymbol( prefix,bottom );
	}
	
	private List<StackSymbol> makeOtherConcrete(List<StackSymbol>  prefix) {
		StackSymbol bottom = DefaultStackMaterialization.SYMBOL_C;
		return addSymbol( prefix,bottom );
	}
	
	private List<StackSymbol> makeAbstract(List<StackSymbol> prefix) {
		AbstractStackSymbol abs = DefaultStackMaterialization.SYMBOL_X;
		return addSymbol(prefix, abs);
	}
	
	private List<StackSymbol> makeOtherAbstract(List<StackSymbol> prefix) {
		AbstractStackSymbol abs = DefaultStackMaterialization.SYMBOL_Y;
		return addSymbol(prefix, abs);
	}

	private List<StackSymbol> addSymbol(List<StackSymbol> prefix, StackSymbol abs) {
		ArrayList<StackSymbol> stack = new ArrayList<>( prefix );
		stack.add( abs );
		return stack;
	}

	private List<StackSymbol> getConcreteStack() {
		List<StackSymbol> stack = getStackPrefix();
		return makeConcrete(stack);
	}

	private Nonterminal getInstantiableNonterminal() {
		List<StackSymbol> stack = getStackWithStackVariable();
		return getMatchingNonterminalWithStack(stack);
	}

	private Nonterminal getMatchingNonterminalWithStack(List<StackSymbol> stack) {
		String label = "matching_EmbeddingStackChecker";
		int rank = 2;
		boolean[] isReductionTentacle = new boolean [rank];
		IndexedNonterminal nt = new IndexedNonterminal(label,rank,isReductionTentacle,stack);
		return nt;
	}
	
	private Nonterminal getOtherMatchingNonterminalWithStack(List<StackSymbol> stack) {
		String label = "matching2_EmbeddingStackChecker";
		int rank = 2;
		boolean[] isReductionTentacle = new boolean [rank];
		IndexedNonterminal nt = new IndexedNonterminal(label,rank,isReductionTentacle,stack);
		return nt;
	}
	
	private Nonterminal getReferenceNonterminalWithStack(List<StackSymbol> stack) {
		String label = "reference_EmbeddingStackChecker";
		int rank = 2;
		boolean[] isReductionTentacle = new boolean [rank];
		IndexedNonterminal nt = new IndexedNonterminal(label,rank,isReductionTentacle,stack);
		return nt;
	}

	private List<StackSymbol> getStackWithStackVariable() {
		List<StackSymbol> stack = new ArrayList<>();
		stack.add( StackVariable.getGlobalInstance() );
		return stack;
	}

	private HeapConfiguration getSimpleInput() {
		HeapConfiguration hc = new InternalHeapConfiguration();
		
		Type type = BalancedTreeGrammar.TYPE;
		SelectorLabel label = GeneralSelectorLabel.getSelectorLabel("label");

	
		TIntArrayList nodes = new TIntArrayList();
		return hc.builder().addNodes(type, 2, nodes)
				.addSelector(nodes.get(0), label, nodes.get(1))
				.build();
	}
	
	private HeapConfiguration getInputWithStack(List<StackSymbol> stack) {
		HeapConfiguration hc = new InternalHeapConfiguration();
		
		Type type = BalancedTreeGrammar.TYPE;
		SelectorLabel label = GeneralSelectorLabel.getSelectorLabel("label");
		
		Nonterminal nt = getMatchingNonterminalWithStack( stack );

	
		TIntArrayList nodes = new TIntArrayList();
		return hc.builder().addNodes(type, 2, nodes)
				.setExternal(nodes.get(0))
				.setExternal(nodes.get(1))
				.addSelector(nodes.get(0), label, nodes.get(1))
				.addNonterminalEdge(nt)
					.addTentacle( nodes.get(0) )
					.addTentacle( nodes.get(1) )
					.build()
				.build();
	}
	
	private HeapConfiguration getInputWithStacks(List<StackSymbol> toMatch, List<StackSymbol> reference) {
		HeapConfiguration hc = new InternalHeapConfiguration();
		
		Type type = BalancedTreeGrammar.TYPE;
		SelectorLabel label = GeneralSelectorLabel.getSelectorLabel("label");
		
		Nonterminal matchingNt = getMatchingNonterminalWithStack( toMatch );
		Nonterminal referenceNt = getReferenceNonterminalWithStack( reference );

	
		TIntArrayList nodes = new TIntArrayList();
		return hc.builder().addNodes(type, 4, nodes)
				.addSelector(nodes.get(0), label, nodes.get(1))
				.addNonterminalEdge( matchingNt )
					.addTentacle( nodes.get(0) )
					.addTentacle( nodes.get(1) )
					.build()
				.addNonterminalEdge(referenceNt)
					.addTentacle( nodes.get(2) )
					.addTentacle( nodes.get(3) )
					.build()
				.build();
	}
	

	private HeapConfiguration getInputWithStacks(
			List<StackSymbol> toMatch1, List<StackSymbol> toMatch2,
			List<StackSymbol> reference1, List<StackSymbol> reference2) {
		HeapConfiguration hc = new InternalHeapConfiguration();
		
		Type type = BalancedTreeGrammar.TYPE;
		SelectorLabel label = GeneralSelectorLabel.getSelectorLabel("label");
		
		Nonterminal matchingNt1 = getMatchingNonterminalWithStack( toMatch1 );
		Nonterminal referenceNt1 = getReferenceNonterminalWithStack( reference1 );
		
		Nonterminal matchingNt2 = getOtherMatchingNonterminalWithStack( toMatch2 );
		Nonterminal referenceNt2 = getReferenceNonterminalWithStack( reference2 );

	
		TIntArrayList nodes = new TIntArrayList();
		return hc.builder().addNodes(type, 4, nodes)
				.addSelector(nodes.get(0), label, nodes.get(1))
				.addNonterminalEdge( matchingNt1 )
					.addTentacle( nodes.get(0) )
					.addTentacle( nodes.get(1) )
					.build()
				.addNonterminalEdge( matchingNt2 )
					.addTentacle( nodes.get(0) )
					.addTentacle( nodes.get(1) )
					.build()
				.addNonterminalEdge(referenceNt1)
					.addTentacle( nodes.get(2) )
					.addTentacle( nodes.get(3) )
					.build()
				.addNonterminalEdge(referenceNt2)
					.addTentacle( nodes.get(2) )
					.addTentacle( nodes.get(3) )
					.build()
				.build();
	}

	private HeapConfiguration getPatternWithStacks(
			List<StackSymbol> stack1, List<StackSymbol> stack2) 
	{
		
		HeapConfiguration hc = new InternalHeapConfiguration();
		
		Type type = BalancedTreeGrammar.TYPE;
		SelectorLabel label = GeneralSelectorLabel.getSelectorLabel("label");
		
		Nonterminal matchingNt1 = getMatchingNonterminalWithStack( stack1 );
		
		Nonterminal matchingNt2 = getOtherMatchingNonterminalWithStack( stack2 );
		
		TIntArrayList nodes = new TIntArrayList();
		return hc.builder().addNodes(type, 2, nodes)
				.setExternal( nodes.get(0) )
				.setExternal( nodes.get(1) )
				.addSelector(nodes.get(0), label, nodes.get(1))
				.addNonterminalEdge( matchingNt1 )
					.addTentacle( nodes.get(0) )
					.addTentacle( nodes.get(1) )
					.build()
				.addNonterminalEdge( matchingNt2 )
					.addTentacle( nodes.get(0) )
					.addTentacle( nodes.get(1) )
					.build()
				.build();
	}


}

package de.rwth.i2.attestor.grammar.testUtil;

import de.rwth.i2.attestor.grammar.materialization.GeneralMaterializationStrategyTest_getActualViolationPoint;
import de.rwth.i2.attestor.grammar.materialization.ViolationPointResolverTest_Default;
import de.rwth.i2.attestor.graph.SelectorLabel;
import de.rwth.i2.attestor.graph.heap.HeapConfiguration;
import de.rwth.i2.attestor.graph.heap.internal.InternalHeapConfiguration;
import de.rwth.i2.attestor.strategies.indexedGrammarStrategies.AnnotatedSelectorLabel;
import de.rwth.i2.attestor.graph.GeneralNonterminal;
import de.rwth.i2.attestor.graph.GeneralSelectorLabel;
import de.rwth.i2.attestor.types.Type;
import de.rwth.i2.attestor.types.TypeFactory;
import gnu.trove.list.array.TIntArrayList;

public class TestGraphs {
	
	public static HeapConfiguration getInput_getActualViolationPoints_Default(){
		HeapConfiguration hc = new InternalHeapConfiguration();
		
		Type type = TypeFactory.getInstance().getType("type");
		GeneralSelectorLabel sel = GeneralSelectorLabel.getSelectorLabel(
				GeneralMaterializationStrategyTest_getActualViolationPoint.DEFAULT_SELECTOR);
		
		TIntArrayList nodes = new TIntArrayList();
		return hc.builder().addNodes(type, 2, nodes)
			.addVariableEdge(GeneralMaterializationStrategyTest_getActualViolationPoint.DEFAULT_VARIABLE, nodes.get(0) )
			.addSelector(nodes.get(0), sel, nodes.get(1) )
			.build();
	}
	
	public static HeapConfiguration getInput_getActualViolationPoints_Indexed(){
		HeapConfiguration hc =  new InternalHeapConfiguration();
		
		Type type = TypeFactory.getInstance().getType("type");
		AnnotatedSelectorLabel annotatedSel = new AnnotatedSelectorLabel(
				GeneralMaterializationStrategyTest_getActualViolationPoint.ANNOTATED_SELECTOR, 
				GeneralMaterializationStrategyTest_getActualViolationPoint.ANNOTATION );
		
		TIntArrayList nodes = new TIntArrayList();
		return hc.builder().addNodes(type, 2, nodes)
			.addVariableEdge(GeneralMaterializationStrategyTest_getActualViolationPoint.ANNOTATED_VARIABLE, nodes.get(0))
			.addSelector(nodes.get(0), annotatedSel, nodes.get(1) )
			.build();
	}

	public static HeapConfiguration getInput_getActualViolationPoints_Mixed(){
		HeapConfiguration hc =  new InternalHeapConfiguration();
		
		Type type = TypeFactory.getInstance().getType("type");
		AnnotatedSelectorLabel annotatedSel = new AnnotatedSelectorLabel(
				GeneralMaterializationStrategyTest_getActualViolationPoint.ANNOTATED_SELECTOR, 
				GeneralMaterializationStrategyTest_getActualViolationPoint.ANNOTATION );
		GeneralSelectorLabel defaultSel = GeneralSelectorLabel.getSelectorLabel(
				GeneralMaterializationStrategyTest_getActualViolationPoint.DEFAULT_SELECTOR );
		
		TIntArrayList nodes = new TIntArrayList();
		return hc.builder().addNodes(type, 2, nodes)
			.addVariableEdge(
					GeneralMaterializationStrategyTest_getActualViolationPoint.ANNOTATED_VARIABLE, 
					GeneralMaterializationStrategyTest_getActualViolationPoint.NODE_FOR_ANNOTATED_VARIABLE
					)	
			.addSelector(
					GeneralMaterializationStrategyTest_getActualViolationPoint.NODE_FOR_ANNOTATED_VARIABLE,
					annotatedSel,
					nodes.get(1)
					)
			.addVariableEdge(
					GeneralMaterializationStrategyTest_getActualViolationPoint.DEFAULT_VARIABLE,
					GeneralMaterializationStrategyTest_getActualViolationPoint.NODE_FOR_DEFAULT_VARIABLE)
			.addSelector(GeneralMaterializationStrategyTest_getActualViolationPoint.NODE_FOR_DEFAULT_VARIABLE, 
					defaultSel,
					nodes.get(0)
					)
			.build();
	}
	
	public static HeapConfiguration getRuleGraph_CreatingNextAt0_PrevAt1(){
		HeapConfiguration hc = new InternalHeapConfiguration();
		Type nodeType = TypeFactory.getInstance().getType("type");
		
		SelectorLabel next = GeneralSelectorLabel.getSelectorLabel( ViolationPointResolverTest_Default.SELECTOR_NAME_NEXT );
		SelectorLabel prev = GeneralSelectorLabel.getSelectorLabel( ViolationPointResolverTest_Default.SELECTOR_NAME_PREV );
		
		TIntArrayList nodes = new TIntArrayList();
		return hc.builder()
				.addNodes( nodeType, 2, nodes )
				.setExternal( nodes.get(0) )
				.setExternal( nodes.get(1) )
				.addSelector( nodes.get( ViolationPointResolverTest_Default.TENTACLE_FOR_NEXT ), next, nodes.get(1) )
				.addSelector( nodes.get( ViolationPointResolverTest_Default.TENTACLE_FOR_PREV ), prev, nodes.get(0) )
				.build();
	}
	
	public static HeapConfiguration getRuleGraph_CreatingNext(){
		HeapConfiguration hc = new InternalHeapConfiguration();
		Type nodeType = TypeFactory.getInstance().getType("type");
		
		SelectorLabel next = GeneralSelectorLabel.getSelectorLabel( ViolationPointResolverTest_Default.SELECTOR_NAME_NEXT );
		SelectorLabel prev = GeneralSelectorLabel.getSelectorLabel( ViolationPointResolverTest_Default.SELECTOR_NAME_PREV );
		
		GeneralNonterminal nonterminal = ViolationPointResolverTest_Default.DEFAULT_NONTERMINAL;
		
		TIntArrayList nodes = new TIntArrayList();
		return hc.builder()
				.addNodes( nodeType, 3, nodes )
				.setExternal( nodes.get(0) )
				.setExternal( nodes.get(1) )
				.addSelector( nodes.get( ViolationPointResolverTest_Default.TENTACLE_FOR_NEXT), next, nodes.get(2) )
				.addSelector( nodes.get(2), prev, nodes.get(0) )
				.addNonterminalEdge( nonterminal )
					.addTentacle( nodes.get(2) )
					.addTentacle( nodes.get(1) )
					.build()
				.build();
	}
	
	public static HeapConfiguration getRuleGraph_creatingPrevAt1(){
		HeapConfiguration hc = new InternalHeapConfiguration();
		Type nodeType = TypeFactory.getInstance().getType("type");
		
		SelectorLabel next = GeneralSelectorLabel.getSelectorLabel( ViolationPointResolverTest_Default.SELECTOR_NAME_NEXT );
		SelectorLabel prev = GeneralSelectorLabel.getSelectorLabel( ViolationPointResolverTest_Default.SELECTOR_NAME_PREV );
		
		GeneralNonterminal nonterminal = ViolationPointResolverTest_Default.DEFAULT_NONTERMINAL;
		
		TIntArrayList nodes = new TIntArrayList();
		return hc.builder()
				.addNodes( nodeType, 3, nodes )
				.setExternal( nodes.get(0) )
				.setExternal( nodes.get(1) )
				.addSelector( nodes.get(2), next, nodes.get(1) )
				.addSelector( nodes.get( ViolationPointResolverTest_Default.TENTACLE_FOR_PREV), prev, nodes.get(2) )
				.addNonterminalEdge( nonterminal )
					.addTentacle( nodes.get(0) )
					.addTentacle( nodes.get(2) )
					.build()
				.build();
	}
	
	public static HeapConfiguration getRuleGraph_creatingNoSelector(){
		HeapConfiguration hc = new InternalHeapConfiguration();
		Type nodeType = TypeFactory.getInstance().getType("type");
		
		GeneralNonterminal nonterminal = ViolationPointResolverTest_Default.DEFAULT_NONTERMINAL;
		
		TIntArrayList nodes = new TIntArrayList();
		return hc.builder()
				.addNodes( nodeType, 3, nodes )
				.setExternal( nodes.get(0) )
				.setExternal( nodes.get(2) )
				.addNonterminalEdge(nonterminal)
					.addTentacle( nodes.get(0))
					.addTentacle( nodes.get(1) )
					.build()
				.addNonterminalEdge( nonterminal )
					.addTentacle( nodes.get(1) )
					.addTentacle( nodes.get(2) )
					.build()
				.build();
	}

}

package generator;

import java.util.ArrayList;
import java.util.List;

public class ProcessNode {
	private List<ProcessNode> children = new ArrayList<ProcessNode>();
	private ProcessNode defaultChild = null;
	private List<Double> probabilities = new ArrayList<Double>();
	private String name;
	
	
	public ProcessNode(String name) {
		if(name == null || name.length() == 0) {
			throw new IllegalArgumentException("Process nodes must have a name.");
		}
		this.name = name;
	}

	public List<ProcessNode> getChildren() {
		List<ProcessNode> cl = new ArrayList<ProcessNode>(children);
		cl.add(defaultChild);
		return cl;
	}
	
	public List<ProcessNode> getChildrenWithoutDefaultChild() {
		return children;
	}

	public boolean isDefaultChild(ProcessNode node) {
		return node != null && node == defaultChild;
	}

	public String getName() {
		return name;
	}
	
	/**
	 * A child node with a certain probability on its edge.
	 * @param node
	 * @param probability
	 */
	public void addChild(ProcessNode node, Double probability) {
		if(node == null) {
			throw new NullPointerException("Process node children must not be null.");
		}
		if(probability != null && (probability.doubleValue() < 0 || probability.doubleValue() > 1)) {
			throw new IllegalArgumentException("Process node transition probabilities must be between 0 and 1.");
		}
		children.add(node);
		probabilities.add(probability);
	}
	
	/**
	 * A child node without a probability, that is, the default choice iff the sum of probabilities
	 * from all children are less than 1. Otherwise, ignored.
	 * @param node
	 */
	public void setDefaultChild(ProcessNode node) {
		defaultChild = node;
	}
	
	public Double getProbability(ProcessNode childNode) {
		return getProbability(children.indexOf(childNode));
	}
	
	public Double getProbability(int indexOfChildNode) {
		return probabilities.get(indexOfChildNode);
	}

	public boolean hasChildren() {
		return defaultChild != null || !children.isEmpty();
	}

	@Override
	public String toString() {
		return name;
	}

	public ProcessNode getDefaultChild() {
		return defaultChild;
	}

	public void printTree() {
		printTree(this);
	}
	
	private void printTree(ProcessNode node) {
		if(node != null) {
			System.out.println(node.toString());
			for(ProcessNode child: node.getChildren()) {
				printTree(child);
			}
		}
	}

}

package generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * All events from a certain phase as directed graph.
 */
public class RandomProcess {
	private ProcessNode root;
	private String processName;
	private List<ProcessNode> randomProcessPath;
	
	public RandomProcess(String processName) {
		if(processName == null || processName.length() == 0) {
			throw new IllegalArgumentException("No process name given.");
		}
		
		this.processName = processName;
		this.root = new ProcessNode(processName);
	}
	
	public ProcessNode getRoot() {
		return root;
	}

	public String getProcessName() {
		return processName;
	}

	public List<ProcessNode> getRandomProcessPath() throws Exception {
		randomProcessPath = new ArrayList<ProcessNode>();
		
		Random random = new Random();
		double prob = 0;
		double probSum = 0;
		boolean found = false;
		ProcessNode node = root;
		
		while(node != null && node.hasChildren()) {
			prob = random.nextDouble();
			probSum = 0;
			found = false;
			for(ProcessNode childNode: node.getChildrenWithoutDefaultChild()) {
				
				double currentEdgeProb = node.getProbability(childNode);
				if(currentEdgeProb + probSum >= prob) {
					randomProcessPath.add(childNode);
					node = childNode;
					found = true;
					break;
				}
				
				probSum += node.getProbability(childNode).doubleValue();
				if(probSum > 1.0) {
					throw new Exception("Overall probability higher than 1.0!");
				}
			}
			
			if(!found) {
				if(node.getDefaultChild() != null) {
					randomProcessPath.add(node.getDefaultChild());
					node = node.getDefaultChild();
				} else {
					node = null;
				}
			}
			
		}

		return randomProcessPath;
	}
	
}

package sk.lkrnac.discorg.model.interfaces;

/**
 * Represents actual status of the node in storage tree
 * @author sitko
 *
 */
public interface INodeStatus {
	/**
	 * @return priority belonging to this status   
	 * <br>Is used for populating status to parent nodes 
	 */
	public int getSeverity();
	
	/**
	 * @return get icon name belonging to this status
	 */
	public String getIconName();
}

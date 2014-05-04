import java.util.ArrayList;

public class TDeployLine {

	public String ResourceLabel;
	public String ResourcePath;
	public ArrayList<TDeployItem> DeployItems;

	public TDeployLine(Integer DeployOrgsCount) {
		ResourceLabel = "";
		ResourcePath = "";
		DeployItems = new ArrayList<TDeployItem>();
		for (int i = 0; i < DeployOrgsCount; i++) {
			TDeployItem deployItem = new TDeployItem();
			DeployItems.add(deployItem);
		}
	}

}

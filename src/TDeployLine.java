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

	public String getMetaXmlPath() {
		String metaxmlPath = null;
		if (ResourcePath.indexOf("classes\\") != -1 || ResourcePath.indexOf("components\\") != -1 || ResourcePath.indexOf("pages\\") != -1 || ResourcePath.indexOf("staticresources\\") != -1 || ResourcePath.indexOf("triggers\\") != -1) {
			if (ResourcePath.indexOf("-meta.xml") == -1) {
				metaxmlPath = ResourcePath + "-meta.xml";
			}
		}
		return metaxmlPath;
	}

}

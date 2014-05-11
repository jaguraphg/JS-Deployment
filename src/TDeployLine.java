import java.util.ArrayList;

public class TDeployLine {

	private String FILE_SEPARATOR;
	public String ResourceLabel;
	public String ResourcePath;
	public ArrayList<TDeployItem> DeployItems;

	public TDeployLine(Integer DeployOrgsCount) {
		FILE_SEPARATOR = System.getProperty("file.separator");
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
		if (ResourcePath.indexOf("classes"+FILE_SEPARATOR) != -1 || ResourcePath.indexOf("components"+FILE_SEPARATOR) != -1 || ResourcePath.indexOf("pages"+FILE_SEPARATOR) != -1 || ResourcePath.indexOf("staticresources"+FILE_SEPARATOR) != -1 || ResourcePath.indexOf("triggers"+FILE_SEPARATOR) != -1) {
			if (ResourcePath.indexOf("-meta.xml") == -1) {
				metaxmlPath = ResourcePath + "-meta.xml";
			}
		}
		return metaxmlPath;
	}

}

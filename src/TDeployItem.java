import java.util.ArrayList;

public class TDeployItem {

	public Boolean Matched;
	public Boolean IsFileExists;
	public Boolean ForceAction;
	public String FullFilePath;
	public ArrayList<String> FileContent;

	public TDeployItem() {
		Matched = false;
		IsFileExists = false;
		ForceAction = false;
		FullFilePath = "";
		FileContent = new ArrayList<String>();
	}

	public Boolean getIsIncludedToDeploy() {
		return (
			(Matched == true && ForceAction == false) ||
			(Matched == false && ForceAction == true)
		);
	}

}

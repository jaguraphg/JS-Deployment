import java.util.ArrayList;

public class TDeployItem {

	public Boolean Matched;
	public Boolean IsFileExists;
	public Boolean ForceAction;
	public String FullFilePath;
	public String FullMetaXmlPath;
	public TStringList FileTextContent;
	public TStringList FileMetaXmlContent;

	private static String FILE_SEPARATOR;

	public TDeployItem() {
		FILE_SEPARATOR = System.getProperty("file.separator");
		Matched = false;
		IsFileExists = false;
		ForceAction = false;
		FullFilePath = "";
		FullMetaXmlPath = "";
		FileTextContent = new TStringList();
		FileMetaXmlContent = new TStringList();
	}

	public void loadContent() {
		if (FullFilePath != null) {
			if (FullFilePath.contains("src"+FILE_SEPARATOR+"classes"+FILE_SEPARATOR) || FullFilePath.contains("src"+FILE_SEPARATOR+"components"+FILE_SEPARATOR) || FullFilePath.contains("src"+FILE_SEPARATOR+"pages"+FILE_SEPARATOR) || FullFilePath.contains("src"+FILE_SEPARATOR+"triggers"+FILE_SEPARATOR)) {
//				Load Text Content
				FileTextContent.LoadFromFile(FullFilePath);
			}
			if (FullFilePath.contains("src"+FILE_SEPARATOR+"labels"+FILE_SEPARATOR) || FullFilePath.contains("src"+FILE_SEPARATOR+"objects"+FILE_SEPARATOR) || FullFilePath.contains("src"+FILE_SEPARATOR+"pages"+FILE_SEPARATOR) || FullFilePath.contains("src"+FILE_SEPARATOR+"triggers"+FILE_SEPARATOR)) {
//				Load Text Content (metadata)
				FileTextContent.LoadFromFile(FullFilePath);
			}
			if (FullFilePath.contains("src"+FILE_SEPARATOR+"staticresources"+FILE_SEPARATOR)) {
//				Load Binary Content
			}
		}
		if (FullMetaXmlPath != null) {
			FileMetaXmlContent.LoadFromFile(FullMetaXmlPath);
			// Exclude '<packageVersions>' tags from '-meta.xml' (FileContentPreprocessing)
			ArrayList<String> content = FileMetaXmlContent.getStrings();
			TStringList post = new TStringList();
			for (Integer i = 0; i < content.size(); i++) {
				if (content.get(i).indexOf("<packageVersions>") == 0) {
					post.add(content.get(i));
				}
				else {
					do {
						i++;
					}
					while (i < content.size() && content.get(i).indexOf("</packageVersions>") == 0);
				}
			}
			FileMetaXmlContent = post;
		}
	}

	public Boolean compareTo(TDeployItem CompareItem) {
		Boolean b1 = false;
		Boolean b2 = false;
		if (FullFilePath.contains("staticresources"+FILE_SEPARATOR)) {
//			b1 = CompareContentBinary(pOrgFile->FileBinaryContent, pOrgFileCore->FileBinaryContent);
//			b2 = CompareContentString(pOrgFile->FileMetaXmlContent, pOrgFileCore->FileMetaXmlContent);
		}
		else {
			if (FileTextContent != null) {
				b1 = FileTextContent.compareTo(CompareItem.FileTextContent);
			}
			if (FileMetaXmlContent != null) {
				b2 = FileMetaXmlContent.compareTo(CompareItem.FileMetaXmlContent);
			}
		}
		return (b1 && b2);
	}

	public Boolean getIsIncludedToDeploy() {
		return (
			(Matched == true && ForceAction == false) ||
			(Matched == false && ForceAction == true)
		);
	}

}

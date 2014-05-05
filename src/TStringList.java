import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashSet;

public class TStringList {

	public Boolean AllowDuplicates;
	public Boolean Sorted;

	private ArrayList<String> pStrings;
	public ArrayList<String> getStrings() {
		ArrayList<String> result = AllowDuplicates ? pStrings : new ArrayList<String>(new HashSet<String>(pStrings));
		if (Sorted == false) {
			return result;
		}
		Collections.sort(result);
		return result;
	}
	public void setStrings(ArrayList<String> s) {
		pStrings = s;
	}

	public TStringList() {
		pStrings = new ArrayList<String>();
		AllowDuplicates = true;
		Sorted = false;
	}

	public Boolean add(String s) {
		return pStrings.add(s);
	}

	public void LoadFromFile(String filePath) {
		File f = new File(filePath);
		try {
			pStrings = new ArrayList<String>(Files.readAllLines(f.toPath(), Charset.forName("UTF-8")));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void SaveToFile(String filePath) {

	}

}

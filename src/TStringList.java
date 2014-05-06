import java.io.File;
import java.io.FileWriter;
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
		try {
			File f = new File(filePath);
			pStrings = new ArrayList<String>(Files.readAllLines(f.toPath(), Charset.forName("UTF-8")));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void SaveToFile(String filePath) {
		try {
			String newLine = System.getProperty("line.separator");
			FileWriter f0 = new FileWriter(filePath);
			for (Integer i = 0; i < pStrings.size(); i++) {
				f0.append(pStrings.get(i) + newLine);
			}
			f0.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}

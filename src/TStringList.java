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

	public Boolean compareTo(TStringList s) {
		if (s == null) {
			return false;
		}
		ArrayList<String> lines1 = getStrings();
		ArrayList<String> lines2 = s.getStrings();
		if (lines1.size() != lines2.size()) {
			return false;
		}
		Integer diffs = 0;
		for (Integer i = 0; i < lines1.size(); i++) {
			if (lines1.get(i).equals(lines2.get(i)) == false) {
				diffs++;
				do {
					i++;
				}
				while (i < lines1.size() && lines1.get(i).equals(lines2.get(i)) == false);
			}
		}
		return (diffs == 0);
	}

	public void LoadFromFile(String filePath) {
		try {
//			System.out.println(filePath);
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

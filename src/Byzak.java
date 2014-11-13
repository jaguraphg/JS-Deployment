import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;

import java.beans.Beans;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Properties;
import java.util.ArrayList;

public class Byzak extends JFrame {

	private JPanel contentPane;
	private JTextArea textAreaInput;
	private JTable tableDeployPlan;
	private JButton btnBuildPlan;
	private JButton btnDeployAll;
	private DefaultTableModel tableDeployPlanModel;
	private JPopupMenu popupMenu;

	private static String WORKSPACE_BASE_PATH;
	private static String ANT_SPACE_BASE_PATH;
	private static String DEPLOY_DEV_ORG_DIR;
	private static String DEPLOY_COMMAND;

	private static String FILE_SEPARATOR;

	private ArrayList<TDeployLine> DeployLines;
	private ArrayList<TDeployOrganization> DeployOrganizations;

	private Integer PopupMenuFileIndex, PopupMenuOrgIndex;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Byzak frame = new Byzak();
					frame.setVisible(true);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// Constructor
	public Byzak() {
/*
		String[] commands = {"cmd", "/C", "start", "d:\\Dropbox\\Force.com\\ant\\JS-Recruiting\\dipa-ORG.bat", "deploy-JS-Recruiting-CORE"};
		try {
			ProcessBuilder builder = new ProcessBuilder(commands);
			builder.start();
//			Runtime.getRuntime().exec(command);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
*/
		DeployLines = new ArrayList<TDeployLine>();
		DeployOrganizations = new ArrayList<TDeployOrganization>();
		DeployOrganizations.add(new TDeployOrganization("P:4.356", "zRecruiting-Patch-4.356"));
		DeployOrganizations.add(new TDeployOrganization("P:4.400", "zRecruiting-Patch-4.400"));
		DeployOrganizations.add(new TDeployOrganization("CORE", "JS-Recruiting-CORE"));

		FILE_SEPARATOR = System.getProperty("file.separator");

		Properties props = new Properties();
		File propsFile = new File("config.ini");
		try {
			props.load(new FileInputStream(propsFile));
			String os = System.getProperty("os.name").toLowerCase();
			String osPrefix = (
				(os.indexOf( "win" ) >= 0) ? "WIN_" :
				(os.indexOf( "mac" ) >= 0) ? "MAC_" :
				(os.indexOf( "nix" ) >= 0) ? "NIX_" :
				""
			);
			WORKSPACE_BASE_PATH = props.getProperty(osPrefix + "WORKSPACE_BASE_PATH").trim().replace("~", System.getProperty("user.home"));
			ANT_SPACE_BASE_PATH = props.getProperty(osPrefix + "ANT_SPACE_BASE_PATH").trim().replace("~", System.getProperty("user.home"));
			DEPLOY_DEV_ORG_DIR = props.getProperty(osPrefix + "DEPLOY_DEV_ORG_DIR").trim();
			DEPLOY_COMMAND = props.getProperty(osPrefix + "DEPLOY_COMMAND").trim();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Boolean isApplicationConfigured = Beans.isDesignTime() || (
			WORKSPACE_BASE_PATH != null &&
			ANT_SPACE_BASE_PATH != null &&
			DEPLOY_DEV_ORG_DIR != null &&
			DEPLOY_COMMAND != null
		);

		setTitle("Bysh");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 875, 406);

		String TextAreaInitialContent = "";
		String TextAreaInitialContentDefault = "U /Dev-Base/src/classes/candidateBulkAction.cls\r\nU /Dev-Base/src/classes/candidateBulkAction.cls-meta.xml\r\nU /Dev-Base/src/classes/s_CTagController.cls\r\nU /Dev-Base/src/classes/s_CTagController.cls-meta.xml\r\nU /Dev-Base/src/components/s_Result.component\r\n\r\nU /Dev-Base/src/labels/CustomLabels.labels\r\nU /Dev-Base/src/objects/Contact.object\r\n\r\nU /Dev-Base/src/pages/AddTagsCandidate.page\r\nU /Dev-Base/src/pages/AddTagsCandidate.page-meta.xml\r\nU /Dev-Base/src/pages/StoryBoardReplica.page\r\nU /Dev-Base/src/pages/s_CTag.page-meta.xml\r\nU /Dev-Base/src/staticresources/SSearch.resource\r\n";
		try {
			String ClipboardString = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
			TextAreaInitialContent = (ClipboardString.contains("/Dev-Base/src/")) ? ClipboardString : TextAreaInitialContentDefault;
		}
		catch (Exception e) {
			TextAreaInitialContent = TextAreaInitialContentDefault;
		}

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		if (isApplicationConfigured == false) {
			JLabel lblApplicationNotConfigured = new JLabel("Application is not configured!");
			contentPane.add(lblApplicationNotConfigured);
			return;
		}

		tableDeployPlanModel = new DefaultTableModel(0, DeployOrganizations.size() + 1);
		tableDeployPlan = new JTable(tableDeployPlanModel) {
			public boolean isCellEditable(int rowIndex, int colIndex) {
				return false;
			}
		};
		tableDeployPlan.setFont(new Font("Dialog", Font.PLAIN, 12));
		tableDeployPlan.setRowMargin(2);
		tableDeployPlan.setRowHeight(20);
		for (Integer i = 0; i < tableDeployPlanModel.getColumnCount(); i++) {
			String colName = (i == 0) ? "" : DeployOrganizations.get(i-1).Label;
			tableDeployPlan.getColumnModel().getColumn(i).setHeaderValue(colName);
			tableDeployPlan.getColumnModel().getColumn(i).setPreferredWidth(58);
		}
		tableDeployPlan.setGridColor(new Color(194, 192, 197));
		tableDeployPlan.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
		tableDeployPlan.setRowSelectionAllowed(false);
		tableDeployPlan.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableDeployPlan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableDeployPlan.setDefaultRenderer(Object.class, new MyRenderer(DeployLines, DeployOrganizations));
		tableDeployPlan.getColumnModel().getColumn(0).setPreferredWidth(180);
		tableDeployPlan.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				TableDeployClick(e);
			}
		});

		JScrollPane scrollPane1 = new JScrollPane();
		scrollPane1.setBackground(new Color(105, 105, 105));
		scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane1.setViewportView(tableDeployPlan);

		popupMenu = new JPopupMenu();
		JMenuItem mntmIncludeExclude = new JMenuItem("Include Exclude");
		mntmIncludeExclude.setName("IncludeExclude");
		mntmIncludeExclude.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				TablePopupMenuClick(e);
			}
		});
		popupMenu.add(mntmIncludeExclude);
		JMenuItem mntmCompareWithCore = new JMenuItem("Compare With CORE");
		mntmCompareWithCore.setName("CompareWithCore");
		mntmCompareWithCore.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				TablePopupMenuClick(e);
			}
		});
		popupMenu.add(mntmCompareWithCore);

		textAreaInput = new JTextArea();
		textAreaInput.setFont(new Font("Dialog", Font.PLAIN, 12));
		textAreaInput.setBorder(new EmptyBorder(2, 2, 2, 2));
		textAreaInput.setTabSize(4);
		textAreaInput.setText(TextAreaInitialContent);

		JScrollPane scrollPane2 = new JScrollPane();
		scrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane2.setViewportView(textAreaInput);

		JButton btnPrepare = new JButton("Prepare It");
		btnPrepare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PrepareItClick();
			}
		});

		btnBuildPlan = new JButton("Buld Plan for ANT");
		btnBuildPlan.setEnabled(false);
		btnBuildPlan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BuildPlanClick();
			}
		});

		btnDeployAll = new JButton("Deploy All");
		btnDeployAll.setActionCommand("Deploy All");
		btnDeployAll.setEnabled(false);

		JButton btnBye = new JButton("BYE");
		btnBye.setAlignmentY(Component.TOP_ALIGNMENT);
		btnBye.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(NORMAL);
			}
		});

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(btnBuildPlan, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnDeployAll, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 450, Short.MAX_VALUE)
					.addComponent(btnPrepare)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnBye, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE))
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
					.addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 437, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane2))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
						.addComponent(scrollPane2, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnBuildPlan)
							.addComponent(btnDeployAll))
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnBye)
							.addComponent(btnPrepare))))
		);
		contentPane.setLayout(gl_contentPane);
	}

	private void PrepareItClick() {
		Integer DeployOrgsCount = DeployOrganizations.size();
		DeployLines.clear();
		ArrayList<String> DupeControl = new ArrayList<String>();
		String[] Lines = textAreaInput.getText().split("\\r?\\n");
		Integer LinesCount = Lines.length;
		if (LinesCount > 0) {
			for (Integer i = 0; i < LinesCount; i++) {
				String fileStr = Lines[i];
				if (fileStr != "") {
					if (FILE_SEPARATOR != "/") {
						fileStr = fileStr.replace("/", FILE_SEPARATOR);
					}
					Integer indexMETA = fileStr.indexOf("-meta.xml");
					if (indexMETA > 0) {
						fileStr = fileStr.substring(0, indexMETA);
					}
					Integer index = fileStr.indexOf(FILE_SEPARATOR+"src"+FILE_SEPARATOR);
					if (index > 0) {
						if (DupeControl.contains(fileStr)) {
							continue;
						}
						DupeControl.add(fileStr);
						TDeployLine deployLine = new TDeployLine(DeployOrgsCount);
						deployLine.ResourcePath = fileStr.substring(index+5);
						Integer index2 = deployLine.ResourcePath.lastIndexOf(FILE_SEPARATOR);
						deployLine.ResourceLabel = deployLine.ResourcePath.substring(index2+1);

						for (Integer orgindex = DeployOrgsCount-1; orgindex >= 0; orgindex--) {
							TDeployItem deployItem = deployLine.DeployItems.get(orgindex);
							deployItem.FullFilePath = WORKSPACE_BASE_PATH + DeployOrganizations.get(orgindex).Directory + FILE_SEPARATOR + "src" + FILE_SEPARATOR + deployLine.ResourcePath;
							String MetaXmlPath = deployLine.getMetaXmlPath();
							deployItem.FullMetaXmlPath = (MetaXmlPath != null) ? WORKSPACE_BASE_PATH + DeployOrganizations.get(orgindex).Directory + FILE_SEPARATOR + "src" + FILE_SEPARATOR + MetaXmlPath : null;
							File resFile = new File(deployItem.FullFilePath);
							deployItem.IsFileExists = resFile.exists();
							if (deployItem.IsFileExists) {
								deployItem.loadContent();
								if (orgindex == DeployOrgsCount-1) {
									deployItem.Matched = true;
								}
								else {
									TDeployItem itemCore = (TDeployItem) deployLine.DeployItems.get(DeployOrgsCount-1);;
									deployItem.Matched = deployItem.compareTo(itemCore);
								}
							}
							DeployOrganizations.get(orgindex).DeployResourcesCount = 0;
						}
						DeployLines.add(deployLine);
					}
				}
			}
		}
		tableDeployPlanModel.setRowCount(DeployLines.size() + 1);
		tableDeployPlan.getColumnModel().getColumn(0).setPreferredWidth(getMinGridWidth() + 6);
		btnBuildPlan.setEnabled(DeployLines.size() > 0);
		btnDeployAll.setEnabled(false);
		tableDeployPlan.repaint();
	}

	private void BuildPlanClick() {
		if (DeployLines.size() > 0) {
			btnBuildPlan.setEnabled(false);
			for (int orgindex = DeployOrganizations.size() - 1; orgindex >= 0; orgindex--) {
				String AntDirectory = ANT_SPACE_BASE_PATH + DeployOrganizations.get(orgindex).Directory;
				deleteDirectory(AntDirectory);
				createDirectory(AntDirectory);
				buildAntTask(AntDirectory, DeployLines, orgindex);
			}
			btnDeployAll.setEnabled(true);
			tableDeployPlan.repaint();
		}
	}

	private void TableDeployClick(MouseEvent event) {
		JTable target = (JTable) event.getSource();
		int row = target.rowAtPoint(event.getPoint());
		int column = target.columnAtPoint(event.getPoint());
		// Double click on Deploy button
		if (event.getClickCount() == 2 && target.getRowCount() >= 2 && row == target.getRowCount()-1 && column > 0) {
			int DeployResourcesCount = DeployOrganizations.get(column-1).DeployResourcesCount;
			if (DeployResourcesCount > 0) {
				String AntTask = "deploy-" + DeployOrganizations.get(column-1).Directory;
				String command = DEPLOY_COMMAND.replace("{ANT_SPACE_BASE_PATH}", ANT_SPACE_BASE_PATH).replace("{0}", AntTask);
//				String[] commands = {"cmd", "/C", "start", ANT_SPACE_BASE_PATH + DEPLOY_COMMAND, AntTask};
				String[] commands = command.split("\\s");
				try {
					ProcessBuilder builder = new ProcessBuilder(commands);
					builder.start();
//					Runtime.getRuntime().exec(command);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
//				AnsiString command = "/k " + AnsiString(AntBasePath) + "deploy-ORG.bat deploy-" + DeployOrgs[orgindex].Directory;
//				ShellExecute(Handle, "open", "cmd", command.c_str(), NULL, SW_SHOW);
			}
		}
		// Single click on deploy resource
		if (event.getClickCount() == 1 && event.getButton() == MouseEvent.BUTTON3 && column > 0 && row >= 0 && row < target.getRowCount()-1) {
//			target.selectClickedRow !
			TDeployLine deployLine = DeployLines.get(row);
			TDeployItem deployItem = deployLine.DeployItems.get(column-1);
			for (Integer i = 0; i < popupMenu.getSubElements().length; i++) {
				JMenuItem MenuItem = (JMenuItem) popupMenu.getSubElements()[i];
				if (MenuItem.getName() == "IncludeExclude") {
					Boolean IsIncludedToDeploy = deployItem.getIsIncludedToDeploy();
					MenuItem.setText((IsIncludedToDeploy ? "Exclude " : "Include ") + deployLine.ResourceLabel + (IsIncludedToDeploy ? " from  " : " to ") + DeployOrganizations.get(column-1).Label);
					PopupMenuFileIndex = row;
					PopupMenuOrgIndex = column-1;
				}
				if (MenuItem.getName() == "CompareWithCore") {
					MenuItem.setVisible(column < tableDeployPlanModel.getColumnCount()-1);
				}
			}
			popupMenu.show(event.getComponent(), event.getX()+10, event.getY());
		}
	}

	private void TablePopupMenuClick(ActionEvent event) {
		TDeployLine deployLine = DeployLines.get(PopupMenuFileIndex);
		TDeployItem deployItem = deployLine.DeployItems.get(PopupMenuOrgIndex);
		JMenuItem MenuItem = (JMenuItem) event.getSource();
		if (MenuItem.getName() == "IncludeExclude") {
			Boolean IsIncludedToDeploy = deployItem.getIsIncludedToDeploy();
			String PopupMenuOperation = IsIncludedToDeploy ? "E" : "I";
			if (PopupMenuOperation == "E") {
				deployItem.ForceAction = deployItem.Matched;
			}
			if (PopupMenuOperation == "I") {
				deployItem.ForceAction = !(deployItem.Matched);
			}
			DeployOrganizations.get(PopupMenuOrgIndex).DeployResourcesCount = 0;
			tableDeployPlan.repaint();
		}
		if (MenuItem.getName() == "CompareWithCore") {
		}
	}


	// Set min width of the first column
	private int getMinGridWidth() {
		int WidthMin = 180;
		if (DeployLines.size() > 0) {
			JLabel label = new JLabel();
			FontMetrics fontMetrics = label.getFontMetrics(new Font("Arial", Font.BOLD, 12));
			for (int i = 0; i < DeployLines.size(); i++) {
				TDeployLine deployLine = DeployLines.get(i);
				int tmp = fontMetrics.stringWidth(deployLine.ResourceLabel);
				WidthMin = (tmp > WidthMin) ? tmp : WidthMin;
			}
		}
		return WidthMin;
	}

	private void buildAntTask(String AntDirectory, ArrayList<TDeployLine> DeployLines, Integer orgindex) {
		TStringList packagexml = new TStringList();
		packagexml.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		packagexml.add("<Package xmlns=\"http://soap.sforce.com/2006/04/metadata\">");

		TStringList classesxml = new TStringList();
		classesxml.AllowDuplicates = false;
		classesxml.Sorted = true;

		TStringList componentsxml = new TStringList();
		componentsxml.AllowDuplicates = false;
		componentsxml.Sorted = true;

		TStringList pagesxml = new TStringList();
		pagesxml.AllowDuplicates = false;
		pagesxml.Sorted = true;

		TStringList staticresourcesxml = new TStringList();
		staticresourcesxml.AllowDuplicates = false;
		staticresourcesxml.Sorted = true;

		TStringList triggersxml = new TStringList();
		triggersxml.AllowDuplicates = false;
		triggersxml.Sorted = true;

		Integer DeployResourcesCount = 0;
		for (Integer i = 0; i < DeployLines.size(); i++) {
			TDeployLine deployLine = DeployLines.get(i);
			TDeployItem deployItem = deployLine.DeployItems.get(orgindex);
			if (deployItem.getIsIncludedToDeploy()) {
				String res1 = deployLine.ResourcePath;
				String res2 = deployLine.getMetaXmlPath();
				copyResource(res1, DeployOrganizations.get(orgindex).Directory);
				if (res2 != null) {
					copyResource(res2, DeployOrganizations.get(orgindex).Directory);
				}
				String resname = res1.substring(res1.indexOf(FILE_SEPARATOR)+1, res1.indexOf("."));
				if (res1.indexOf("classes"+FILE_SEPARATOR) != -1) {
					classesxml.add(resname);
				}
				if (res1.indexOf("components"+FILE_SEPARATOR) != -1) {
					componentsxml.add(resname);
				}
				if (res1.indexOf("pages"+FILE_SEPARATOR) != -1) {
					pagesxml.add(resname);
				}
				if (res1.indexOf("staticresources"+FILE_SEPARATOR) != -1) {
					staticresourcesxml.add(resname);
				}
				if (res1.indexOf("triggers"+FILE_SEPARATOR) != -1) {
					triggersxml.add(resname);
				}
				DeployResourcesCount++;
			}
		}
		addTypes(packagexml, classesxml, "ApexClass");
		addTypes(packagexml, componentsxml, "ApexComponent");
		addTypes(packagexml, pagesxml, "ApexPage");
		addTypes(packagexml, staticresourcesxml, "StaticResource");
		addTypes(packagexml, triggersxml, "ApexTrigger");

		DeployOrganizations.get(orgindex).DeployResourcesCount = DeployResourcesCount;

		packagexml.add("    <version>31.0</version>");
		packagexml.add("</Package>");
		packagexml.SaveToFile(AntDirectory + FILE_SEPARATOR + "package.xml");
	}

	private void addTypes(TStringList packagexml, TStringList types, String name) {
		List<String> members = types.getStrings();
		if (members.size() > 0) {
			packagexml.add("    <types>");
			for (Integer i = 0; i < members.size(); i++) {
				packagexml.add("        <members>" + members.get(i) + "</members>");
			}
			packagexml.add("        <name>" + name + "</name>");
			packagexml.add("    </types>");
		}
	}

	private void deleteDirectory(String directoryPath) {
		File dir = new File(directoryPath);
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i=0; i<children.length; i++) {
				File f = new File(dir, children[i]);
				deleteDirectory(f.getAbsolutePath());
			}
			dir.delete();
		}
		else {
			dir.delete();
		}
	}

	private void createDirectory(String directoryPath) {
		File dir = new File(directoryPath);
		dir.mkdir();
	}

	private void copyResource(String res, String orgDir) {
		String dirname = res.substring(0, res.indexOf(FILE_SEPARATOR));
		String dirpath = ANT_SPACE_BASE_PATH + orgDir + FILE_SEPARATOR + dirname;
		File dir = new File(dirpath);
		if (dir.exists() == false || dir.isDirectory() == false) {
			dir.mkdir();
		}
		File source = new File(WORKSPACE_BASE_PATH + DEPLOY_DEV_ORG_DIR + FILE_SEPARATOR + "src" + FILE_SEPARATOR + res);
		File target = new File(ANT_SPACE_BASE_PATH + orgDir + FILE_SEPARATOR + res);
		try {
			Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}

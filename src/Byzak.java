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

import java.io.File;
import java.io.FileInputStream;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.FontMetrics;

import java.util.Properties;
import java.util.ArrayList;

public class Byzak extends JFrame {

	private JPanel contentPane;
	private JTextArea textAreaInput;
	private JTable tableDeployPlan;
	private JButton btnBuldPlan;
	private JButton btnDeployAll;
	private DefaultTableModel tableDeployPlanModel;

	private static String WORKSPACE_BASE_PATH;
	private static String ANT_SPACE_BASE_PATH;
	private static String DEPLOY_DEV_ORG_DIR;

	private ArrayList<TDeployLine> DeployLines;
	private ArrayList<TDeployOrganization> DeployOrganizations;

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
		DeployLines = new ArrayList<TDeployLine>();
		DeployOrganizations = new ArrayList<TDeployOrganization>();
		DeployOrganizations.add(new TDeployOrganization("P:4.356", "zRecruiting-Patch-4.356"));
		DeployOrganizations.add(new TDeployOrganization("P:4.400", "zRecruiting-Patch-4.400"));
		DeployOrganizations.add(new TDeployOrganization("CORE", "JS-Recruiting-CORE"));

		Properties props = new Properties();
		try {
			String os = System.getProperty("os.name").toLowerCase();
			String osPrefix = (
				(os.indexOf( "win" ) >= 0) ? "WIN_" :
				(os.indexOf( "mac" ) >= 0) ? "MAC_" :
				(os.indexOf( "nix" ) >= 0) ? "NIX_" :
				""
			);
			props.load(new FileInputStream(new File("config.ini")));
			WORKSPACE_BASE_PATH = props.getProperty(osPrefix + "WORKSPACE_BASE_PATH");
			ANT_SPACE_BASE_PATH = props.getProperty(osPrefix + "ANT_SPACE_BASE_PATH");
			DEPLOY_DEV_ORG_DIR = props.getProperty(osPrefix + "DEPLOY_DEV_ORG_DIR");
		}
		catch (Exception e) {
		}

		setTitle("Bysh");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 875, 406);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		tableDeployPlanModel = new DefaultTableModel(0, DeployOrganizations.size() + 1);
		tableDeployPlan = new JTable(tableDeployPlanModel);
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
		tableDeployPlan.setDefaultRenderer(Object.class, new MyRenderer(DeployLines));
		tableDeployPlan.getColumnModel().getColumn(0).setPreferredWidth(180);

		JScrollPane scrollPane1 = new JScrollPane();
		scrollPane1.setBackground(new Color(105, 105, 105));
		scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane1.setViewportView(tableDeployPlan);

		textAreaInput = new JTextArea();
		textAreaInput.setBorder(new EmptyBorder(2, 2, 2, 2));
		textAreaInput.setTabSize(4);
		textAreaInput.setFont(new Font("Courier New", Font.PLAIN, 12));
		textAreaInput.setText("U /Dev-Base/src/classes/candidateBulkAction.cls\r\nU /Dev-Base/src/classes/candidateBulkAction.cls-meta.xml\r\nU /Dev-Base/src/classes/s_CTagController.cls\r\nU /Dev-Base/src/classes/s_CTagController.cls-meta.xml\r\nU /Dev-Base/src/components/s_Result.component\r\n\r\nU /Dev-Base/src/labels/CustomLabels.labels\r\nU /Dev-Base/src/objects/Contact.object\r\n\r\nU /Dev-Base/src/pages/AddTagsCandidate.page\r\nU /Dev-Base/src/pages/AddTagsCandidate.page-meta.xml\r\nU /Dev-Base/src/pages/StoryBoardReplica.page\r\nU /Dev-Base/src/pages/s_CTag.page-meta.xml\r\nU /Dev-Base/src/staticresources/SSearch.resource\r\n");

		JScrollPane scrollPane2 = new JScrollPane();
		scrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane2.setViewportView(textAreaInput);

		JButton btnPrepare = new JButton("Prepare It");
		btnPrepare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PrepareItClick();
			}
		});

		btnBuldPlan = new JButton("Buld Plan for ANT");
		btnBuldPlan.setEnabled(false);
		btnBuldPlan.addActionListener(new ActionListener() {
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
					.addComponent(btnBuldPlan, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)
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
							.addComponent(btnBuldPlan)
							.addComponent(btnDeployAll))
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnBye)
							.addComponent(btnPrepare))))
		);

		contentPane.setLayout(gl_contentPane);
	}

	private void PrepareItClick() {
		String fileSeparator = System.getProperty("file.separator");
		Integer DeployOrgsCount = DeployOrganizations.size();
		DeployLines.clear();
		String[] Lines = textAreaInput.getText().split(System.getProperty("line.separator"));
		Integer LinesCount = Lines.length;
		if (LinesCount > 0) {
			for (Integer i = 0; i < LinesCount; i++) {
				String fileStr = Lines[i];
				if (fileStr != "") {
					if (fileSeparator != "/") {
						fileStr = fileStr.replace("/", fileSeparator);
					}
					Integer index = fileStr.indexOf(fileSeparator+"src"+fileSeparator);
					if (index > 0) {
						TDeployLine deployLine = new TDeployLine(DeployOrgsCount);
						deployLine.ResourcePath = fileStr.substring(index+5);
						Integer index2 = deployLine.ResourcePath.lastIndexOf(fileSeparator);
						deployLine.ResourceLabel = deployLine.ResourcePath.substring(index2+1);

						for (Integer orgindex = DeployOrgsCount-1; orgindex >= 0; orgindex--) {
							TDeployItem deployItem = deployLine.DeployItems.get(orgindex);
							deployItem.FullFilePath = WORKSPACE_BASE_PATH + DeployOrganizations.get(orgindex).Directory + fileSeparator + "src" + fileSeparator + deployLine.ResourcePath;
							File resFile = new File(deployItem.FullFilePath);
							deployItem.IsFileExists = resFile.exists();
							if (deployItem.IsFileExists) {
//								pOrgFile->FileContent->LoadFromFile(pOrgFile->FullFilePath);
//								FileContentPreprocessing(pOrgFile->FileContent, pOrgFile->FullFilePath);
								if (orgindex == DeployOrgsCount-1) {
									deployItem.Matched = true;
								}
								else {
//									TOrgFile *pOrgFileCore = (TOrgFile*) pDeployFile->Orgs->Items[DeployOrgsCount-1];
//									TStringList *pContentCore = pOrgFileCore->FileContent;
//									deployItem.Matched = CompareContent(pOrgFile->FileContent, pContentCore);
									deployItem.Matched = false;
								}
							}
						}
						DeployLines.add(deployLine);
					}
				}
			}
		}
		tableDeployPlanModel.setRowCount(DeployLines.size());
		tableDeployPlan.getColumnModel().getColumn(0).setPreferredWidth(getMinGridWidth() + 6);
		btnBuldPlan.setEnabled(DeployLines.size() > 0);
	}

	private void BuildPlanClick() {
		if (DeployLines.size() > 0) {
			btnBuldPlan.setEnabled(false);
			for (int orgindex = DeployOrganizations.size() - 1; orgindex >= 0; orgindex--) {
				String AntDirectory = ANT_SPACE_BASE_PATH + DeployOrganizations.get(orgindex).Directory;
				deleteDirectory(AntDirectory);
				createDirectory(AntDirectory);
				buildAntTask(AntDirectory, DeployLines, orgindex);
			}
			btnBuldPlan.setEnabled(true);
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

	private void buildAntTask(String directoryPath, ArrayList<TDeployLine> DeployLines, Integer orgindex) {

	}

}

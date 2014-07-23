import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.UIManager;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.table.TableCellRenderer;

import java.util.ArrayList;

public class MyRenderer extends JLabel implements TableCellRenderer {

	private ArrayList<TDeployLine> DeployLines;
	private ArrayList<TDeployOrganization> DeployOrganizations;

	public MyRenderer() {
		setOpaque(true);
	}
	public MyRenderer(ArrayList<TDeployLine> dl, ArrayList<TDeployOrganization> d2) {
		this();
		DeployLines = dl;
		DeployOrganizations = d2;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		// Draw data row
		if (row >= 0 && row < DeployLines.size()) {
			setHorizontalAlignment(LEFT);
			setEnabled(true);
			TDeployLine deployLine = DeployLines.get(row);
			// Draw title column
			if (column == 0) {
				setFont(new Font("Arial", Font.BOLD, 12));
				setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
				setForeground(UIManager.getColor("TableHeader.foreground"));
				setBackground(UIManager.getColor("TableHeader.background"));
				setText(deployLine.ResourceLabel);
			}
			// Draw data column
			if (column > 0) {
				TDeployItem deployItem = deployLine.DeployItems.get(column-1);
				setBorder(hasFocus ? BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(UIManager.getColor("Table.selectionBackground"), 1), BorderFactory.createEmptyBorder(1, 1, 1, 1)) : BorderFactory.createEmptyBorder(2, 2, 2, 2));
				setForeground(isSelected ? UIManager.getColor("Table.selectionForeground") : UIManager.getColor("Table.foreground"));
				if (deployItem.IsFileExists) {
					if (deployItem.getIsIncludedToDeploy()) {
						setBackground(new Color(0x008000));
					}
					else {
						setBackground(new Color(0xff0000));
					}
				}
				else {
					setBackground(Color.WHITE);
				}
				if (deployItem.ForceAction) {
					setForeground(deployItem.IsFileExists ? Color.WHITE : Color.BLACK);
					setFont(new Font("Arial", Font.BOLD, 12));
					setText("F");
					setHorizontalAlignment(CENTER);
				}
				else {
					setText("");
				}
			}
		}
		// Draw deploy buttons row
		if (row == DeployLines.size()) {
			if (column == 0) {
				setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
				setForeground(UIManager.getColor("TableHeader.foreground"));
				setBackground(UIManager.getColor("TableHeader.background"));
				setText("");
			}
			if (column > 0) {
				Integer DeployResourcesCount = DeployOrganizations.get(column-1).DeployResourcesCount;
				setFont(new Font("Arial", Font.BOLD, 12));
				setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
				setForeground(UIManager.getColor("TableHeader.foreground"));
				setBackground(UIManager.getColor("TableHeader.background"));
				setText("D >");
				setHorizontalAlignment(CENTER);
				setEnabled(DeployResourcesCount > 0);
			}
		}
		return this;
	}

}

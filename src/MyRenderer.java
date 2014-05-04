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

	public MyRenderer() {
		setOpaque(true);
	}
	public MyRenderer(ArrayList<TDeployLine> dl) {
		this();
		DeployLines = dl;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		// Draw data row
		if (row >= 0) {
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
				setFont(new Font("Arial", Font.PLAIN, 12));
				setBorder(hasFocus ? BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(UIManager.getColor("Table.selectionBackground"), 1), BorderFactory.createEmptyBorder(1, 1, 1, 1)) : BorderFactory.createEmptyBorder(2, 2, 2, 2));
				setForeground(isSelected ? UIManager.getColor("Table.selectionForeground") : UIManager.getColor("Table.foreground"));
//				setBackground(isSelected ? UIManager.getColor("Table.selectionBackground") : UIManager.getColor("Table.background"));
				setText("");
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
/*
				if (pOrgFile->IsFileExists) {
					if (pOrgFile->getIsIncludedToDeploy()) {
						OrgsGrid->Canvas->Brush->Color = clGreen;
					}
					else {
						OrgsGrid->Canvas->Brush->Color = clRed;
					}
				}
				else {
					OrgsGrid->Canvas->Brush->Color = clWhite;
				}
				OrgsGrid->Canvas->FillRect(Rect);
				if (pOrgFile->ForceAction) {
					OrgsGrid->Canvas->Font->Color = (pOrgFile->IsFileExists) ? clWhite : clBlack;
					OrgsGrid->Canvas->Font->Name = "Arial";
					OrgsGrid->Canvas->Font->Size = 12;
					OrgsGrid->Canvas->Font->Style = TFontStyles() << fsBold;
					AnsiString CellText = "F";
					int dx = (OrgsGrid->ColWidths[(int)ACol] - OrgsGrid->Canvas->TextWidth(CellText)) / 2;
					OrgsGrid->Canvas->TextOut(Rect.Left+dx, Rect.Top+2, CellText);
				}
*/
			}
		}
		return this;
	}

}

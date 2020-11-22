package simulator.control;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

public class BodiesTable extends JPanel {
	
	private static final long serialVersionUID = 1L;

	public BodiesTable(Controller controller) {
		
		JTable bodiesTable = new JTable(new BodiesTableModel(controller));
		
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black,3),
				"Bodies",
				TitledBorder.LEFT,
				TitledBorder.TOP));
		
		bodiesTable.setPreferredScrollableViewportSize(new Dimension(getWidth(), 100));
		bodiesTable.setFillsViewportHeight(true);
		
		this.add(new JScrollPane(bodiesTable));
		
	}


}

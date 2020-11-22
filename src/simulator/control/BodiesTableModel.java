package simulator.control;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class BodiesTableModel extends AbstractTableModel implements SimulatorObserver{

	private static final long serialVersionUID = 1L;

	private List<Body> _bodies;
	private final static String[] columnNames = { "Id", "Mass", "Position", "Velocity", "Acceleration" };
	
	public BodiesTableModel(Controller controller) {
		_bodies= new ArrayList<>();
		controller.addObserver(this);
	}
	
	@Override
	public int getRowCount() {
		return _bodies.size();
	}
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	
	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Body body = _bodies.get(rowIndex);
		
		switch(columnNames[columnIndex]) {
			case "Id":
				return body.getIdentification();
			case "Mass":
				return body.getMass();
			case "Position":
				return body.getPosition();
			case "Velocity":
				return body.getVelocity();
			case "Acceleration":
				return body.getAcceleration();
			default:
				return "";
		}
	}
	
	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {
		_bodies = new ArrayList<>(bodies);
		this.fireTableStructureChanged();
		
	}
	
	@Override
	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {
		_bodies= new ArrayList<>();
		this.fireTableStructureChanged();
	}
	
	@Override
	public void onBodyAdded(List<Body> bodies, Body body) {
		_bodies = bodies;
		this.fireTableStructureChanged();
		
	}
	
	@Override
	public void onAdvance(List<Body> bodies, double time) {
		_bodies = bodies;
		this.fireTableStructureChanged();
	}
	
	@Override
	public void onDeltaTimeChanged(double dt) {
		
	}
	@Override
	public void onGravityLawChanged(String gLawsDesc) {
		
	}
	
	
}

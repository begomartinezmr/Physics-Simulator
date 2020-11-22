package simulator.control;

import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class StatusBar extends JPanel implements SimulatorObserver{

	private JLabel _currTime; //for current time
	private JLabel _currLaws; //for gravity laws
	private JLabel _numOfBodies; // for number of bodies
	
	StatusBar(Controller controller){
		initGUI();
		controller.addObserver(this);
	}
	
	private void initGUI (){
		this.setLayout ( new FlowLayout (FlowLayout.LEFT));
		this.setBorder(BorderFactory.createBevelBorder(1));
		
		// complete with the code to build the tool bar
		JToolBar inferior = new JToolBar();
		
		_currTime = new JLabel();
		_currLaws = new JLabel();
		_numOfBodies= new JLabel();
		
//		JButton time = new JButton();
//		time.setText("Time: " + _currTime);
//		inferior.add(time);
//		
//		JButton bodies = new JButton();
//		bodies.setText("Bodies: " + _numOfBodies);
//		inferior.add(bodies);
//		
//		JButton laws = new JButton();
//		laws.setText("Laws: " + _currLaws);
//		inferior.add(laws);
		
		inferior.add(_currTime);
		inferior.addSeparator();
		inferior.add(_currLaws);
		inferior.addSeparator();
		inferior.add(_numOfBodies);
		this.add(inferior);
		
		
	}

	@Override
	public void onRegister(List<Body> bodies, double time, double dt,
			String gLawsDesc) {
		this._currTime.setText("Time: " + Double.toString(time));
		this._numOfBodies.setText("Bodies: " + Integer.toString(bodies.size()));
		this._currLaws.setText("Laws: "+ gLawsDesc);
		
	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt,
			String gLawsDesc) {
		
		this._currTime.setText("Time: " + Double.toString(time));
		this._numOfBodies.setText("Bodies: " + Integer.toString(bodies.size()));
		this._currLaws.setText("Laws: "+ gLawsDesc);
		
	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		this._numOfBodies.setText("Bodies: " + Integer.toString(bodies.size()));
		
	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {
		
		this._currTime.setText("Time: " + Double.toString(time));
		this._numOfBodies.setText("Bodies: " + Integer.toString(bodies.size()));
		
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		
	}

	@Override
	public void onGravityLawChanged(String gLawsDesc) {
		this._currLaws.setText("Laws: "+ gLawsDesc);
		
	}
}

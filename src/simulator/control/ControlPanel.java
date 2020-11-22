package simulator.control;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import org.json.JSONObject;

import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class ControlPanel extends JPanel implements SimulatorObserver{

	private static final long serialVersionUID = 1L;
	private Controller _ctrl;
	private boolean _stopped;
	private JTextField dTime;
	private SpinnerNumberModel stepsNM;
	private JFileChooser fileChooser;
	
	private JButton open;
	private JButton laws;
	private JButton play;
	private JButton stop;
	private JButton exit;
	
	
	public ControlPanel(Controller controller) {
		_ctrl = controller;
		_stopped = true;
		initGUI();
		_ctrl.addObserver(this);
	}
	
	private void initGUI() {
		JPanel panel = new JPanel();
		
		// Layout principal que contiene text field y botones

		JToolBar superiorWest = new JToolBar();
		JToolBar superiorEast = new JToolBar();
		
		fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("Finder\\Documentos\\EclipseProjects\\Pr5")+ "/resources"));
		fileChooser.setMultiSelectionEnabled(false);
		
		//Botones
		
		open = new JButton();
		open.setIcon(new ImageIcon("resources/icons/open.png"));
		open.setToolTipText("Open file");
		open.addActionListener(new OpenListener());
		superiorWest.add(open);
		
		
		laws = new JButton();
		laws.setIcon(new ImageIcon("resources/icons/physics.png"));
		laws.setToolTipText("Select law");
		laws.addActionListener(new LawsListener());
		superiorWest.add(laws);
		
		play = new JButton();
		play.setIcon(new ImageIcon("resources/icons/run.png"));
		play.setToolTipText("Play simulation");
		play.addActionListener(new PlayListener());
		superiorWest.add(play);
		
		stop = new JButton();
		stop.setIcon(new ImageIcon("resources/icons/stop.png"));
		stop.setToolTipText("Stop simulation");
		stop.addActionListener(new StopListener());
		superiorWest.add(stop);
		
		exit = new JButton();
		exit.setIcon(new ImageIcon("resources/icons/exit.png"));
		exit.setToolTipText("Exit");
		exit.addActionListener(new ExitListener());
		exit.setAlignmentX(RIGHT_ALIGNMENT);
		superiorEast.add(exit);
		
		//Steps
		
		superiorWest.add(new JLabel ("Steps: "));
		superiorWest.addSeparator();
		stepsNM = new SpinnerNumberModel(100, 0, 1000000, 100); //inicial, maximo, minimo y cantidad del salto
		JSpinner steps = new JSpinner(stepsNM);
		superiorWest.add(steps);
		
		//DeltaTime
		
		superiorWest.addSeparator();
		superiorWest.add(new JLabel ("Delta Time: "));
		superiorWest.addSeparator();
		dTime = new JTextField(5);
		superiorWest.add(dTime);
		
		JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		rightPanel.add(superiorEast);
		
		JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		leftPanel.add(superiorWest);

		
		panel.add(leftPanel);
		panel.add(rightPanel);
		this.add(panel);
	
	}
	
	
	
	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {
			dTime.setText(Double.toString(dt));
		
	}
	
	@Override
	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {
			dTime.setText(Double.toString(dt));
	
	}
	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
	
	}
	@Override
	public void onAdvance(List<Body> bodies, double time) {
		
	}
	@Override
	public void onDeltaTimeChanged(double dt) {
		dTime.setText(Double.toString(dt));

	}
	@Override
	public void onGravityLawChanged(String gLawsDesc) {
		
		
	}
	private void run_sim(final int n) {
		   if ( n>0 && !_stopped ) {
		      try {
		          _ctrl.run(1);
		      } catch (Exception e) {
		          //Show the error in a dialog box
		    	  JOptionPane.showMessageDialog(null," Error", "Error Icon",
		    			  JOptionPane.ERROR_MESSAGE);
		    	  
		          //Enable all buttons
		    	  	stop.setEnabled(true);
					open.setEnabled(true);
					play.setEnabled(true);			
					laws.setEnabled(true);
					exit.setEnabled(true);
		          _stopped = true;
		          return;
		      }
		      SwingUtilities.invokeLater( new Runnable() {
		    	  @Override
		    	  public void run() {
		  		   run_sim(n-1);
		  		}
		});
		} else {
		         _stopped = true;
		         
		         // enable all buttons
	    	  	stop.setEnabled(true);
				open.setEnabled(true);
				play.setEnabled(true);			
				laws.setEnabled(true);
				exit.setEnabled(true);
		      }
		}
	
	
	//Listeners
	
	
	public class OpenListener implements ActionListener {
	
		@Override
		public void actionPerformed(ActionEvent e) {
			if(fileChooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) {
				_ctrl.reset();
			}
			try {
				_ctrl.loadBodies(new FileInputStream(fileChooser.getSelectedFile()));
			}catch(FileNotFoundException e1) {
				throw new IllegalArgumentException("File not found");
			}
		}
			
	}
	
	
	public class LawsListener implements ActionListener {

		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			List<String>options = new ArrayList<>();
			List<org.json.JSONObject> lawsInfo = _ctrl.getGravityLawsFactory().getInfo();
			
			for(JSONObject object : _ctrl.getGravityLawsFactory().getInfo()){
				options.add(object.getString("desc") + "(" + object.getString("type") + ")");
			}
			
			String select = (String) JOptionPane.showInputDialog(null, "Select a gravity law", "GravityLaws", 
					JOptionPane.DEFAULT_OPTION, null, options.toArray(),  options.get(0));
			
			if(select != null) { 
				boolean found = false;
				int i = 0;
				do{
					if(select.contains(lawsInfo.get(i).getString("type"))){
						found = true;
;
						_ctrl.setGravityLaws(lawsInfo.get(i));
					}
					i++;
					
				}while(!found && i<_ctrl.getGravityLawsFactory().getInfo().size());
				
			}
		}
		
	}
	
	public void enableButtons(boolean enable){
		open.setEnabled(enable);
		play.setEnabled(enable);			
		laws.setEnabled(enable);
		exit.setEnabled(enable);
		
	}
	
	public class PlayListener implements ActionListener{

		
		@Override
		public void actionPerformed(ActionEvent e) {
			_stopped = false;
			_ctrl.setDeltaTime(Double.parseDouble(ControlPanel.this.dTime.getText()));
			
			enableButtons(false);

			run_sim((int)ControlPanel.this.stepsNM.getNumber());
		}	
	}
	
	public class StopListener implements ActionListener{

		
		@Override
		public void actionPerformed(ActionEvent e) {
			_stopped = true;
			_ctrl.setDeltaTime(Double.parseDouble(ControlPanel.this.dTime.getText()));
			
			enableButtons(false);
		
			run_sim((int)ControlPanel.this.stepsNM.getNumber());
		}	
	}
	
	public class ExitListener implements ActionListener{

		
		@Override
		public void actionPerformed(ActionEvent e) {
			int option = JOptionPane.showConfirmDialog(null,"Do you want to exit the simulator?"
					,"Physics Simulator", JOptionPane.YES_NO_OPTION);
			
			if(option==JOptionPane.YES_OPTION) {
				System.exit(0);
			}
		}
		
		
		
	}

	
}

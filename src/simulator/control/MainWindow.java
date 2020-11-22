package simulator.control;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainWindow extends JFrame{

	private static final long serialVersionUID = 1L;
	Controller _controller;
	
	public MainWindow (Controller controller){
		
		super("Physics Simulator");
		_controller = controller;
		initGUI();
			
	}
	
	private void initGUI(){

		JPanel mainPanel = new JPanel(new BorderLayout());
		setContentPane(mainPanel);
		
		//complete this method to build the GUI
		
		mainPanel.add(new ControlPanel(_controller), BorderLayout.PAGE_START);
		mainPanel.add(new StatusBar(_controller), BorderLayout.PAGE_END);
		
		
		
		
		

	
		
		JPanel otherPanel = new JPanel ();
		otherPanel.setLayout(new BoxLayout(otherPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);
	
		otherPanel.add(new BodiesTable(_controller));
		otherPanel.add(new Viewer(_controller));
		
		mainPanel.add(otherPanel);
		
		mainPanel.setBackground(Color.white);

		this.setMinimumSize(new Dimension(800,600));
		
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		
	}
	

}

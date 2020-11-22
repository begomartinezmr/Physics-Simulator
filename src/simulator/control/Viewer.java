package simulator.control;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.List;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;






import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.TitledBorder;

import simulator.misc.Vector;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class Viewer extends JComponent implements SimulatorObserver {

	private int _centerX;
	private int _centerY;
	private double _scale;
	private java.util.List<Body> _bodies;
	private boolean _showHelp;

	Viewer(Controller controller) {
		initGUI();
		controller.addObserver(this);
	}
	
	private void initGUI() {
		//Add border with title

		setLayout ( new BorderLayout());
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black,2),
				"Viewer", TitledBorder.LEFT, TitledBorder.TOP));
		
		_bodies = new ArrayList<>();
		_scale = 1.0;
		_showHelp = true;
		
	
		addKeyListener(new KeyListener() {
		
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyChar()) {
				case '-':
					_scale = _scale * 1.1;
					break;
				case '+':
					_scale = Math.max(1000.0, _scale / 1.1);
				break;
				case '=':
					autoScale();
				break;
				case 'h':
					_showHelp = !_showHelp;
					break;
				default:
				}
				repaint();
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	
	addMouseListener(new MouseListener() {
		 
		@Override
		public void mouseEntered(MouseEvent e) {
			requestFocus();
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	});
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// use 'gr' to draw not 'g'
		Graphics2D gr = (Graphics2D) g;
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		gr.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		// calculate the center
		_centerX = getWidth() / 2;
		_centerY = getHeight() / 2;
		
		// draw a cross at center
		gr.setColor(Color.red);
		gr.drawLine(_centerX-20, _centerY, _centerX + 20, _centerY);
		gr.drawLine(_centerX, _centerY-20, _centerX, _centerY+20);
		
		// draw bodies
		for(Body b: this._bodies){
			int x= _centerX + (int)(b.getPosition().coordinate(0)/_scale)-5;
			int y= _centerY + (int)(b.getPosition().coordinate(1)/_scale)-5;
			
			gr.setColor(Color.BLUE);
			gr.fillOval(x, y, 10, 10);
			gr.setColor(Color.BLACK);
			gr.drawString(b.getIdentification(), x-2, y-13);
		}
		
		// draw help if _showHelp is true
		if(this._showHelp){
			long boundsX = Math.round(this.getBounds().getMinX());
			long boundsY = Math.round(this.getBounds().getMinY());
			gr.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
			gr.setColor(Color.RED);
			gr.drawString("h: toggle help,+: zoom-in, -: zoom-out, =:fit", boundsX + 8, boundsY/7);
			gr.drawString("Scaling ratio: "+  this._scale, boundsX + 8, boundsY/5);
			
		}
		
	}
	
		// other private/protected methods
		// ...
	private void autoScale() {
		double max = 1.0;
		for (Body b : _bodies) {	
			Vector p = b.getPosition();
			for (int i = 0; i < p.dim(); i++)
				max = Math.max(max, Math.abs(b.getPosition().coordinate(i)));
		}
		double size = Math.max(1.0, Math.min((double) getWidth(), (double) getHeight()));
		_scale = max > size ? 4.0 * max / size : 1.0;
	}
	
		@Override
		public void onRegister(java.util.List<Body> bodies, double time, double dt, String gLawsDesc) {
			_bodies = new ArrayList<>(bodies);
			autoScale();
			repaint();
			
			
		}
		@Override
		public void onReset(java.util.List<Body> bodies, double time, double dt, String gLawsDesc) {
			this._bodies.clear(); //los borro porque se duplican
			autoScale();
			repaint();
		}
		@Override
		public void onBodyAdded(java.util.List<Body> bodies, Body body) {
			this._bodies.add(body);
			autoScale();
			repaint(); //llamo a paintComponent
		}
		@Override
		public void onAdvance(java.util.List<Body> bodies, double time) {
			repaint();
			
		}
		@Override
		public void onDeltaTimeChanged(double dt) {
	
			
		}
		@Override
		public void onGravityLawChanged(String gLawsDesc) {
		
			
		}
		
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(getWidth(),350);
		}

		
}

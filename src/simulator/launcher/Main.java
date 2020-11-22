package simulator.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONObject;

import simulator.control.Controller;
import simulator.control.MainWindow;
import simulator.factories.BasicBodyBuilder;
import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.Factory;
import simulator.factories.FallingToCenterGravityBuilder;
import simulator.factories.MassLosingBodyBuilder;
import simulator.factories.NewtonUniversalGravitationBuilder;
import simulator.factories.NoGravityBuilder;
import simulator.model.Body;
import simulator.model.GravityLaws;
import simulator.model.PhysicsSimulator;


/*
 * Examples of command-line parameters:
 * 
 *  -h
 *  -i resources/examples/ex4.4body.txt -s 100
 *  -i resources/examples/ex4.4body.txt -o resources/examples/ex4.4body.out -s 100
 *  -i resources/examples/ex4.4body.txt -o resources/examples/ex4.4body.out -s 100 -gl ftcg
 *  -i resources/examples/ex4.4body.txt -o resources/examples/ex4.4body.out -s 100 -gl nlug
 *
 */
import java.util.ArrayList;

public class Main {

	// default values for some parameters

	private final static Double _dtimeDefaultValue = 2500.0;
	private final static Integer stepsDefaultValue = 150;
	private final static String _dModeDefaultValue = "gui";

	// some attributes to stores values corresponding to command-line parameters

	private static Integer _steps = null;
	private static Double _dtime = null;
	private static String _inFile = null;
	private static String _outFile = null;
	private static JSONObject _gravityLawsInfo = null;
	private static String _mode = null;

	// factories
	private static Factory<Body> _bodyFactory;
	private static Factory<GravityLaws> _gravityLawsFactory;

	private static void init() {
		// initialize the bodies factory
		ArrayList<Builder<Body>> bodyBuilder = new ArrayList <> ();
		bodyBuilder.add(new BasicBodyBuilder());
		bodyBuilder.add(new MassLosingBodyBuilder());
		_bodyFactory = new BuilderBasedFactory<Body>(bodyBuilder);
		
		// initialize the gravity laws factory
		ArrayList<Builder<GravityLaws>> gravityLawsBuilder = new ArrayList <> ();
		gravityLawsBuilder.add(new NewtonUniversalGravitationBuilder());
		gravityLawsBuilder.add(new FallingToCenterGravityBuilder());
		gravityLawsBuilder.add(new NoGravityBuilder());
		_gravityLawsFactory = new BuilderBasedFactory <GravityLaws>(gravityLawsBuilder);
		
	}

	private static void parseArgs(String[] args) throws Exception {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseHelpOption(line, cmdLineOptions);
			parseInFileOption(line);
			parseDeltaTimeOption(line);
			parseGravityLawsOption(line);
			parseStepsOption(line);
			parseOutFileOption(line);
			parseModeOption(line);

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		// help
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message.").build());

		// input file
		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Bodies JSON input file.").build());
		
		// output file
		cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg().desc("Output file,"
				+ "where output is written. Default value: the standard output." ).build());

		// delta-time
		cmdLineOptions.addOption(Option.builder("dt").longOpt("delta-time").hasArg()
				.desc("A double representing actual time, in seconds, per simulation step. Default value: "
						+ _dtimeDefaultValue + ".")
				.build());
		
		// steps
		cmdLineOptions.addOption(Option.builder("s").longOpt("steps").hasArg().desc("An integer representing the number of simulation steps."
				+ " Default value: "+ stepsDefaultValue).build());
		
		//mode
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc("Execution Mode. Possible values: 'batch'\n ."
				+ " (Batch mode), 'gui' (Grphical User Interface mode). Default value: 'batch' . ").build());
		
		
		
		String gravityLawsValues = "N/A";
		String defaultGravityLawsValue = "N/A";
		if (_gravityLawsFactory != null) {
			gravityLawsValues = "";
			for (JSONObject fe : _gravityLawsFactory.getInfo()) {
				if (gravityLawsValues.length() > 0) {
					gravityLawsValues = gravityLawsValues + ", ";
				}
				gravityLawsValues = gravityLawsValues + "'" + fe.getString("type") + "' (" + fe.getString("desc") + ")";
			}
			defaultGravityLawsValue = _gravityLawsFactory.getInfo().get(0).getString("type");
		}
		cmdLineOptions.addOption(Option.builder("gl").longOpt("gravity-laws").hasArg()
				.desc("Gravity laws to be used in the simulator. Possible values: " + gravityLawsValues
						+ ". Default value: '" + defaultGravityLawsValue + "'.")
				.build());

		return cmdLineOptions;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}
	
	private static void parseOutFileOption(CommandLine line){
		_outFile = line.getOptionValue("o");		
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		//if (_inFile == null) {
			//throw new ParseException("An input file of bodies is required");
		//}
	}

	private static void parseDeltaTimeOption(CommandLine line) throws ParseException {
		String dt = line.getOptionValue("dt", _dtimeDefaultValue.toString());
		try {
			_dtime = Double.parseDouble(dt);
			assert (_dtime > 0);
		} catch (Exception e) {
			throw new ParseException("Invalid delta-time value: " + dt);
		}
	}

	private static void parseGravityLawsOption(CommandLine line) throws ParseException {
		// this line is just a work around to make it work even when _gravityLawsFactory
		// is null, you can remove it when've defined _gravityLawsFactory
		if (_gravityLawsFactory == null)
			return;

		String gl = line.getOptionValue("gl");
		if (gl != null) {
			for (JSONObject fe : _gravityLawsFactory.getInfo()) {
				if (gl.equals(fe.getString("type"))) {
					_gravityLawsInfo = fe;
					break;
				}
			}
			if (_gravityLawsInfo == null) {
				throw new ParseException("Invalid gravity laws: " + gl);
			}
		} else {
			_gravityLawsInfo = _gravityLawsFactory.getInfo().get(0);
		}
	}
	
	private static void parseStepsOption(CommandLine line) throws ParseException {
		String steps = line.getOptionValue("s", stepsDefaultValue.toString());
		try {
			_steps = Integer.parseInt(steps);
			assert (_steps > 0);
		} catch (Exception e) {
			throw new ParseException("Invalid steps value: " + steps);
		}
		
	}
	
	private static void parseModeOption(CommandLine line) throws Exception{
		_mode = line.getOptionValue("m");
		if(_mode== null){
			_mode = line.getOptionValue("m", _dModeDefaultValue);
			startGUIMode();
		}else{
			if(_mode.equals("gui")){
				startGUIMode();
			}else if(_mode.equals("batch")){
				startBatchMode();
			}else{
				throw new ParseException("Invalid mode value: " + _mode);
			}
		}
	}

	private static void startBatchMode() throws Exception {
		// create and connect components, then start the simulator
		GravityLaws laws = _gravityLawsFactory.createInstance(_gravityLawsInfo);
		PhysicsSimulator physicsSimulator = new PhysicsSimulator(laws, _dtime);
		Controller controller = new Controller(physicsSimulator, _bodyFactory, _gravityLawsFactory);
		
		InputStream inputStream = new FileInputStream(new File(_inFile));
		OutputStream outputStream;
			if(_outFile==null){
				outputStream= System.out;
			}else{
				outputStream= new FileOutputStream(_outFile);
			}
		controller.loadBodies(inputStream);
		controller.run(_steps,outputStream);
		System.out.println();
		System.out.println("The End");
	}
	
	private static void startGUIMode() throws Exception{
		GravityLaws laws = _gravityLawsFactory.createInstance(_gravityLawsInfo);
		PhysicsSimulator simulator = new PhysicsSimulator(laws, _dtime);
		final Controller controller = new Controller(simulator, _bodyFactory, _gravityLawsFactory);
		
		SwingUtilities.invokeAndWait( new Runnable(){
			
			@Override
			public void run(){
				new MainWindow(controller);
			}
			});
		
		if(_inFile != null){
			InputStream input = new FileInputStream(_inFile);
			controller.loadBodies(input);
		}
		
		}

	private static void start(String[] args) throws Exception {
		parseArgs(args);
		
	}

	public static void main(String[] args) {
		try {
			init();
			start(args);

			
			} catch (Exception e) {
			System.err.println("Something went wrong ...");
			System.err.println();
			e.printStackTrace();
		}
	}
}

package simulator.control;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Body;
import simulator.model.GravityLaws;
import simulator.model.PhysicsSimulator;
import simulator.model.SimulatorObserver;


public class Controller {
	
	private PhysicsSimulator physicsSimulator;
	private Factory <Body> bodiesFactory;
	private Factory <GravityLaws> lawsFactory;
	
	public Controller(PhysicsSimulator physicsSim, Factory<Body> bodiesFact,
	Factory<GravityLaws> lawsFact){
		physicsSimulator =  physicsSim;
		bodiesFactory=bodiesFact;
		lawsFactory = lawsFact;
	}
	
	public void run (int steps, OutputStream outputStream) {
		// Cuando el simulador avanza, llamas al simulador para ver como esta, lo metes en un JSON y 
		//lo metes en el array (s.put(new JSONObject(simulator.toString())); 
		//Para que avance se usa .advance
		//el toString hay que hacerlo a mano
		
		JSONObject states = new JSONObject();
		JSONArray jsonArray = new JSONArray();

		jsonArray.put(new JSONObject(physicsSimulator.toString()));
		
		for(int i =0;i< steps; i++){
			physicsSimulator.advance();
			jsonArray.put(new JSONObject(physicsSimulator.toString()));
		}
		
		states.put("states", jsonArray);
		
		try{
			outputStream.write(states.toString().getBytes());
		}catch(IOException e){
			System.out.println("OutputStream error");
		}
		
	}
	public void loadBodies (InputStream inputStream) {
		
		JSONObject jsonInput = new JSONObject (new JSONTokener(inputStream));
		
		JSONArray bodies = jsonInput.getJSONArray("bodies");
		
		for(int i =0; i<bodies.length();i++) {
			
			physicsSimulator.addBody(bodiesFactory.createInstance(bodies.getJSONObject(i)));
			
		}
	}
	
	public void reset() {
		physicsSimulator.reset();
	}
	
	public void setDeltaTime(double dt) {
		physicsSimulator.setDeltaTime(dt);
	}
	
	public void addObserver(SimulatorObserver o) {
		physicsSimulator.addObsever(o);
	}
	
	public void run(int n) {
		physicsSimulator.advance();
	}
	
	public Factory<GravityLaws> getGravityLawsFactory(){
		return lawsFactory ;
	}
	
	public void setGravityLaws(JSONObject info) {
		GravityLaws laws = lawsFactory.createInstance(info);
		physicsSimulator.setGravityLaws(laws);
	}
}


package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Vector;
import simulator.model.Body;

public class BasicBodyBuilder extends Builder<Body> {


	public BasicBodyBuilder() {
	_typeTag = "basic";
	_desc = "Default body";
	}

	@Override
	protected Body createTheInstance(JSONObject json) {
		Body body = null;
		
		if( json!= null){
			String id = json.getString("id");
			double[] position = jsonArrayTodoubleArray(json.getJSONArray("pos"));
			double[] velocity = jsonArrayTodoubleArray(json.getJSONArray("vel"));
			double mass = json.getDouble("mass");
			double[] acceleration= {0.0, 0.0};
			body = new Body(id,new Vector(velocity), new Vector(acceleration),new Vector(position),mass);
		}
		return body;

	}
	
	@Override
	protected JSONObject createData(){
		
		JSONObject json = new JSONObject();
		
		json.put("id", "identifier");
		json.put("pos", "position vector");
		json.put("vel", "velocity vector");
		json.put("mass", "mass");
			
		return json;
	}

}

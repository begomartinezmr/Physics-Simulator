package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Vector;
import simulator.model.Body;
import simulator.model.MassLosingBody;

public class MassLosingBodyBuilder extends Builder<Body> {


	public MassLosingBodyBuilder() {
	_typeTag = "mlb";
	_desc = "Mass losing body";
	}

	@Override
	public Body createTheInstance(JSONObject json) {
		Body body = null;
		
		if( json!= null){
			
			String id = json.getString("id");
			double[] position = jsonArrayTodoubleArray(json.getJSONArray("pos"));
			double[] velocity = jsonArrayTodoubleArray(json.getJSONArray("vel"));
			double mass = json.getDouble("mass");
			double lossFrequency = json.getDouble("freq");
			double lossFactor = json.getDouble("factor"); 
	

			body = new MassLosingBody(id, new Vector(velocity), new Vector(velocity.length), new Vector(position), mass, lossFactor, lossFrequency);
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
		json.put("freq", "lossFrequency");
		json.put("factor", "lossFactor");
			
		return json;
	}

}

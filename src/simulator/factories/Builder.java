package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector;

public abstract class Builder<T> {
	protected String _typeTag;
	protected String _desc;

	public Builder(){};
	
	protected double[] jsonArrayTodoubleArray(JSONArray jsons){
		double[] doubles = new double [jsons.length()];
		
		for(int i =0; i<jsons.length();i++) {
			doubles[i]=jsons.getDouble(i);
		}

		return doubles;
	}
	
	public T createInstance(JSONObject json) throws IllegalArgumentException{
		T constructor = null;
		
		if( _typeTag != null && _typeTag.equals(json.getString("type"))&& json.getJSONObject("data")!= null){
			constructor = createTheInstance(json.getJSONObject("data")) ;
		}
		
		return constructor;
				
	}
	
	public JSONObject getBuilderInfo(){
		JSONObject json = new JSONObject();
		json.put("type", _typeTag);
		json.put("data", createData());
		json.put("desc", _desc);
		
		return json;
	}
	
	protected JSONObject createData(){
		return new JSONObject();
	}
	
	protected abstract T createTheInstance(JSONObject json);
}

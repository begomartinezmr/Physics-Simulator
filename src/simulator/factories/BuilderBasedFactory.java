package simulator.factories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

public class BuilderBasedFactory<T> implements Factory<T> {
		
	private List<Builder<T>> listBuilders;
	
	
	public BuilderBasedFactory(List<Builder<T>> builders) {
		listBuilders = new ArrayList<>(builders);
			
		List<JSONObject> info= new ArrayList<>();
		
	//	for(Builder<T> item : builders){
	//		info.add(item.getBuilderInfo());
	//	}
		
	}

	@Override
	public T createInstance(JSONObject info) {
		
		if(info==null){
			throw new IllegalArgumentException("null");
		}
		
		for(Builder<T> item: listBuilders){
			T constructor = item.createInstance(info);
			if(constructor!= null){
				return constructor;
			}
		}
		throw new IllegalArgumentException(info.toString());
		
	}

	@Override
	public List<JSONObject> getInfo() {

		List<JSONObject> jsonArray= new ArrayList<>();
		
		for(Builder<T> item: listBuilders){
			jsonArray.add(item.getBuilderInfo());
		}

		return jsonArray;

	}

}

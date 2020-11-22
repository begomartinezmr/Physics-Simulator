package simulator.factories;

import org.json.JSONObject;

import simulator.model.GravityLaws;
import simulator.model.NoGravity;

public class NoGravityBuilder extends Builder<GravityLaws>{
	
	public NoGravityBuilder() {
		//Hay que hacerlo un public static final String
		//como esta en FallingToCenterGravityBuilder 
		//(hacerlo en todos los builder de las leyes)
		_typeTag = "ng";
		_desc = "No gravity law";
	}

	@Override
	protected GravityLaws createTheInstance(JSONObject json) {
		return new NoGravity();
	}

}

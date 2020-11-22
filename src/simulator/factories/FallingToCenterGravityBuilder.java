package simulator.factories;

import org.json.JSONObject;

import simulator.model.FallingToCenterGravity;
import simulator.model.GravityLaws;

public class FallingToCenterGravityBuilder extends Builder<GravityLaws>{
	public static final String typeTag = "ftcg";
	public static final String desc = "Falling to center gravity";

	public  FallingToCenterGravityBuilder () {
		this._typeTag = typeTag;
		this._desc = desc;
	}

	@Override
	protected GravityLaws createTheInstance(JSONObject json) {
		return new FallingToCenterGravity();
		
	}
}
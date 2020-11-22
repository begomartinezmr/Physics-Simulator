 package simulator.factories;

import org.json.JSONObject;

import simulator.model.NewtonUniversalGravitation;
import simulator.model.GravityLaws;


public class NewtonUniversalGravitationBuilder extends Builder<GravityLaws>{
	

	public NewtonUniversalGravitationBuilder () {
		_typeTag= "nlug";
		_desc = "Newton lay of Universal Gravitation";
	}

	@Override
	protected GravityLaws createTheInstance(JSONObject json) {
		return new NewtonUniversalGravitation();
		
	}
}

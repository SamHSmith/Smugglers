package physics;

import controler.GameState;
import controler.UniverseHandler;
import entity.BasicEntity;

public class MainEngine implements PhysicsEngine {

	UniverseHandler unihand;
	
	public MainEngine(UniverseHandler unihand) {
		super();
		this.unihand = unihand;
	}

	@Override
	public void simulate() {
		if(unihand.getState()==GameState.InGame){
			for (BasicEntity ent : unihand.entitys) {
				ent.getPosition().add(ent.getVelocity());
				ent.rotate(ent.getRotVelocity().x, ent.getRotVelocity().y,
						ent.getRotVelocity().z);
			}
		}

	}

	@Override
	public void collision() {
		// TODO Auto-generated method stub

	}

	@Override
	public void collisionResponse() {
		// TODO Auto-generated method stub

	}

}

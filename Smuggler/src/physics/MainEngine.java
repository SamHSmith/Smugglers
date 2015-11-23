package physics;

import controler.GameState;
import controler.UniverseHandler;
import entity.BasicEntity;
import entity.Collision;

import java.util.ArrayList;

import math3d.Vector3f;

public class MainEngine implements PhysicsEngine {

	UniverseHandler unihand;
	
	public MainEngine(UniverseHandler unihand) {
		super();
		this.unihand = unihand;
	}

	@Override
	public void simulate() {
		if(unihand.getState()!=GameState.InGame) return; // throw new java.lang.RuntimeException("Should not call movements and collision detection when mode is not INGAME");
		for (BasicEntity ent : unihand.entitys) {
			ent.getPosition().add(ent.getVelocity());
			ent.rotate(ent.getRotVelocity().x, ent.getRotVelocity().y,
					ent.getRotVelocity().z);
		}


	}

	@Override
	public void collision() {
		ArrayList<Collision> collisions = collisionDetection();
		collisionResponse(collisions);

	}

	private ArrayList<Collision> collisionDetection() {
		ArrayList<Collision> collisions = new ArrayList<Collision>();
		if(unihand.getState()!=GameState.InGame) return collisions; // TODO throw new java.lang.RuntimeException("Should not call movements and collision detection when mode is not INGAME");

		BasicEntity entA;
		BasicEntity entB;
		// Note - we can have the second loop smaller than the first, since we dont need to 
		// detect collisions in both directions, nor with ourself
		int a = unihand.entitys.size();
		for (int indexA = 0; indexA<unihand.entitys.size();  indexA++) {
			entA = unihand.entitys.get(indexA); 
			for (int indexB = indexA + 1; indexB<unihand.entitys.size(); indexB++) {
				entB = unihand.entitys.get(indexB);
				if (entA.collides(entB)) {
					collisions.add(new Collision(entA, entB));
				}
			}

		}
		return collisions;

	}

	private void collisionResponse(ArrayList<Collision> collisions) {
		if(unihand.getState()!=GameState.InGame) return; // TODO throw new java.lang.RuntimeException("Should not call movements and collision detection when mode is not INGAME");
		for (int i=0; i<collisions.size(); i++) {
			System.out.println("Collision "+i+" of "+collisions.size()+" found");
			Collision coll = collisions.get(i);
			// First find the normalized vector n from the center of A to the center of B
			BasicEntity a = coll.getEntityA();
			BasicEntity b = coll.getEntityB();
			Vector3f aAbsolutePos = new Vector3f();
			aAbsolutePos.add(a.getPosition());
			aAbsolutePos.add(a.getPositionOffset());
			Vector3f bAbsolutePos = new Vector3f();
			bAbsolutePos.add(b.getPosition());
			bAbsolutePos.add(b.getPositionOffset());			
			Vector3f n = aAbsolutePos.sub(bAbsolutePos);
			n.normalize();
			
			// Find the length of the component of each of the movement vectors along the direction
			// between the two centers
			float a1= a.getVelocity().dot(n);
			float b1= b.getVelocity().dot(n);
			
			// Complicated maths proof shown elsewhere
			// optimizedP = 2(a1 - b1) / (mass1 + mass2)
			float p = 2 * (a1-b1)/ (a.getMass()+b.getMass());
			
			Vector3f pushA = new Vector3f(n);
			pushA.mul(p*b.getMass());
			Vector3f pushB = new Vector3f(n);
			pushB.mul(p*a.getMass());
			
			a.setVelocity(a.getVelocity().sub(pushA));
			b.setVelocity(b.getVelocity().add(pushB));
			;
		}
	}

}

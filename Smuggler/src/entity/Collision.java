package entity;

public class Collision {
	BasicEntity entityA; 
		BasicEntity entityB; 
	public Collision(BasicEntity entityA, BasicEntity entityB) {
		this.entityA=entityA; 
		this.entityB=entityB;
		//TODO Work out the collision direction and magnitude
	}
	
	public BasicEntity getEntityA() {
		return entityA;
	}
	public BasicEntity getEntityB() {
		return entityB;
	}
	
	
}

package physics;

import org.joml.Vector3f;

public class IntersectData {
	
	public boolean intersecting;
	public Vector3f a_to_b_direction;
	
	public IntersectData(boolean intersecting, Vector3f a_to_b_direction) {
		super();
		this.intersecting = intersecting;
		this.a_to_b_direction = a_to_b_direction;
	}

}

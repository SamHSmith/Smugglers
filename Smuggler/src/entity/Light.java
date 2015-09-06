package entity;

import org.joml.Vector3f;

public class Light extends Effect {

	public Light( Vector3f velocity,
			Vector3f position, boolean dyn, float rx, float ry, float rz,
			float scale, Vector3f color) {
		super(velocity, position, dyn, rx, ry, rz, scale, null, color);
		
	}

}

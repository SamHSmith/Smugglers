package sound;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

import toolbox.Maths;

public class Listener {
	FloatBuffer listenerPos;
	FloatBuffer listenerVel;
	FloatBuffer listenerOri;
	public Listener() {
		/** Position of the listener. */
		listenerPos = BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f });
		 
		/** Velocity of the listener. */
		listenerVel = BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f });
		 
		/** Orientation of the listener. (first 3 elements are "at", second 3 are "up") */
		listenerOri =
		    BufferUtils.createFloatBuffer(6).put(new float[] { 0.0f, 0.0f, -1.0f,  0.0f, 1.0f, 0.0f });
	}
	
	public void SetListenerValuse(Vector3f pos,Vector3f vel,float rx,float ry,float rz) {
		/** Position of the listener. */
		listenerPos = BufferUtils.createFloatBuffer(3).put(new float[] { pos.x, pos.y, pos.z });
		 
		/** Velocity of the listener. */
		listenerVel = BufferUtils.createFloatBuffer(3).put(new float[] { vel.x, vel.y, vel.z });
		 
		/** Orientation of the listener. (first 3 elements are "at", second 3 are "up") 
		 * Calculating what direction is forward depending on the rotation values*/
		
		Vector3f dir=new Vector3f();
		Matrix4f mat = new Matrix4f();
		mat.rotateX(rx);
		mat.rotateY(ry);
		mat.rotateZ(rz);
		Maths.angleMove(mat, dir);
		
		listenerOri =
		    BufferUtils.createFloatBuffer(6).put(new float[] { dir.x, dir.y, dir.z,  0.0f, 1.0f, 0.0f });
	}

}

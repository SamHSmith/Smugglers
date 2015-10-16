package sound;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

import toolbox.Maths;

public class Listener {
	FloatBuffer listenerOri;

	public Listener() {

	}

	public void UpdateListenerValuse(Vector3f pos, Vector3f vel, float rx,
			float ry, float rz) {

		/**
		 * Orientation of the listener. (first 3 elements are "at", second 3 are
		 * "up") Calculating what direction is forward depending on the rotation
		 * values
		 */

		Vector3f dir = new Vector3f(0, 0, -1);
		Matrix4f mat = new Matrix4f();
		mat.rotateX(rx);
		mat.rotateY(ry);
		mat.rotateZ(rz);
		Maths.angleMove(mat, dir);

		listenerOri = BufferUtils.createFloatBuffer(6).put(
				new float[] { dir.x, dir.y, dir.z, 0.0f, 1.0f, 0.0f });

		listenerOri.flip();
		AL10.alListener3f(AL10.AL_POSITION, pos.x, pos.y, pos.z);
		AL10.alListener3f(AL10.AL_VELOCITY, vel.x, vel.y, vel.z);
		AL10.alListener(AL10.AL_ORIENTATION, listenerOri);
	}

}

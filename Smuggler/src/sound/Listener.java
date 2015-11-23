package sound;

import java.nio.FloatBuffer;

import math3d.Matrix4f;
import math3d.Vector3f;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

import render.Camera;
import toolbox.Maths;

public class Listener {
	FloatBuffer listenerOri;

	public void UpdateListenerValuse(Vector3f vel, Camera cam) {

		/**
		 * Orientation of the listener. (first 3 elements are "at", second 3 are
		 * "up") Calculating what direction is forward depending on the rotation
		 * values
		 */

		Vector3f dir = new Vector3f(0, 0, -1);
		Matrix4f mat = new Matrix4f();
		mat.rotateX(cam.getRx());
		mat.rotateY(cam.getRy());
		mat.rotateZ(cam.getRz());
		Maths.angleMove(mat, dir);

		listenerOri = BufferUtils.createFloatBuffer(6).put(
				new float[] { dir.x, dir.y, dir.z, 0.0f, 1.0f, 0.0f });

		listenerOri.flip();
		AL10.alListener3f(AL10.AL_POSITION, cam.getPosition().x, cam.getPosition().y, cam.getPosition().z);
		AL10.alListener3f(AL10.AL_VELOCITY, vel.x, vel.y, vel.z);
		AL10.alListener(AL10.AL_ORIENTATION, listenerOri);
	}

}

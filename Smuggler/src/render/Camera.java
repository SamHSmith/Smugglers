package render;

import javax.vecmath.Vector3f;

import org.lwjgl.glfw.GLFWKeyCallback;

public class Camera {

	Vector3f pos = new Vector3f();
	private float pitch;
	private float yaw;
	private float roll;

	public void move(float x, float y, float z) {

		pos.x += x;
		pos.y += y;
		pos.z += z;

	}

	public Vector3f getPos() {
		return pos;
	}

	public void setPos(Vector3f pos) {
		this.pos = pos;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getRoll() {
		return roll;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}

}

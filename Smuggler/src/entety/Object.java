package entety;

import javax.vecmath.Vector3f;

import models.Texturedmodel;

public interface Object {

	public void move(float x, float y, float z);

	public void rotate(float rx, float ry, float rz);

	public void Scale(float s);

	public Vector3f getVelocity();

	public void setVelocity(Vector3f Velocity);

	public Vector3f getPosition();

	public void setPosition(Vector3f position);

	public float getRx();

	public void setRx(float rx);

	public float getRy();

	public void setRy(float ry);

	public float getRz();

	public void setRz(float rz);

	public float getScale();

	public void setScale(float scale);

	public Texturedmodel getModel();

	public void setModel(Texturedmodel model);

}

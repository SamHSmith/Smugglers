package entity;

import java.util.ArrayList;



import org.joml.Vector3f;

import models.Texturedmodel;

public interface BasicEntity {

	public void move(float x, float y, float z);

	public void rotate(float rx, float ry, float rz);

	public void Scale(float s);

	public Vector3f getVelocity();

	public void setRotVelocity(Vector3f Velocity);
	
	public Vector3f getRotVelocity();

	public void setVelocity(Vector3f Velocity);
	
	public void ModifyVelocity(Vector3f Velocity);
	
	public void ModifyRotVelocity(Vector3f Velocity);

	public Vector3f getPosition();

	public void setPosition(Vector3f position);

	public float getRx();

	public void setRx(float rx);

	public float getRy();

	public void setRy(float ry);
	
	public boolean isDyn();

	public void setDyn(boolean dyn);

	public float getRz();

	public void setRz(float rz);

	public float getScale();

	public void setScale(float scale);

	public Texturedmodel getModel();
	
	public BasicEntity getMaster();
	
	public void setMaster(BasicEntity master);
	
	public void addObject(BasicEntity object);
	
	public void removeObject(BasicEntity object);

	public void setModel(Texturedmodel model);
	
	public ArrayList<BasicEntity> getSubObjects();

	public void setSubObjects(ArrayList<BasicEntity> subObjects);

}

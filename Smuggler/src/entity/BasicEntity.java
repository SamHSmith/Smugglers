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
	
	/**
	 * Calculates whether this object collides (overlaps) with another object b
	 * @param b
	 * @return True if the objects overlap, false otherwise
	 */
	public boolean collides(BasicEntity b);
	
	/**
	 * Indicates whether this object is hard or infinitely soft (and therefore cannot collide)
	 * Could perhaps be replaced by using 0 mass (Which would have the same physical effect)
	 * TODO Replace isHard with mass of 0
	 * @return True if hard, false if infintely soft
	 */
	public boolean isHard();
	
	/** 
	 * The vector offset between the position of the object (reflected in getPosition) and the position of the 
	 * geographical center of the sphere that can be considered to approximate the object
	 * @return offset vector
	 */
	public Vector3f getPositionOffset(); 
	/**
	 * The radius of the approximated sphere reflecting the rough shape of the object for the purposes of collision detection
	 * @return radius in m
	 */
	public float getRadius();
	
	/**
	 * Mass of the object in kg
	 * @return mass in kg
	 */
	public float getMass();
	

}

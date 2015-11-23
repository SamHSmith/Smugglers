package entity;

import math3d.Vector3f;
import models.Texturedmodel;

public class Warp extends Effect {
	
	/**
	 * This class is a graphical warp. Remember that the if a vertex is outside the range then all distortion is removed.
	 * It is adviced to tweek the warp until it is right.
	 * @serialField strength the strength of the warp
	 * @serialField range the range of the warp
	 * 
	 * @author Sam
	 */
	
	private float strength;
	private float range;

	public Warp(Vector3f velocity, Vector3f position, float rx, float ry,
			float rz, float scale, Texturedmodel model,float range,float strength) {
		super(velocity, position, rx, ry, rz, scale, model, new Vector3f());
		
		this.strength=strength;
		this.range=range;
	}
	
	public Warp(Vector3f velocity, Vector3f position, float rx, float ry,
			float rz, float scale,float range,float strength) {
		super(velocity, position, rx, ry, rz, scale, new Vector3f());
		
		this.strength=strength;
		this.range=range;
	}

	public float getStrength() {
		return strength;
	}

	public void setStrength(float strength) {
		this.strength = strength;
	}

	public float getRange() {
		return range;
	}

	public void setRange(float range) {
		this.range = range;
	}
	
	

}

package entity;

import models.Texturedmodel;

import org.joml.Vector3f;

public class Warp extends Effect {
	
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

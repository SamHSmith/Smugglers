package entity;

import controler.UniverseHandler;
import math3d.Vector3f;
import models.Texturedmodel;

public class Partical extends Effect {
	private int decatime;
	private int decatimer;
	
	/**
	 * @param velocity
	 * @param rotvelocity
	 * @param position
	 * @param rx
	 * @param ry
	 * @param rz
	 * @param scale
	 * @param model
	 * @param decatime
	 */
	
	public Partical(Vector3f velocity, Vector3f rotvelocity, Vector3f position,
			float rx, float ry, float rz, float scale, Texturedmodel model,
			int decatime) {
		super(velocity, rotvelocity, position, rx, ry, rz, scale, model, new Vector3f());
		this.decatime = decatime;
		this.decatimer = 0;
	}
	
	@Override
	public void tick(){
		decatimer++;
		System.out.println(decatimer);
		if(decatimer>=decatime){
			UniverseHandler.removeEntity(this);
		}
	}
}

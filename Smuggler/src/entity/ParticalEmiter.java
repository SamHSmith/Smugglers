package entity;

import java.util.Random;

import controler.UniverseHandler;
import math3d.Vector3f;
import models.Texturedmodel;

public class ParticalEmiter extends Effect {

	private int particals_per_tick;
	private int partical_deca_time;
	private Texturedmodel model;
	private Vector3f generaldirection = new Vector3f();

	/**
	 * @param velocity
	 *            velocity of the effect
	 * @param position
	 *            position of the effect
	 * @param rx
	 *            rotation x
	 * @param ry
	 *            rotation y
	 * @param rz
	 *            rotation z
	 * @param particals_per_tick
	 *            the amount of particals that will be created every tick so it
	 *            is basicly the intensity
	 * @param model
	 *            the partical
	 */
	public ParticalEmiter(Vector3f velocity, Vector3f position,
			Vector3f generaldirection, float rx, float ry, float rz,
			float scale, int particals_per_tick, int partical_deca_time,
			Texturedmodel model) {
		super(velocity, position, rx, ry, rz, scale, new Vector3f());
		this.particals_per_tick = particals_per_tick;
		this.partical_deca_time = partical_deca_time;
		this.model = model;
		this.generaldirection = generaldirection;
	}

	@Override
	public void tick() {
		Random r = new Random();
		for (int i = 0; i < particals_per_tick; i++) {

			UniverseHandler.addEntity(new Partical(new Vector3f(
					generaldirection.x - ((r.nextFloat() - 0.5f)/2),
					generaldirection.y - ((r.nextFloat() - 0.5f)/2),
					generaldirection.z - ((r.nextFloat() - 0.5f)/2)),
					new Vector3f(), new Vector3f(this.position), i, i, i,
					this.scale, model, partical_deca_time));
		}
	}

}

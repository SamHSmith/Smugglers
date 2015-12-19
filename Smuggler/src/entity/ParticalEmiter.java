package entity;

import java.util.Random;

import math3d.Vector3f;
import models.ParticalTexture;
import controler.UniverseHandler;

public class ParticalEmiter extends Effect {

	private float particals_per_tick;
	private int partical_deca_time;
	private Vector3f generaldirection = new Vector3f();
	private float errorRotation, errorScale, errorVelocity;
	private float particals_to_create = 0;
	private ParticalTexture texture;

	/**
	 * @param velocity
	 *            velocity of the effect
	 * @param position
	 *            position of the effect
	 * @param errorRotaition
	 *            the randomness of the rotation
	 * 
	 * @param errorScale
	 *            randomness of the scale
	 * 
	 * @param particals_per_tick
	 *            the amount of particals that will be created every tick so it
	 *            is basicly the intensity
	 */
	public ParticalEmiter(Vector3f velocity, Vector3f position,
			Vector3f generaldirection, float errorRotaition, float errorScale,
			float errorVelocity, float particals_per_tick,
			int partical_deca_time,ParticalTexture texture) {
		super(velocity, position, 0, 0, 0, 0, new Vector3f());
		this.particals_per_tick = particals_per_tick;
		this.partical_deca_time = partical_deca_time;
		this.generaldirection = generaldirection;
		this.errorRotation = errorRotaition;
		this.errorScale = errorScale;
		this.errorVelocity = errorVelocity;
		this.texture=texture;
	}

	@Override
	public void tick() {
		Random r = new Random();

		particals_to_create += particals_per_tick;

		int spawn_amount = (int) Math.floor(particals_to_create);

		for (int i = 0; i < spawn_amount; i++) {

			float randomX, randomY, randomZ;

			randomX = (r.nextFloat() * errorVelocity)
					- (r.nextFloat() * errorVelocity / 2);

			randomY = (r.nextFloat() * errorVelocity)
					- (r.nextFloat() * errorVelocity / 2);

			randomZ = (r.nextFloat() * errorVelocity)
					- (r.nextFloat() * errorVelocity / 2);

			particals_to_create--;

			UniverseHandler.addEntity(new Partical(new Vector3f(
					generaldirection.x + randomX, generaldirection.y + randomY,
					generaldirection.z + randomZ), new Vector3f(this
					.getPosition()), r.nextFloat() * 360 * errorRotation, (r
					.nextFloat() + 0.5f) * errorScale, partical_deca_time,texture));
		}
	}

}

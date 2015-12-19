package entity;

import math3d.Vector2f;
import math3d.Vector3f;
import models.ParticalTexture;
import controler.UniverseHandler;

public class Partical extends Effect {
	private int decatime;
	private int decatimer;
	private ParticalTexture texture;
	private Vector2f textoffset1 = new Vector2f();
	private Vector2f textoffset2 = new Vector2f();
	private float blend;

	/**
	 * @param velocity
	 * @param rotvelocity
	 * @param position
	 * @param rx
	 * @param ry
	 * @param rz
	 * @param scale
	 * @param decatime
	 */

	public Partical(Vector3f velocity, Vector3f position, float rz,
			float scale, int decatime, ParticalTexture texture) {
		super(velocity, new Vector3f(), position, 0, 0, rz, scale, null,
				new Vector3f());
		this.decatime = decatime;
		this.decatimer = 0;
		this.texture = texture;
	}

	@Override
	public void tick() {
		super.tick();
		decatimer++;
		
		updateTextcoordinfo();
		
		if (decatimer >= decatime) {
			UniverseHandler.removeEntity(this);
		}
		
		
	}

	public ParticalTexture getTexture() {
		return texture;
	}

	public Vector2f getTextoffset1() {
		return textoffset1;
	}

	public Vector2f getTextoffset2() {
		return textoffset2;
	}

	public float getBlend() {
		return blend;
	}

	private void updateTextcoordinfo() {
		float lifefactor = (float) decatimer / (float) decatime;
		int stageCount = texture.getNumberofrows() * texture.getNumberofrows();
		float atlasProgression = (float)lifefactor * (float)stageCount;

		int index1 = (int) Math.floor(atlasProgression);
		int index2 = index1 < stageCount - 1 ? index1 + 1 : index1;
		blend = atlasProgression % 1;

		setTextureoffset(textoffset1, index1);
		setTextureoffset(textoffset2, index2);

	}

	private void setTextureoffset(Vector2f offset, int index) {

		int collum = index % texture.getNumberofrows();
		int row = index / texture.getNumberofrows();

		offset.x = (float)collum / texture.getNumberofrows();
		offset.y = (float)row / texture.getNumberofrows();

	}

}

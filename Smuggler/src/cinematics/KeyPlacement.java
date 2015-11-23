package cinematics;

import math3d.Vector3f;

public class KeyPlacement {
	private float rx,ry,rz;
	private Vector3f pos;
	private int time;
	/**
	 * @param rx rotation x
	 * @param ry rotation y
	 * @param rz rotation z
	 * @param pos position
	 * @param time when this is the state to be in
	 */
	public KeyPlacement(float rx, float ry, float rz,int time, Vector3f pos) {
		super();
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
		this.pos = pos;
		this.time=time;
	}
	
	public float getRx() {
		return rx;
	}
	public void setRx(float rx) {
		this.rx = rx;
	}
	public float getRy() {
		return ry;
	}
	public void setRy(float ry) {
		this.ry = ry;
	}
	public float getRz() {
		return rz;
	}
	public void setRz(float rz) {
		this.rz = rz;
	}
	public Vector3f getPos() {
		return pos;
	}
	public void setPos(Vector3f pos) {
		this.pos = pos;
	}

}

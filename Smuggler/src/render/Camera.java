package render;

import math3d.Vector3f;

public class Camera {
	
	private Vector3f position;
	private float rx,ry,rz;
	private boolean cin;
	/**
	 * 
	 * This class reperesents the camera
	 * 
	 * @param position the position
	 * @param rx rotation x
	 * @param ry rotation y
	 * @param rz rotation z
	 * @param cin if the camera is currently being cinematic
	 */
	public Camera(Vector3f position, float rx, float ry, float rz,boolean cin) {
		super();
		this.position = position;
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
		this.cin=cin;
	}
	public Vector3f getPosition() {
		return position;
	}
	public void setPosition(Vector3f position) {
		this.position = position;
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
	public boolean isCin() {
		return cin;
	}
	public void setCin(boolean cin) {
		this.cin = cin;
	}
	
	

}

package entety;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import models.Texturedmodel;

public class PhiObject implements Object {
	
	ArrayList<Object> subObjects= new ArrayList<Object>();
	boolean dyn;
	Vector3f position=new Vector3f();
	Vector3f velocity = new Vector3f();
	float rx = 0;
	float ry = 0;
	float rz = 0;
	float scale = 0;
	Texturedmodel model = null;

	public PhiObject(Vector3f position, Vector3f velocity, float rx, float ry,
			float rz, float scale, Texturedmodel model,boolean dyn) {
		super();
		this.position = position;
		this.velocity = velocity;
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
		this.scale = scale;
		this.model = model;
		this.dyn=dyn;
	}

	@Override
	public void move(float x, float y, float z) {
		
		position.x+=x;
		position.y+=y;
		position.z+=z;

	}

	@Override
	public void rotate(float rx, float ry, float rz) {
		this.rx+=rx;
		this.ry+=ry;
		this.rz+=rz;

	}

	@Override
	public void Scale(float s) {
		this.scale+=s;

	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector3f velocity) {
		this.velocity = velocity;
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

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public Texturedmodel getModel() {
		return model;
	}

	public void setModel(Texturedmodel model) {
		this.model = model;
	}

	public ArrayList<Object> getSubObjects() {
		return subObjects;
	}

	public void setSubObjects(ArrayList<Object> subObjects) {
		this.subObjects = subObjects;
	}

	public boolean isDyn() {
		return dyn;
	}

	public void setDyn(boolean dyn) {
		this.dyn = dyn;
	}

	

}

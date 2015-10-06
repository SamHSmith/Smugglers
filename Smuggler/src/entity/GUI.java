package entity;

import java.util.ArrayList;

import models.Texturedmodel;

import org.joml.Vector3f;

public class GUI implements BasicEntity {

	Vector3f position=new Vector3f();
	float rx = 0;
	float ry = 0;
	float rz = 0;
	float scale = 0;
	Texturedmodel model = null;

	public GUI(Vector3f position, float rx, float ry,
			float rz, float scale, Texturedmodel model,boolean threeD) {
		super();
		this.position = position;
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
		this.scale = scale;
		this.model = model;
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
	public void ModifyRotVelocity(Vector3f Velocity) {}

	@Override
	public void Scale(float s) {
		this.scale += s;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getVelocity() {
		return new Vector3f();
	}

	public void setVelocity(Vector3f velocity) {}

	public float getRx() {
		return rx;
	}

	public void setRx(float rx) {
		this.rx = rx;
	}

	public float getRy() {
		return ry;
	}

	public BasicEntity getMaster() {
		return null;
	}

	public void setMaster(BasicEntity master) {}

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

	public ArrayList<BasicEntity> getSubObjects() {
		return null;
	}

	public void setSubObjects(ArrayList<BasicEntity> subObjects) {}
	

	public boolean isDyn() {
		return false;
	}

	public void setDyn(boolean dyn) {}

	@Override
	public void addObject(BasicEntity object) {}

	@Override
	public void removeObject(BasicEntity object) {}
	
	@Override
	public void ModifyVelocity(Vector3f Velocity) {}

	@Override
	public void setRotVelocity(Vector3f Velocity) {}

	@Override
	public Vector3f getRotVelocity() {
		return new Vector3f();
	}

}

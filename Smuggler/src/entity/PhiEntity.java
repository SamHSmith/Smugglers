package entity;

import java.util.ArrayList;



import org.joml.Vector3f;

import models.Texturedmodel;

public class PhiEntity implements BasicEntity {
	
	boolean dyn;
	Vector3f velocity = new Vector3f();
	ArrayList<BasicEntity> subObjects= new ArrayList<BasicEntity>();
	BasicEntity master=null;
	Vector3f position=new Vector3f();
	float rx = 0;
	float ry = 0;
	float rz = 0;
	float scale = 0;
	Texturedmodel model = null;

	public PhiEntity(Vector3f position, Vector3f velocity, float rx, float ry,
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
		
		for(int i=0;i<subObjects.size();i++){
			subObjects.get(i).move(x, y, z);
		}

	}

	@Override
	public void rotate(float rx, float ry, float rz) {
		this.rx+=rx;
		this.ry+=ry;
		this.rz+=rz;
		
		for(int i=0;i<subObjects.size();i++){
			subObjects.get(i).rotate(rx, ry, rz);
		}
		
	}

	@Override
	public void Scale(float s) {
		this.scale += s;
		
		for (int i = 0; i < subObjects.size(); i++) {
			subObjects.get(i).Scale(s);
		}
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

	public BasicEntity getMaster() {
		return master;
	}

	public void setMaster(BasicEntity master) {
		this.master = master;
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

	public ArrayList<BasicEntity> getSubObjects() {
		return subObjects;
	}

	public void setSubObjects(ArrayList<BasicEntity> subObjects) {
		this.subObjects = subObjects;
	}
	

	public boolean isDyn() {
		return dyn;
	}

	public void setDyn(boolean dyn) {
		this.dyn = dyn;
	}

	@Override
	public void addObject(BasicEntity object) {
		this.subObjects.add(object);
		object.setMaster(this);
		
	}

	@Override
	public void removeObject(BasicEntity object) {
		this.subObjects.remove(object);
		object.setMaster(null);
	}

	

}

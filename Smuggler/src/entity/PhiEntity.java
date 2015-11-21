package entity;

import java.util.ArrayList;



import org.joml.Vector3f;

import models.Texturedmodel;

public class PhiEntity implements BasicEntity {
	
	Vector3f velocity = new Vector3f();
	Vector3f rotVelocity = new Vector3f();
	ArrayList<BasicEntity> subObjects= new ArrayList<BasicEntity>();
	BasicEntity master=null;
	Vector3f position=new Vector3f();
	float rx = 0;
	float ry = 0;
	float rz = 0;
	float scale = 0;
	float mass = 0;
	Texturedmodel model = null;
	
    // Assuming the object can be approximated as a sphere, this gives the offset of the estimated 
	// center of that sphere relative to the position stored in @position
	Vector3f positionOffset;
	
	// Assuming the object can be approximated as a sphere, this gives the radius of that sphere
	float radius = 0;
	


	public PhiEntity(Vector3f position, Vector3f velocity,Vector3f rotVelocity, float rx, float ry,
			float rz, float scale, float mass, Texturedmodel model) {
		super();
		this.position = position;
		this.velocity = velocity;
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
		this.scale = scale;
		this.mass = mass;
		this.model = model;
		this.rotVelocity=rotVelocity;
		calculateCenterAndRadius();
	}
	
	/** 
	 * Calculate the centre of the object and its radius from its model 
	 */
	private void calculateCenterAndRadius() {
		//TODO Work out properly based on the objects shape and size
		radius = 2f*scale; // Sam said the spheres were 1m wide
		positionOffset = new Vector3f(0,0,0);
		
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

	public Vector3f getRotVelocity() {
		return rotVelocity;
	}

	public void setRotVelocity(Vector3f rotVelocity) {
		this.rotVelocity = rotVelocity;
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

	@Override
	public void ModifyVelocity(Vector3f Velocity) {
		this.velocity.add(Velocity);
		
	}
	
	@Override
	public void ModifyRotVelocity(Vector3f Velocity) {
		this.rotVelocity.add(Velocity);
		
	}

	
	@Override
	public boolean collides(BasicEntity b) {
		if (!(b.isHard())) return false;
		
		Vector3f centerA = getPosition();
		centerA.add(getPositionOffset());
		
		Vector3f centerB = b.getPosition();
		centerB.add(b.getPositionOffset());	
		
		float totalGap = centerA.distance(centerB);
		float totalRadius = getRadius()+b.getRadius();
		return totalGap<totalRadius ;
		
		
	}
	
	@Override
	public boolean isHard() {
		return true;
			}

	@Override
	/**
	 * getter for positionOffset
	 * @return the offset between the objects position and the center of the approximated sphere representing the object
	 */
	public Vector3f getPositionOffset() {
		return positionOffset;
	}	
	
	@Override
	/**
	 * getter for radius
	 * @return the radius of the approximated sphere representing the object
	 */
	public float getRadius() {
		return radius;
	}

	@Override
	public float getMass() {
		return mass;
	}
	
	
}

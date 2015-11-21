package gui;

import input.Mouse;

import java.util.ArrayList;

import models.Texturedmodel;

import org.joml.Vector3f;

import controler.GameState;
import entity.BasicEntity;

public class GUI implements BasicEntity {

	Vector3f position=new Vector3f();
	float rx = 0;
	float ry = 0;
	float rz = 0;
	float width = 0;
	float height = 0;
	Texturedmodel model = null;
	private ArrayList<GUI> subGUIs;
	private GameState showstate;

	public GUI(Vector3f position, float rx, float ry,
			float rz, float width, float height,GameState showstate, Texturedmodel model) {
		super();
		this.position = position;
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
		this.width = width;
		this.height = height;
		this.model = model;
		this.showstate=showstate;
		subGUIs=new ArrayList<GUI>();
	}
	
	public void add(GUI gui){
		subGUIs.add(gui);
	}
	
	public void remove(GUI gui){
		subGUIs.remove(gui);
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
	public void Scale(float s) {}
	
	public void Scale(float width,float height){
		this.width+=width;
		this.height+=height;
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
		return 0;}

	public void setScale(float scale) {}

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

	public ArrayList<GUI> getSubGUIs() {
		return subGUIs;
	}

	public void setSubGUIs(ArrayList<GUI> subGUIs) {
		this.subGUIs = subGUIs;
	}
	public void update(Mouse mouse, GameState state){
		if(state==getShowstate()||GameState.All==getShowstate()){
		for(GUI gui:subGUIs){
			gui.update(mouse, state);
		}
		}
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public GameState getShowstate() {
		return showstate;
	}

	public void setShowstate(GameState showstate) {
		this.showstate = showstate;
	}
	
	public void prepare(){
		for (GUI gui : this.getSubGUIs()) {
			gui.prepare();
		}
	}
	
	public void endrendering(){}

	@Override
	public boolean collides(BasicEntity b) {
		return false;
	}

	@Override
	public boolean isHard() {
		return false;
	}

	@Override
	public Vector3f getPositionOffset() {
		return new Vector3f(0,0,0);
	}

	@Override
	public float getRadius() {
		return 0;
	}

	@Override
	public float getMass() {
		return 0;
	}
	
	

}

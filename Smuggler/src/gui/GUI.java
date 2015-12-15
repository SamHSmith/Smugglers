package gui;

import input.Mouse;

import java.util.ArrayList;

import math3d.Vector3f;
import models.Texturedmodel;
import controler.GameState;

public class GUI{

	protected Vector3f position=new Vector3f();
	protected float rx = 0;
	protected float ry = 0;
	protected float rz = 0;
	protected float width = 0;
	protected float height = 0;
	protected Texturedmodel model = null;
	private GameState showstate;
	private ArrayList<GUI> subGUIs;

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
		this.subGUIs=new ArrayList<GUI>();
	}
	
	public GUI(Vector3f position, float rx, float ry,
			float rz, float width, float height,GameState showstate) {
		super();
		this.position = position;
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
		this.width = width;
		this.height = height;
		this.showstate=showstate;
		this.subGUIs=new ArrayList<GUI>();
	}

	public void move(float x, float y, float z) {
		
		position.x+=x;
		position.y+=y;
		position.z+=z;
		

	}

	public void rotate(float rx, float ry, float rz) {
		this.rx+=rx;
		this.ry+=ry;
		this.rz+=rz;
	}
	
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

	public void setRy(float ry) {
		this.ry = ry;
	}

	public float getRz() {
		return rz;
	}

	public void setRz(float rz) {
		this.rz = rz;
	}

	public Texturedmodel getModel() {
		return model;
	}

	public void setModel(Texturedmodel model) {
		this.model = model;
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
	
	public void addGUI(GUI gui){
		subGUIs.add(gui);
	}
	
	public void removeGUI(GUI gui){
		subGUIs.remove(gui);
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

}

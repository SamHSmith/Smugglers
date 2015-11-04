package universe;

import java.util.ArrayList;

import entity.BasicEntity;
import entity.GUI;
import entity.Light;
import entity.Warp;
import input.MainLoop;

public class UniverseHandler {
	protected MainLoop loop;
	public ArrayList<BasicEntity> entitys;
	public ArrayList<GUI> guis;
	public ArrayList<Light> lights;
	public Warp warp;
	
	public UniverseHandler(){
		loop=new MainLoop(this);
	}
	
	public void updatepositions() {
		for (BasicEntity ent : entitys) {
			ent.getPosition().add(ent.getVelocity());
			ent.rotate(ent.getRotVelocity().x, ent.getRotVelocity().y,
					ent.getRotVelocity().z);
		}
	}

	public static void main(String[] args) {
		new UniverseHandler();
	}

}

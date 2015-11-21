package controler;

import input.Key;
import input.MainLoop;
import input.Mouse;
import models.Texturedmodel;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import entity.PhiEntity;
import fontMeshCreator.FontType;
import gui.MTextBox;

public class Exicuter extends MTextBox {
	UniverseHandler unihand;

	public Exicuter(Vector3f position, float rx, float ry, float rz,
			float width, float height, GameState showstate,
			Texturedmodel model, float fontsize, FontType font, Vector3f color,
			UniverseHandler unihand) {
		super(position, rx, ry, rz, width, height, showstate, model, fontsize,
				font, color);
		this.unihand = unihand;
	}

	public void act(String s) {
		String[] com = s.split(" ");
		
		System.out.println("Doing something...");
		
		if (com[0] == "spawn") {
			unihand.entitys.add(new PhiEntity(new Vector3f(Float
					.parseFloat(com[2]), Float.parseFloat(com[3]), Float
					.parseFloat(com[4])), new Vector3f(), new Vector3f(), 0, 0,
					0, 1, 1.0f,UniverseHandler.models[Integer.parseInt(com[1])]));
			
			System.out.println("Object was created");
		}else{
			System.out.println("Invalid Command: "+s);
		}
	}

	@Override
	public void enter() {
		act(this.getStext());
		this.setStext("");
	}

	@Override
	public void update(Mouse mouse, GameState state) {
		if (MainLoop.getKey(GLFW.GLFW_KEY_K) == Key.Press) {
			MainLoop.setTypeStream(this);
		}
	}

}

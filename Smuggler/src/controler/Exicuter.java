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
	private boolean hosting;

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
		
<<<<<<< HEAD
		if (com[0] == "host") {
			hosting=true;
			System.out.println("What port?");
=======
		System.out.println("Doing something...");
		
		if (com[0] == "spawn") {
			unihand.entitys.add(new PhiEntity(new Vector3f(Float
					.parseFloat(com[2]), Float.parseFloat(com[3]), Float
					.parseFloat(com[4])), new Vector3f(), new Vector3f(), 0, 0,
					0, 1, 1.0f,UniverseHandler.models[Integer.parseInt(com[1])]));
			
			System.out.println("Object was created");
>>>>>>> branch 'master' of https://github.com/Sainstprogramer/Smugglers.git
		}else{
			System.out.println("the Command '"+com[0]+"' is not valid");
		}
	}

	@Override
	public void enter() {
		act(this.getStext());
		this.setStext("");
		MainLoop.setTypeStream(null);
	}

	@Override
	public void update(Mouse mouse, GameState state) {
		if (MainLoop.getKey(GLFW.GLFW_KEY_K) == Key.Press) {
			MainLoop.setTypeStream(this);
		}
	}

}

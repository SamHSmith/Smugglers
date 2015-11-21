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
		
		if (com[0] == "host") {
			hosting=true;
			System.out.println("What port?");
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

package gui;

import math3d.Vector3f;
import models.Texturedmodel;
import controler.GameState;

public class MPanel extends GUI {

	
	public MPanel(Vector3f position, float rx, float ry, float rz, float width, float height,
			GameState showstate,Texturedmodel model) {
		super(position, rx, ry, rz, width, height, showstate, model);
	}

}

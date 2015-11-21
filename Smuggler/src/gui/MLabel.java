package gui;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import models.Texturedmodel;

import org.joml.Vector2f;
import org.joml.Vector3f;

import controler.GameState;

public class MLabel extends GUI {

	GUIText text;

	public MLabel(Vector3f position, float rx, float ry, float rz, float width,
			float height, GameState showstate, Texturedmodel model,
			String title, FontType font, Vector3f colour, float fontsize) {
		super(position, rx, ry, rz, width, height, showstate, model);

		text = new GUIText(title, fontsize, font, new Vector2f(
				(position.x + 0.5f) - (width / 2), (position.y + 0.5f)
						- (height / 2) + (0.03f / fontsize)), width, true);
		text.setColour(colour.x, colour.y, colour.z);

		TextMaster.removeText(text);
	}

	@Override
	public void prepare() {
		TextMaster.removeText(text);
		
		for (GUI gui : this.getSubGUIs()) {
			gui.prepare();
		}
	}

	@Override
	public void endrendering() {
		TextMaster.loadText(text);
	}

}

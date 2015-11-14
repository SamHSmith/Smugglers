package gui;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import input.Mouse;
import models.Texturedmodel;

import org.joml.Vector2f;
import org.joml.Vector3f;

import controler.GameState;

public class MButton extends GUI {

	ActivationListener act;
	GUIText text;
	String textcontent;

	public MButton(Vector3f position, float rx, float ry, float rz,
			float width, float height, GameState showstate,
			Texturedmodel model, ActivationListener act, String title,
			FontType font, Vector3f colour, float fontsize) {
		super(position, rx, ry, rz, width, height, showstate, model);
		this.act = act;

		text = new GUIText(title, fontsize, font, new Vector2f(
				(position.x + 0.5f) - (width / 2), (position.y + 0.5f)
						- (height / 2) + (0.03f / fontsize)), width, true,
				showstate);
		text.setColour(colour.x, colour.y, colour.z);

		TextMaster.removeText(text);
	}

	@Override
	public void update(Mouse mouse) {
		if (mouse.isLeft()) {
			float minx = position.x - (width);
			float maxx = minx + width * 2;
			float miny = position.y - (height);
			float maxy = miny + height * 2;

			if (mouse.x < maxx && mouse.x > minx)
				if (mouse.y < maxy && mouse.y > miny) {
					act.Action();
				}
			for (GUI gui : this.getSubGUIs()) {
				gui.update(mouse);
			}
		}
	}

	@Override
	public void prepare() {
		TextMaster.removeText(text);
		for (GUI gui : this.getSubGUIs()) {
			gui.prepare();
		}
		System.out.println("preparing");
	}

	@Override
	public void endrendering() {
		TextMaster.loadText(text);
		System.out.println("endingrendering");
	}

}

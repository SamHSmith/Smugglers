package gui.buildingblocks;

import math3d.Vector2f;
import math3d.Vector3f;
import models.Texturedmodel;
import controler.GameState;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import gui.GUI;
import input.MainLoop;
import input.Mouse;
import input.Typestream;

public class MTextBox extends GUI implements Typestream {

	GUIText text;
	String stext = "";
	FontType font;
	float fontsize;
	private Vector3f color;

	public MTextBox(Vector3f position, float rx, float ry, float rz,
			float width, float height, GameState showstate,
			Texturedmodel model, float fontsize, FontType font, Vector3f color) {
		super(position, rx, ry, rz, width, height, showstate, model);

		this.font=font;
		this.color=color;
		this.fontsize=fontsize;
		
		text = new GUIText(stext, this.fontsize, font, new Vector2f(
				(position.x + 0.5f) - (width / 2), (position.y + 0.5f)
						- (height / 2) + (0.03f / fontsize)), width, true);
		text.setColour(color.x, color.y, color.z);
	}

	public MTextBox(Vector3f position, float rx, float ry, float rz,
			float width, float height, GameState showstate,
			Texturedmodel model, float fontsize, FontType font, Vector3f color,
			String stext) {
		super(position, rx, ry, rz, width, height, showstate, model);

		this.stext = stext;
		this.font=font;
		this.color=color;
		this.fontsize=fontsize;
		text = new GUIText(stext, this.fontsize, font, new Vector2f(
				(position.x + 0.5f) - (width / 2), (position.y + 0.5f)
						- (height / 2) + (0.03f / fontsize)), width, true);
		text.setColour(color.x, color.y, color.z);
	}

	@Override
	public void type(String nstext) {
		this.text.remove();
		this.stext = this.stext + nstext;
		text = new GUIText(stext, fontsize, font, new Vector2f(
				(position.x + 0.5f) - (width / 2), (position.y + 0.5f)
						- (height / 2) + (0.03f / fontsize)), width, true);
		text.setColour(color.x, color.y, color.z);
	}

	@Override
	public void update(Mouse mouse,GameState state) {
		if(state==getShowstate()||GameState.All==getShowstate()){
			if (mouse.isLeft()) {
				float minx = (position.x/10) - (width);
				float maxx = minx + (width*2);
				float miny = (position.y/10) - (height);
				float maxy = miny + (height*2);

				if (mouse.x < maxx && mouse.x > minx)
					if (mouse.y < maxy && mouse.y > miny) {
						MainLoop.setTypeStream(this);
					}
				for (GUI gui : this.getSubGUIs()) {
					gui.update(mouse, state);
				}
			}
		}
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

	@Override
	public void backspace() {
		text.remove();
		stext=stext.substring(0, stext.length()-1);
		text = new GUIText(stext, fontsize, font, new Vector2f(
				(position.x + 0.5f) - (width / 2), (position.y + 0.5f)
						- (height / 2) + (0.03f / fontsize)), width, true);
		text.setColour(color.x, color.y, color.z);
	}

	@Override
	public void enter() {}

	public String getStext() {
		return stext;
	}

	public void setStext(String stext) {
		this.stext = "";
		type(stext);
	}
	
	

}

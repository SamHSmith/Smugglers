package gui;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import models.Texturedmodel;

import org.joml.Vector2f;
import org.joml.Vector3f;

import controler.GameState;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;

public class MConsole extends MLabel {
	
	private MPrinstream p;
	private String stext="";
	private FontType font;
	private float fontsize;
	private Vector3f color;
	private int max_chars=200;
	

	public MConsole(Vector3f position, float rx, float ry, float rz,
			float width, float height, GameState showstate,
			Texturedmodel model, String title, FontType font, Vector3f colour,
			float fontsize) {
		super(position, rx, ry, rz, width, height, showstate, model, title, font,
				colour, fontsize);
		this.font=font;
		this.color=colour;
		this.fontsize=fontsize;
		stext=title;
		try {
			p=new MPrinstream(this);
		} catch (FileNotFoundException e) {}
	}
	
	public void type(String nstext) {
		this.text.remove();
		this.stext = this.stext +"          "+ nstext;
		
		StringBuilder b=new StringBuilder();
		
		if(stext.length()>max_chars){
			char[] chars=stext.toCharArray();
			
			for(int i =0;i<max_chars;i++){
				b.append(chars[chars.length-max_chars+i]);
			}
			stext=b.toString();
		}
		
		text = new GUIText(stext, fontsize, font, new Vector2f(
				(position.x + 0.5f) - (width / 2), (position.y + 0.5f)
						- (height / 2) + (0.03f / fontsize)), width, true);
		text.setColour(color.x, color.y, color.z);
	}
	
	public void reset(){
		stext="";
	}

	public PrintStream getP() {
		return p;
	}

	public void setP(MPrinstream p) {
		this.p = p;
	}

	public float getMax_chars() {
		return max_chars;
	}

	public void setMax_chars(int max_chars) {
		this.max_chars = max_chars;
	}
	
	

}

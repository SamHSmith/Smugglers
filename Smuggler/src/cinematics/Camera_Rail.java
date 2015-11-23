package cinematics;

import java.util.ArrayList;

import render.Camera;

public class Camera_Rail {
	ArrayList<KeyPlacement> rail;
	Camera cam;
	
	
	
	/**
	 * @param cam the camera
	 */
	public Camera_Rail(Camera cam) {
		super();
		this.cam = cam;
		rail=new ArrayList<KeyPlacement>();
	}

	public void tick() {
		
	}
	
	public void addPlacement(KeyPlacement place) {
		rail.add(place);
	}

}

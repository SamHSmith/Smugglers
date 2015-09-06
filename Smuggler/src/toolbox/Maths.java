package toolbox;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import render.MainLoop;


public class Maths {

	public static Matrix4f createnegativetransmat(Vector3f pos){
		Matrix4f m = new Matrix4f();
		Vector3f vec = new Vector3f(pos);
		vec.negate();
		m.translate(vec);
		
		return m;
	}
	
	public static Matrix4f createtransmat(Vector3f pos, float s){
		Matrix4f m = new Matrix4f();
		m.scale(s);
		m.translate(pos);
		
		return m;
	}
	
	public static Matrix4f createrotmat(float rx,float ry ,float rz){
		Matrix4f m = new Matrix4f();
		m.rotateX(rx);
		m.rotateY(ry);
		m.rotateZ(rz);
		
		return m;
	}
	
	public static Matrix4f setToProjection (Matrix4f m, float near, float far, float fov, float aspectRatio) {
		if (m==null)
			m = new Matrix4f();
		m.perspective(fov, (float)MainLoop.WIDTH/MainLoop.HEIGHT, 0.01f, 100.0f);
		return m;
	}

}

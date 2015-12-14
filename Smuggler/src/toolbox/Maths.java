package toolbox;

import math3d.Matrix4f;
import math3d.Vector3f;
import math3d.Vector4f;

import org.lwjgl.openal.AL10;


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
	
	public static String baddCharecterFilter(String s){
		
		String olds=new String(s);
		
		String news =new String(s.replaceAll("[^a-zA-Z.]", ""));
		
		return news;
	}
	
	public static Matrix4f createtransmat(Vector3f pos, float width, float height){
		Matrix4f m = new Matrix4f();
		m.scale(width, height, 0);
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
	public static Matrix4f flippedcreaterotmat(float rx,float ry ,float rz){
		Matrix4f m = new Matrix4f();
		
		
		m.rotateZ(rz);
		m.rotateY(ry);
		m.rotateX(rx);
		return m;
	}
	
	public static Matrix4f setToProjection (Matrix4f m, float near, float far, float fov, float aspectRatio) {
		if (m==null)
			m = new Matrix4f();
		m.perspective(fov, aspectRatio, near, far);
		return m;
	}

	public static Vector3f angleMove(Matrix4f mat,Vector3f move) {
		Vector4f vec = new Vector4f(move,1.0f);
		
		vec.mul(mat);
		
		
		return new Vector3f(vec.x,vec.y,vec.z);
	}
	 /**
	   * 1) Identify the error code.
	   * 2) Return the error as a string.
	   */
	  public static String getALErrorString(int err) {
	    switch (err) {
	      case AL10.AL_NO_ERROR:
	        return "AL_NO_ERROR";
	      case AL10.AL_INVALID_NAME:
	        return "AL_INVALID_NAME";
	      case AL10.AL_INVALID_ENUM:
	        return "AL_INVALID_ENUM";
	      case AL10.AL_INVALID_VALUE:
	        return "AL_INVALID_VALUE";
	      case AL10.AL_INVALID_OPERATION:
	        return "AL_INVALID_OPERATION";
	      case AL10.AL_OUT_OF_MEMORY:
	        return "AL_OUT_OF_MEMORY";
	      default:
	        return "No such error code";
	    }
	  }

}

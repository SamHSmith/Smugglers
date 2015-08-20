package toolbox;

import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

public class Maths {

	public static Matrix4f createrotmat(float rx, float ry, float rz, float s){
		Matrix4f mat = new Matrix4f();
		mat.setIdentity();
		mat.rotX(rx);
		mat.rotY(ry);
		mat.rotZ(rz);
		mat.setScale(s);
		return mat;
	}
	
	public static Matrix4f setToProjection (Matrix4f m, float near, float far, float fov, float aspectRatio) {
		if (m==null)
			m = new Matrix4f();
		m.setIdentity();
		float l_fd = (float)(1.0 / Math.tan((fov * (Math.PI / 180)) / 2.0));
		float l_a1 = (far + near) / (near - far);
		float l_a2 = (2 * far * near) / (near - far);
		m.m00 = l_fd / aspectRatio;
		m.m10 = 0;
		m.m20 = 0;
		m.m30 = 0;
		m.m01 = 0;
		m.m11 = l_fd;
		m.m21 = 0;
		m.m31 = 0;
		m.m02 = 0;
		m.m12 = 0;
		m.m22 = l_a1;
		m.m32 = -1;
		m.m03 = 0;
		m.m13 = 0;
		m.m23 = l_a2;
		m.m33 = 0;
		return m;
	}

}

package sound;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

public class Source {
	int source;
	
	public Source(Vector3f pos,Vector3f vel,Sound s) {
		/** Position of the source sound. */
		FloatBuffer sourcePos = BufferUtils.createFloatBuffer(3).put(new float[] { pos.x, pos.y, pos.z });
		 
		/** Velocity of the source sound. */
		FloatBuffer sourceVel = BufferUtils.createFloatBuffer(3).put(new float[] { vel.x, vel.y, vel.z });
		
		source=AL10.alGenSources();
		if (AL10.alGetError() != AL10.AL_NO_ERROR){
			 
			AL10.alSourcei(source, AL10.AL_BUFFER,   s.getBuff());
			AL10.alSourcef(source, AL10.AL_PITCH,    1.0f          );
			AL10.alSourcef(source, AL10.AL_GAIN,     1.0f          );
			AL10.alSource (source, AL10.AL_POSITION, sourcePos     );
			AL10.alSource (source, AL10.AL_VELOCITY, sourceVel     );
	}
	}
	
	public void Play(){
		AL10.alSourcePlay(source);
	}
	
	public void Destroy(){
		AL10.alDeleteSources(source);
	}
}

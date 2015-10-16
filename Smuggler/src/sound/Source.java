package sound;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;

public class Source {
	int source;

	public Source(Vector3f pos, Vector3f vel, float volume) {

		source = AL10.alGenSources();

		float max, min;

		min = volume;
		max = min * volume;

		if (AL10.alGetError() != AL10.AL_NO_ERROR) {
			AL10.alSourcef(source, AL10.AL_PITCH, 1.0f);
			AL10.alSourcef(source, AL10.AL_MAX_DISTANCE, max);
			AL10.alSourcef(source, AL10.AL_REFERENCE_DISTANCE, min);
			AL10.alSource3f(source, AL10.AL_POSITION, pos.x, pos.y, pos.z);
			AL10.alSource3f(source, AL10.AL_VELOCITY, vel.x, vel.y, vel.z);
			AL10.alSourcei(source, AL10.AL_LOOPING, AL10.AL_TRUE);
		}
	}

	public void update(Vector3f pos, Vector3f vel, float volume) {

		float max, min;

		min = volume;
		max = min * volume;
		
		AL10.alSourcef(source, AL10.AL_PITCH, 1.0f);
		AL10.alSourcef(source, AL10.AL_MAX_DISTANCE, max);
		AL10.alSourcef(source, AL10.AL_REFERENCE_DISTANCE, min);
		AL10.alSource3f(source, AL10.AL_POSITION, pos.x, pos.y, pos.z);
		AL10.alSource3f(source, AL10.AL_VELOCITY, vel.x, vel.y, vel.z);
		AL10.alSourcei(source, AL10.AL_LOOPING, AL10.AL_TRUE);
	}

	public void Play(Sound s) {
		AL10.alSourcei(source, AL10.AL_BUFFER, s.getBuff());
		AL10.alSourcePlay(source);
		AL10.alSourcei(source, AL10.AL_BUFFER, 0);
	}

	public void Destroy() {
		AL10.alDeleteSources(source);
	}
}

package sound;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;

public class Source {
	int source;

	public Source(Vector3f pos, Vector3f vel, Sound s) {

		source = AL10.alGenSources();
		if (AL10.alGetError() != AL10.AL_NO_ERROR) {
			AL10.alSourcei(source, AL10.AL_BUFFER, s.getBuff());
			AL10.alSourcef(source, AL10.AL_PITCH, 1.0f);
			AL10.alSourcef(source, AL10.AL_GAIN, 1.0f);
			AL10.alSource3f(source, AL10.AL_POSITION, pos.x, pos.y, pos.z);
			AL10.alSource3f(source, AL10.AL_VELOCITY, vel.x, vel.y, vel.z);
		}
	}

	public void update(Vector3f pos, Vector3f vel) {
		AL10.alSourcef(source, AL10.AL_PITCH, 1.0f);
		AL10.alSourcef(source, AL10.AL_GAIN, 1.0f);
		AL10.alSource3f(source, AL10.AL_POSITION, pos.x, pos.y, pos.z);
		AL10.alSource3f(source, AL10.AL_VELOCITY, vel.x, vel.y, vel.z);
	}

	public void Play() {
		AL10.alSourcePlay(source);
	}

	public void Destroy() {
		AL10.alDeleteSources(source);
	}
}

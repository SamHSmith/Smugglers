package sound;

import math3d.Vector3f;

import org.lwjgl.openal.AL10;

public class Source {
	int source;
	private boolean threed;

	public Source(Vector3f pos, Vector3f vel, float volume, boolean threed) {

		source = AL10.alGenSources();

		this.threed = threed;

		float max, min;

		min = volume;
		max = min * volume;

		if (AL10.alGetError() != AL10.AL_NO_ERROR) {
			AL10.alSourcef(source, AL10.AL_PITCH, 1.0f);
			if (threed) {
				AL10.alSourcef(source, AL10.AL_MAX_DISTANCE, max);
				AL10.alSourcef(source, AL10.AL_REFERENCE_DISTANCE, min);
			} else {
				AL10.alSourcef(source, AL10.AL_GAIN, volume);
			}

			AL10.alSource3f(source, AL10.AL_POSITION, pos.x, pos.y, pos.z);
			AL10.alSource3f(source, AL10.AL_VELOCITY, vel.x, vel.y, vel.z);
		}
	}

	public void update(Vector3f pos, Vector3f vel, float volume) {

		float max, min;

		min = volume;
		max = min * min;

		AL10.alSourcef(source, AL10.AL_PITCH, 1.0f);
		if (threed) {
			AL10.alSourcef(source, AL10.AL_MAX_DISTANCE, max);
			AL10.alSourcef(source, AL10.AL_REFERENCE_DISTANCE, min);
		} else {
			AL10.alSourcef(source, AL10.AL_GAIN, volume);
		}
		AL10.alSource3f(source, AL10.AL_POSITION, pos.x, pos.y, pos.z);
		AL10.alSource3f(source, AL10.AL_VELOCITY, vel.x, vel.y, vel.z);
	}

	public void Play(Sound s, boolean looping) {
		if (looping) {
			AL10.alSourcei(source, AL10.AL_LOOPING, AL10.AL_TRUE);
		} else {
			AL10.alSourcei(source, AL10.AL_LOOPING, AL10.AL_FALSE);
		}
		AL10.alSourcei(source, AL10.AL_BUFFER, s.getBuff());
		AL10.alSourcePlay(source);
		AL10.alSourcei(source, AL10.AL_BUFFER, 0);
	}

	public void Stop(Sound s) {
		AL10.alSourcei(source, AL10.AL_BUFFER, s.getBuff());
		AL10.alSourceStop(source);
		AL10.alSourcei(source, AL10.AL_BUFFER, 0);
	}

	public boolean isPlaying() {
		int playing = 0;
		AL10.alSourcei(source, AL10.AL_PLAYING, playing);

		if (playing == AL10.AL_TRUE) {
			return true;
		}
		return false;
	}

	public void Destroy() {
		AL10.alDeleteSources(source);
	}
}

package sound;

import java.io.IOException;

import org.lwjgl.openal.AL10;

public class Sound {
	int buffer;

	public Sound(String path) throws IOException {
		// Load wav data into a buffer.
		buffer=AL10.alGenBuffers();
		 
		WaveData waveFile = WaveData.create(path);
		AL10.alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
		waveFile.dispose();
	}

	public int getBuff() {
		return buffer;
	}

	public void setBuff(int buff) {
		this.buffer = buff;
	}
	
	public void destroy(){
		AL10.alDeleteBuffers(buffer);
	}

}

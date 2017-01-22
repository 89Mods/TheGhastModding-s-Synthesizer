package theGhastModding.synthesizer.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import jouvieje.bass.Bass;
import jouvieje.bass.BassInit;
import jouvieje.bass.defines.BASS_ATTRIB;
import jouvieje.bass.defines.BASS_CONFIG;
import jouvieje.bass.defines.BASS_DEVICE;
import jouvieje.bass.defines.BASS_MIDI;
import jouvieje.bass.defines.BASS_SAMPLE;
import jouvieje.bass.structures.BASS_INFO;
import jouvieje.bass.structures.BASS_MIDI_FONT;
import jouvieje.bass.structures.HSOUNDFONT;
import jouvieje.bass.structures.HSTREAM;
import jouvieje.bass.utils.BufferUtils;
import jouvieje.bass.utils.Pointer;

public class TGMSynthesizer {
	
	private static boolean started = false;
	private static int handle = 0;
	private static HSTREAM stream = null;
	private static int maxVoices = 500;
	private static float renderingLimit = 95;
	private static int volume = 100;
	private static final String VERSION = "1.2.0";
	
	/*public static void main(String[] args){
		try {
			startSynth(44100);
			loadFont("default.sf2");
			sendEvent(new MidiEvent(MidiEvent.MIDI_EVENT_PROGRAM, 1, 0,0));
			sendEvent(new MidiEvent(MidiEvent.MIDI_EVENT_NOTE, 1, 100, 60));
			Thread.sleep(1000);
			sendEvent(new MidiEvent(MidiEvent.MIDI_EVENT_NOTE,1,0,60));
			Thread.sleep(2000);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try {
			stopSynth();
		}catch(Exception e){
			e.printStackTrace();
		}
		System.exit(0);
	}*/
	
	/*
	 * Get the version of TGM's Synthesizer
	 */
	public static String getVersion(){
		return VERSION;
	}
	
	/**
	 * Get the current rendering time percentage
	 */
	public static float getRenderingTime() throws Exception {
        FloatBuffer fbfr = BufferUtils.newFloatBuffer(1);
        Bass.BASS_ChannelGetAttribute(handle, BASS_ATTRIB.BASS_ATTRIB_CPU, fbfr);
        return fbfr.get(0);
	}
	
	/**
	 * Get the currently set volume
	 */
	public static int getVolume(){
		return volume;
	}
	
	/**
	 * Set the volume.
	 * @param newVolume: value between 0 and 100
	 */
	public static void setVolume(int newVolume) throws Exception {
		volume = newVolume;
		Bass.BASS_SetConfig(BASS_CONFIG.BASS_CONFIG_GVOL_STREAM, (10000 / 100) * volume);
	}
	
	/**
	 * Get the currently set limit for the rendering time
	 */
	public static float getRenderingLimit(){
		return renderingLimit;
	}
	
	/**
	 * Set the limit for the rendering time
	 * @param newLimit: value between 0 and Float.MAX_VALUE, 0 = infinite
	 */
	public static void setRenderingLimit(float newLimit) throws Exception {
		renderingLimit = newLimit;
		Bass.BASS_ChannelSetAttribute(handle, BASS_ATTRIB.BASS_ATTRIB_MIDI_CPU, newLimit);
	}
	
	/**
	 * Get the amount of currently active voices
	 */
	public static int getActiveVoices() throws Exception {
		if(!started){
			throw new SynthesizerException("Synthesizer isnt started");
		}
		float num11;
        FloatBuffer fbfr = BufferUtils.newFloatBuffer(1);
        Bass.BASS_ChannelGetAttribute(handle, 73732, fbfr);
        num11 = fbfr.get(0);
        return (int)num11;
	}
	
	/**
	 * Convert a string to a pointer
	 */
	public static Pointer pointerFromString(String s){
		return BufferUtils.asPointer(BufferUtils.fromString(s));
	}
	
	/**
	 * Java-implementation of c++'s MAKEWORD
	 */
	public static int MAKEWORD(byte low, byte high){
		return ((int)high << 8) | low;
	}
	
	/**
	 * Send multiple events to the synthesizer using an array
	 */
	public static void sendEvents(MidiEvent[] events) throws Exception {
		List<MidiEvent> eventsList = new ArrayList<MidiEvent>();
		for(MidiEvent e:events){
			eventsList.add(e);
		}
		sendEvents(eventsList);
	}
	
	/**
	 * Send multiple events to the synthesizer using a list
	 */
	public static void sendEvents(List<MidiEvent> events) throws Exception {
		if(!started){
			throw new SynthesizerException("Synthesizer isnt started");
		}
		for(MidiEvent e:events){
			sendEvent(e);
		}
	}
	
	/**
	 * Send a single event to the synthesizer
	 */
	public static void sendEvent(MidiEvent event) throws Exception {
		if(!started){
			throw new SynthesizerException("Synthesizer isnt started");
		}
		Bass.BASS_MIDI_StreamEvent(stream, event.getChannel(), event.getID(), MAKEWORD((byte)event.getValue1(), (byte)event.getValue2()));
	}
	
	/**
	 * Unload a soundfont
	 */
	public static void unloadFont(HSOUNDFONT font) throws Exception {
		if(!started){
			throw new SynthesizerException("Synthesizer isnt started");
		}
		Bass.BASS_MIDI_FontFree(font);
	}
	
	private static int sf = 0;
	
	/***
	 * Load multiple soundfonts using an array
	 * @Returns A list of HSOUNDFONTs which can be used to unload the soundfonts using unloadFont(HSOUNDFONT font)
	 */
	public static List<HSOUNDFONT> loadFonts(String[] soundfonts) throws Exception {
		List<String> fontsList = new ArrayList<String>();
		for(String e:soundfonts){
			fontsList.add(e);
		}
		return loadFonts(fontsList);
	}
	
	/**
	 * Load multiple soundfonts using a list
	 * @Returns A list of HSOUNDFONTs which can be used to unload the soundfonts using unloadFont(HSOUNDFONT font)
	 */
	public static List<HSOUNDFONT> loadFonts(List<String> soundfonts) throws Exception {
		if(!started){
			throw new SynthesizerException("Synthesizer isnt started");
		}
		List<HSOUNDFONT> toReturn = new ArrayList<HSOUNDFONT>();
		for(int i = 0; i < soundfonts.size(); i++){
			if(!new File(soundfonts.get(i)).exists()){
				throw new FileNotFoundException(soundfonts.get(i));
			}
			BASS_MIDI_FONT font = BASS_MIDI_FONT.allocate();
			font.setFont(Bass.BASS_MIDI_FontInit(pointerFromString(soundfonts.get(i)), 0));
	        font.setPreset(-1);
	        font.setBank(0);
	        sf++;
	        Bass.BASS_MIDI_StreamSetFonts(stream, font, sf);
	        Bass.BASS_MIDI_StreamLoadSamples(stream);
	        toReturn.add(font.getFont());
		}
		return toReturn;
	}
	
	/**
	 * Load a single soundfont
	 * @Returns A HSOUNDFONT which can be used to unload the soundfont using unloadFont(HSOUNDFONT font)
	 */
	public static HSOUNDFONT loadFont(String fontLocation) throws Exception {
		if(!started){
			throw new SynthesizerException("Synthesizer isnt started");
		}
		if(!new File(fontLocation).exists()){
			throw new FileNotFoundException(fontLocation);
		}
        BASS_MIDI_FONT font = BASS_MIDI_FONT.allocate();
        font.setFont(Bass.BASS_MIDI_FontInit(pointerFromString(fontLocation), 0));
        font.setPreset(0);
        font.setBank(0);
        Bass.BASS_MIDI_StreamSetFonts(stream, font, 1);
        Bass.BASS_MIDI_StreamLoadSamples(stream);
        return font.getFont();
	}
	
	/**
	 * Start the synthesizer
	 * @Param audioFrequency: The audio frequency that BASS should use
	 */
	public static void startSynth(int audioFrequency, boolean mono) throws Exception {
		if(started){
			throw new SynthesizerException("Synth is allready running");
		}
		BassInit.loadLibraries();
		if(mono){
			Bass.BASS_Init(-1, audioFrequency, BASS_DEVICE.BASS_DEVICE_MONO, null, null);
		}else{
			Bass.BASS_Init(-1, audioFrequency, BASS_DEVICE.BASS_DEVICE_LATENCY, null, null);
		}
        Bass.BASS_SetConfig(BASS_CONFIG.BASS_CONFIG_UPDATEPERIOD, 0);
        Bass.BASS_SetConfig(BASS_CONFIG.BASS_CONFIG_UPDATETHREADS, 32);
        BASS_INFO info = BASS_INFO.allocate();
        Bass.BASS_GetInfo(info);
        Bass.BASS_SetConfig(BASS_CONFIG.BASS_CONFIG_BUFFER, info.getMinBuffer() + 10);
        stream = Bass.BASS_MIDI_StreamCreate(16, BASS_SAMPLE.BASS_SAMPLE_SOFTWARE | BASS_SAMPLE.BASS_SAMPLE_FLOAT, audioFrequency);
        handle = stream.asInt();
        started = true;
        setMaxVoices(500);
        setUseFx(true);
        setOnlyReleaseOnOverlapingInstances(false);
        Bass.BASS_ChannelPlay(handle, false);
        new Thread(new UpdateThread()).start();
	}
	
	/**
	 * Enable/Disable BASSMIDI to only release the oldest instance upon a note off event when there are overlapping instances of that note
	 */
	public static void setOnlyReleaseOnOverlapingInstances(boolean b) throws Exception {
		if(!started){
			throw new SynthesizerException("Synthesizer isnt started");
		}
		if(b){
			Bass.BASS_ChannelFlags(handle, 65536, 65536);
		}else{
			Bass.BASS_ChannelFlags(handle, 0, 65536);
		}
	}
	
	/**
	 * Enable/Disable sound effects
	 */
	public static void setUseFx(boolean useFx) throws Exception {
		if(!started){
			throw new SynthesizerException("Synthesizer isnt started");
		}
		if(useFx){
			Bass.BASS_ChannelFlags(handle, BASS_MIDI.BASS_MIDI_NOFX, BASS_MIDI.BASS_MIDI_NOFX);
		}else{
			Bass.BASS_ChannelFlags(handle, 0, BASS_MIDI.BASS_MIDI_NOFX);
		}
	}
	
	private static class UpdateThread implements Runnable {
		
		private UpdateThread(){
			
		}

		@Override
		public void run() {
			while(started){
				Bass.BASS_ChannelUpdate(handle, 0);
				/*try {
					Thread.sleep(1);
				}catch(Exception e){
					e.printStackTrace();
				}*/
			}
		}
		
	}
	
	/**
	 * Get the currently set voice limit
	 */
	public static int getMaxVoices(){
		return maxVoices;
	}
	
	/**
	 * Set the voice limit
	 */
	public static void setMaxVoices(int newMaxVoices) throws Exception {
		if(!started){
			throw new SynthesizerException("Synthesizer isnt started");
		}
		maxVoices = newMaxVoices;
		Bass.BASS_ChannelSetAttribute(handle, 73731, maxVoices);
	}
	
	/**
	 * Stop the synthesizer
	 */
	public static void stopSynth() throws Exception {
		if(!started){
			throw new SynthesizerException("Cant stop the synth when it isnt even running");
		}
		started = false;
		Bass.BASS_StreamFree(stream);
		Bass.BASS_Free();
	}
	
	/**
	 * Check if the synthesizer is started
	 */
	public static boolean isSynthStarted(){
		return started;
	}
	
}
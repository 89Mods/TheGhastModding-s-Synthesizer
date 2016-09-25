package theGhastModding.synthesizer.main;

public class MidiEvent {
	
	private int ID;
	private int channel;
	private int value1;
	private int value2;
	
	public static final int MIDI_EVENT_NOTE = 1;
	public static final int MIDI_EVENT_PROGRAM = 2;
	public static final int MIDI_EVENT_CHANPRES = 3;
	public static final int MIDI_EVENT_PITCH = 4;
	public static final int MIDI_EVENT_PITCHRANGE = 5;
	public static final int MIDI_EVENT_DRUMS = 6;
	public static final int MIDI_EVENT_FINETUNE = 7;
	public static final int MIDI_EVENT_COARSETUNE = 8;
	public static final int MIDI_EVENT_MASTERVOL = 9;
	public static final int MIDI_EVENT_BANK = 10;
	public static final int MIDI_EVENT_MODULATION = 11;
	public static final int MIDI_EVENT_VOLUME = 12;
	public static final int MIDI_EVENT_PAN = 13;
	public static final int MIDI_EVENT_EXPRESSION = 14;
	public static final int MIDI_EVENT_SUSTAIN = 15;
	public static final int MIDI_EVENT_SOUNDOFF = 16;
	public static final int MIDI_EVENT_RESET = 17;
	public static final int MIDI_EVENT_NOTESOFF	= 18;
	public static final int MIDI_EVENT_PORTAMENTO = 19;
	public static final int MIDI_EVENT_PORTATIME = 20;
	public static final int MIDI_EVENT_PORTANOTE = 21;
	public static final int MIDI_EVENT_MODE	= 22;
	public static final int MIDI_EVENT_REVERB = 23;
	public static final int MIDI_EVENT_CHORUS = 24;
	public static final int MIDI_EVENT_CUTOFF = 25;
	public static final int MIDI_EVENT_RESONANCE = 26;
	public static final int MIDI_EVENT_RELEASE = 27;
	public static final int MIDI_EVENT_ATTACK = 28;
	public static final int MIDI_EVENT_DECAY = 29;
	public static final int MIDI_EVENT_REVERB_MACRO = 30;
	public static final int MIDI_EVENT_CHORUS_MACRO = 31;
	public static final int MIDI_EVENT_REVERB_TIME = 32;
	public static final int MIDI_EVENT_REVERB_DELAY = 33;
	public static final int MIDI_EVENT_REVERB_LOCUTOFF = 34;
	public static final int MIDI_EVENT_REVERB_HICUTOFF = 35;
	public static final int MIDI_EVENT_REVERB_LEVEL = 36;
	public static final int MIDI_EVENT_CHORUS_DELAY = 37;
	public static final int MIDI_EVENT_CHORUS_DEPTH = 38;
	public static final int MIDI_EVENT_CHORUS_RATE = 39;
	public static final int MIDI_EVENT_CHORUS_FEEDBACK = 40;
	public static final int MIDI_EVENT_CHORUS_LEVEL	= 41;
	public static final int MIDI_EVENT_CHORUS_REVERB = 42;
	public static final int MIDI_EVENT_USERFX = 43;
	public static final int MIDI_EVENT_USERFX_LEVEL	= 44;
	public static final int MIDI_EVENT_USERFX_REVERB = 45;
	public static final int MIDI_EVENT_USERFX_CHORUS = 46;
	public static final int MIDI_EVENT_DRUM_FINETUNE = 50;
	public static final int MIDI_EVENT_DRUM_COARSETUNE = 51;
	public static final int MIDI_EVENT_DRUM_PAN = 52;
	public static final int MIDI_EVENT_DRUM_REVERB = 53;
	public static final int MIDI_EVENT_DRUM_CHORUS = 54;
	public static final int MIDI_EVENT_DRUM_CUTOFF = 55;
	public static final int MIDI_EVENT_DRUM_RESONANCE = 56;
	public static final int MIDI_EVENT_DRUM_LEVEL = 57;
	public static final int MIDI_EVENT_DRUM_USERFX = 58;
	public static final int MIDI_EVENT_SOFT	= 60;
	public static final int MIDI_EVENT_SYSTEM = 61;
	public static final int MIDI_EVENT_TEMPO = 62;
	public static final int MIDI_EVENT_SCALETUNING = 63;
	public static final int MIDI_EVENT_CONTROL = 64;
	public static final int MIDI_EVENT_CHANPRES_VIBRATO	= 65;
	public static final int MIDI_EVENT_CHANPRES_PITCH = 66;
	public static final int MIDI_EVENT_CHANPRES_FILTER = 67;
	public static final int MIDI_EVENT_CHANPRES_VOLUME = 68;
	public static final int MIDI_EVENT_MODRANGE	= 69;
	public static final int MIDI_EVENT_BANK_LSB	= 70;
	public static final int MIDI_EVENT_KEYPRES = 71;
	public static final int MIDI_EVENT_KEYPRES_VIBRATO	= 72;
	public static final int MIDI_EVENT_KEYPRES_PITCH  = 73;
	public static final int MIDI_EVENT_KEYPRES_FILTER = 74;
	public static final int MIDI_EVENT_KEYPRES_VOLUME = 75;
	public static final int MIDI_EVENT_MIXLEVEL	= 0x10000;
	public static final int MIDI_EVENT_TRANSPOSE = 0x10001;
	public static final int MIDI_EVENT_SYSTEMEX	= 0x10002;
	public static final int MIDI_EVENT_END = 0;
	public static final int MIDI_EVENT_END_TRACK = 0x10003;
	
	/*
	 * The Midi even class
	 * @Param id: the ID of the event. All event types are stored in this class as final variables
	 * @Param channel: the channel of the even
	 * @Param: value1: first data byte of the event
	 * @Param: value2: second data byte of the event
	 */
	public MidiEvent(int iD, int channel, int value1, int value2) {
		super();
		ID = iD;
		this.channel = channel;
		this.value1 = value1;
		this.value2 = value2;
	}
	
	public int getID() {
		return ID;
	}
	
	public void setID(int iD) {
		ID = iD;
	}
	
	public int getChannel() {
		return channel;
	}
	
	public void setChannel(int channel) {
		this.channel = channel;
	}
	
	public int getValue1() {
		return value1;
	}
	
	public void setValue1(int value1) {
		this.value1 = value1;
	}
	
	public int getValue2() {
		return value2;
	}
	
	public void setValue2(int value2) {
		this.value2 = value2;
	}
	
}
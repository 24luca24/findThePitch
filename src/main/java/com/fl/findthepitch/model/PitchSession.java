package com.fl.findthepitch.model;

public class PitchSession {
    private static PitchSession instance;
    private PitchData pitchData;

    // Private constructor to prevent instantiation
    private PitchSession() { }

    // Get the single instance of the PitchSession class
    public static PitchSession getInstance() {
        if (instance == null) {
            instance = new PitchSession();
        }
        return instance;
    }

    // Get the current pitch data
    public PitchData getPitchData() {
        return pitchData;
    }

    // Set the current pitch data
    public void setPitchData(PitchData pitchData) {
        this.pitchData = pitchData;
    }

    // Clear the current pitch data
    public void clearSession() {
        pitchData = null;
    }
}

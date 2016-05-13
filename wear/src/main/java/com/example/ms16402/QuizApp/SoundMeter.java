package com.example.ms16402.QuizApp;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

public class SoundMeter {

    private AudioRecord ar = null;
    private int minSize;

    public void start() {
        minSize= AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        ar = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000,AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT,minSize);
        if (ar.getState() == AudioRecord.STATE_INITIALIZED) {
            ar.startRecording();
        }


    }

    public void stop() {
        if (ar != null) {
            if (ar.getState() == AudioRecord.STATE_INITIALIZED) {
                ar.stop();
            }
        }
    }

    public double getdB() {
        short[] buffer = new short[minSize];
        int bufferread = ar.read(buffer, 0, minSize);
        int max = 0;
        for (short s : buffer)
        {
            if (Math.abs(s) > max)
            {
                max += s;
            }
        }
        if (bufferread > 0)
        {
            max = Math.abs((max / bufferread));
        }

        return max;
    }

}
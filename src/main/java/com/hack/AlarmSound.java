package com.hack;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AlarmSound {
    public static void sound() {
        try {
            while(true){
                // Load the sound file
                
                File soundFile = new File("/Users/akshatmundra/Desktop/uwbot3/src/main/java/com/hack/BurglarAlarm.wav"); // Replace with your sound file path
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
                

                // Keep the program running to hear the sound
                Thread.sleep(clip.getMicrosecondLength() / 1000);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}


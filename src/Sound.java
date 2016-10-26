/**
 * Earl Timothy D. Malaki
 * BSCS - II | CMSC 22 - OOP
 * MP #4 - Pong
 */

import java.applet.Applet;
import java.applet.AudioClip;


public class Sound {
    public static final AudioClip BALL = Applet.newAudioClip(Sound.class.getResource("Bottle.aiff"));
    public static final AudioClip GAMEOVER = Applet.newAudioClip(Sound.class.getResource("Blow.aiff"));
}
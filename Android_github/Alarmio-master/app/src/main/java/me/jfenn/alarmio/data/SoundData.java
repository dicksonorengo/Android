package me.jfenn.alarmio.data;

import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import com.google.android.exoplayer2.C;

import io.reactivex.annotations.Nullable;
import me.jfenn.alarmio.Alarmio;

public class SoundData {

    private static final String SEPARATOR = ":AlarmioSoundData:";

    private String name;
    private String url;

    private Ringtone ringtone;

    public SoundData(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public SoundData(String name, String url, Ringtone ringtone) {
        this(name, url);
        this.ringtone = ringtone;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    /**
     * Plays the sound. This will pass the SoundData instance to the provided
     * [Alarmio](../Alarmio) class, which will store the currently playing sound
     * until it is stopped or cancelled.
     *
     * @param alarmio           The active Application instance.
     */
    public void play(Alarmio alarmio) {
        if (url.startsWith("content://")) {
            if (ringtone == null) {
                ringtone = RingtoneManager.getRingtone(alarmio, Uri.parse(url));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ringtone.setAudioAttributes(new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_ALARM)
                            .build());
                }
            }

            alarmio.playRingtone(ringtone);
        } else {
            alarmio.playStream(url, new com.google.android.exoplayer2.audio.AudioAttributes.Builder()
                    .setUsage(C.USAGE_ALARM)
                    .build());
        }
    }

    /**
     * Stops the currently playing alarm. This only differentiates between sounds
     * if the sound is a ringtone; if it is a stream, then all streams will be stopped,
     * regardless of whether this sound is in fact the currently playing stream or not.
     *
     * @param alarmio           The active Application instance.
     */
    public void stop(Alarmio alarmio) {
        if (ringtone != null)
            ringtone.stop();
        else alarmio.stopStream();
    }

    /**
     * Preview the sound on the "media" volume channel.
     *
     * @param alarmio           The active Application instance.
     */
    public void preview(Alarmio alarmio) {
        if (url.startsWith("content://")) {
            if (ringtone == null) {
                ringtone = RingtoneManager.getRingtone(alarmio, Uri.parse(url));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ringtone.setAudioAttributes(new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_ALARM)
                            .build());
                }
            }

            alarmio.playRingtone(ringtone);
        } else {
            alarmio.playStream(url, new com.google.android.exoplayer2.audio.AudioAttributes.Builder()
                    .setUsage(C.USAGE_ALARM)
                    .build());
        }
    }

    /**
     * Decide whether the sound is currently playing or not.
     *
     * @param alarmio           The active Application instance.
     * @return                  True if "this" sound is playing.
     */
    public boolean isPlaying(Alarmio alarmio) {
        if (ringtone != null)
            return ringtone.isPlaying();
        else return alarmio.isPlayingStream(url);
    }

    /**
     * Returns an identifier string that can be used to recreate this
     * SoundDate class.
     *
     * @return                  A non-null identifier string.
     */
    @Override
    public String toString() {
        return name + SEPARATOR + url;
    }

    /**
     * Construct a new instance of SoundData from an identifier string which was
     * (hopefully) created by [toString](#tostring).
     *
     * @param string            A non-null identifier string.
     * @return                  A recreated SoundData instance.
     */
    @Nullable
    public static SoundData fromString(String string) {
        if (string.contains(SEPARATOR)) {
            String[] data = string.split(SEPARATOR);
            if (data.length == 2 && data[0].length() > 0 && data[1].length() > 0)
                return new SoundData(data[0], data[1]);
        }

        return null;
    }

    /**
     * Decide if two SoundDatas are equal.
     *
     * @param obj               The object to compare to.
     * @return                  True if the SoundDatas contain the same sound.
     */
    @Override
    public boolean equals(Object obj) {
        return (obj != null && obj instanceof SoundData && ((SoundData) obj).url.equals(url));
    }
}

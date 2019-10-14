package com.cecs;

import com.cecs.model.Music;
import com.google.gson.GsonBuilder;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class MusicServices {
    MusicServices(){

    }

    public static Music[] loadSongs(String asdf) {
        var reader = new InputStreamReader(App.class.getResourceAsStream("/music.json"), StandardCharsets.UTF_8);
        var musics = new GsonBuilder().create().fromJson(reader, Music[].class);
        for (Music music : musics) {
            music.getSong().setArtist(music.getArtist().getName());
        }
        return musics;
    }
}

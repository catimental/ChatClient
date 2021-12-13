package io.github.catimental.chatclient.client;

import io.github.catimental.chatclient.net.ByteBufReader;

public class Profile {
    private int id;
    private String displayName;
    private String description;

    public Profile(int id, String displayName, String description) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
    }

     public static Profile decode(ByteBufReader r) {
        return new Profile(
                r.readInt(),
                r.readAsciiString(),
                r.readAsciiString()
        );
     }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }
}

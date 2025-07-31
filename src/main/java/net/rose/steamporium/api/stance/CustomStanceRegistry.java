package net.rose.steamporium.api.stance;

import java.util.ArrayList;
import java.util.List;

public class CustomStanceRegistry {
    public static final List<CustomStance> REGISTERED = new ArrayList<>();

    public static void register(CustomStance stance) {
        REGISTERED.add(stance);
    }
}

package GameObject;

public enum Glyph {
    ANSI_RESET("\u001B[0m"),
    ANSI_RED("\u001B[31m"),
    FLOOR_LIGHT(0x2591),
    FLOOR(" "), // light: 0x2591  period: 0x002E
    WALL(0x2588), // solid block: 0x2588  pound (#): 0x0023
    PLAYER(0x0040), // standing person (more than one monotype space): 0x1F9CD  at (@): 0x0040
    DAGGER(0x2020),
    SHIELD(0x1F6E1),
    SMILEY(0x1F600),
    SPADES("\u2660"),
    HEARTS(Glyph.ANSI_RED.get() + "\u2764" + Glyph.ANSI_RESET.get());

    private final String glyph;

    Glyph(int charCode) {
        glyph = new String(Character.toChars(charCode));
    }

    Glyph(String string) {
        glyph = string;
    }

    public String get() {
        return glyph;
    }
}

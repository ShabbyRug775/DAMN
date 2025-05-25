package com.example.lampvibrasincronia;

public class MorseCodeConverter {
    private static final long SPEED_BASE = 100;
    public static final long DOT = SPEED_BASE;
    public static final long DASH = SPEED_BASE * 3;
    public static final long GAP = SPEED_BASE;
    public static final long LETTER_GAP = SPEED_BASE * 3;
    public static final long WORD_GAP = SPEED_BASE * 7;

    // Patrones para letras (A-Z)
    private static final long[][] LETTERS = {
            /* A */ new long[]{DOT, GAP, DASH},
            /* B */ new long[]{DASH, GAP, DOT, GAP, DOT, GAP, DOT},
            /* C */ new long[]{DASH, GAP, DOT, GAP, DASH, GAP, DOT},
            /* D */ new long[]{DASH, GAP, DOT, GAP, DOT},
            /* E */ new long[]{DOT},
            /* F */ new long[]{DOT, GAP, DOT, GAP, DASH, GAP, DOT},
            /* G */ new long[]{DASH, GAP, DASH, GAP, DOT},
            /* H */ new long[]{DOT, GAP, DOT, GAP, DOT, GAP, DOT},
            /* I */ new long[]{DOT, GAP, DOT},
            /* J */ new long[]{DOT, GAP, DASH, GAP, DASH, GAP, DASH},
            /* K */ new long[]{DASH, GAP, DOT, GAP, DASH},
            /* L */ new long[]{DOT, GAP, DASH, GAP, DOT, GAP, DOT},
            /* M */ new long[]{DASH, GAP, DASH},
            /* N */ new long[]{DASH, GAP, DOT},
            /* O */ new long[]{DASH, GAP, DASH, GAP, DASH},
            /* P */ new long[]{DOT, GAP, DASH, GAP, DASH, GAP, DOT},
            /* Q */ new long[]{DASH, GAP, DASH, GAP, DOT, GAP, DASH},
            /* R */ new long[]{DOT, GAP, DASH, GAP, DOT},
            /* S */ new long[]{DOT, GAP, DOT, GAP, DOT},
            /* T */ new long[]{DASH},
            /* U */ new long[]{DOT, GAP, DOT, GAP, DASH},
            /* V */ new long[]{DOT, GAP, DOT, GAP, DOT, GAP, DASH},
            /* W */ new long[]{DOT, GAP, DASH, GAP, DASH},
            /* X */ new long[]{DASH, GAP, DOT, GAP, DOT, GAP, DASH},
            /* Y */ new long[]{DASH, GAP, DOT, GAP, DASH, GAP, DASH},
            /* Z */ new long[]{DASH, GAP, DASH, GAP, DOT, GAP, DOT}
    };

    public static long[] pattern(String message) {
        if (message == null || message.isEmpty()) {
            return new long[0];
        }

        message = message.toUpperCase();
        int length = 1; // Comienza con 0 (inicio inmediato)

        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            if (c == ' ') {
                length += 1; // WORD_GAP
            } else if (c >= 'A' && c <= 'Z') {
                length += LETTERS[c - 'A'].length + 1; // Letra + LETTER_GAP
            }
        }

        long[] pattern = new long[length];
        pattern[0] = 0; // Inicio inmediato
        int index = 1;

        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            if (c == ' ') {
                pattern[index++] = WORD_GAP;
            } else if (c >= 'A' && c <= 'Z') {
                System.arraycopy(LETTERS[c - 'A'], 0, pattern, index, LETTERS[c - 'A'].length);
                index += LETTERS[c - 'A'].length;
                pattern[index++] = LETTER_GAP;
            }
        }

        return pattern;
    }
}
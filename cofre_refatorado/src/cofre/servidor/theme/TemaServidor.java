package cofre.servidor.theme;

import java.awt.*;

/**
 * Paleta de cores e fontes da interface do Servidor.
 */
public final class TemaServidor {
    private TemaServidor() {}

    public static final Color BG        = new Color(15, 17, 26);
    public static final Color CARD      = new Color(25, 28, 42);
    public static final Color BORDA     = new Color(50, 55, 80);
    public static final Color ACCENT    = new Color(255, 196, 0);
    public static final Color VERDE     = new Color(0, 200, 130);
    public static final Color TEXT_MAIN = new Color(230, 230, 240);
    public static final Color TEXT_DIM  = new Color(120, 125, 150);

    public static final Font FONT_TITULO  = new Font("Monospaced", Font.BOLD,  18);
    public static final Font FONT_CARD_V  = new Font("Monospaced", Font.BOLD,  22);
    public static final Font FONT_CARD_T  = new Font("Monospaced", Font.PLAIN, 10);
    public static final Font FONT_STATUS  = new Font("Monospaced", Font.BOLD,  12);
    public static final Font FONT_LOG     = new Font("Monospaced", Font.PLAIN, 12);
    public static final Font FONT_LOG_TIT = new Font("Monospaced", Font.BOLD,  12);
    public static final Font FONT_FOOTER  = new Font("Monospaced", Font.PLAIN, 11);
}

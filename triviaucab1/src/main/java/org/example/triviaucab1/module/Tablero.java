package org.example.triviaucab1.module;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase que representa el tablero de juego de Trivia UCAB.
 * Contiene un mapa de todas las casillas del tablero, accesibles por su ID de FXML,
 * y define la ruta principal de casillas para el movimiento de los jugadores.
 */
public class Tablero {
    /**
     * Mapa que almacena las casillas del tablero, indexadas por su ID de FXML.
     */
    private Map<String, Casilla> casillasPorId;
    /**
     * Array que define la secuencia de IDs de las casillas en la ruta principal del tablero.
     * Esta ruta se utiliza para calcular los movimientos secuenciales de los jugadores.
     */
    private String[] rutaPrincipalCasillaIds = {
            "casilla1", "casilla7", "casilla8", "casilla9", "casilla10", "casilla11",
            "casilla12", "casilla13", "casilla14", "casilla15", "casilla16", "casilla17",
            "casilla18", "casilla19", "casilla20", "casilla21", "casilla4", "casilla22",
            "casilla23", "casilla24", "casilla25", "casilla26", "casilla27", "casilla28",
            "casilla29", "casilla30Path", "casilla31", "casilla6", "casilla32", "casilla33",
            "casilla34", "casilla35", "casilla36", "casilla37", "casilla38", "casilla39",
            "casilla40", "casilla41", "casilla42", "casilla43", "casilla44", "casilla45",
            "casilla46"

    };

    /**
     * Constructor por defecto de la clase Tablero.
     * Inicializa el mapa de casillas y procede a inicializar todas las casillas del tablero.
     */
    public Tablero() {
        this.casillasPorId = new HashMap<>();
        inicializarCasillas();
    }

    /**
     * Inicializa todas las casillas del tablero con sus respectivos IDs, colores hexadecimales,
     * y propiedades de si son casillas centrales o de quesito. Asigna una categoría a cada casilla
     * basándose en su color.
     */
    private void inicializarCasillas() {

        Map<String, String> colorCategoriaMap = new HashMap<>();
        colorCategoriaMap.put("0x0018ecff", "Geografía");
        colorCategoriaMap.put("0xfbff00ff", "Historia");
        colorCategoriaMap.put("0xff9306ff", "Deportes y pasatiempos");
        colorCategoriaMap.put("0x05eb25ff", "Ciencias y naturaleza");
        colorCategoriaMap.put("0xb703c4ff", "Arte y Literatura");
        colorCategoriaMap.put("0xff78f1ff", "Entretenimiento");
        colorCategoriaMap.put("0xffffffff", "Especial");
        colorCategoriaMap.put("0x000000ff", "Centro");


        agregarCasilla("casilla1", "0x0018ecff", false, false);
        agregarCasilla("casilla2", "0xfbff00ff", false, false);
        agregarCasilla("casilla3", "0xff9306ff", false, false);
        agregarCasilla("casilla4", "0x05eb25ff", false, false);
        agregarCasilla("casilla5", "0xb703c4ff", false, false);
        agregarCasilla("casilla6", "0xff78f1ff", false, false);
        agregarCasilla("casilla7", "0xb703c4ff", false, false);
        agregarCasilla("casilla8", "0xffffffff", true, false);
        agregarCasilla("casilla9", "0xff78f1ff", false, false);
        agregarCasilla("casilla10", "0xffffffff", false, false);
        agregarCasilla("casilla11", "0x05eb25ff", false, false);
        agregarCasilla("casilla12", "0xff78f1ff", false, false);
        agregarCasilla("casilla13", "0xffffffff", false, false);
        agregarCasilla("casilla14", "0x0018ecff", false, false);
        agregarCasilla("casilla15", "0xffffffff", false, false);
        agregarCasilla("casilla16", "0xb703c4ff", false, false);
        agregarCasilla("casilla17", "0x0018ecff", false, false);
        agregarCasilla("casilla18", "0xffffffff", false, false);
        agregarCasilla("casilla19", "0xfbff00ff", false, false);
        agregarCasilla("casilla20", "0xffffffff", false, false);
        agregarCasilla("casilla21", "0xff78f1ff", false, false);
        agregarCasilla("casilla22", "0xfbff00ff", false, false);
        agregarCasilla("casilla23", "0xffffffff", false, false);
        agregarCasilla("casilla24", "0xff9306ff", false, false);
        agregarCasilla("casilla25", "0xffffffff", false, false);
        agregarCasilla("casilla26", "0x0018ecff", false, false);
        agregarCasilla("casilla27", "0xff9306ff", false, false);
        agregarCasilla("casilla28", "0xffffffff", false, false);
        agregarCasilla("casilla29", "0x05eb25ff", false, false);
        agregarCasilla("casilla30Path", "0xffffffff", false, false);
        agregarCasilla("casilla31", "0xfbff00ff", false, false);
        agregarCasilla("casilla32", "0x05eb25ff", false, false);
        agregarCasilla("casilla33", "0xffffffff", false, false);
        agregarCasilla("casilla34", "0xb703c4ff", false, false);
        agregarCasilla("casilla35", "0xffffffff", false, false);
        agregarCasilla("casilla36", "0xff9306ff", false, false);
        agregarCasilla("casilla37", "0xff78f1ff", false, false);
        agregarCasilla("casilla38", "0xb703c4ff", false, false);
        agregarCasilla("casilla39", "0x05eb25ff", false, false);
        agregarCasilla("casilla40", "0xff9306ff", false, false);
        agregarCasilla("casilla41", "0xfbff00ff", false, false);
        agregarCasilla("casilla42", "0xb703c4ff", false, false);
        agregarCasilla("casilla43", "0xff78f1ff", false, false);
        agregarCasilla("casilla44", "0x0018ecff", false, false);
        agregarCasilla("casilla45", "0xfbff00ff", false, false);
        agregarCasilla("casilla46", "0xff9306ff", false, false);

        agregarCasilla("casilla47", "0x0018ecff", false, false);
        agregarCasilla("casilla48", "0xff78f1ff", false, false);
        agregarCasilla("casilla49", "0xb703c4ff", false, false);
        agregarCasilla("casilla50", "0x05eb25ff", false, false);
        agregarCasilla("casilla51", "0xff9306ff", false, false);

        agregarCasilla("casilla52", "0xff78f1ff", false, false);
        agregarCasilla("casilla53", "0x0018ecff", false, false);
        agregarCasilla("casilla54", "0xfbff00ff", false, false);
        agregarCasilla("casilla55", "0xff9306ff", false, false);
        agregarCasilla("casilla56", "0x05eb25ff", false, false);

        agregarCasilla("casilla57", "0xfbff00ff", false, false);
        agregarCasilla("casilla58", "0x0018ecff", false, false);
        agregarCasilla("casilla59", "0xff78f1ff", false, false);
        agregarCasilla("casilla60", "0xb703c4ff", false, false);
        agregarCasilla("casilla61", "0x05eb25ff", false, false);

        agregarCasilla("casilla62", "0x0018ecff", false, false);
        agregarCasilla("casilla63", "0xfbff00ff", false, false);
        agregarCasilla("casilla64", "0xff9306ff", false, false);
        agregarCasilla("casilla65", "0x05eb25ff", false, false);
        agregarCasilla("casilla66", "0xb703c4ff", false, false);

        agregarCasilla("casillaCentral", "0x000000ff", true, false);
    }

    /**
     * Método auxiliar para añadir una casilla al mapa {@code casillasPorId}.
     * @param fxId El ID de FXML de la casilla.
     * @param colorHex El color hexadecimal de la casilla.
     * @param esCentral Verdadero si la casilla es la casilla central del tablero.
     * @param esQuesito Verdadero si la casilla es una casilla de quesito.
     */
    private void agregarCasilla(String fxId, String colorHex, boolean esCentral, boolean esQuesito) {
        String categoria = getColorToCategory(colorHex);
        Casilla casilla = new Casilla(categoria, esCentral, esQuesito);
        casillasPorId.put(fxId, casilla);
    }

    /**
     * Convierte un color hexadecimal de JavaFX a la categoría de pregunta correspondiente.
     * @param colorHex El valor hexadecimal del color (ej. "0x0018ecff").
     * @return La categoría de pregunta asociada al color, o "Desconocido" si el color no está mapeado.
     */
    private String getColorToCategory(String colorHex) {
        switch (colorHex.toLowerCase()) {
            case "0x0018ecff": return "Geografía";
            case "0xfbff00ff": return "Historia";
            case "0xff9306ff": return "Deportes y pasatiempos";
            case "0x05eb25ff": return "Ciencias y naturaleza";
            case "0xb703c4ff": return "Arte y Literatura";
            case "0xff78f1ff": return "Entretenimiento";
            case "0xffffffff": return "Especial";
            case "0x000000ff": return "Centro";
            default: return "Desconocido";
        }
    }

    /**
     * Obtiene un objeto Casilla por su ID de FXML.
     * @param fxId El ID de FXML de la casilla (ej. "casilla1", "casillaCentral").
     * @return El objeto Casilla correspondiente al ID, o null si no se encuentra.
     */
    public Casilla getCasillaPorId(String fxId) {
        return casillasPorId.get(fxId);
    }

    /**
     * Obtiene la lista ordenada de IDs de las casillas que conforman la ruta principal del tablero.
     * @return Un array de Strings con los IDs de las casillas de la ruta principal.
     */
    public String[] getRutaPrincipalCasillaIds() {
        return rutaPrincipalCasillaIds;
    }
}

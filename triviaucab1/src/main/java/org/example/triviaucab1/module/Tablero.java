package org.example.triviaucab1.module;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.paint.Color;

public class Tablero {
    private Map<String, Casilla> casillasPorId;
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

    public Tablero() {
        this.casillasPorId = new HashMap<>();
        inicializarCasillas();
    }

    private void inicializarCasillas() {

        Map<String, String> colorCategoriaMap = new HashMap<>();
        colorCategoriaMap.put("0x0018ecff", "Geografía"); // Azul
        colorCategoriaMap.put("0xfbff00ff", "Historia"); // Amarillo
        colorCategoriaMap.put("0xff9306ff", "Deportes y pasatiempos"); // Naranja
        colorCategoriaMap.put("0x05eb25ff", "Ciencias y naturaleza"); // Verde
        colorCategoriaMap.put("0xb703c4ff", "Arte y Literatura"); // Morado
        colorCategoriaMap.put("0xff78f1ff", "Entretenimiento"); // Rosa
        colorCategoriaMap.put("0xffffffff", "Especial"); // Blanco (Casillas especiales o de ruta)
        colorCategoriaMap.put("0x000000ff", "Centro"); // Si la central es negra o sin color (se renderiza negra)


        agregarCasilla("casilla1", "0x0018ecff", false, false); // Geografía
        agregarCasilla("casilla2", "0xfbff00ff", false, false); // Historia
        agregarCasilla("casilla3", "0xff9306ff", false, false); // Deportes
        agregarCasilla("casilla4", "0x05eb25ff", false, false); // Ciencias
        agregarCasilla("casilla5", "0xb703c4ff", false, false); // Arte
        agregarCasilla("casilla6", "0xff78f1ff", false, false); // Entretenimiento
        agregarCasilla("casilla7", "0xb703c4ff", false, false); // Arte
        agregarCasilla("casilla8", "0xffffffff", true, false); // Especial / Quesito
        agregarCasilla("casilla9", "0xff78f1ff", false, false); // Entretenimiento
        agregarCasilla("casilla10", "0xffffffff", false, false); // Especial
        agregarCasilla("casilla11", "0x05eb25ff", false, false); // Ciencias
        agregarCasilla("casilla12", "0xff78f1ff", false, false); // Entretenimiento
        agregarCasilla("casilla13", "0xffffffff", false, false); // Especial
        agregarCasilla("casilla14", "0x0018ecff", false, false); // Geografía
        agregarCasilla("casilla15", "0xffffffff", false, false); // Especial
        agregarCasilla("casilla16", "0xb703c4ff", false, false); // Arte
        agregarCasilla("casilla17", "0x0018ecff", false, false); // Geografía
        agregarCasilla("casilla18", "0xffffffff", false, false); // Especial
        agregarCasilla("casilla19", "0xfbff00ff", false, false); // Historia
        agregarCasilla("casilla20", "0xffffffff", false, false); // Especial
        agregarCasilla("casilla21", "0xff78f1ff", false, false); // Entretenimiento
        agregarCasilla("casilla22", "0xfbff00ff", false, false); // Historia
        agregarCasilla("casilla23", "0xffffffff", false, false); // Especial
        agregarCasilla("casilla24", "0xff9306ff", false, false); // Deportes
        agregarCasilla("casilla25", "0xffffffff", false, false); // Especial
        agregarCasilla("casilla26", "0x0018ecff", false, false); // Geografía
        agregarCasilla("casilla27", "0xff9306ff", false, false); // Deportes
        agregarCasilla("casilla28", "0xffffffff", false, false); // Especial
        agregarCasilla("casilla29", "0x05eb25ff", false, false); // Ciencias
        agregarCasilla("casilla30Path", "0xffffffff", false, false); // Especial (Renombrado para evitar conflicto con ID 30 de la leyenda)
        agregarCasilla("casilla31", "0xfbff00ff", false, false); // Historia
        agregarCasilla("casilla32", "0x05eb25ff", false, false); // Ciencias
        agregarCasilla("casilla33", "0xffffffff", false, false); // Especial
        agregarCasilla("casilla34", "0xb703c4ff", false, false); // Arte
        agregarCasilla("casilla35", "0xffffffff", false, false); // Especial
        agregarCasilla("casilla36", "0xff9306ff", false, false); // Deportes
        agregarCasilla("casilla37", "0xff78f1ff", false, false); // Entretenimiento
        agregarCasilla("casilla38", "0xb703c4ff", false, false); // Arte
        agregarCasilla("casilla39", "0x05eb25ff", false, false); // Ciencias
        agregarCasilla("casilla40", "0xff9306ff", false, false); // Deportes
        agregarCasilla("casilla41", "0xfbff00ff", false, false); // Historia
        agregarCasilla("casilla42", "0xb703c4ff", false, false); // Arte
        agregarCasilla("casilla43", "0xff78f1ff", false, false); // Entretenimiento
        agregarCasilla("casilla44", "0x0018ecff", false, false); // Geografía
        agregarCasilla("casilla45", "0xfbff00ff", false, false); // Historia
        agregarCasilla("casilla46", "0xff9306ff", false, false); // Deportes

        // Ramificaciones (IDs 47 a 66)
        agregarCasilla("casilla47", "0x0018ecff", false, false); // Geografía
        agregarCasilla("casilla48", "0xff78f1ff", false, false); // Entretenimiento
        agregarCasilla("casilla49", "0xb703c4ff", false, false); // Arte
        agregarCasilla("casilla50", "0x05eb25ff", false, false); // Ciencias
        agregarCasilla("casilla51", "0xff9306ff", false, false); // Deportes

        agregarCasilla("casilla52", "0xff78f1ff", false, false); // Entretenimiento
        agregarCasilla("casilla53", "0x0018ecff", false, false); // Geografía
        agregarCasilla("casilla54", "0xfbff00ff", false, false); // Historia
        agregarCasilla("casilla55", "0xff9306ff", false, false); // Deportes
        agregarCasilla("casilla56", "0x05eb25ff", false, false); // Ciencias

        agregarCasilla("casilla57", "0xfbff00ff", false, false); // Historia
        agregarCasilla("casilla58", "0x0018ecff", false, false); // Geografía
        agregarCasilla("casilla59", "0xff78f1ff", false, false); // Entretenimiento
        agregarCasilla("casilla60", "0xb703c4ff", false, false); // Arte
        agregarCasilla("casilla61", "0x05eb25ff", false, false); // Ciencias

        agregarCasilla("casilla62", "0x0018ecff", false, false); // Geografía
        agregarCasilla("casilla63", "0xfbff00ff", false, false); // Historia
        agregarCasilla("casilla64", "0xff9306ff", false, false); // Deportes
        agregarCasilla("casilla65", "0x05eb25ff", false, false); // Ciencias
        agregarCasilla("casilla66", "0xb703c4ff", false, false); // Arte

        // Casilla Central
        agregarCasilla("casillaCentral", "0x000000ff", true, false); // Es central
    }

    /**
     * Helper para agregar casillas al mapa.
     */
    private void agregarCasilla(String fxId, String colorHex, boolean esCentral, boolean esQuesito) {
        String categoria = getColorToCategory(colorHex);
        Casilla casilla = new Casilla(categoria, esCentral, esQuesito);
        casillasPorId.put(fxId, casilla);
    }

    /**
     * Convierte un color hexadecimal de JavaFX a la categoría correspondiente.
     */
    private String getColorToCategory(String colorHex) {
        // Normalizar el formato del color si es necesario (ej: "0xRRGGBBAAff")
        // Asegúrate de que los colores que uses aquí coincidan con los de tu FXML
        switch (colorHex.toLowerCase()) {
            case "0x0018ecff": return "Geografía";
            case "0xfbff00ff": return "Historia";
            case "0xff9306ff": return "Deportes y pasatiempos";
            case "0x05eb25ff": return "Ciencias y naturaleza";
            case "0xb703c4ff": return "Arte y Literatura";
            case "0xff78f1ff": return "Entretenimiento";
            case "0xffffffff": return "Especial"; // Casillas blancas
            case "0x000000ff": return "Centro"; // Casilla central (si su color de fondo es negro o no tiene fill)
            default: return "Desconocido"; // En caso de un color no mapeado
        }
    }

    /**
     * Obtiene una casilla por su ID de FXML.
     * @param fxId El ID de FXML de la casilla (ej. "casilla1", "casillaCentral").
     * @return El objeto Casilla correspondiente.
     */
    public Casilla getCasillaPorId(String fxId) {
        return casillasPorId.get(fxId);
    }

    /**
     * Obtiene la lista ordenada de IDs de las casillas de la ruta principal.
     * Esto es útil para calcular movimientos secuenciales.
     */
    public String[] getRutaPrincipalCasillaIds() {
        return rutaPrincipalCasillaIds;
    }

}
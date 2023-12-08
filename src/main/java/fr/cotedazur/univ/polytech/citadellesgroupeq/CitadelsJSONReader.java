package fr.cotedazur.univ.polytech.citadellesgroupeq;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

/**
 * La classe CitadelsJSONReader lit les données d'un fichier JSON contenant des informations sur les citadelles.
 * Elle convertit ces données en une liste d'objets Citadel.
 */
public class CitadelsJSONReader {

    /** Le tableau JSON contenant les données des citadelles. */
    private JSONArray jsonArray;
    /** La liste des citadelles lue à partir du fichier JSON. */
    private List<Citadel> citadelsList;


    public static final String DEFAULT_PATH="high_cards_citadels.json";

    /**
     * Construit un objet CitadelsJSONReader en lisant les données du fichier JSON contenant la liste des citadels
     *
     * @throws IOException Si une erreur se produit lors de la lecture du fichier ou si le format du fichier est incorrect.
     */
    public CitadelsJSONReader(String path) throws ParseException {
        try {
            // Lis les données du fichier JSON
            jsonArray = (JSONArray) new JSONParser().parse(new FileReader(getClass().getClassLoader().getResource(path).getFile()));
            // Initialise la liste des citadelles
            citadelsList = new ArrayList<>();
            // Parcours chaque élément du tableau JSON et crée un objet Citadel correspondant
            for (JSONObject citadelObject : (Iterable<JSONObject>) jsonArray) {
                if (citadelObject.get("name") == null || citadelObject.get("cost") == null) {
                    throw new InvalidPropertiesFormatException("Format du fichier JSON incorrect");
                } else {
                    String name = (String) citadelObject.get("name");
                    int cost = ((Long) citadelObject.get("cost")).intValue();
                    citadelsList.add(new Citadel(name, cost));
                }
            }
        }
        catch(IOException | NullPointerException e) {
            throw new ParseException(0);//arbitrary value
        }
    }

    public CitadelsJSONReader() throws ParseException {
        this(DEFAULT_PATH);
    }

    public List<Citadel> getCitadelsList() {
        return citadelsList;
    }


    /**
     * Récupère le nom et le cout de chaque citadels présentes dans la liste de citadels
     *
     * @return Une chaîne de caractères décrivant les citadelles avec leur nom et coût.
     * @throws IOException Si une erreur se produit lors de la lecture du fichier ou si le format du fichier est incorrect.
     */
    public String getCitadelsListDescription() throws BadlyInitializedReader {
        StringBuilder output=new StringBuilder("Citadelles présentes dans le .json:\n");
        CitadelsJSONReader citadelsList;

        try {
            citadelsList = new CitadelsJSONReader();//création du reader
        }
        catch(ParseException e) {
            throw new BadlyInitializedReader(e);
        }


        for(Citadel citadel: citadelsList.getCitadelsList()) {
            output.append(citadel.getName() + " : " + citadel.getCost()).append("\n");
        }

        return output.toString();
    }

    private class BadlyInitializedReader extends ExceptionInInitializerError {
        public BadlyInitializedReader(Throwable error) {
            super(error);
        }
    }
}

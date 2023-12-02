package fr.cotedazur.univ.polytech.citadellesgroupeq;

import org.json.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;

/**
 * La classe CitadelsJSONReader lit les données d'un fichier JSON contenant des informations sur les citadelles.
 * Elle convertit ces données en une liste d'objets Citadel.
 */
public class CitadelsJSONReader {

    /** Le tableau JSON contenant les données des citadelles. */
    private JSONArray jsonArray;
    /** La liste des citadelles lue à partir du fichier JSON. */
    private List<Citadel> citadelsList;

    /**
     * Construit un objet CitadelsJSONReader en lisant les données du fichier JSON contenant la liste des citadels
     *
     * @throws IOException Si une erreur se produit lors de la lecture du fichier ou si le format du fichier est incorrect.
     */
    public CitadelsJSONReader() throws IOException {
        try {
            // Lis les données du fichier JSON
            jsonArray = (JSONArray) new JSONParser().parse(new FileReader("src/main/resources/citadels.json"));
            // Initialise la liste des citadelles
            citadelsList=new ArrayList<>();
            // Parcours chaque élément du tableau JSON et crée un objet Citadel correspondant
            for (JSONObject citadelObject : (Iterable<JSONObject>) jsonArray) {
                String name = (String) citadelObject.get("name");
                int cost = ((Long) citadelObject.get("cost")).intValue();
                citadelsList.add(new Citadel(name, cost));
            }
        }
        catch(Exception e) {
            throw new IOException("Incorrect file structure or path.", e);
        }
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
    public String getCitadelsListDescription() throws IOException {
        StringBuilder output=new StringBuilder("Citadelles présentes dans le .json:\n");
        CitadelsJSONReader citadelsList = new CitadelsJSONReader();
        for(Citadel citadel: citadelsList.getCitadelsList()) {
            output.append(citadel.getName() + " : " + citadel.getCost()).append("\n");
        }

        return output.toString();
    }
}

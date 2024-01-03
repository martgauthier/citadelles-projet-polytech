package fr.cotedazur.univ.polytech.citadellesgroupeq;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

/**
 * La classe DistrictsJSONReader lit les données d'un fichier JSON contenant des informations sur les districts.
 * Elle convertit ces données en une liste d'objets District.
 */
public class DistrictsJSONReader {

    /** Le tableau JSON contenant les données des districts. */
    private JSONArray jsonArray;
    /** La liste des districts lue à partir du fichier JSON. */
    private List<District> districtsList;

    private Random randomGenerator=new Random();


    public static final String DEFAULT_PATH= "high_cards_districts.json";

    /**
     * Construit un objet DistrictsJSONReader en lisant les données du fichier JSON contenant la liste des districts
     *
     * @throws IOException Si une erreur se produit lors de la lecture du fichier ou si le format du fichier est incorrect.
     */
    public DistrictsJSONReader(String path) throws ParseException {
        try {
            // Lis les données du fichier JSON
            jsonArray = (JSONArray) new JSONParser().parse(new FileReader(getClass().getClassLoader().getResource(path).getFile()));
            // Initialise la liste des districts
            districtsList = new ArrayList<>();
            // Parcours chaque élément du tableau JSON et crée un objet District correspondant
            for (JSONObject districtObject : (Iterable<JSONObject>) jsonArray) {
                if (districtObject.get("name") == null || districtObject.get("cost") == null || districtObject.get("color") == null) {
                    throw new InvalidPropertiesFormatException("Format du fichier JSON incorrect");
                } else {
                    String name = (String) districtObject.get("name");
                    int cost = ((Long) districtObject.get("cost")).intValue();
                    String color = (String) districtObject.get("color");
                    districtsList.add(new District(name, cost, color));
                }
            }
        }
        catch(IOException | NullPointerException e) {
            throw new ParseException(0);//arbitrary value
        }
    }

    public DistrictsJSONReader() throws ParseException {
        this(DEFAULT_PATH);
    }

    public List<District> getDistrictsList() {
        return districtsList;
    }


    /**
     * Récupère le nom et le cout de chaque district présentes dans la liste de districts
     *
     * @return Une chaîne de caractères décrivant les districts avec leur nom et coût.
     * @throws BadlyInitializedReader Si une erreur se produit lors de la lecture du fichier ou si le format du fichier est incorrect.
     */
    public String getDistrictsListDescription() throws BadlyInitializedReader {
        StringBuilder output=new StringBuilder("Districts présentes dans le .json:\n");

        if(districtsList.isEmpty()) {
            throw new BadlyInitializedReader("District list is empty");
        }

        for(District district : districtsList) {
            output.append(district.getName() + " : " + district.getCost()).append("\n");
        }

        return output.toString();
    }

    public District getFromIndex(int index) throws BadlyInitializedReader, IllegalArgumentException {
        if(index<0||index>= districtsList.size()) {
            throw new IllegalArgumentException("index must be in districts list");
        }
        else {
            return districtsList.get(index);
        }
    }

    public District getRandomDistrict() throws BadlyInitializedReader {
        if (districtsList.isEmpty()) {
            throw new BadlyInitializedReader("District list is empty");
        }
        return districtsList.get(randomGenerator.nextInt(districtsList.size()));
    }

    private class BadlyInitializedReader extends ExceptionInInitializerError {
        public BadlyInitializedReader(Throwable error) {
            super(error);
        }

        public BadlyInitializedReader(String error) {
            super(error);
        }
    }
}

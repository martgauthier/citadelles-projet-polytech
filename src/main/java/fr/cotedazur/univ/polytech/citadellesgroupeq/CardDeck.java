package fr.cotedazur.univ.polytech.citadellesgroupeq;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

/**
 * La classe DistrictsJSONReader lit les données d'un fichier JSON contenant des informations sur les districts.
 * Elle convertit ces données en une liste d'objets District.
 */
public class CardDeck {

    /** Le tableau JSON contenant les données des districts. */
    private JSONArray jsonArray;
    /** La liste des districts lue à partir du fichier JSON. */
    private List<District> districtsList;

    private Queue<District> districtQueue;


    public static final String DEFAULT_PATH= "districts.json";

    /**
     * Construit un objet DistrictsJSONReader en lisant les données du fichier JSON contenant la liste des districts
     *
     * @throws BadlyInitializedReader Si une erreur se produit lors de la lecture du fichier ou si le format du fichier est incorrect.
     */
    public CardDeck(String path) {
        try {
            // Lis les données du fichier JSON
            jsonArray = (JSONArray) new JSONParser().parse(new FileReader(getClass().getClassLoader().getResource(path).getFile()));
            // Initialise la liste des districts
            districtsList = new ArrayList<>();
            // Parcours chaque élément du tableau JSON et crée un objet District correspondant
            for (JSONObject districtObject : (Iterable<JSONObject>) jsonArray) {
                if (districtObject.get("name") == null || districtObject.get("cost") == null || districtObject.get("color") == null || districtObject.get("quantity") == null) {
                    throw new InvalidPropertiesFormatException("Format du fichier JSON incorrect");
                } else {
                    String name = (String) districtObject.get("name");
                    int cost = ((Long) districtObject.get("cost")).intValue();
                    String color = (String) districtObject.get("color");
                    String pouvoir = (String) districtObject.get("power");
                    Long quantity = (Long) districtObject.get("quantity");

                    for(int i=0; i < quantity; i++) districtsList.add(new District(name, cost, color, pouvoir));//l'ajoute autant de fois que sa quantité
                }
            }
            Collections.shuffle(districtsList);
            districtQueue=new LinkedList<>(districtsList);
        }
        catch(IOException | NullPointerException | ParseException e) {
            throw new BadlyInitializedReader("Error while initializing reader");//arbitrary value
        }
    }

    public CardDeck() {
        this(DEFAULT_PATH);
    }

    public List<District> getDistrictsList() {
        return districtsList;
    }

    public Queue<District> getDistrictQueue() {
        return districtQueue;
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

    public District getFromIndex(int index) throws BadlyInitializedReader {
        if(index<0||index>= districtsList.size()) {
            throw new IllegalArgumentException("index must be in districts list");
        }
        else {
            return districtsList.get(index);
        }
    }

    public District pickTopCard() throws BadlyInitializedReader {
        District returnedDistrict=districtQueue.poll();
        if(returnedDistrict==null) {
            districtQueue=new LinkedList<>(districtsList);
            return districtQueue.poll();
        }
        else {
            return returnedDistrict;
        }
    }

    public void addDistrictUnderCardsPile(District district) {
        districtQueue.add(district);
    }

    public static class BadlyInitializedReader extends RuntimeException {
        public BadlyInitializedReader(Throwable error) {
            super(error);
        }

        public BadlyInitializedReader(String error) {
            super(error);
        }
    }
}

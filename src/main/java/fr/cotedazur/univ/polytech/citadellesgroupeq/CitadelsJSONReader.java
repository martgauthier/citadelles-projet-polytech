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

public class CitadelsJSONReader {
    private JSONArray jsonArray;
    private List<Citadel> citadelsList;
    public CitadelsJSONReader() throws IOException {
        try {
            jsonArray = (JSONArray) new JSONParser().parse(new FileReader("src/main/resources/citadels.json"));
            citadelsList=new ArrayList<>();
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

    public String getCitadelsListDescription() throws IOException {
        StringBuilder output=new StringBuilder("Citadelles présentes dans le .json:\n");
        CitadelsJSONReader citadelsList = new CitadelsJSONReader();
        for(Citadel citadel: citadelsList.getCitadelsList()) {
            output.append(citadel.getName() + " : " + citadel.getCost()).append("\n");
        }

        return output.toString();
    }
}

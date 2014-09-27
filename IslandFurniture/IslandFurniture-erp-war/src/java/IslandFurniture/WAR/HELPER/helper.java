/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IslandFurniture.WAR.HELPER;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.faces.model.SelectItem;

/**
 *
 * @author James
 */
public class helper {

    public static List<SelectItem> convertHMToDropdown(HashMap<String, String> data) {

        List<SelectItem> return_data = new ArrayList<SelectItem>();

        for (String k : data.keySet()) {
            return_data.add(new SelectItem(k, data.get(k).toString()));
        }
        return return_data;
    }

}

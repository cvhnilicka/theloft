package com.cormicopiastudios.theloftshared;
import com.cormicopiastudios.theloftshared.SharedObjects.PlayerObject;
import com.cormicopiastudios.theloftshared.SharedObjects.PlayerPos;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Shared utility methods for the client/servers
 *
 * */

public class SharedUtils {

    private static final String delimiter = ":DELM:";

    public static String serverMapToString(Map serverMap) {
        String string = "";

        Iterator itr = serverMap.entrySet().iterator();

        while (itr.hasNext()) {
            Map.Entry e = (Map.Entry) itr.next();
            Long tid = ((Long)e.getKey());
            PlayerObject po = ((PlayerObject)e.getValue());
            string += tid + delimiter;
            // need to update playerstuff to include positions
            string += po.pos.x + delimiter + po.pos.y + delimiter;
            string+= "\n";
        }
//        System.out.println(string);
        return string;
    }


    public static void stringToMap(HashMap clientMap, String payload) {
        String[] lines = payload.split("\n");
        for(int i = 0; i < lines.length; i++) {
            String[] posInfo = lines[i].split(delimiter);
            PlayerObject tmpPlayer = new PlayerObject();
            tmpPlayer.pos.x = Float.valueOf(posInfo[1]);
            tmpPlayer.pos.y = Float.valueOf(posInfo[2]);
            if (clientMap.containsKey(Integer.valueOf(posInfo[0]))) {
                //update
                ((PlayerObject)clientMap.get(Integer.valueOf(posInfo[0]))).pos.x = tmpPlayer.pos.x;
                ((PlayerObject)clientMap.get(Integer.valueOf(posInfo[0]))).pos.y = tmpPlayer.pos.y;
            } else {
                // add
                clientMap.put(Integer.valueOf(posInfo[0]), tmpPlayer);
            }


        }
    }




}

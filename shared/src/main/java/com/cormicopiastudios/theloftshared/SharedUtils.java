package com.cormicopiastudios.theloftshared;
import com.cormicopiastudios.theloftshared.SharedObjects.PlayerObject;

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

        }

        return string;
    }

}

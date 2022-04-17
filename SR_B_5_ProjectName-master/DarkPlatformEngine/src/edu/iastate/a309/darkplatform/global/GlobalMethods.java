package edu.iastate.a309.darkplatform.global;

import com.google.gson.Gson;
import edu.iastate.a309.darkplatform.server.entity.Map;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class GlobalMethods {

    public static final String mapURL = "http://proj-309-sr-b-5.cs.iastate.edu:8080/map/";

    public static int MAP_1;

    public static List<Map> maps = new ArrayList<Map>();

    public static void init() {
        //MAP_1 = createMap(1L);
    }

    public static String getRest(String url) {
        return new RestTemplate().getForObject(url, String.class);
    }

    public static void putMap(Long id, Map map) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map> entity = new HttpEntity<Map>(map, httpHeaders);
        ResponseEntity<String> out = restTemplate.exchange(mapURL + id, HttpMethod.PUT, entity, String.class);

    }

    private static int createMap(Long id) {
        maps.add(new Gson().fromJson(GlobalMethods.getRest(mapURL + id), Map.class));
        return maps.size() - 1;
    }

    public static Map getMap(int index) {
        return maps.get(index);
    }

    public static void updateMaps() {
        int index = 0;
        for (Map map : maps) {
            long id = map.getId();
            Map newMap = new Gson().fromJson(GlobalMethods.getRest(mapURL + id), Map.class);
            maps.set(index++, newMap);
        }
    }

    /**
     * Aplha blends two colors together.
     *
     * @param oolor1 the source color
     * @param color2 the color being added
     * @return the value of the two colors blended
     */
    public static int alphaBlend(int oolor1, int color2) {
        int alpha = (oolor1 & 0xff000000) >>> 24;
        if (alpha == 0)
            return color2;

        int red1 = (oolor1 & 0x00ff0000) >> 16;
        int red2 = (color2 & 0x00ff0000) >> 16;

        int green1 = (oolor1 & 0x0000ff00) >> 8;
        int green2 = (color2 & 0x0000ff00) >> 8;

        int blue1 = (oolor1 & 0x000000ff);
        int blue2 = (color2 & 0x000000ff);

        float src_alpha = ((float) alpha) / 255.0f;

        int red = (int) ((red1 * src_alpha) + (red2 * (1.0f - src_alpha)));
        int green = (int) ((green1 * src_alpha) + (green2 * (1.0f - src_alpha)));
        int blue = (int) ((blue1 * src_alpha) + (blue2 * (1.0f - src_alpha)));

        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }
}

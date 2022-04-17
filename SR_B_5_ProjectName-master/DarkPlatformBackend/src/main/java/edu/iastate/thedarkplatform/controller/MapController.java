package edu.iastate.thedarkplatform.controller;

import edu.iastate.thedarkplatform.entity.Map;
import edu.iastate.thedarkplatform.repository.MapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/map")
public class MapController {

    @Autowired
    private MapRepository mapRepository;

    @GetMapping("/{name}")
    public Map getMap(@PathVariable String name) {
        return mapRepository.findByName(name);
    }

    @PostMapping
    @ResponseBody
    public Map saveMap(@RequestBody Map map) {
        return mapRepository.save(map);
    }


    @PutMapping
    @ResponseBody
    public Map updateMap(@RequestBody Map map) {
        return mapRepository.save(map);
    }

}

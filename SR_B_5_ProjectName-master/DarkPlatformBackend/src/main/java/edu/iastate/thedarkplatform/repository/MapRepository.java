package edu.iastate.thedarkplatform.repository;

import edu.iastate.thedarkplatform.entity.Map;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MapRepository extends JpaRepository<Map, Long> {
    Map findByName(String name);
}

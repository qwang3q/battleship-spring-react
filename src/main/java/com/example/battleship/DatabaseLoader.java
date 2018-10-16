package com.example.battleship;

import com.example.battleship.controller.DataController;
import com.example.battleship.controller.GameController;
import com.example.battleship.repository.CellRepository;
import com.example.battleship.repository.MapRepository;
import com.example.battleship.repository.ShipRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
public class DatabaseLoader<DatabaseController> implements CommandLineRunner {

    private EntityManager em;

    private final MapRepository mapRepository;
    private final CellRepository cellRepository;
    private final ShipRepository shipRepository;

    private Logger logger = LoggerFactory.getLogger(DatabaseLoader.class);

    @Autowired
    public DatabaseLoader(MapRepository mapRepository,
                          CellRepository cellRepository,
                          ShipRepository shipRepository) {
        this.mapRepository = mapRepository;
        this.cellRepository = cellRepository;
        this.shipRepository = shipRepository;
    }

    @Override
    public void run(String... strings) throws Exception {
        GameController gameController = new GameController();
        gameController.setUpGame();

        this.mapRepository.save(gameController.getUserFleetBoard());
        this.mapRepository.save(gameController.getComputerFleetBoard());

        logger.info("An INFO Message: Game started");
    }
}
package nsu.fit.tsukanov.courier;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nsu.fit.tsukanov.interfaces.PizzaService;
import nsu.fit.tsukanov.storage.Storage;
import nsu.fit.tsukanov.workingType.WorkingType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class CourierService implements PizzaService {
    private final CourierRepository courierRepository;
    private final List<Thread> threads = new ArrayList<>();
    private final Map<Courier, CourierRun> courierRunMap = new HashMap<>();
    private final Storage storage;

    @Override
    public void initialize() {
        List<Courier> couriers = courierRepository.findAll();
        if (couriers.isEmpty()) {
            List<Courier> initCouriers = new ArrayList<>();
            initCouriers.add(new Courier(0L, "paul", 4, 4, 2));
            initCouriers.add(new Courier(1L, "albert", 8, 1, 1));
            initCouriers.add(new Courier(2L, "ban", 5, 2, 3));
            courierRepository.addAll(initCouriers);
            log.info("First initialization curs");
        } else {
            log.info("Downloaded couriers, amount: {}", couriers.size());
        }
        log.info("Couriers was initialized: {}", couriers.size());

    }

    @Override
    public void startWorking() {
        log.info("Courier service start working");
        for (Courier courier : courierRepository.findAll()) {
            var courierRun = new CourierRun(courier, storage, WorkingType.WORKING);
            courierRunMap.put(courier, courierRun);
            Thread thread = new Thread(courierRun);
            thread.start();
            threads.add(thread);
        }
    }

    @Override
    public void enableWorking() {

    }

    @Override
    public void stopWorking() {

    }

    @Override
    public void finalWorking() {

    }

    @Override
    public void alarmWorking() {

    }
    //TODO: CourierService class
}

package nsu.fit.tsukanov.baker;

import lombok.extern.slf4j.Slf4j;
import nsu.fit.tsukanov.baker.repository.BakerRepository;
import nsu.fit.tsukanov.interfaces.PizzaService;
import nsu.fit.tsukanov.order.OrderBoard;
import nsu.fit.tsukanov.storage.Storage;
import nsu.fit.tsukanov.workingType.WorkingType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class BakerService implements PizzaService {
    private final BakerRepository bakerRepository;
    private final List<Thread> threads = new ArrayList<>();
    private final Map<Baker, BakerRun> bakerRunMap = new HashMap<>();
    private final Storage storage;

    private final OrderBoard orderBoard;

    public BakerService(BakerRepository bakerRepository, Storage storage, OrderBoard orderBoard) {
        this.bakerRepository = bakerRepository;
        this.storage = storage;
        this.orderBoard = orderBoard;
    }

    private List<Baker> initializationList() {
        List<Baker> bakers = new ArrayList<>();
        bakers.add(new Baker(0L, "paul", 4, 4));
        bakers.add(new Baker(1L, "albert", 8, 1));
        bakers.add(new Baker(2L, "ban", 5, 2));
        bakers.add(new Baker(3L, "bert", 15, 0));
        bakers.add(new Baker(4L, "random", 2, 15));
        return bakers;
    }

    @Override
    public void initialize() {
        List<Baker> bakers = bakerRepository.findAll();
        if (bakers.isEmpty()) {
            bakerRepository.addAll(initializationList());
            log.info("First initialization");
        } else {
            log.info("Downloaded bakers, amount: {}", bakers.size());
        }
        log.info("Bakers was initialized: {}", bakers.size());
    }

    public void startWorking() {
        for (Baker baker : bakerRepository.findAll()) {
            var bakerRun = new BakerRun(baker, storage, orderBoard, WorkingType.WORKING);
            bakerRunMap.put(baker, bakerRun);
            Thread thread = new Thread(bakerRun);
            thread.start();
            threads.add(thread);
        }
        log.info("Baker Service started working, amount: {}", threads.size());
    }

    public void enableWorking() {
        setWorking(WorkingType.WORKING);
        for (Thread thread : threads) {
            thread.start();
        }
    }

    public void finalWorking() {
        setWorking(WorkingType.LAST);
    }

    @Override
    public void alarmWorking() {
        setWorking(WorkingType.ALARM);
        for (Thread thread : threads) {
            thread.interrupt();
        }
        threads.clear();
    }

    public void stopWorking() {
        setWorking(WorkingType.STOP);
    }


    public void setWorking(WorkingType workingType) {
        log.info("Set working [{}] for all ", workingType);
        bakerRunMap.values().forEach((bakerRun -> bakerRun.setWorkingType(workingType)));
    }

    public void setWorking(Baker baker, WorkingType workingType) {
        bakerRunMap.get(baker).setWorkingType(workingType);
    }

}

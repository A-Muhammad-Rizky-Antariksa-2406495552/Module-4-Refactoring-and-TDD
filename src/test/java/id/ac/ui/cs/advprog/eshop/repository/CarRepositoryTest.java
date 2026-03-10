package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class CarRepositoryTest {

    private CarRepository carRepository;
    private Car car;

    @BeforeEach
    void setUp() {
        carRepository = new CarRepository();

        car = new Car();
        car.setCarId("car-001");
        car.setCarName("Toyota Avanza");
        car.setCarColor("White");
        car.setCarQuantity(5);
    }

    @Test
    void testCreateCarWithId() {
        Car saved = carRepository.create(car);
        assertEquals("car-001", saved.getCarId());
        assertEquals("Toyota Avanza", saved.getCarName());
    }

    @Test
    void testCreateCarWithoutId() {
        Car newCar = new Car();
        newCar.setCarName("Honda Jazz");
        newCar.setCarColor("Black");
        newCar.setCarQuantity(3);

        Car saved = carRepository.create(newCar);
        assertNotNull(saved.getCarId());
    }

    @Test
    void testFindAll() {
        carRepository.create(car);

        Car car2 = new Car();
        car2.setCarId("car-002");
        car2.setCarName("Honda Civic");
        car2.setCarColor("Black");
        car2.setCarQuantity(2);
        carRepository.create(car2);

        Iterator<Car> iterator = carRepository.findAll();
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    void testFindByIdValid() {
        carRepository.create(car);
        Car found = carRepository.findById("car-001");
        assertNotNull(found);
        assertEquals("car-001", found.getCarId());
    }

    @Test
    void testFindByIdInvalid() {
        Car found = carRepository.findById("nonexistent");
        assertNull(found);
    }

    @Test
    void testUpdateExistingCar() {
        carRepository.create(car);

        Car updatedCar = new Car();
        updatedCar.setCarName("Toyota Avanza Updated");
        updatedCar.setCarColor("Red");
        updatedCar.setCarQuantity(10);

        Car result = carRepository.update("car-001", updatedCar);
        assertNotNull(result);
        assertEquals("Toyota Avanza Updated", result.getCarName());
        assertEquals("Red", result.getCarColor());
        assertEquals(10, result.getCarQuantity());
    }

    @Test
    void testUpdateNonExistingCar() {
        Car result = carRepository.update("nonexistent", car);
        assertNull(result);
    }

    @Test
    void testDeleteCar() {
        carRepository.create(car);
        carRepository.delete("car-001");
        assertNull(carRepository.findById("car-001"));
    }

    @Test
    void testFindByIdWithOtherCarInList() {
        carRepository.create(car);

        Car car2 = new Car();
        car2.setCarId("car-002");
        car2.setCarName("Honda Civic");
        car2.setCarColor("Black");
        car2.setCarQuantity(2);
        carRepository.create(car2);

        Car found = carRepository.findById("car-002");
        assertNotNull(found);
        assertEquals("car-002", found.getCarId());
    }

    @Test
    void testUpdateWithOtherCarInList() {
        carRepository.create(car);

        Car car2 = new Car();
        car2.setCarId("car-002");
        car2.setCarName("Honda Civic");
        car2.setCarColor("Black");
        car2.setCarQuantity(2);
        carRepository.create(car2);

        Car updatedCar = new Car();
        updatedCar.setCarName("Honda Civic Updated");
        updatedCar.setCarColor("Red");
        updatedCar.setCarQuantity(5);

        Car result = carRepository.update("car-002", updatedCar);
        assertNotNull(result);
        assertEquals("Honda Civic Updated", result.getCarName());
    }
}
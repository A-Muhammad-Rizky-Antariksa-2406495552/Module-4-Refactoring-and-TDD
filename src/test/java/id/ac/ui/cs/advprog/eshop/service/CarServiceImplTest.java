package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarServiceImpl carService;

    private Car car;

    @BeforeEach
    void setUp() {
        car = new Car();
        car.setCarId("car-001");
        car.setCarName("Toyota Avanza");
        car.setCarColor("White");
        car.setCarQuantity(5);
    }

    @Test
    void testCreate() {
        when(carRepository.create(any(Car.class))).thenReturn(car);
        Car result = carService.create(car);
        assertEquals("car-001", result.getCarId());
        verify(carRepository, times(1)).create(car);
    }

    @Test
    void testFindAll() {
        List<Car> cars = Arrays.asList(car);
        Iterator<Car> iterator = cars.iterator();
        when(carRepository.findAll()).thenReturn(iterator);

        List<Car> result = carService.findAll();
        assertEquals(1, result.size());
        assertEquals("car-001", result.get(0).getCarId());
    }

    @Test
    void testFindByIdValid() {
        when(carRepository.findById("car-001")).thenReturn(car);
        Car result = carService.findById("car-001");
        assertNotNull(result);
        assertEquals("car-001", result.getCarId());
    }

    @Test
    void testFindByIdInvalid() {
        when(carRepository.findById("nonexistent")).thenReturn(null);
        Car result = carService.findById("nonexistent");
        assertNull(result);
    }

    @Test
    void testUpdate() {
        carService.update("car-001", car);
        verify(carRepository, times(1)).update("car-001", car);
    }

    @Test
    void testDelete() {
        carService.delete("car-001");
        verify(carRepository, times(1)).delete("car-001");
    }
}
package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CarControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CarService carService;

    @InjectMocks
    private CarController carController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(carController)
                .setViewResolvers((viewName, locale) -> (model, request, response) -> {})
                .build();
    }

    @Test
    void createCarPageReturnsCreateCarView() throws Exception {
        mockMvc.perform(get("/car/createCar"))
                .andExpect(status().isOk())
                .andExpect(view().name("createCar"))
                .andExpect(model().attributeExists("car"));
    }

    @Test
    void createCarPostRedirectsToCarList() throws Exception {
        mockMvc.perform(post("/car/createCar")
                        .param("carName", "Toyota Avanza")
                        .param("carColor", "Red")
                        .param("carQuantity", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/car/listCar"));

        verify(carService).create(any(Car.class));
    }

    @Test
    void carListPageReturnsCarListView() throws Exception {
        Car car = new Car();
        car.setCarId("1");
        car.setCarName("Toyota Avanza");
        car.setCarColor("Red");
        car.setCarQuantity(5);

        when(carService.findAll()).thenReturn(Arrays.asList(car));

        mockMvc.perform(get("/car/listCar"))
                .andExpect(status().isOk())
                .andExpect(view().name("carList"))
                .andExpect(model().attributeExists("cars"));
    }

    @Test
    void carListPageReturnsEmptyListWhenNoCars() throws Exception {
        when(carService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/car/listCar"))
                .andExpect(status().isOk())
                .andExpect(view().name("carList"))
                .andExpect(model().attributeExists("cars"));
    }

    @Test
    void editCarPageWithExistingCarReturnsEditView() throws Exception {
        Car car = new Car();
        car.setCarId("1");
        car.setCarName("Toyota Avanza");
        car.setCarColor("Red");
        car.setCarQuantity(5);

        when(carService.findById("1")).thenReturn(car);

        mockMvc.perform(get("/car/editCar/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("editCar"))
                .andExpect(model().attributeExists("car"));
    }

    @Test
    void editCarPageWithNonExistentCarRedirects() throws Exception {
        when(carService.findById("999")).thenReturn(null);

        mockMvc.perform(get("/car/editCar/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/car/listCar"));
    }

    @Test
    void editCarPostRedirectsToCarList() throws Exception {
        mockMvc.perform(post("/car/editCar")
                        .param("carId", "1")
                        .param("carName", "Toyota Avanza")
                        .param("carColor", "Blue")
                        .param("carQuantity", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/car/listCar"));

        verify(carService).update(eq("1"), any(Car.class));
    }

    @Test
    void deleteCarWithExistingCarDeletesAndRedirects() throws Exception {
        Car car = new Car();
        car.setCarId("1");
        car.setCarName("Toyota Avanza");

        when(carService.findById("1")).thenReturn(car);

        mockMvc.perform(post("/car/deleteCar/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/car/listCar"));

        verify(carService).delete("1");
    }

    @Test
    void deleteCarWithNonExistentCarRedirectsWithoutDeleting() throws Exception {
        when(carService.findById("999")).thenReturn(null);

        mockMvc.perform(post("/car/deleteCar/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/car/listCar"));

        verify(carService, never()).delete(any());
    }
}
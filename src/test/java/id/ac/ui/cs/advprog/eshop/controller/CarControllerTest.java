package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.service.CarServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarControllerTest {
    private static final String CAR_ID = "CAR-001";

    @Mock
    private CarServiceImpl carService;

    @Mock
    private Model model;

    @InjectMocks
    private CarController controller;

    @Test
    void testCreateCarPage() {
        String view = controller.createCarPage(model);

        ArgumentCaptor<Car> carCaptor = ArgumentCaptor.forClass(Car.class);
        verify(model).addAttribute(eq("car"), carCaptor.capture());
        assertNotNull(carCaptor.getValue());
        assertEquals("CreateCar", view);
    }

    @Test
    void testCreateCarPost() {
        Car car = new Car();
        car.setCarId(CAR_ID);

        String view = controller.createCarPost(car, model);

        verify(carService).create(car);
        assertEquals("redirect:listCar", view);
    }

    @Test
    void testCarListPage() {
        Car car = new Car();
        car.setCarId(CAR_ID);
        List<Car> cars = List.of(car);
        when(carService.findAll()).thenReturn(cars);

        String view = controller.carListPage(model);

        verify(model).addAttribute("cars", cars);
        assertEquals("CarList", view);
    }

    @Test
    void testEditCarPage() {
        Car car = new Car();
        car.setCarId(CAR_ID);
        when(carService.findById(CAR_ID)).thenReturn(car);

        String view = controller.editCarPage(CAR_ID, model);

        verify(model).addAttribute("car", car);
        assertEquals("EditCar", view);
    }

    @Test
    void testEditCarPost() {
        Car car = new Car();
        car.setCarId(CAR_ID);

        String view = controller.editCarPost(car, model);

        verify(carService).update(CAR_ID, car);
        assertEquals("redirect:listCar", view);
    }

    @Test
    void testDeleteCar() {
        String view = controller.deteleCar(CAR_ID);

        verify(carService).deleteCarById(CAR_ID);
        assertEquals("redirect:listCar", view);
    }
}

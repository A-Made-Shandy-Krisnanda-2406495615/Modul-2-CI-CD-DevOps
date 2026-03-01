package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.repository.CarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {
    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarServiceImpl carService;

    @Test
    void testCreate() {
        Car car = buildCar("CAR-001", "BMW", "Black", 2);

        Car createdCar = carService.create(car);

        assertSame(car, createdCar);
        verify(carRepository).create(car);
    }

    @Test
    void testFindAll() {
        Car firstCar = buildCar("CAR-001", "BMW", "Black", 2);
        Car secondCar = buildCar("CAR-002", "Audi", "White", 3);
        when(carRepository.findAll()).thenReturn(List.of(firstCar, secondCar).iterator());

        List<Car> result = carService.findAll();

        assertEquals(2, result.size());
        assertSame(firstCar, result.get(0));
        assertSame(secondCar, result.get(1));
        verify(carRepository).findAll();
    }

    @Test
    void testFindById() {
        Car car = buildCar("CAR-001", "BMW", "Black", 2);
        when(carRepository.findById("CAR-001")).thenReturn(car);

        Car foundCar = carService.findById("CAR-001");

        assertSame(car, foundCar);
        verify(carRepository).findById("CAR-001");
    }

    @Test
    void testUpdate() {
        Car updatedCar = buildCar("CAR-001", "Porsche", "Red", 9);

        carService.update("CAR-001", updatedCar);

        verify(carRepository).update("CAR-001", updatedCar);
    }

    @Test
    void testDeleteCarById() {
        carService.deleteCarById("CAR-001");

        verify(carRepository).delete("CAR-001");
    }

    private Car buildCar(String carId, String carName, String carColor, int carQuantity) {
        Car car = new Car();
        car.setCarId(carId);
        car.setCarName(carName);
        car.setCarColor(carColor);
        car.setCarQuantity(carQuantity);
        return car;
    }
}

package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.repository.CarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {
    private static final String CAR_ID_001 = "CAR-001";
    private static final String CAR_ID_002 = "CAR-002";
    private static final String CAR_NAME_BMW = "BMW";
    private static final String CAR_NAME_AUDI = "Audi";
    private static final String CAR_NAME_PORSCHE = "Porsche";
    private static final String COLOR_BLACK = "Black";
    private static final String COLOR_WHITE = "White";
    private static final String COLOR_RED = "Red";

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarServiceImpl carService;

    @Test
    void testCreate() {
        Car car = buildCar(CAR_ID_001, CAR_NAME_BMW, COLOR_BLACK, 2);

        Car createdCar = carService.create(car);

        assertSame(car, createdCar);
        verify(carRepository).create(car);
    }

    @Test
    void testCreateWhenCarIdNullShouldGenerateId() {
        Car car = buildCar(null, CAR_NAME_BMW, COLOR_BLACK, 2);

        Car createdCar = carService.create(car);

        assertNotNull(createdCar.getCarId());
        assertFalse(createdCar.getCarId().isBlank());
        verify(carRepository).create(car);
    }

    @Test
    void testCreateWhenCarIdBlankShouldGenerateId() {
        Car car = buildCar("   ", CAR_NAME_BMW, COLOR_BLACK, 2);

        Car createdCar = carService.create(car);

        assertNotNull(createdCar.getCarId());
        assertFalse(createdCar.getCarId().isBlank());
        verify(carRepository).create(car);
    }

    @Test
    void testFindAll() {
        Car firstCar = buildCar(CAR_ID_001, CAR_NAME_BMW, COLOR_BLACK, 2);
        Car secondCar = buildCar(CAR_ID_002, CAR_NAME_AUDI, COLOR_WHITE, 3);
        when(carRepository.findAll()).thenReturn(List.of(firstCar, secondCar).iterator());

        List<Car> result = carService.findAll();

        assertEquals(2, result.size());
        assertSame(firstCar, result.get(0));
        assertSame(secondCar, result.get(1));
        verify(carRepository).findAll();
    }

    @Test
    void testFindById() {
        Car car = buildCar(CAR_ID_001, CAR_NAME_BMW, COLOR_BLACK, 2);
        when(carRepository.findById(CAR_ID_001)).thenReturn(Optional.of(car));

        Car foundCar = carService.findById(CAR_ID_001);

        assertSame(car, foundCar);
        verify(carRepository).findById(CAR_ID_001);
    }

    @Test
    void testFindByIdWhenMissingShouldReturnNull() {
        when(carRepository.findById(CAR_ID_001)).thenReturn(Optional.empty());

        assertNull(carService.findById(CAR_ID_001));
        verify(carRepository).findById(CAR_ID_001);
    }

    @Test
    void testUpdate() {
        Car updatedCar = buildCar(CAR_ID_001, CAR_NAME_PORSCHE, COLOR_RED, 9);

        carService.update(CAR_ID_001, updatedCar);

        verify(carRepository).update(CAR_ID_001, updatedCar);
    }

    @Test
    void testDeleteCarById() {
        carService.deleteCarById(CAR_ID_001);

        verify(carRepository).delete(CAR_ID_001);
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

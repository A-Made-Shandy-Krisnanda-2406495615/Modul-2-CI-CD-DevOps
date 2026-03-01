package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CarRepositoryTest {
    private static final String CAR_ID_001 = "CAR-001";
    private static final String CAR_ID_002 = "CAR-002";
    private static final String CAR_ID_404 = "CAR-404";
    private static final String CAR_NAME_BMW = "BMW";
    private static final String CAR_NAME_AUDI = "Audi";
    private static final String CAR_NAME_PORSCHE = "Porsche";
    private static final String COLOR_BLACK = "Black";
    private static final String COLOR_WHITE = "White";
    private static final String COLOR_RED = "Red";

    private CarRepository carRepository;

    @BeforeEach
    void setUp() {
        carRepository = new CarRepository();
    }

    @Test
    void testCreateWhenIdNullShouldGenerateId() {
        Car car = buildCar(null, CAR_NAME_BMW, COLOR_BLACK, 2);

        Car createdCar = carRepository.create(car);

        assertSame(car, createdCar);
        assertNotNull(createdCar.getCarId());
        assertFalse(createdCar.getCarId().isBlank());
    }

    @Test
    void testCreateWhenIdProvidedShouldKeepId() {
        String carId = CAR_ID_001;
        Car car = buildCar(carId, CAR_NAME_AUDI, COLOR_WHITE, 3);

        Car createdCar = carRepository.create(car);

        assertEquals(carId, createdCar.getCarId());
    }

    @Test
    void testFindByIdShouldReturnCarIfFound() {
        Car car = buildCar(CAR_ID_001, CAR_NAME_BMW, COLOR_BLACK, 2);
        carRepository.create(car);

        Car foundCar = carRepository.findById(CAR_ID_001);

        assertSame(car, foundCar);
        assertNull(carRepository.findById(CAR_ID_404));
    }

    @Test
    void testFindAllShouldReturnAllCreatedCars() {
        Car firstCar = buildCar(CAR_ID_001, CAR_NAME_BMW, COLOR_BLACK, 2);
        Car secondCar = buildCar(CAR_ID_002, CAR_NAME_AUDI, COLOR_WHITE, 3);
        carRepository.create(firstCar);
        carRepository.create(secondCar);

        Iterator<Car> carIterator = carRepository.findAll();

        assertTrue(carIterator.hasNext());
        assertSame(firstCar, carIterator.next());
        assertTrue(carIterator.hasNext());
        assertSame(secondCar, carIterator.next());
        assertFalse(carIterator.hasNext());
    }

    @Test
    void testUpdateShouldReturnUpdatedCarIfFound() {
        Car existingCar = buildCar(CAR_ID_001, CAR_NAME_BMW, COLOR_BLACK, 2);
        carRepository.create(existingCar);

        Car newData = buildCar(CAR_ID_001, CAR_NAME_PORSCHE, COLOR_RED, 9);
        Car updatedCar = carRepository.update(CAR_ID_001, newData);

        assertSame(existingCar, updatedCar);
        assertEquals(CAR_NAME_PORSCHE, updatedCar.getCarName());
        assertEquals(COLOR_RED, updatedCar.getCarColor());
        assertEquals(9, updatedCar.getCarQuantity());
    }

    @Test
    void testUpdateShouldReturnNullIfNotFound() {
        Car newData = buildCar(CAR_ID_404, CAR_NAME_PORSCHE, COLOR_RED, 9);

        Car updatedCar = carRepository.update(CAR_ID_404, newData);

        assertNull(updatedCar);
    }

    @Test
    void testDeleteShouldRemoveMatchingCar() {
        Car firstCar = buildCar(CAR_ID_001, CAR_NAME_BMW, COLOR_BLACK, 2);
        Car secondCar = buildCar(CAR_ID_002, CAR_NAME_AUDI, COLOR_WHITE, 3);
        carRepository.create(firstCar);
        carRepository.create(secondCar);

        carRepository.delete(CAR_ID_001);

        assertNull(carRepository.findById(CAR_ID_001));
        assertSame(secondCar, carRepository.findById(CAR_ID_002));
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

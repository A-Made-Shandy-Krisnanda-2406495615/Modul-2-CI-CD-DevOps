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
    private CarRepository carRepository;

    @BeforeEach
    void setUp() {
        carRepository = new CarRepository();
    }

    @Test
    void testCreateWhenIdNullShouldGenerateId() {
        Car car = buildCar(null, "BMW", "Black", 2);

        Car createdCar = carRepository.create(car);

        assertSame(car, createdCar);
        assertNotNull(createdCar.getCarId());
        assertFalse(createdCar.getCarId().isBlank());
    }

    @Test
    void testCreateWhenIdProvidedShouldKeepId() {
        String carId = "CAR-001";
        Car car = buildCar(carId, "Audi", "White", 3);

        Car createdCar = carRepository.create(car);

        assertEquals(carId, createdCar.getCarId());
    }

    @Test
    void testFindByIdShouldReturnCarIfFound() {
        Car car = buildCar("CAR-001", "BMW", "Black", 2);
        carRepository.create(car);

        Car foundCar = carRepository.findById("CAR-001");

        assertSame(car, foundCar);
        assertNull(carRepository.findById("CAR-404"));
    }

    @Test
    void testFindAllShouldReturnAllCreatedCars() {
        Car firstCar = buildCar("CAR-001", "BMW", "Black", 2);
        Car secondCar = buildCar("CAR-002", "Audi", "White", 3);
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
        Car existingCar = buildCar("CAR-001", "BMW", "Black", 2);
        carRepository.create(existingCar);

        Car newData = buildCar("CAR-001", "Porsche", "Red", 9);
        Car updatedCar = carRepository.update("CAR-001", newData);

        assertSame(existingCar, updatedCar);
        assertEquals("Porsche", updatedCar.getCarName());
        assertEquals("Red", updatedCar.getCarColor());
        assertEquals(9, updatedCar.getCarQuantity());
    }

    @Test
    void testUpdateShouldReturnNullIfNotFound() {
        Car newData = buildCar("CAR-404", "Porsche", "Red", 9);

        Car updatedCar = carRepository.update("CAR-404", newData);

        assertNull(updatedCar);
    }

    @Test
    void testDeleteShouldRemoveMatchingCar() {
        Car firstCar = buildCar("CAR-001", "BMW", "Black", 2);
        Car secondCar = buildCar("CAR-002", "Audi", "White", 3);
        carRepository.create(firstCar);
        carRepository.create(secondCar);

        carRepository.delete("CAR-001");

        assertNull(carRepository.findById("CAR-001"));
        assertSame(secondCar, carRepository.findById("CAR-002"));
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

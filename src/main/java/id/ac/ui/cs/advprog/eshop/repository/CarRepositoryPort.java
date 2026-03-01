package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;

import java.util.Iterator;
import java.util.Optional;

public interface CarRepositoryPort {
    Car create(Car car);
    Iterator<Car> findAll();
    Optional<Car> findById(String id);
    Car update(String id, Car updatedCar);
    void delete(String id);
}


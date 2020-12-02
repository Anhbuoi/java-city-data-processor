package com.wwt.locations;

import com.wwt.locations.data.CityDataSource;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

// FIXME: #2 Please implement this! See CityDataLocationServiceTest for some examples of how this should work.
public class CityDataLocationService implements LocationService {
    private final Set<City> allCities;

    public CityDataLocationService(CityDataSource cityDataSource) throws IOException {
        this.allCities = cityDataSource.getAllCities();
    }

    @Override
    public Set<State> getStates() {
        return allCities.stream()
                .map(City::getState)
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<State> findStateByName(String stateName) {
        return allCities.stream()
                .map(City::getState)
                .filter(state -> stateName.equals(state.getName()))
                .findFirst();
    }

    @Override
    public Set<City> findCitiesByZipCode(String zipCode) {
        return allCities.stream()
                .filter(city -> city.getZipCodes().contains(zipCode))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<City> findCitiesIn(State state) {
        return allCities.stream()
                .filter(city -> state.equals(city.getState()))
                .collect(Collectors.toSet());
    }

    @Override
    public int calculatePopulation(State state) {
        return findCitiesIn(state).stream()
                .mapToInt(City::getPopulation)
                .sum();
    }

    @Override
    public Set<City> findCitiesNearLocation(Coordinate location, double radiusInKilometers) {
        if (radiusInKilometers < 0) {
            throw new IllegalArgumentException("Invalid negative radius.");
        }
        return allCities.stream()
                .filter(city -> radiusInKilometers >= CoordinateDistanceCalculator.kilometersBetween(location, city.getLocation()))
                .collect(Collectors.toSet());
    }
}

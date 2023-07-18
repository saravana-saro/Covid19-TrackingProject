package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.example.demo.model.LocationStates;


@Repository
@EnableJpaRepositories
public interface CovidDataRepository extends JpaRepository< LocationStates,Long> {

	List<LocationStates> findBycountryBylatestTotalDeaths(int count);

	LocationStates findByCountry(String countryName);

	Optional<LocationStates> findById(int countryid);

	void save(List<LocationStates> allStates);
	
	@Query(value = "SELECT MAX(state) FROM LocationStates")
	public int maxValue();
	
	@Query(value = "SELECT s FROM LocationStates s WHERE state=(SELECT MAX(state)FROM LocationStates)")
	public LocationStates minValue();
       	
}
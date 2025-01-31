package com.shopme.admin.setting.state;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import com.shopme.common.entity.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.State;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class StateRepositoryTests {

	@Autowired private StateRepository stateRepository;
	@Autowired private TestEntityManager entityManager;
	
	@Test
	public void testCreateStatesInIndia() {
		Integer countryId = 2;
		Country country = entityManager.find(Country.class, countryId);
		
		//  State state = stateRepository.save(new State("Karnataka", country));
	    //  State state = stateRepository.save(new State("Punjab", country));
	    //	State state = stateRepository.save(new State("Uttar Pradesh", country));
		State state = stateRepository.save(new State("West Bengal", country));
		
		assertThat(state).isNotNull();
		assertThat(state.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateStatesInUS() {
		Integer countryId = 3;
		Country country = entityManager.find(Country.class, countryId);
		
      //  State state = stateRepository.save(new State("California", country));
      //  State state = stateRepository.save(new State("Texas", country));
      //  State state = stateRepository.save(new State("New York", country));
     	State state = stateRepository.save(new State("Washington", country));
		
		assertThat(state).isNotNull();
		assertThat(state.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListStatesByCountry() {
		Integer countryId = 2;
		Country country = entityManager.find(Country.class, countryId);
		List<State> listStates = stateRepository.findByCountryOrderByNameAsc(country);
		
		listStates.forEach(System.out::println);
		
		assertThat(listStates.size()).isGreaterThan(0);
	}
	
	@Test
	public void testUpdateState() {
		Integer stateId = 1;
		String stateName = "Shanghai";

		State state = stateRepository.findById(stateId).get();
		
        state.setName(stateName);
		State updatedState = stateRepository.save(state);
		
		assertThat(updatedState.getName()).isEqualTo(stateName);
	}
	
	@Test
	public void testGetState() {
		Integer stateId = 1;
		Optional<State> findById = stateRepository.findById(stateId);
		assertThat(findById.isPresent());
	}
	
	@Test
	public void testDeleteState() {
		Integer stateId = 8;
		stateRepository.deleteById(stateId);

		Optional<State> findById = stateRepository.findById(stateId);
		assertThat(findById.isEmpty());		
	}
}

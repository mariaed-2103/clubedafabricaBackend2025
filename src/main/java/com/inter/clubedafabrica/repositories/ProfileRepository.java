package com.inter.clubedafabrica.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inter.clubedafabrica.entities.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
}


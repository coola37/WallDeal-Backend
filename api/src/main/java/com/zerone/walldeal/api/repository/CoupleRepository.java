package com.zerone.walldeal.api.repository;

import com.zerone.walldeal.api.entity.Couple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoupleRepository extends JpaRepository<Couple, String> {

}

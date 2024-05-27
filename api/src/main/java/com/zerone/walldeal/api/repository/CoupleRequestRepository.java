package com.zerone.walldeal.api.repository;

import com.zerone.walldeal.api.entity.CoupleRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoupleRequestRepository extends JpaRepository<CoupleRequest, String> {
    List<CoupleRequest> findByReceiverUser_UserId(String userId);
}

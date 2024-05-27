package com.zerone.walldeal.api.repository;

import com.zerone.walldeal.api.entity.WallpaperRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WallpaperRequestRepository extends JpaRepository<WallpaperRequest, String> {
}

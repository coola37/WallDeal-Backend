package com.zerone.walldeal.api.repository;

import com.zerone.walldeal.api.entity.Wallpaper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WallpaperRepository extends JpaRepository<Wallpaper, Integer> {
    List<Wallpaper> findByUser_UserId(String userId);
    List<Wallpaper> findByCategory(String category);
}

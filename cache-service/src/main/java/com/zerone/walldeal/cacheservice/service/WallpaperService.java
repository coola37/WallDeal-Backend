package com.zerone.walldeal.cacheservice.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.zerone.walldeal.cacheservice.entity.Wallpaper;
import com.zerone.walldeal.cacheservice.feign.WsFeignClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Service
public class WallpaperService {
    private final WsFeignClient wsFeignClient;

    public List<Wallpaper> getWallpapers(){
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference wallpapersRef = db.collection("wallpapers");
        List<Wallpaper> wallpaperList = new ArrayList<>();
        ApiFuture<QuerySnapshot> future = wallpapersRef.get();

        List<Wallpaper> wallpapers = new ArrayList<>();

        try {
            QuerySnapshot querySnapshot = future.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

            for (QueryDocumentSnapshot document : documents) {
                Wallpaper wallpaper = document.toObject(Wallpaper.class);
                wallpapers.add(wallpaper);
            }
            Optional<List<Wallpaper>> optionalWallpapers = Optional.of(wallpapers);
            if(optionalWallpapers.isPresent()){
                wallpaperList = optionalWallpapers.get();
                System.err.println("Present");
            }else {
                System.err.println("Else");
                wallpaperList = (List<Wallpaper>) wsFeignClient.getWallpapers().getBody().getPayload();
            }
        } catch (FeignException.FeignClientException.NotFound | InterruptedException | ExecutionException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallpapers not found");
        }
        return wallpaperList;
    }
}

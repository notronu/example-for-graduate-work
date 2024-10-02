package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.exception.NotFoundException;


import java.io.IOException;


@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class AdsController {
    private final AdsService adsService;


    @GetMapping
    public ResponseEntity<Ads> getAllAds() {
        return ResponseEntity.ok(adsService.getAll());
    }

    @GetMapping("/me")
    public ResponseEntity<Ads> getAdsMe(Authentication authentication) {
        Ads ads = adsService.getMyAds(authentication.getName());
        return ResponseEntity.ok(ads);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAd(@PathVariable Integer id) {
        return ResponseEntity.ok(adsService.getExtendedAd(id));
    }

    @GetMapping(value = "{id}/image", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<byte[]> getImage(@PathVariable Integer id) throws IOException {
        return ResponseEntity.ok(adsService.getImage(id));
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Ad> addAd(@RequestPart(value = "properties") CreateOrUpdateAd createOrUpdateAd,
                                    @RequestPart(value = "image") MultipartFile image,
                                    Authentication authentication) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(adsService.addAd(createOrUpdateAd, image, authentication));
    }

    @PreAuthorize("hasAuthority('ADMIN') or @adServiceImpl.getAd(#id).user.email == authentication.principal.username")
    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAd(@PathVariable Integer id,
                                          @RequestBody CreateOrUpdateAd createOrUpdateAd,
                                          Authentication authentication) {
        return ResponseEntity.ok(adsService.updateAd(id, createOrUpdateAd, authentication));
    }

    @PreAuthorize("hasAuthority('ADMIN') or @adServiceImpl.getAd(#id).user.email == authentication.principal.username")
    @PatchMapping(value = "{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<byte[]> updateAdImage(@PathVariable Integer id,
                                                @RequestParam MultipartFile image,
                                                Authentication authentication) throws IOException {

        return ResponseEntity.ok(adsService.updateAdImage(id, image, authentication));

    }

    @PreAuthorize("hasAuthority('ADMIN') or @adServiceImpl.getAd(#id).user.email == authentication.principal.username")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAd(@PathVariable Integer id, Authentication authentication) {
        try {
            adsService.deleteAd(id, authentication);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}

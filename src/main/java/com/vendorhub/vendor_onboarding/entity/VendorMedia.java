package com.vendorhub.vendor_onboarding.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "vendor_media")
@Getter
@Setter
public class VendorMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "image is required.")
    @Column(nullable = false , name = "image")
    private String image;

    @NotBlank(message = "video is required .")
    @Column(nullable = false , name = "video")
    private String video;

    @OneToOne
    @JoinColumn(name = "vendor_id",referencedColumnName = "id",nullable = false)
    @JsonBackReference
    private VendorProfile vendorProfile;
}

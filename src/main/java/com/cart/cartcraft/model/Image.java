package com.cart.cartcraft.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Blob;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String fileType;

    @Lob
    @JsonIgnore
    private Blob image;

    private String downloadUrl;

    // Relationship
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "product_id")
    private Product product;
}
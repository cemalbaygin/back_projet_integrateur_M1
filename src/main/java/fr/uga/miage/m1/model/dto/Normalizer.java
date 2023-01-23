package fr.uga.miage.m1.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
public class Normalizer<T> {
    int currentPage;
    int totalPage;
    long totalItems;
    int nbElementPerPage;
    Object data;
    
    public Normalizer(Object data, Page<T> page) {
        this.data = data;
        this.currentPage = page.getNumber();
        this.totalPage = page.getTotalPages();
        this.totalItems = page.getTotalElements();
        this.nbElementPerPage = page.getSize();
    }
}

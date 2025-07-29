package com.itheima.ratelimitdemo.entity;

import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem implements Serializable {
    private Integer productId;
    private Integer quantity;
}
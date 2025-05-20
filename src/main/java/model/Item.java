package model;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Item {
    private String code;
    private String name;
    private String description;
    private Double unitPrice;
    private Double qtyOnHand;
}

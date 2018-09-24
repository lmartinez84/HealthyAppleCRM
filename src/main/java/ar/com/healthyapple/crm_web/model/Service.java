package ar.com.healthyapple.crm_web.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode (of = {"id"})
@NoArgsConstructor
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @OneToOne
    private Product product;
    private BigDecimal cost;

    public Service(String name, String description, Product product, BigDecimal cost) {
        this.name = name;
        this.description = description;
        this.product = product;
        this.cost = cost;
    }
}
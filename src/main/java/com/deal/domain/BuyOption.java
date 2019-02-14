package com.deal.domain;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "buy_option")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "deal")
@ToString(exclude = "deal")
public class BuyOption {

    @Id
    private String title;

    @Min(0)
    @Column(nullable = false)
    private Double normalPrice = 0.0;

    @Min(0)
    @Column(nullable = false)
    private Double salePrice = 0.0;

    @Min(0)
    @Column(nullable = false)
    private Double percentageDiscount = 0.0;

    @Min(0)
    @Column(nullable = false)
    private Long quantityCupom = 0L;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "deal_id")
    private Deal deal;

    public BuyOption sellUnity() {
        this.quantityCupom = --this.quantityCupom;
        return this;
    }

    public boolean isNew() {
        return this.title == null;
    }

    public void calculeSalesPrice() {
        final BigDecimal normalPrice = BigDecimal.valueOf(this.getNormalPrice());
        final BigDecimal discount = normalPrice
                .multiply(BigDecimal.valueOf(this.getPercentageDiscount()))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        final BigDecimal salePrice = normalPrice.subtract(discount);
        this.setSalePrice(salePrice.doubleValue());
    }
}

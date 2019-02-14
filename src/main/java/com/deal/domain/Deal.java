package com.deal.domain;

import com.github.slugify.Slugify;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "deal")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "buyOptions")
public class Deal {

    @Id
    private String title;

    private String text;

    @CreatedDate
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createDate;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date publishDate;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    @Min(0)
    private Long totalSold = 0L;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeDeal type;

    @OneToMany
    @JoinColumn(name = "deal_id")
    private Set<BuyOption> buyOptions = new HashSet<>();

    public void plusTotalSold() {
        this.totalSold = ++this.totalSold;
    }

    public boolean isNew() {
        return this.title == null;
    }

    public Deal slugfyDeal() {
        final Slugify slg = new Slugify();
        this.setUrl(slg.slugify(this.getTitle()));
        return this;
    }
}

package com.example.demo.Stock.Domain;

import com.example.demo.utils.Exception.BusinessException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "stocks")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class Stock{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Long total;

    @NotNull
    //@Column(nullable = false, columnDefinition = "INTEGER CHECK > 0")
    private Long remain;

    public Stock(@NotNull Long total, @NotNull Long remain) {
        this.total = total;
        this.remain = remain;
    }

    public void update(Long num){
        Long selled = total - remain;
        if(num - selled < 0){ //팔린 개수가 업데이트 되려는 개수보다 많다면
            throw new IllegalArgumentException();
        }

        if(total < num){
            increase(num);
        }else {
            decrease(num);
        }
    }

    protected void increase(Long num){
        this.remain += (num - total);
        this.total = num;
    }

    protected void decrease(Long num){
        Long selled = total - remain;
        this.total = num;
        this.remain = num - selled;
    }

    public void reserved(){
        if(this.remain <= 0)
            throw new BusinessException(ErrorCode.쿠폰_재고_부족);
        this.remain -= 1;
    }

    public void cancel(){
        this.remain += 1;
    }
}

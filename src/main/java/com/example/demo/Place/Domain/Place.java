package com.example.demo.Place.Domain;

import com.example.demo.Room.Domain.Room;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place {
    private static final String PROCEDURE_PARAM = "PLACE";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
//    @GenericGenerator(name = "IdGenerator",
//        strategy = "com.example.demo.utils.Generator.IdGenerator",
//        parameters = @org.hibernate.annotations.Parameter(
//                name = IdGenerator.ID_GENERATOR_KEY,
//                value = PROCEDURE_PARAM
//        ))
//    @GeneratedValue(generator = "IdGenerator")
    private Long id;

    @Column(nullable = false)
    private PlaceType placeType;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = true)
    private String facilitiesServices;

    @OneToMany(mappedBy = "place")
    private List<Room> rooms;

    @OneToMany(mappedBy = "place")
    private List<PriceType> priceTypes;

    @OneToMany(mappedBy = "place")
    private List<PlacePeriod> placePeriods;

    @OneToMany(mappedBy = "place")
    private List<Discount> discounts;

    @OneToMany(mappedBy = "place")
    private List<ChangeOfDay> changeOfDays;

    @Builder
    public Place(PlaceType placeType, String name, String address, String facilitiesServices){
        this.placeType = placeType;
        this.name = name;
        this.address = address;
        this.facilitiesServices = facilitiesServices;
    }

    public void addPlacePeriod(PlacePeriod placePeriod){
        PlacePeriodGroups placePeriodGroups = new PlacePeriodGroups(placePeriods);
        placePeriodGroups.isOverlap(placePeriod);
        placePeriods.add(placePeriod);
    }

    public void addDiscount(Discount discount){
        this.discounts.add(discount);
    }

    public void deleteDiscount(Long discountId){
        Iterator<Discount> iterator = discounts.iterator();
        while(iterator.hasNext()){
            if(iterator.next().getId().equals(discountId)){
                iterator.remove();
            }
        }
    }

    public void deleteDiscountUsingStream(Long discountId){
        discounts.stream().filter(discount -> discountId == discount.getId()).collect(Collectors.toList());
    }

    public void addChangeOfDay(ChangeOfDay changeOfDay){
        this.changeOfDays.add(changeOfDay);
    }

    public void deleteDayOfWeek(Long changeOfDayId){
        Iterator<ChangeOfDay> iterator = changeOfDays.iterator();
        while(iterator.hasNext()){
            if(iterator.next().getId().equals(changeOfDayId)){
                iterator.remove();
            }
        }
    }

    public PriceType getDefaultPriceType(){
        return this.getPriceTypes().stream()
                .filter(priceType -> priceType.getTypeName() == "비수기")
                .findAny()
                .orElseThrow(EntityNotFoundException::new);
    }
}

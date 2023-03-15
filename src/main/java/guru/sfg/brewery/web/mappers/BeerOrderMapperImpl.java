package guru.sfg.brewery.web.mappers;

import guru.sfg.brewery.domain.Beer;
import guru.sfg.brewery.domain.BeerOrder;
import guru.sfg.brewery.domain.BeerOrderLine;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.web.model.BeerOrderDto;
import guru.sfg.brewery.web.model.BeerOrderLineDto;
import guru.sfg.brewery.web.model.OrderStatusEnum;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Component
public class BeerOrderMapperImpl implements BeerOrderMapper {

    @Autowired
    private BeerRepository beerRepository;

    @Override
    public BeerOrderDto beerOrderToDto(BeerOrder beerOrder) {
        if (beerOrder == null) {
            return null;
        }

        UUID id = beerOrder.getId();
        Integer version = null;
        OffsetDateTime createdDate = null;
        OffsetDateTime lastModifiedDate = null;
        UUID customerId = null;
        List<BeerOrderLineDto> beerOrderLines = null;
        OrderStatusEnum orderStatus = null;
        String orderStatusCallbackUrl = null;
        String customerRef = null;

        BeerOrderDto beerOrderDto = new BeerOrderDto(id, version, createdDate, lastModifiedDate, customerId, beerOrderLines, orderStatus, orderStatusCallbackUrl, customerRef);

        return beerOrderDto;
    }

    @Override
    public BeerOrder dtoToBeerOrder(BeerOrderDto dto) {
        if (dto == null) {
            return null;
        }

        val beerOrderLinesSet = new HashSet<BeerOrderLine>();

        dto.getBeerOrderLines().forEach((item) -> {
            Beer beer = beerRepository.findOneById(item.getBeerId());
            var beerOrderLine = BeerOrderLine.builder()
                    .beer(beer)
                    .orderQuantity(item.getOrderQuantity())
                    .quantityAllocated(0)
                    .build();
            beerOrderLinesSet.add(beerOrderLine);
        });

        BeerOrder beerOrder = BeerOrder.builder()
                .id(dto.getId())
                .customerRef(dto.getCustomerRef())
                .beerOrderLines(beerOrderLinesSet)
                .build();

        /*UUID id = null;
        Long version = null;
        Timestamp createdDate = null;
        Timestamp lastModifiedDate = null;
        String customerRef = null;
        Customer customer = null;
        Set<BeerOrderLine> beerOrderLines = null;
        guru.sfg.brewery.domain.OrderStatusEnum orderStatus = null;
        String orderStatusCallbackUrl = null;

        BeerOrder beerOrder = new BeerOrder( id, version, createdDate, lastModifiedDate, customerRef, customer, beerOrderLines, orderStatus, orderStatusCallbackUrl );
*/
        return beerOrder;
    }
}
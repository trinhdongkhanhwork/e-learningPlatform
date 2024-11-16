package edu.cfd.e_learningPlatform.mapstruct;


import edu.cfd.e_learningPlatform.dto.PaymentDto;
import edu.cfd.e_learningPlatform.dto.response.PaymentResponse;
import edu.cfd.e_learningPlatform.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "paymentStatus.id", target = "paymentStatusId")
    PaymentDto paymentToPaymentDto(Payment payment);

    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "course.title", target = "courseTitle")  // Ánh xạ courseTitle
    @Mapping(source = "paymentStatus.id", target = "paymentStatusId")
    PaymentResponse paymentToPaymentResponse(Payment payment);

    @Mapping(source = "courseId", target = "course.id")
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "paymentStatusId", target = "paymentStatus.id")
    Payment paymentDtoToPayment(PaymentDto paymentDto);
}

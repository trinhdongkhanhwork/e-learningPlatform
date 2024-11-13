package edu.cfd.e_learningPlatform.mapstruct;

import edu.cfd.e_learningPlatform.dto.WithdrawDto;
import edu.cfd.e_learningPlatform.entity.Withdraw;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WithdrawMapper {

    @Mapping(source = "user.id", target = "userId")
    WithdrawDto toDto(Withdraw withdraw);

    @Mapping(source = "userId", target = "user.id")
    Withdraw toEntity(WithdrawDto withdrawDto);
}
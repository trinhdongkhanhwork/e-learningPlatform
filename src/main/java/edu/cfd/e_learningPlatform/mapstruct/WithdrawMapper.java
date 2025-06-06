package edu.cfd.e_learningPlatform.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import edu.cfd.e_learningPlatform.dto.WithdrawDto;
import edu.cfd.e_learningPlatform.entity.Withdraw;

@Mapper(componentModel = "spring")
public interface WithdrawMapper {
    WithdrawMapper INSTANCE = Mappers.getMapper(WithdrawMapper.class);

    @Mapping(source = "user.id", target = "userId")
    WithdrawDto toDto(Withdraw withdraw);

    @Mapping(source = "userId", target = "user.id")
    Withdraw toEntity(WithdrawDto withdrawDto);
}

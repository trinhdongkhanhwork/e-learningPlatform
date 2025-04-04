package edu.cfd.e_learningPlatform.mapstruct;

import edu.cfd.e_learningPlatform.dto.response.WalletResponse;
import edu.cfd.e_learningPlatform.entity.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    @Mapping(source = "id", target = "walletId")
    @Mapping(source = "user.id", target = "userId", qualifiedByName = "mapUserId")
    @Mapping(source = "user.fullname", target = "fullname")
    WalletResponse toWalletResponse(Wallet wallet);

    @Named("mapUserId")
    default String mapUserId(Object userId) {
        return userId != null ? userId.toString() : null;
    }

    default WalletResponse toWalletResponse(Wallet wallet, BigDecimal pendingEarnings) {
        WalletResponse response = toWalletResponse(wallet);
        response.setBalance(pendingEarnings); // Hiển thị số dư giả lập
        return response;
    }
}
package dev.usenkonastia.backend_lab2.service.mapper;


import dev.usenkonastia.backend_lab2.domain.Category;
import dev.usenkonastia.backend_lab2.domain.UserDetails;
import dev.usenkonastia.backend_lab2.dto.category.CategoryEntryDto;
import dev.usenkonastia.backend_lab2.dto.category.CategoryListDto;
import dev.usenkonastia.backend_lab2.dto.user.UserEntryDto;
import dev.usenkonastia.backend_lab2.dto.user.UserListDto;
import dev.usenkonastia.backend_lab2.dto.user.request.RegisterRequestDto;
import dev.usenkonastia.backend_lab2.entity.CategoryEntity;
import dev.usenkonastia.backend_lab2.entity.UserEntity;
import dev.usenkonastia.backend_lab2.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    UserDetails toUser(UserEntity userEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "username", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    UserDetails toUser(RegisterRequestDto registerRequestDto);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    UserEntity toUserEntity(UserDetails user);

    @Mapping(source = "name", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    RegisterRequestDto toRegisterUserDto(UserDetails userDetails);

    default UserListDto toUserListDto(List<UserDetails> userDetails) {
        return UserListDto.builder().users(toUserEntry(userDetails)).build();
    }

    List<UserEntryDto> toUserEntry(List<UserDetails> users);

    default List<UserDetails> toUserList(Iterator<UserEntity> userServiceEntityIterator) {
        List<UserDetails> result = new ArrayList<>();
        userServiceEntityIterator.forEachRemaining(
                (cosmoCatEntity) -> {
                    result.add(toUser(cosmoCatEntity));
                });
        return result;
    }
}

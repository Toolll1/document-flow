package ru.rosatom.documentflow.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.rosatom.documentflow.dto.*;
import ru.rosatom.documentflow.mappers.UserMapper;
import ru.rosatom.documentflow.models.*;
import ru.rosatom.documentflow.services.DocAttributeService;
import ru.rosatom.documentflow.services.DocTypeService;
import ru.rosatom.documentflow.services.UserOrganizationService;
import ru.rosatom.documentflow.services.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v2/org")
@AllArgsConstructor
@Tag(name = "Организации")
public class OrgController {

    UserOrganizationService userOrganizationService;
    DocTypeService docTypeService;
    DocAttributeService docAttributeService;
    UserService userService;
    ModelMapper modelMapper;
    UserMapper userMapper;

    @Operation(summary = "Получить все организации")
    @GetMapping
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER') || hasAuthority('COMPANY_ADMIN')")
    public Page<OrgDto> getAllOrgs(@ParameterObject @PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return userOrganizationService.getAllOrganizations(pageable)
                .map(o -> modelMapper.map(o, OrgDto.class));
    }

    @Operation(summary = "Добавить организацию")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ADMIN')")
    public OrgDto createOrg(@Valid @RequestBody OrgCreateRequestDto orgCreateRequestDto) {
        OrgCreationRequest orgCreationRequest =
                modelMapper.map(orgCreateRequestDto, OrgCreationRequest.class);
        UserOrganization organization = userOrganizationService.createOrganization(orgCreationRequest);
        return modelMapper.map(organization, OrgDto.class);
    }

    @Operation(summary = "Получить организацию по Id")
    @GetMapping("/{orgId}")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER') || hasAuthority('COMPANY_ADMIN')")
    public OrgDto getOrg(@PathVariable @Parameter(description = "ID организации") Long orgId) {
        UserOrganization organization = userOrganizationService.getOrganization(orgId);
        return modelMapper.map(organization, OrgDto.class);
    }

    @Operation(summary = "Получить всех пользователей по организации")
    @GetMapping("/{orgId}/users")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserReplyDto> getUsersByOrganization(@PathVariable @Parameter(description = "Id организации") Long orgId) {
        List<User> users = userService.findAllByOrganizationId(orgId);
        return users.stream().map(userMapper::objectToReplyDto).collect(Collectors.toList());
    }

    @Operation(summary = "Получить всех пользователей по своей организации")
    @GetMapping("/my/users")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ADMIN') ||hasAuthority('USER') || hasAuthority('COMPANY_ADMIN')")
    public List<UserReplyDto> getUsersMyOrganization(@AuthenticationPrincipal @Parameter(name = "user", hidden = true) User user) {
        List<User> users = userService.findAllByOrganizationId(user.getOrganization().getId());
        return users.stream().map(userMapper::objectToReplyDto).collect(Collectors.toList());
    }

    @Operation(summary = "Получить все типы по организации")
    @GetMapping("/{orgId}/doctypes")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<DocTypeDto> getDocTypesByOrganization(@PathVariable @Parameter(description = "Id организации") Long orgId) {
        List<DocType> docTypes = docTypeService.findAllByOrganizationId(orgId);
        return docTypes.stream()
                .map(o -> modelMapper.map(o, DocTypeDto.class))
                .collect(Collectors.toList());
    }
    @Operation(summary = "Получить все типы по своей организации")
    @GetMapping("/my/doctypes")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ADMIN') ||hasAuthority('USER') || hasAuthority('COMPANY_ADMIN')")
    public List<DocTypeDto> getDocTypesMyOrganization(@AuthenticationPrincipal @Parameter(name = "user", hidden = true) User user) {
        List<DocType> docTypes = docTypeService.findAllByOrganizationId(user.getOrganization().getId());
        return docTypes.stream()
                .map(o -> modelMapper.map(o, DocTypeDto.class))
                .collect(Collectors.toList());
    }
    @Operation(summary = "Получить все атрибуты по организации")
    @GetMapping("/{orgId}/docattributes")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<DocAttributeDto> getAttributesByOrganization(@PathVariable @Parameter(description = "Id организации") Long orgId) {
        List<DocAttribute> docAttributes = docAttributeService.findAllByOrganizationId(orgId);
        return docAttributes.stream()
                .map(o -> modelMapper.map(o, DocAttributeDto.class))
                .collect(Collectors.toList());
    }
    @Operation(summary = "Получить все атрибуты по своей организации")
    @GetMapping("/my/docattributes")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ADMIN') ||hasAuthority('USER') || hasAuthority('COMPANY_ADMIN')")
    public List<DocAttributeDto> getAttributesMyOrganization(@AuthenticationPrincipal @Parameter(name = "user", hidden = true) User user) {
        List<DocAttribute> docAttributes = docAttributeService.findAllByOrganizationId(user.getOrganization().getId());
        return docAttributes.stream()
                .map(o -> modelMapper.map(o, DocAttributeDto.class))
                .collect(Collectors.toList());
    }


    @Operation(summary = "Поиск организации по подстроке в имени")
    @GetMapping("/name/{name}")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER') || hasAuthority('COMPANY_ADMIN')")
    public List<OrgDto> getOrgsByNameLike(
            @PathVariable @Parameter(description = "Подстрока в имени") String name) {
        List<UserOrganization> organizations = userOrganizationService.getOrganizationsByNameLike(name);
        return organizations.stream()
                .map(o -> modelMapper.map(o, OrgDto.class))
                .collect(Collectors.toList());
    }

    @Operation(summary = "Изменить организацию.", description = "При запросе от ADMIN по указанной компании, для остальных ролей обновиться своя компания.")
    @RequestMapping(value = "/{orgId}", method = RequestMethod.PATCH)
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("(#orgId == #user.organization.id && hasAuthority('COMPANY_ADMIN')) || hasAuthority('ADMIN')")
    public OrgDto updateOrg(
            @PathVariable @Parameter(description = "ID организации") Long orgId,
            @Valid @RequestBody OrgUpdateRequestDto orgUpdateRequestDto,
            @AuthenticationPrincipal @Parameter(description = "Пользователь", hidden = true) User user) {
        OrgUpdateRequest orgUpdateRequest =
                modelMapper.map(orgUpdateRequestDto, OrgUpdateRequest.class);
        UserOrganization organization =
                userOrganizationService.updateOrganization(orgId, orgUpdateRequest, user);
        return modelMapper.map(organization, OrgDto.class);
    }

    @Operation(summary = "Удалить организацию")
    @DeleteMapping("/{orgId}")
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasAuthority('ADMIN')")
    public OrgDto deleteOrg(@PathVariable @Parameter(description = "ID организации") Long orgId) {
        UserOrganization organization = userOrganizationService.deleteOrganization(orgId);
        return modelMapper.map(organization, OrgDto.class);
    }
}

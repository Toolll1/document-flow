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
import org.springframework.web.bind.annotation.*;
import ru.rosatom.documentflow.dto.OrgCreateRequestDto;
import ru.rosatom.documentflow.dto.OrgDto;
import ru.rosatom.documentflow.dto.OrgUpdateRequestDto;
import ru.rosatom.documentflow.models.OrgCreationRequest;
import ru.rosatom.documentflow.models.OrgUpdateRequest;
import ru.rosatom.documentflow.models.UserOrganization;
import ru.rosatom.documentflow.services.UserOrganizationService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/org")
@AllArgsConstructor
//@PreAuthorize("hasAuthority('ADMIN')")
@Tag(name = "Организации")
public class OrgController {

    UserOrganizationService userOrganizationService;
    ModelMapper modelMapper;

    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER') || hasAuthority('ADMINCOMPANY')")
    @Operation(summary = "Получить все организации")
    @GetMapping
    @SecurityRequirement(name = "JWT")
    public Page<OrgDto> getAllOrgs(@ParameterObject @PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return userOrganizationService.getAllOrganizations(pageable)
                .map(o -> modelMapper.map(o, OrgDto.class));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Добавить организацию")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "JWT")
    public OrgDto createOrg(@Valid @RequestBody OrgCreateRequestDto orgCreateRequestDto) {
        OrgCreationRequest orgCreationRequest =
                modelMapper.map(orgCreateRequestDto, OrgCreationRequest.class);
        UserOrganization organization = userOrganizationService.createOrganization(orgCreationRequest);
        return modelMapper.map(organization, OrgDto.class);
    }


    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER') || hasAuthority('ADMINCOMPANY')")
    @Operation(summary = "Получить организацию по Id")
    @GetMapping("/{orgId}")
    @SecurityRequirement(name = "JWT")
    public OrgDto getOrg(@PathVariable @Parameter(description = "ID организации") Long orgId) {
        UserOrganization organization = userOrganizationService.getOrganization(orgId);
        return modelMapper.map(organization, OrgDto.class);
    }

    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER') || hasAuthority('ADMINCOMPANY')")
    @Operation(summary = "Поиск организации по подстроке в имени")
    @GetMapping("/name/{name}")
    @SecurityRequirement(name = "JWT")
    public List<OrgDto> getOrgsByNameLike(
            @PathVariable @Parameter(description = "Подстрока в имени") String name) {
        List<UserOrganization> organizations = userOrganizationService.getOrganizationsByNameLike(name);
        return organizations.stream()
                .map(o -> modelMapper.map(o, OrgDto.class))
                .collect(Collectors.toList());
    }

    @PreAuthorize("(orgId == authentication.principal.organization.id && hasAuthority('ADMINCOMPANY')) || hasAuthority('ADMIN')")
    @Operation(summary = "Изменить организацию")
    @RequestMapping(value = "/{orgId}", method = RequestMethod.PATCH)
    @SecurityRequirement(name = "JWT")
    public OrgDto updateOrg(
            @PathVariable @Parameter(description = "ID организации") Long orgId,
            @Valid @RequestBody OrgUpdateRequestDto orgUpdateRequestDto) {
        OrgUpdateRequest orgUpdateRequest =
                modelMapper.map(orgUpdateRequestDto, OrgUpdateRequest.class);
        UserOrganization organization =
                userOrganizationService.updateOrganization(orgId, orgUpdateRequest);
        return modelMapper.map(organization, OrgDto.class);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Удалить организацию")
    @DeleteMapping("/{orgId}")
    @SecurityRequirement(name = "JWT")
    public OrgDto deleteOrg(@PathVariable @Parameter(description = "ID организации") Long orgId) {
        UserOrganization organization = userOrganizationService.deleteOrganization(orgId);
        return modelMapper.map(organization, OrgDto.class);
    }
}

package ru.rosatom.documentflow.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
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
@PreAuthorize("hasAuthority('ADMIN')")
@Tag(name = "Организации", description = "Управляет организациями")
public class OrgController {

    UserOrganizationService userOrganizationService;
    ModelMapper modelMapper;

    @GetMapping
    public List<OrgDto> getAllOrgs() {
        List<UserOrganization> organizations = userOrganizationService.getAllOrganizations();
        return organizations.stream()
                .map(o -> modelMapper.map(o, OrgDto.class))
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrgDto createOrg(@Valid @RequestBody OrgCreateRequestDto orgCreateRequestDto) {
        OrgCreationRequest orgCreationRequest =
                modelMapper.map(orgCreateRequestDto, OrgCreationRequest.class);
        UserOrganization organization = userOrganizationService.createOrganization(orgCreationRequest);
        return modelMapper.map(organization, OrgDto.class);
    }

    @GetMapping("/{orgId}")
    public OrgDto getOrg(@PathVariable Long orgId) {
        UserOrganization organization = userOrganizationService.getOrganization(orgId);
        return modelMapper.map(organization, OrgDto.class);
    }

    @GetMapping("/name/{name}")
    public List<OrgDto> getOrgsByNameLike(@PathVariable String name) {
        List<UserOrganization> organizations = userOrganizationService.getOrganizationsByNameLike(name);
        return organizations.stream()
                .map(o -> modelMapper.map(o, OrgDto.class))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/{orgId}", method = RequestMethod.PATCH)
    public OrgDto updateOrg(
            @PathVariable Long orgId, @Valid @RequestBody OrgUpdateRequestDto orgUpdateRequestDto) {
        OrgUpdateRequest orgUpdateRequest =
                modelMapper.map(orgUpdateRequestDto, OrgUpdateRequest.class);
        UserOrganization organization =
                userOrganizationService.updateOrganization(orgId, orgUpdateRequest);
        return modelMapper.map(organization, OrgDto.class);
    }

    @DeleteMapping("/{orgId}")
    public OrgDto deleteOrg(@PathVariable Long orgId) {
        UserOrganization organization = userOrganizationService.deleteOrganization(orgId);
        return modelMapper.map(organization, OrgDto.class);
    }
}

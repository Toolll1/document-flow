package ru.rosatom.documentflow.controllers;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.rosatom.documentflow.dto.OrgCreateRequestDto;
import ru.rosatom.documentflow.dto.OrgDto;
import ru.rosatom.documentflow.models.OrgCreationRequest;
import ru.rosatom.documentflow.models.UserOrganization;
import ru.rosatom.documentflow.services.UserOrganizationService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/org")
@AllArgsConstructor
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
    public OrgDto createOrg(@Valid @RequestBody OrgCreateRequestDto orgCreateRequestDto) {
        OrgCreationRequest orgCreationRequest = modelMapper.map(orgCreateRequestDto, OrgCreationRequest.class);
        UserOrganization organization = userOrganizationService.createOrganization(orgCreationRequest);
        return modelMapper.map(organization, OrgDto.class);
    }

    @GetMapping("/{orgId}")
    @ResponseStatus(HttpStatus.CREATED)
    public OrgDto getOrg(@PathVariable Long orgId) {
        UserOrganization organization = userOrganizationService.getOrganization(orgId);
        return modelMapper.map(organization, OrgDto.class);
    }
}

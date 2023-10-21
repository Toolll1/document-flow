package ru.rosatom.documentflow.controllers;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rosatom.documentflow.dto.OrgDto;
import ru.rosatom.documentflow.models.UserOrganization;
import ru.rosatom.documentflow.services.UserOrganizationService;

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
}

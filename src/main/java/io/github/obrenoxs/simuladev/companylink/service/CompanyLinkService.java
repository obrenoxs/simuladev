package io.github.obrenoxs.simuladev.companylink.service;

import io.github.obrenoxs.simuladev.companylink.dto.request.CompanyLinkRequest;
import io.github.obrenoxs.simuladev.companylink.dto.response.CompanyLinkResponse;
import io.github.obrenoxs.simuladev.companylink.entity.CompanyLink;
import io.github.obrenoxs.simuladev.companylink.mapper.CompanyLinkMapper;
import io.github.obrenoxs.simuladev.companylink.repository.CompanyLinkRepository;
import io.github.obrenoxs.simuladev.companytype.entity.CompanyType;
import io.github.obrenoxs.simuladev.companytype.repository.CompanyTypeRepository;
import io.github.obrenoxs.simuladev.shared.exception.ResourceNotFoundException;
import io.github.obrenoxs.simuladev.user.entity.User;
import io.github.obrenoxs.simuladev.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CompanyLinkService {

    private final CompanyLinkRepository companyLinkRepository;
    private final UserRepository userRepository;
    private final CompanyTypeRepository companyTypeRepository;
    private final CompanyLinkMapper companyLinkMapper;

    public CompanyLinkService(CompanyLinkRepository companyLinkRepository, UserRepository userRepository, CompanyTypeRepository companyTypeRepository, CompanyLinkMapper companyLinkMapper) {
        this.companyLinkRepository = companyLinkRepository;
        this.userRepository = userRepository;
        this.companyTypeRepository = companyTypeRepository;
        this.companyLinkMapper = companyLinkMapper;
    }

    @Transactional
    public CompanyLinkResponse create(CompanyLinkRequest request, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        CompanyType companyType = companyTypeRepository.findById(request.companyTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de empresa não encontrado"));

        CompanyLink companyLink = companyLinkMapper.toEntity(request, user, companyType);

        companyLink = companyLinkRepository.save(companyLink);

        return companyLinkMapper.toResponse(companyLink);
    }

    @Transactional(readOnly = true)
    public List<CompanyLinkResponse> findAllByUser(UUID userId) {
        List<CompanyLink> list = companyLinkRepository.findAllByUserId(userId);
        List<CompanyLinkResponse> listDTO = list.stream().map(companyLinkMapper::toResponse).toList();
        return listDTO;
    }

    @Transactional(readOnly = true)
    public CompanyLinkResponse findById(UUID id, UUID userId) {
        CompanyLink companyLink = companyLinkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vínculo não encontrado"));

        if (!companyLink.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Vínculo não encontrado");
        }

        return companyLinkMapper.toResponse(companyLink);
    }
}

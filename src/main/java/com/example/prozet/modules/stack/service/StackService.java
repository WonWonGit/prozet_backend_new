package com.example.prozet.modules.stack.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.prozet.common.CustomException;
import com.example.prozet.common.ErrorCode;
import com.example.prozet.enum_pakage.FileType;
import com.example.prozet.enum_pakage.StackType;
import com.example.prozet.modules.file.domain.dto.response.FileMasterDTO;
import com.example.prozet.modules.file.service.FileService;
import com.example.prozet.modules.project.repository.ProjectRepository;
import com.example.prozet.modules.stack.domain.dto.request.StackReqDTO;
import com.example.prozet.modules.stack.domain.dto.response.StackFindResDTO;
import com.example.prozet.modules.stack.domain.dto.response.StackResDTO;
import com.example.prozet.modules.stack.domain.dto.response.StackUnmappedResDTO;
import com.example.prozet.modules.stack.domain.entity.StackCategoryEntity;
import com.example.prozet.modules.stack.domain.entity.StackEntity;
import com.example.prozet.modules.stack.repository.StackCategoryRepository;
import com.example.prozet.modules.stack.repository.StackRepository;

@Service
@Transactional(readOnly = true)
public class StackService {

    @Autowired
    private StackRepository stackRepository;

    @Autowired
    private StackCategoryRepository stackCategoryRepository;

    @Autowired
    private FileService fileService;

    @Transactional
    public StackUnmappedResDTO saveStack(StackReqDTO stackReqDTO, MultipartFile iconImg) {

        StackEntity stackEntity = null;

        StackCategoryEntity stackCategoryEntity = stackCategoryRepository.findByIdx(stackReqDTO.getStackCategoryIdx())
                .orElseThrow(() -> new CustomException(ErrorCode.STACK_CATEGORY_NOT_EXIST));

        if (iconImg != null) {

            FileMasterDTO fileMasterDTO = fileService.fileSave(FileType.STACK_ICON, iconImg);
            String iconUrl = fileMasterDTO.getFileList().get(0).getUrl();
            stackEntity = StackEntity.builder()
                    .name(stackReqDTO.getName())
                    .icon(iconUrl)
                    .stackCategory(stackCategoryEntity)
                    .stackType(StackType.CUSTOMSTACK)
                    .build();

        } else {
            stackEntity = StackEntity.builder()
                    .name(stackReqDTO.getName())
                    .icon(stackReqDTO.getIconUrl())
                    .stackCategory(stackCategoryEntity)
                    .stackType(StackType.CUSTOMSTACK)
                    .build();

        }

        StackEntity stackEntityPS = stackRepository.save(stackEntity);

        if (stackEntityPS != null) {
            return stackEntityPS.toStackUnmappedResDTO();
        }
        return null;

    }

    @Transactional
    public void deleteStackService(StackResDTO stackResDTO) {
        stackRepository.delete(stackResDTO.toEntity());
    }

    public StackResDTO findByIdx(Long idx) {

        Optional<StackEntity> stackEntity = stackRepository.findByIdx(idx);

        if (stackEntity.isPresent()) {
            return stackEntity.get().toStackResDTO();
        }

        return null;
    }

    public Map<String, List<StackFindResDTO>> getStackList(String projectKey) {

        List<StackFindResDTO> stackList = stackRepository.getStackList(projectKey);

        if (stackList.isEmpty()) {
            return null;
        }

        Map<String, List<StackFindResDTO>> stackGroup = stackList.stream().collect(
                Collectors.groupingBy(StackFindResDTO::getCategory));

        return stackGroup;
    }

}

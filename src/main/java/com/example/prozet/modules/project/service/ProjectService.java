package com.example.prozet.modules.project.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.prozet.common.CustomException;
import com.example.prozet.common.ErrorCode;
import com.example.prozet.enum_pakage.FileType;
import com.example.prozet.modules.file.domain.dto.response.FileMasterDTO;
import com.example.prozet.modules.file.service.FileService;
import com.example.prozet.modules.member.domain.entity.MemberEntity;
import com.example.prozet.modules.project.domain.dto.response.ProjectListDTO;
import com.example.prozet.modules.project.domain.dto.response.ProjectResDTO;
import com.example.prozet.modules.project.domain.entity.ProjectEntity;
import com.example.prozet.modules.project.repository.ProjectRepository;
import com.example.prozet.modules.project.utils.ProjectUtil;
import com.example.prozet.modules.projectInformation.domain.dto.request.ProjectInfoReqDTO;
import com.example.prozet.modules.projectInformation.domain.dto.response.ProjectInfoResDTO;
import com.example.prozet.modules.projectInformation.domain.entity.ProjectInfoEntity;
import com.example.prozet.modules.projectInformation.repository.ProjectInfoRepository;
import com.example.prozet.utils.UtilsClass;

@Service
@Transactional(readOnly = true)
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    public ProjectResDTO findByProjectKey(String projectKey) {

        Optional<ProjectEntity> projectEntity = projectRepository.findByProjectKeyAndDeleteYn(projectKey, "N");
        if (projectEntity.isPresent()) {
            return projectEntity.get().toProjectResDTO();
        } else {
            return null;
        }

    }

    @Transactional
    public ProjectResDTO createProject(ProjectInfoResDTO projectInfoResDTO) {

        ProjectEntity projectEntity = ProjectEntity.builder()
                .projectKey(ProjectUtil.createProjectKey())
                .projectInformation(projectInfoResDTO.toEntity())
                .deleteYn("N")
                .build();

        ProjectEntity projectEntityPS = projectRepository.save(projectEntity);

        if (projectEntityPS == null) {
            return null;
        }

        return projectEntityPS.toProjectResDTO();
    }

    // @Transactional
    // public ProjectResDTO __createProject(
    // MemberEntity memberEntity,
    // ProjectInfoReqDTO projectInfoReqDTO,
    // MultipartFile projectImg) {

    // ProjectInfoEntity projectInfoEntity = projectInfoReqDTO.toEntity();

    // if (projectImg != null) {
    // FileMasterDTO fileMasterDTO = fileService.fileSave(FileType.PROJECT_MAIN,
    // projectImg);
    // projectInfoEntity.saveFileMasterEntity(fileMasterDTO.toEntity());
    // }

    // ProjectInfoEntity projectInfoEntityPS =
    // projectInfoRepository.save(projectInfoEntity);

    // ProjectEntity projectEntity = ProjectEntity.builder()
    // // .owner(memberEntity)
    // .projectKey(ProjectUtil.createProjectKey())
    // .deleteYn("N")
    // .build();

    // projectEntity.saveProjectInfoEntity(projectInfoEntityPS);

    // ProjectEntity projectEntityPS = projectRepository.save(projectEntity);

    // if (projectInfoEntityPS == null) {
    // return null;
    // }

    // return
    // projectEntityPS.toProjectResDTO(projectInfoEntityPS.toProjectInfoResDTO());

    // }

    @Transactional
    public void deleteProject(String projectKey, String username) {
        ProjectEntity projectEntity = projectRepository.findByProjectKeyAndDeleteYn(projectKey, "N")
                .orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_EXIST));

        // if (!projectEntity.getOwner().getUsername().equals(username)) {
        // throw new CustomException(ErrorCode.PROJECT_OWNER_ONLY);
        // }

        projectEntity.setDeleteYn("Y");
        projectEntity.setDeleteDate(LocalDateTime.now());

    }

    public List<ProjectListDTO> getProjectList(String username) {

        List<ProjectListDTO> projectList = projectRepository.getProjectList(username);

        return projectList;

    }

}

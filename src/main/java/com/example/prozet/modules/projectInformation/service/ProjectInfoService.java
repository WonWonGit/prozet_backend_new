package com.example.prozet.modules.projectInformation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.prozet.common.CustomException;
import com.example.prozet.common.ErrorCode;
import com.example.prozet.enum_pakage.AccessType;
import com.example.prozet.enum_pakage.FileType;
import com.example.prozet.modules.file.domain.dto.response.FileMasterDTO;
import com.example.prozet.modules.file.service.FileService;
import com.example.prozet.modules.project.domain.dto.response.ProjectResDTO;
import com.example.prozet.modules.project.domain.entity.ProjectEntity;
import com.example.prozet.modules.project.repository.ProjectRepository;
import com.example.prozet.modules.projectInformation.domain.dto.request.ProjectInfoReqDTO;
import com.example.prozet.modules.projectInformation.domain.dto.request.ProjectInfoUpdateReqDTO;
import com.example.prozet.modules.projectInformation.domain.dto.response.ProjectInfoResDTO;
import com.example.prozet.modules.projectInformation.domain.entity.ProjectInfoEntity;
import com.example.prozet.modules.projectInformation.repository.ProjectInfoRepository;
import com.example.prozet.modules.projectMember.domain.dto.response.ProjectMemberListResDTO;
import com.example.prozet.modules.projectMember.repository.ProjectMemberRepository;

@Service
@Transactional(readOnly = true)
public class ProjectInfoService {

    @Autowired
    private ProjectInfoRepository projectInfoRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private FileService fileService;

    @Transactional
    public ProjectInfoResDTO saveProjectInfo(ProjectResDTO projectResDTO, ProjectInfoReqDTO projectInfoReqDTO,
            MultipartFile projectImg) {

        ProjectInfoEntity projectInfoEntity = projectInfoReqDTO.toEntity();

        if (projectImg != null) {
            FileMasterDTO fileMasterDTO = fileService.fileSave(FileType.PROJECT_MAIN, projectImg);
            projectInfoEntity.saveFileMasterEntity(fileMasterDTO.toEntity());
        }

        ProjectInfoEntity projectInfoEntityPS = projectInfoRepository.save(projectInfoEntity);

        return projectInfoEntityPS.toProjectInfoResDTO();

    }

    @Transactional
    public ProjectInfoResDTO updateProjectInfo(String projectKey, String username,
            ProjectInfoUpdateReqDTO projectInfoUpdateReqDTO, MultipartFile projectImg) {

        boolean projectMember = false;

        ProjectEntity projectEntity = projectRepository.findByProjectKeyAndDeleteYn(projectKey, "N")
                .orElseThrow(() -> new CustomException(ErrorCode.FIND_PROJECT_INFO_FAIL));

        List<ProjectMemberListResDTO> projectMemberList = projectMemberRepository.getEditProjectMemberList(projectKey);

        if (projectMemberList != null) {
            projectMember = projectMemberList.stream().anyMatch(
                    member -> member.getUsername().equals(username));
        }

        // if (!projectEntity.getOwner().getUsername().equals(username) ||
        // projectMember) {
        // return null;
        // }

        ProjectInfoEntity projectInfoEntity = projectEntity.getProjectInformation();

        projectInfoEntity.updateProjectInfo(projectInfoUpdateReqDTO);

        if (projectImg != null) {

            // old file delete -> update to new file
            if (projectInfoEntity.getFileMasterEntity() != null) {

                fileService.fileDelete(projectInfoEntity.getFileMasterEntity().getFileList().get(0).getIdx());

                FileMasterDTO fileMasterDTO = fileService.fileUpdate(FileType.PROJECT_MAIN, projectImg,
                        projectInfoEntity.getFileMasterEntity().getIdx());

                projectInfoEntity.saveFileMasterEntity(fileMasterDTO.toEntity());
                return projectInfoEntity.toProjectInfoResDTO();
            } else {

                FileMasterDTO fileMasterDTO = fileService.fileSave(FileType.PROJECT_MAIN, projectImg);
                projectInfoEntity.saveFileMasterEntity(fileMasterDTO.toEntity());
                return projectInfoEntity.toProjectInfoResDTO();
            }

        } else {

            if (projectInfoUpdateReqDTO.getFileIdx() != 0) {
                fileService.fileDelete(projectInfoUpdateReqDTO.getFileIdx());
                FileMasterDTO fileMasterDTO = fileService
                        .getFileMasterDTO(projectInfoEntity.getFileMasterEntity().getIdx());
                projectInfoEntity.saveFileMasterEntity(fileMasterDTO.toEntity());
                return projectInfoEntity.toProjectInfoResDTO();
            }

        }

        return projectInfoEntity.toProjectInfoResDTO();

    }
}

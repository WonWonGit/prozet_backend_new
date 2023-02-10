package com.example.prozet.module.projectInformation.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.example.prozet.enum_pakage.FileType;
import com.example.prozet.modules.file.domain.dto.response.FileMasterDTO;
import com.example.prozet.modules.file.domain.entity.FileEntity;
import com.example.prozet.modules.file.domain.entity.FileMasterEntity;
import com.example.prozet.modules.file.service.FileService;
import com.example.prozet.modules.member.domain.entity.MemberEntity;
import com.example.prozet.modules.project.domain.entity.ProjectEntity;
import com.example.prozet.modules.project.repository.ProjectRepository;
import com.example.prozet.modules.projectInformation.domain.dto.request.ProjectInfoReqDTO;
import com.example.prozet.modules.projectInformation.domain.dto.request.ProjectInfoUpdateReqDTO;
import com.example.prozet.modules.projectInformation.domain.dto.response.ProjectInfoResDTO;
import com.example.prozet.modules.projectInformation.domain.entity.ProjectInfoEntity;
import com.example.prozet.modules.projectInformation.repository.ProjectInfoRepository;
import com.example.prozet.modules.projectInformation.service.ProjectInfoService;
import com.example.prozet.modules.projectMember.repository.ProjectMemberRepository;

@ExtendWith(MockitoExtension.class)
public class ProjectInfoServiceTest {

        @InjectMocks
        ProjectInfoService projectInfoService;

        @Mock
        FileService fileService;

        @Mock
        ProjectRepository projectRepository;

        @Mock
        ProjectInfoRepository projectInfoRepository;

        @Mock
        ProjectMemberRepository projectMemberRepository;

        String path = "src/test/resources/rocketlauncher.png";
        String fileName = "rocketlauncher";
        String contentType = "png";

        String newPath = "src/test/resources/spaceship.png";
        String newFileName = "spaceship";

        @Test
        public void saveProjectInfoTest() {

                ProjectInfoReqDTO projectInfoReqDTO = ProjectInfoReqDTO.builder()
                                .title("title")
                                .content("content")
                                .startDate(LocalDateTime.now())
                                .endDate(LocalDateTime.now())
                                .build();

                ProjectInfoEntity projectInfoEntity = ProjectInfoEntity.builder()
                                .idx(1L)
                                .title("title")
                                .content("content")
                                .startDate(LocalDateTime.now())
                                .endDate(LocalDateTime.now())
                                .build();

                when(projectInfoRepository.save(any())).thenReturn(projectInfoEntity);

                ProjectInfoResDTO projectInfoResDTO = projectInfoService
                                .saveProjectInfo(getProjectEntity().toProjectResDTO(), projectInfoReqDTO, null);

                assertThat(projectInfoResDTO.getTitle()).isEqualTo("title");
        }

        /**
         * save -> no file
         * update -> no file, only information
         */
        @Test
        public void updateProjectInfoWithoutFileTest() {

                ProjectInfoUpdateReqDTO projectInfoUpdateReqDTO = ProjectInfoUpdateReqDTO.builder().title("New title")
                                .content("New content").startDate(LocalDateTime.now())
                                .endDate(LocalDateTime.now()).build();

                ProjectInfoEntity projectInfoEntity = getProjectInfoWithoutFileEntity();

                ProjectEntity projectEntity = getProjectEntity();
                projectEntity.setProjectInformation(projectInfoEntity);

                when(projectRepository.findByProjectKeyAndDeleteYn(any(), any()))
                                .thenReturn(Optional.of(projectEntity));
                when(projectMemberRepository.getEditProjectMemberList(any())).thenReturn(null);

                ProjectInfoResDTO projectInfoResDTO = projectInfoService.updateProjectInfo("projectKey", "username",
                                projectInfoUpdateReqDTO, null);

                assertThat(projectInfoResDTO.getTitle()).isEqualTo("New title");

        }

        /**
         * save -> no file
         * update -> save new file & update information
         * 
         */
        @Test
        public void updateProjectInfoWithFileTest() throws IOException {

                ProjectInfoUpdateReqDTO projectInfoUpdateReqDTO = ProjectInfoUpdateReqDTO.builder().title("New title")
                                .content("New content").startDate(LocalDateTime.now())
                                .endDate(LocalDateTime.now()).build();

                ProjectInfoEntity projectInfoEntity = getProjectInfoWithoutFileEntity();

                ProjectEntity projectEntity = getProjectEntity();
                projectEntity.setProjectInformation(projectInfoEntity);

                FileMasterEntity fileMasterEntity = getFileMasterEntity();
                fileMasterEntity.setFileList(List.of(getFileEntity()));
                FileMasterDTO fileMasterDTO = fileMasterEntity.toFileMasterDTO();

                when(projectRepository.findByProjectKeyAndDeleteYn(any(), any()))
                                .thenReturn(Optional.of(projectEntity));
                when(fileService.fileSave(any(), any())).thenReturn(fileMasterDTO);

                ProjectInfoResDTO projectInfoResDTO = projectInfoService.updateProjectInfo("projectKey", "username",
                                projectInfoUpdateReqDTO, getFile());

                assertThat(projectInfoResDTO.getFileMaster()).isNotNull();

        }

        /**
         * save -> file
         * update -> old file delete & save new file & update information
         * 
         * @throws IOException
         * 
         */
        @Test
        public void updateProjectInfoWithNewFileTest() throws IOException {
                ProjectInfoUpdateReqDTO projectInfoUpdateReqDTO = ProjectInfoUpdateReqDTO.builder().title("New title")
                                .content("New content").startDate(LocalDateTime.now())
                                .endDate(LocalDateTime.now()).fileIdx(1).build();

                ProjectInfoEntity projectInfoEntity = getProjectInfoWithFileEntity();

                ProjectEntity projectEntity = getProjectEntity();
                projectEntity.setProjectInformation(projectInfoEntity);

                FileMasterEntity fileMasterEntity = getFileMasterEntity();
                fileMasterEntity.setFileList(List.of(getNewFileEntity()));
                FileMasterDTO fileMasterDTO = fileMasterEntity.toFileMasterDTO();

                when(projectRepository.findByProjectKeyAndDeleteYn(any(), any()))
                                .thenReturn(Optional.of(projectEntity));
                when(fileService.fileUpdate(any(), any(), anyInt())).thenReturn(fileMasterDTO);

                ProjectInfoResDTO projectInfoResDTO = projectInfoService.updateProjectInfo("projectKey", "username",
                                projectInfoUpdateReqDTO, getNewFile());

                assertThat(projectInfoResDTO.getFileMaster().getFileList().get(0).getOriginalName())
                                .isEqualTo(newFileName);
        }

        private MemberEntity getMemberEntity() {
                return MemberEntity.builder().name("name").email("test@gmail").username("username").build();
        }

        private ProjectInfoEntity getProjectInfoWithoutFileEntity() {

                ProjectEntity projectEntity = getProjectEntity();

                ProjectInfoEntity projectInfoEntity = ProjectInfoEntity.builder().title("title")
                                .content("content")
                                .startDate(LocalDateTime.now())
                                .fileMasterEntity(null)
                                .endDate(LocalDateTime.now()).build();

                return projectInfoEntity;
        }

        private ProjectEntity getProjectEntity() {
                ProjectEntity projectEntity = ProjectEntity.builder()
                                .projectKey("projectKey")
                                .deleteYn("N").build();

                return projectEntity;
        }

        private ProjectInfoEntity getProjectInfoWithFileEntity() {

                FileMasterEntity fileMasterEntity = FileMasterEntity.builder().idx(1)
                                .category(FileType.PROJECT_MAIN.fileType())
                                .build();

                FileEntity fileEntity = FileEntity.builder().idx(1).deleteYn("N").extension("png").fileKey("fileKey")
                                .fileMaster(fileMasterEntity).build();

                fileMasterEntity.setFileList(List.of(fileEntity));

                ProjectInfoEntity projectInfoEntity = ProjectInfoEntity.builder().title("title")
                                .content("content")
                                .fileMasterEntity(fileMasterEntity)
                                .startDate(LocalDateTime.now())
                                .endDate(LocalDateTime.now()).build();

                return projectInfoEntity;

        }

        private FileMasterEntity getFileMasterEntity() {
                return FileMasterEntity.builder().idx(1)
                                .category(FileType.PROJECT_MAIN.fileType())
                                .build();
        }

        private FileEntity getFileEntity() {
                return FileEntity.builder().deleteYn("N")
                                .fileMaster(getFileMasterEntity())
                                .extension(contentType)
                                .originalName(fileName)
                                .idx(1)
                                .url("urltest")
                                .build();
        }

        private FileEntity getNewFileEntity() {
                return FileEntity.builder().deleteYn("N")
                                .fileMaster(getFileMasterEntity())
                                .extension(contentType)
                                .originalName("spaceship")
                                .idx(2)
                                .url("urltest2")
                                .build();
        }

        private MultipartFile getFile() throws IOException {

                FileInputStream fileInputStream = new FileInputStream(new File(path));
                MultipartFile file = new MockMultipartFile(fileName, fileName + "." + contentType, contentType,
                                fileInputStream);

                return file;
        }

        private MultipartFile getNewFile() throws IOException {

                FileInputStream fileInputStream = new FileInputStream(new File("src/test/resources/spaceship.png"));
                MultipartFile file = new MockMultipartFile(newFileName, newFileName + "." + contentType, contentType,
                                fileInputStream);

                return file;
        }

}

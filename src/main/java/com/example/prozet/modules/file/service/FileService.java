package com.example.prozet.modules.file.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.prozet.common.CustomException;
import com.example.prozet.common.ErrorCode;
import com.example.prozet.enum_pakage.FileType;
import com.example.prozet.modules.file.domain.dto.response.FileMasterDTO;
import com.example.prozet.modules.file.domain.entity.FileEntity;
import com.example.prozet.modules.file.domain.entity.FileMasterEntity;
import com.example.prozet.modules.file.repository.FileMasterRepository;
import com.example.prozet.modules.file.repository.FileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileService {

    @Autowired
    private FileMasterRepository fileMasterRespository;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private FileRepository fileRepository;

    // 단일 파일
    @Transactional
    public FileMasterDTO fileSave(FileType category, MultipartFile file) {

        FileMasterEntity fileMasterEntity = FileMasterEntity.builder().category(category.fileType()).build();
        FileMasterEntity fileMasterPS = fileMasterRespository.save(fileMasterEntity);

        String fileUrl = getFileUrl(file, category);

        FileEntity fileEntity = FileEntity.builder().extension(s3Service.getExtension(file.getOriginalFilename()))
                .originalName(file.getOriginalFilename())
                .fileMaster(fileMasterEntity)
                .size(file.getSize())
                .url(fileUrl)
                .deleteYn("N")
                .build();

        FileEntity fileEntityPS = fileRepository.save(fileEntity);

        List<FileEntity> fileEntityList = List.of(fileEntityPS);

        fileMasterPS.setFileList(fileEntityList);

        return fileMasterPS.toFileMasterDTO();

    }

    // 단일파일 업데이트
    @Transactional
    public FileMasterDTO fileUpdate(FileType category, MultipartFile file, int fileMasterIdx) {

        FileMasterEntity fileMasterEntity = fileMasterRespository.findByIdx(fileMasterIdx)
                .orElseThrow(() -> new CustomException(ErrorCode.FILE_MASTER_NOT_EXIST));

        String fileUrl = getFileUrl(file, category);

        String fileKey = fileUrl.substring(fileUrl.lastIndexOf("/") + 1, fileUrl.length());

        FileEntity fileEntity = FileEntity.builder().extension(s3Service.getExtension(file.getOriginalFilename()))
                .originalName(file.getOriginalFilename())
                .fileMaster(fileMasterEntity)
                .size(file.getSize())
                .url(fileUrl)
                .fileKey(category.bucketName() + fileKey)
                .deleteYn("N")
                .build();

        FileEntity fileEntityPS = fileRepository.save(fileEntity);

        List<FileEntity> fileEntityList = List.of(fileEntityPS);

        fileMasterEntity.setFileList(fileEntityList);

        return fileMasterEntity.toFileMasterDTO();
    }

    @Transactional
    public void fileDelete(int fileIdx) {

        FileEntity fileEntity = fileRepository.findByIdx(fileIdx)
                .orElseThrow(() -> new CustomException(ErrorCode.FILE_NOT_EXIST));

        s3Service.deleteS3File(fileEntity.getFileKey());
        fileEntity.setDeleteYn("Y");

    }

    public String getFileUrl(MultipartFile file, FileType category) {

        try {

            return s3Service.uploadS3File(file, category);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public FileMasterDTO getFileMasterDTO(int idx) {
        FileMasterEntity fileMasterEntity = fileMasterRespository.findByIdx(idx)
                .orElseThrow(() -> new CustomException(ErrorCode.FILE_MASTER_NOT_EXIST));
        return fileMasterEntity.toFileMasterDTO();
    }

}

package com.example.prozet.modules.member.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.prozet.common.CustomException;
import com.example.prozet.common.ErrorCode;
import com.example.prozet.enum_pakage.FileType;
import com.example.prozet.modules.file.domain.dto.response.FileMasterDTO;
import com.example.prozet.modules.file.domain.entity.FileMasterEntity;
import com.example.prozet.modules.file.repository.FileMasterRepository;
import com.example.prozet.modules.file.repository.FileRepository;
import com.example.prozet.modules.file.service.FileService;
import com.example.prozet.modules.member.domain.dto.request.MemberInfoSaveReqDTO;
import com.example.prozet.modules.member.domain.dto.request.MemberInfoUpdateDTO;
import com.example.prozet.modules.member.domain.dto.request.MemberRequireDTO;
import com.example.prozet.modules.member.domain.dto.response.MemberInfoResDTO;
import com.example.prozet.modules.member.domain.entity.MemberEntity;
import com.example.prozet.modules.member.domain.entity.MemberInfoEntity;
import com.example.prozet.modules.member.repository.MemberInfoRepository;
import com.example.prozet.modules.member.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
public class MemberInfoService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberInfoRepository memberInfoRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileMasterRepository fileMasterRepository;

    @Transactional
    public MemberInfoResDTO saveMemberInfo(MemberInfoSaveReqDTO memberInfo, String username, MultipartFile imgProfile) {

        // TODO: member service로 옮기기
        MemberEntity memberEntityPS = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));
        memberEntityPS.updateMember(memberInfo.getEmail(), memberInfo.getName());

        MemberInfoEntity memberInfoEntity = memberInfo.toEntity();

        if (imgProfile != null) {

            FileMasterDTO saveFileMasterDTO = fileService.fileSave(FileType.MEMBER_PROFILE, imgProfile);
            memberInfoEntity.saveJoinEntity(saveFileMasterDTO.toEntity(), memberEntityPS);

        } else {

            memberInfoEntity.saveJoinEntity(null, memberEntityPS);

        }

        // memberInfoSave
        MemberInfoEntity memberInfoEntityPS = memberInfoRepository.save(memberInfoEntity);

        return memberInfoEntityPS.toMemberInfoResDto();

    }

    @Transactional
    public MemberInfoResDTO memberInfoUpdate(MemberRequireDTO memberRequireInfo, MemberInfoUpdateDTO memberInfoDTO,
            String username, MultipartFile imgFile) {

        MemberEntity memberEntity = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));
        memberEntity.updateMember(memberRequireInfo.getName(), memberRequireInfo.getName());

        MemberInfoEntity memberInfo = memberInfoRepository.findByMemberEntity_Username(username)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_INFO_NOT_EXIST));
        memberInfo.updateMemberInfo(memberInfoDTO);

        if (imgFile != null) {
            // File Master is not exist
            if (memberInfo.getFileMasterEntity() == null) {

                FileMasterDTO fileMasterDto = fileService.fileSave(FileType.MEMBER_PROFILE, imgFile);
                memberInfo.saveJoinEntity(fileMasterDto.toEntity(), memberEntity);

            } else {

                // File Master is Exist
                fileService.fileDelete(memberInfo.getFileMasterEntity().getFileList().get(0).getIdx());
                FileMasterDTO fileMasterDto = fileService.fileUpdate(FileType.MEMBER_PROFILE, imgFile,
                        memberInfo.getFileMasterEntity().getIdx());
                memberInfo.saveJoinEntity(fileMasterDto.toEntity(), memberEntity);

            }

        } else {

            // File Master is Exist & profile delete
            if (memberInfo.getFileMasterEntity() != null) {

                if (memberInfoDTO.getFileIdx() != 0) {

                    fileService.fileDelete(memberInfoDTO.getFileIdx());
                    FileMasterEntity fileMasterEntity = fileMasterRepository
                            .findByIdx(memberInfo.getFileMasterEntity().getIdx())
                            .orElseThrow(() -> new CustomException(ErrorCode.FILE_MASTER_NOT_EXIST));
                    memberInfo.saveJoinEntity(fileMasterEntity, memberEntity);

                }

            }
        }

        return memberInfo.toMemberInfoResDto();

    }

    public MemberInfoResDTO getMemberInfo(String username) {
        Optional<MemberInfoEntity> memberInfo = memberInfoRepository.findByMemberEntity_Username(username);

        if (memberInfo.isEmpty()) {
            return null;
        }

        return memberInfo.get().toMemberInfoResDto();

    }

}

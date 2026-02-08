package com.cfs.Drive_BE.Servicee;


import com.cfs.Drive_BE.Entity.FileEntity;
import com.cfs.Drive_BE.Repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileServiceStorage {

    //Save file paths
    @Value("${file.upload-dir}")
    private String uploaddir;

    @Autowired
    private FileRepository fileRepository;

    public String saveFile(MultipartFile file , Long parentFolderID) throws Exception{
        Path path = Paths.get(uploaddir);
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            String filename = file.getOriginalFilename();
            Path path1 = path.resolve(filename);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            FileEntity fileEntity = new FileEntity();
            fileEntity.setParentfolderid(parentFolderID);
            fileEntity.setType("file");
            fileEntity.setSize(fileEntity.getSize());
            fileEntity.setName(filename);
            fileEntity.setPath(path1.toString());
            fileEntity.setCreatedAt(LocalDateTime.now());

        }
        catch (Exception e){
            return e.getMessage();
        }
        return "file Uploaded Sucessfully";

    }

    public List<FileEntity> getFilesInFolder(Long parentFolderId){
        if (parentFolderId == null){
            return fileRepository.findAll().stream()
                    .filter(f->parentFolderId == null)
                    .collect(Collectors.toList());
        }

        else {
            return fileRepository.findAll().stream()
                    .filter(f->parentFolderId.equals(f.getParentfolderid()))
                    .collect(Collectors.toList());
        }
    }

    public FileEntity getFileById(Long id){
        return fileRepository.findById(id).orElseThrow(()->new RuntimeException("FIle not Found"));
    }

    public void deleteByID(Long id){
        fileRepository.deleteById(id);
    }
}

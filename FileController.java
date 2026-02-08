package com.cfs.Drive_BE.Controller;


import com.cfs.Drive_BE.Entity.FileEntity;
import com.cfs.Drive_BE.Servicee.FileServiceStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/files")
@CrossOrigin
public class FileController {
    @Autowired
    private FileServiceStorage fileServiceStorage;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadfile(@RequestParam("file")MultipartFile file ,
                                             @RequestParam(value = "parentFolderId" , required = false) Long parentFolderId){
        try{
            String response = fileServiceStorage.saveFile(file , parentFolderId);
            return ResponseEntity.ok(response);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File Uploading fail!");
        }
    }

    @GetMapping("/download/{id}")
    public  ResponseEntity<Resource> downloadfile(@PathVariable Long id){
        try {
            FileEntity fileentity = fileServiceStorage.getFileById(id);
            Path path = Paths.get(fileentity.getPath());
            Resource resource = new UrlResource(path.toUri());
            return ResponseEntity.ok()
                    .header("content-Disposition" , "attachment; filename=\""+fileentity.getName()+"\"")
                    .body(resource);
        }
        catch (Exception e){
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<FileEntity>> listfiles(
            @RequestParam(value = "parentFolderId" , required = false)
            Long parentFolderId)
    {
        List<FileEntity>file;
        if (parentFolderId ==null){
            file = fileServiceStorage.getFilesInFolder(null);
        }
        else {
            file = fileServiceStorage.getFilesInFolder(parentFolderId);
        }
        return ResponseEntity.ok(file);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletefile (@PathVariable Long id){
        try {
            FileEntity fileEntity = new FileEntity();
            Path path = Paths.get(fileEntity.getPath());
            Files.deleteIfExists(path);
            fileServiceStorage.deleteByID(id);
            return ResponseEntity.ok("File Deleted Sucessfully");
        }
        catch (Exception e){
            return ResponseEntity.status(500).body("Failed TO Delete file");
        }
    }

}

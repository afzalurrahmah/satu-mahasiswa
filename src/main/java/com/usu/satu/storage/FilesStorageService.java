package com.usu.satu.storage;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.stream.Stream;

@Service
public class FilesStorageService {

    @Value("${server.local.backend}")
    private String localBackend;

    private String filepath;
    private Path rootLocation = Path.of("files");

    public String saveFile(MultipartFile file, String filepath, String fileName, String type) {
        setFilepath(filepath);
        this.init();
        String fi = save(file, fileName, type);
        return fi;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public void init() {
        try {
            Files.createDirectories(Path.of(rootLocation+this.filepath));
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    public String save(MultipartFile file, String fileName, String type) {
        try {
            String setName     = getFileName(file, fileName, type);

            Path folder = Path.of(this.rootLocation+this.filepath);
            Files.copy(file.getInputStream(), folder.resolve(setName), StandardCopyOption.REPLACE_EXISTING);

            return localBackend+"/"+rootLocation+filepath+"/"+setName;
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public Resource load(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1).filter(path -> !path.equals(this.rootLocation)).map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

    public String getFileName(MultipartFile file, String fileName, String type){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        String setName;
        if(type.equalsIgnoreCase("data")){
            setName     = fileName+"."+ext;
        }else {
            setName     = fileName+"_"+timestamp.getTime()+"."+ext;
        }
        return setName;
    }
}
package com.cfs.Drive_BE.Repository;

import com.cfs.Drive_BE.Entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileEntity , Long> {
}

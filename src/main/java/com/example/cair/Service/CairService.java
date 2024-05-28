package com.example.cair.Service;

import com.example.cair.Entity.Cair;
import com.example.cair.Entity.FileUpload;
import com.example.cair.Entity.Project;
import com.example.cair.Repo.CairRepo;
import com.example.cair.Repo.CairRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class CairService {

    @Autowired
    private CairRepo repo;

    @Autowired
    private CairRepository repo1;

    public void saveOrUpdate(Cair cair) {
        String email = cair.getEmail();
        // Optional<Cair> existingCair = repo.findByEmail1(email);
        Optional<Cair> existingCair = Optional.ofNullable(repo.findByEmail(email));

        if (existingCair.isPresent()) {
            // Update existing Cair entity
            Cair existingEntity = existingCair.get();

            existingEntity.setName(cair.getName());
            existingEntity.setPassword(cair.getPassword());
            existingEntity.setConfirmPassword(cair.getConfirmPassword());
            // existingEntity.setProjects(cair.getProjects());

            // Append new projects to the existing list of projects
            List<Project> existingProjects = existingEntity.getProjects();
            if (existingProjects == null) {
                existingProjects = new ArrayList<>(); // Create new list if it's null
            }
            if (cair.getProjects() != null) {

                /*
                 * anything enclosed within these should be kept as a group. Each such group
                 * indicates an atomic part
                 */
                // existingProjects.addAll(cair.getProjects());

                /*
                 * for (Project project : cair.getProjects()) {
                 * if (!existingProjects.contains(project)) {
                 * existingProjects.add(project);
                 * }
                 * }
                 */
                existingProjects.add(cair.getProjects().get(0));
            }
            existingEntity.setProjects(existingProjects);
            repo.save(existingEntity);
        } else {
            // Save new Cair entity
            repo.save(cair);
        }
    }

    public Iterable<Cair> listAllCairs() {
        return repo.findAll();
    }

    public void deleteProject(String cairId, int projectIndex) {
        Optional<Cair> optionalCair = repo.findById(cairId);
        if (optionalCair.isPresent()) {
            Cair cair = optionalCair.get();
            List<Project> projects = cair.getProjects();

            if (projectIndex >= 0 && projectIndex < projects.size()) {
                projects.remove(projectIndex);
                repo.save(cair);
            } else {
                // Handle invalid project index
                throw new IllegalArgumentException("Invalid project index");
            }
        }
    }

    public Cair getCairById(String cairId) {
        Optional<Cair> optionalCair = repo.findById(cairId);
        return optionalCair.orElse(null);
    }

    public boolean updateProject(String cairId, int projectIndex, Project updatedProject) {
        // Retrieve Cair from the repository
        Optional<Cair> cairOptional = repo.findById(cairId);
        if (cairOptional.isPresent()) {
            Cair cair = cairOptional.get();
            List<Project> projects = cair.getProjects();
            // Check if projectIndex is within bounds
            if (projects != null && projectIndex >= 0 && projectIndex < projects.size()) {
                // Update the project details
                projects.set(projectIndex, updatedProject);
                cair.setProjects(projects);
                // Save the updated Cair object
                repo.save(cair);
                return true;
            }
        }
        return false;
    }

    public Project getProjectByIndex(String cairId, int projectIndex) {
        Optional<Cair> cairOptional = repo.findById(cairId);
        if (cairOptional.isPresent()) {
            Cair cair = cairOptional.get();
            List<Project> projects = cair.getProjects();
            if (projects != null && projectIndex >= 0 && projectIndex < projects.size()) {
                return projects.get(projectIndex);
            }
        }
        return null;
    }

    public void storeFiles(String cairId, int projectIndex, String typeOfUpload, MultipartFile[] files)
            throws IOException {
        System.out.println("Storing files for Cair ID: " + cairId + ", Project Index: " + projectIndex
                + ", Type of Upload: " + typeOfUpload);
        Cair cair = repo.findById(cairId).orElseThrow(() -> new RuntimeException("Cair not found with id: " + cairId));
        System.out.println("Retrieved Cair: " + cair);

        Project project = cair.getProjects().get(projectIndex);
        System.out.println("Retrieved Project: " + project);

        if (project.getFile() == null) {
            project.setFile(new ArrayList<>()); // Initialize file array if null
        }

        List<FileUpload> fileUploads = new ArrayList<>();
        // for (MultipartFile file : files) {
        // String fileName = file.getOriginalFilename();

        // FileUpload fileUpload = new FileUpload();
        // fileUpload.setFileName(fileName);
        // fileUpload.setFileType(file.getContentType());
        // fileUpload.setData(file.getBytes());
        // fileUpload.setTypeOfUpload(typeOfUpload); // Set the type of upload

        // fileUploads.add(fileUpload);
        // }

        for (MultipartFile file : files) {
            String originalFileName = file.getOriginalFilename();

            // Check the type of upload and prepend to the filename accordingly
            String fileName;
            if ("report".equals(typeOfUpload)) {
                fileName = "REPORT_" + originalFileName;
            } else if ("researchPaper".equals(typeOfUpload)) {
                fileName = "PAPER_" + originalFileName;
            } else if ("references".equals(typeOfUpload)) {
                fileName = "REFS_" + originalFileName;
            } else if ("otherFiles".equals(typeOfUpload)) {
                fileName = "MISC_" + originalFileName;
            } else {
                fileName = originalFileName;
            }

            System.out.println("Storing file: " + fileName);

            FileUpload fileUpload = new FileUpload();
            fileUpload.setFileName(fileName);
            fileUpload.setFileType(file.getContentType());
            fileUpload.setData(file.getBytes());
            fileUpload.setTypeOfUpload(typeOfUpload); // Set the type of upload

            fileUploads.add(fileUpload);
        }

        project.getFile().addAll(fileUploads);
        repo.save(cair);
    }

    public Optional<FileUpload> getFileByCairIdAndProjectIndex(String cairId, int projectIndex, String fileName) {
        Optional<Cair> cairOptional = repo.findById(cairId);

        if (cairOptional.isPresent()) {
            Cair cair = cairOptional.get();
            if (projectIndex >= 0 && projectIndex < cair.getProjects().size()) {
                Project project = cair.getProjects().get(projectIndex);
                return project.getFile().stream()
                        .filter(file -> file.getFileName().equals(fileName))
                        .findFirst();
            }
        }
        return Optional.empty();
    }

    public boolean deleteFile(String cairId, int projectIndex, String fileName) {
        Cair cair = repo.findById(cairId).orElse(null);
        if (cair == null) {
            return false;
        }

        Project project = cair.getProjects().get(projectIndex);
        if (project == null) {
            return false;
        }

        List<FileUpload> files = project.getFile();
        for (FileUpload file : files) {
            if (file.getFileName().equals(fileName)) {
                files.remove(file);
                repo.save(cair);
                return true;
            }
        }
        return false;
    }

    public List<Project> getProjectsByYear(String year) {
        return repo.findAll().stream()
                .flatMap(cair -> cair.getProjects().stream())
                .filter(project -> project.getYear().equals(year))
                .collect(Collectors.toList());
    }

    public Optional<Cair> getCairByEmail(String email) {
        return repo1.findByEmail(email);
    }

    public List<Project> getProjectsByEmail(String email) {
        Optional<Cair> cairOptional = repo1.findByEmail(email);
        return cairOptional.map(Cair::getProjects).orElse(null);
    }

    public String getFaculty(String email) {
        Optional<Cair> cairOptional = repo1.findByEmail(email);
        return cairOptional.map(Cair::getName).orElse(null);
    }

}

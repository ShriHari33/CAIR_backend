
package com.example.cair.Controller;

import com.example.cair.Entity.Cair;
import com.example.cair.Entity.FileUpload;
import com.example.cair.Entity.Project;
import com.example.cair.Service.CairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*") // for frontend connection
@RequestMapping("api/v1/cair")
public class CairController {

    @Autowired
    private CairService cairServices;

    @PostMapping(value = "/save")
    private String saveProject(@RequestBody Cair cair) {
        cairServices.saveOrUpdate(cair);
        return cair.get_id();
    }

    @GetMapping(value = "/getAll")
    private Iterable<Cair> getStudents() {
        return cairServices.listAllCairs();
    }

    // @GetMapping(value = "getAll/{id}")
    // private Cair getStudent(@PathVariable(name = "id") String _id) {
    // return cairServices.getCairById(_id);
    // }

    @PutMapping(value = "/edit/{id}")
    private Cair update(@RequestBody Cair cair, @PathVariable(name = "id") String _id) {
        cair.set_id(_id);
        cairServices.saveOrUpdate(cair);
        return cair;
    }

    @DeleteMapping("/{cairId}/projects/{projectIndex}")
    public ResponseEntity<String> deleteProject(@PathVariable String cairId, @PathVariable int projectIndex) {
        try {
            cairServices.deleteProject(cairId, projectIndex);
            return ResponseEntity.ok("Project deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @RequestMapping("/cair/{id}")
    private Cair getCair(@PathVariable(name = "id") String cairid) {
        return cairServices.getCairById(cairid);
    }

    @GetMapping("/{cairId}/projects/{projectIndex}")
    public ResponseEntity<?> getProjectByIndex(@PathVariable String cairId, @PathVariable int projectIndex) {
        try {
            // Fetch the project details based on cairId and projectIndex
            Project project = cairServices.getProjectByIndex(cairId, projectIndex);
            if (project != null) {
                return ResponseEntity.ok(project);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @PutMapping("/{cairId}/projects/{projectIndex}")
    public ResponseEntity<String> updateProject(@PathVariable String cairId,
            @PathVariable int projectIndex,
            @RequestBody Project updatedProject) {
        boolean success = cairServices.updateProject(cairId, projectIndex, updatedProject);
        if (success) {
            return ResponseEntity.ok("Project updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update project");
        }
    }

    @PostMapping(value = "/{cairId}/projects/{projectIndex}/upload")
    public ResponseEntity<?> uploadFilesForProject(
            @PathVariable String cairId,
            @PathVariable int projectIndex,
            @RequestParam("files") MultipartFile[] files) {
        try {
            cairServices.storeFiles(cairId, projectIndex, files);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload files for project.");
        }
    }

    @GetMapping("/{cairId}/projects/{projectIndex}/file/{fileName}/download")
    public ResponseEntity<ByteArrayResource> downloadFile(
            @PathVariable String cairId,
            @PathVariable int projectIndex,
            @PathVariable String fileName) {

        Optional<FileUpload> fileOptional = cairServices.getFileByCairIdAndProjectIndex(cairId, projectIndex, fileName);

        if (fileOptional.isPresent()) {
            FileUpload file = fileOptional.get();
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(file.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                    .body(new ByteArrayResource(file.getData()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{cairId}/projects/{projectIndex}/file/{fileName}")
    public ResponseEntity<ByteArrayResource> getFile(
            @PathVariable String cairId,
            @PathVariable int projectIndex,
            @PathVariable String fileName) {

        Optional<FileUpload> fileOptional = cairServices.getFileByCairIdAndProjectIndex(cairId, projectIndex, fileName);

        if (fileOptional.isPresent()) {
            FileUpload file = fileOptional.get();
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(file.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFileName() + "\"")
                    .body(new ByteArrayResource(file.getData()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{cairId}/projects/{projectIndex}/file/{fileName}")
    public ResponseEntity<?> deleteFile(
            @PathVariable String cairId,
            @PathVariable int projectIndex,
            @PathVariable String fileName) {

        boolean deleted = cairServices.deleteFile(cairId, projectIndex, fileName);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/projects")
    public List<Project> getProjectsByYear(@RequestParam String year) {
        return cairServices.getProjectsByYear(year);
    }

    @GetMapping("/{email}")
    public ResponseEntity<List<Project>> getProjectsByEmail(@PathVariable String email) {
        List<Project> projects = cairServices.getProjectsByEmail(email);
        if (projects != null && !projects.isEmpty()) {
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/details/{email}")
    public ResponseEntity<Cair> getCairByEmail(@PathVariable String email) {
        Optional<Cair> cair = cairServices.getCairByEmail(email);
        return cair.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/faculty/{email}")
    public String getFaculty(@PathVariable String email) {
        return cairServices.getFaculty(email);
    }
}
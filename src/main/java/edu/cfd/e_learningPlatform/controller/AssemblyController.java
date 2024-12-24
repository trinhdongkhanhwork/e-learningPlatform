package edu.cfd.e_learningPlatform.controller;

import edu.cfd.e_learningPlatform.dto.request.AssemblyRequest;
import edu.cfd.e_learningPlatform.dto.request.AssemblyUserRequest;
import edu.cfd.e_learningPlatform.dto.response.AssemblyResponse;
import edu.cfd.e_learningPlatform.dto.response.AssemblyUserResponse;
import edu.cfd.e_learningPlatform.service.AssemblyService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.poi.hslf.record.CString;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/assembly")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AssemblyController {

    AssemblyService assemblyService;

    @GetMapping("user/{id}")
    public ResponseEntity<List<AssemblyResponse>> getAssemblyUser(@PathVariable String id) {
        return ResponseEntity.ok(assemblyService.getAssemblyUserJoin(id));
    }

    @PostMapping("/join")
    public ResponseEntity<AssemblyUserResponse> joinAssembly(@RequestBody AssemblyUserRequest assemblyUserRequest) {
        return ResponseEntity.ok(assemblyService.joinAssembly(assemblyUserRequest));
    }

    @DeleteMapping("/out")
    public ResponseEntity<String> outAssembly(@RequestBody AssemblyUserRequest assemblyUserRequest) {
        return ResponseEntity.ok(assemblyService.outAssembly(assemblyUserRequest));
    }

    @PostMapping
    public ResponseEntity<AssemblyResponse> addAssembly(@RequestBody AssemblyRequest assemblyRequest) {
        return ResponseEntity.ok(assemblyService.addAssembly(assemblyRequest));
    }

    @DeleteMapping("/{idAssembly}")
    public ResponseEntity<String> deleteAssembly(@PathVariable Long idAssembly) {
        return ResponseEntity.ok(assemblyService.deleteAssembly(idAssembly));
    }

    @PutMapping
    public ResponseEntity<AssemblyResponse> updateAssembly(@RequestBody AssemblyRequest assemblyRequest) {
        return ResponseEntity.ok(assemblyService.updateAssembly(assemblyRequest));
    }
}

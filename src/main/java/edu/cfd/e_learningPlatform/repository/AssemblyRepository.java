package edu.cfd.e_learningPlatform.repository;

import edu.cfd.e_learningPlatform.entity.Assembly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AssemblyRepository extends JpaRepository<Assembly, Long> {

    @Query(value = "SELECT EXISTS (SELECT 1 FROM user_assembly WHERE users_id = :idUser AND assembly_id = :idAssembly)",
            nativeQuery = true)
    Integer existsUserJoinGroup(Long idAssembly, String idUser);

    @Query(value = "select a.id, a.image_assembly, a.name_assembly, a.admin, a.active from assembly a " +
            "join user_assembly ua on a.id = ua.assembly_id " +
            "where ua.users_id = :idUser and a.active = true" , nativeQuery = true)
    List<Assembly> getAssemblyUserJoin(String idUser);
}

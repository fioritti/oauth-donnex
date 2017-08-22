package org.donnex.user.repository;

import java.util.List;

import org.donnex.user.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("roleRepository")
public interface RoleRepository extends JpaRepository<Role, Integer>{
	Role findByRole(String role);

	@Query("select a.role from Role a, User b where b.email=?1 and b.id = a.id")
	List<String> findRoleByUserName(String username);

}

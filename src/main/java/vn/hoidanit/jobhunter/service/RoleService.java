package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.PermissionRepository;
import vn.hoidanit.jobhunter.repository.RoleRepository;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public boolean existByName(String name) {
        return this.roleRepository.existsByName(name);
    }

    public boolean existsByNameAndIdNot(String name, long id) {
        return this.roleRepository.existsByNameAndIdNot(name, id);
    }

    public Role handleCreateRole(Role r) {
        if (r.getPermissions() != null) {
            List<Long> ids = r.getPermissions().stream().map(item -> item.getId()).collect(Collectors.toList());

            List<Permission> dbPermissions = this.permissionRepository.findByIdIn(ids);
            r.setPermissions(dbPermissions);
        }

        return this.roleRepository.save(r);
    }

    public Role fetchRoleById(long id) {
        Optional<Role> roleOptional = this.roleRepository.findById(id);
        if (roleOptional.isPresent()) {
            return roleOptional.get();
        }
        return null;
    }

    public Role handleUpdateRole(Role r) {
        Role roleInDB = this.fetchRoleById(r.getId());

        // check permissions
        if (r.getPermissions() != null) {
            List<Long> ids = r.getPermissions().stream().map(item -> item.getId()).collect(Collectors.toList());

            List<Permission> dbPermissions = this.permissionRepository.findByIdIn(ids);
            r.setPermissions(dbPermissions);
        }

        roleInDB.setName(r.getName());
        roleInDB.setDescription(r.getDescription());
        roleInDB.setActive(r.isActive());
        roleInDB.setPermissions(r.getPermissions());

        return this.roleRepository.save(roleInDB);
    }

    public ResultPaginationDTO handleGetAllRoles(Specification<Role> spec, Pageable pageable) {
        Page<Role> rolePage = this.roleRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(rolePage.getTotalPages());
        mt.setTotal(rolePage.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(rolePage.getContent());

        return rs;
    }

    public void handleDeleteRole(long id) {
        this.roleRepository.deleteById(id);
    }
}

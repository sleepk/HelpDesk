package com.schoolofnet.helpdesk.services;

import java.util.List;

import com.schoolofnet.helpdesk.models.Role;

public interface RoleService {

	public List<Role> findAll();

	public Role create(Role role);
	
	public Boolean delete(Long id);

	
	public Role findByName(String name);
}

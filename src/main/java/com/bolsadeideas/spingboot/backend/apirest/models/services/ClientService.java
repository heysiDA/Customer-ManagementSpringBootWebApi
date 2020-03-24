package com.bolsadeideas.spingboot.backend.apirest.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.spingboot.backend.apirest.models.dao.IClientDao;
import com.bolsadeideas.spingboot.backend.apirest.models.entity.Client;

@Service
public class ClientService implements IClientService {
	
	@Autowired
	private IClientDao clientDao;

	@Override
	@Transactional(readOnly = true)
	public List<Client> findAll(){
		// TODO Auto-generated method stub
		return (List<Client>) clientDao.findAll();
	}
	
}

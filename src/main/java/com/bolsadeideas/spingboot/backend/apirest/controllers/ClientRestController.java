package com.bolsadeideas.spingboot.backend.apirest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.spingboot.backend.apirest.models.entity.Client;
import com.bolsadeideas.spingboot.backend.apirest.models.services.IClientService;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class ClientRestController {

	@Autowired
	private IClientService clientService;
	
	@GetMapping("/clients")
	public List<Client> index(){
		return clientService.findAll();		
	}
	
	@GetMapping("/clients/{id}")
	public Client show(@PathVariable Long id){
		return clientService.findById(id);
	}
	
	//use @RequestBody instead @PathVariable because
	//Client come as a json
	@PostMapping("/clients")
	@ResponseStatus(HttpStatus.CREATED)
	public Client create(@RequestBody Client client){
		//client.setCreateAt(new Date());
		return clientService.save(client);
	}
	
	@PutMapping("/clients/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Client update(@RequestBody Client client, @PathVariable Long id){
		
		 Client updatedClient = clientService.findById(id);
		 
		 updatedClient.setName(client.getName());
		 updatedClient.setLastName(client.getLastName());
		 updatedClient.setEmail(client.getEmail());
		 
		 return clientService.save(updatedClient);
	}
	
	@DeleteMapping("/clients/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id){
		 clientService.delete(id);
	}
	
}

package com.bolsadeideas.spingboot.backend.apirest.controllers;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.spingboot.backend.apirest.models.entity.Client;
import com.bolsadeideas.spingboot.backend.apirest.models.services.IClientService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class ClientRestController {

	@Autowired
	private IClientService clientService;

	@GetMapping("/clients")
	public List<Client> index() {
		return clientService.findAll();
	}

	@GetMapping("/clients/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		Client client = null;
		Map<String, Object> response = new HashMap<>();

		try {

			client = clientService.findById(id);

		} catch (DataAccessException e) {
			response.put("message", "Error al realizar la consulta en la base de datos!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}
		if (client == null) {
			response.put("message",
					"El cliente con ID: ".concat(id.toString()).concat(" no existe en la base de datos!"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Client>(client, HttpStatus.OK);
	}

	// use @RequestBody instead @PathVariable because
	// Client come as a json
	@PostMapping("/clients")
	public ResponseEntity<?> create(@Valid @RequestBody Client client,BindingResult result) {
		// client.setCreateAt(new Date());
		Client clientNew = null;
		Map<String, Object> response = new HashMap<>();
		if(result.hasErrors()) {
			/*
			 * List<String> errors=new ArrayList<>(); 
			 * for(FieldError err : result.getFieldErrors()) {
			 * errors.add("El campo '"+err.getField()+"' "+err.getDefaultMessage()); }
			 */
			List<String> errors=result.getFieldErrors()
					.stream()
					.map(err -> "El campo '"+err.getField()+"' "+err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);	
		}
		try {

			clientNew = clientService.save(client);

		} catch (DataAccessException e) {
			response.put("message", "Error al realizar el insert en la base de datos!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}
		response.put("message", "El cliente ha sido insertado con éxito!");
		response.put("client", clientNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping("/clients/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Client client,BindingResult result, @PathVariable Long id) {

		Client clientById = clientService.findById(id);
		Client updatedClient =null;
		Map<String, Object> response = new HashMap<>();
		if(result.hasErrors()) {
			/*
			 * List<String> errors=new ArrayList<>(); 
			 * for(FieldError err : result.getFieldErrors()) {
			 * errors.add("El campo '"+err.getField()+"' "+err.getDefaultMessage()); }
			 */
			List<String> errors=result.getFieldErrors()
					.stream()
					.map(err -> "El campo '"+err.getField()+"' "+err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);	
		}
		
		if(clientById == null) {
			response.put("message",
					"Error: no se pudo editar, el cliente con ID: ".concat(id.toString()).concat(" no existe en la base de datos!"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		try {
			clientById.setName(client.getName());
			clientById.setLastName(client.getLastName());
			clientById.setEmail(client.getEmail());
			clientById.setCreateAt(client.getCreateAt());
			
			updatedClient = clientService.save(clientById);
		}catch (DataAccessException e) {
			response.put("message", "Error al actualizar el cliente en la base de datos!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}
		
		response.put("message", "El cliente ha sido actualizado con éxito!");
		response.put("client", updatedClient);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/clients/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();

		try {
			
			clientService.delete(id);
		
		}catch (DataAccessException e) {
			response.put("message", "Error al eliminar el cliente en la base de datos!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}
		response.put("message", "El cliente ha sido eliminado con éxito!");		
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		
	}

}

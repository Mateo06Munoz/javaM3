package com.krakedev.inventarios1.servicios;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.krakedev.inventarios.bdd.ProveedoresBDD;
import com.krakedev.inventarios.entidades.CabeceraVentas;
import com.krakedev.iventarios.excepciones.krakedevException;

@Path("ventas")
public class serviciosVentas {

	
	@Path("guardar")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response crear(CabeceraVentas cabecera) {
		ProveedoresBDD venta = new ProveedoresBDD();
		
		try {
			venta.insertar(cabecera);
			return Response.ok().build();
		} catch (krakedevException e) {
			e.printStackTrace();	
			return Response.serverError().build();
		}

	}
}

package com.krakedev.inventarios1.servicios;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.krakedev.inventarios.bdd.ProveedoresBDD;
import com.krakedev.inventarios.entidades.Pedido;
import com.krakedev.iventarios.excepciones.krakedevException;

@Path("Pedidos")
public class ServiciosPedidos {
	@Path("registrar")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response crear(Pedido pedido) {
		ProveedoresBDD provBDD = new ProveedoresBDD();
		try {
			provBDD.insertarPedido(pedido);
			return Response.ok().build();
		} catch (krakedevException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}

	}
}

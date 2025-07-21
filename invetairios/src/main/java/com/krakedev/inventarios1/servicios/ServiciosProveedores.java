package com.krakedev.inventarios1.servicios;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.krakedev.inventarios.bdd.ProveedoresBDD;
import com.krakedev.inventarios.entidades.Proveedor;
import com.krakedev.inventarios.entidades.TipoDato;
import com.krakedev.iventarios.excepciones.krakedevException;

@Path("proveedores")
public class ServiciosProveedores {

	@Path("buscar/{sub}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscar(@PathParam("sub") String subCadena) {

		ArrayList<Proveedor> proveedores = null;
		ProveedoresBDD provBDD = new ProveedoresBDD();
		try {
			proveedores = provBDD.buscar(subCadena);
			return Response.ok(proveedores).build();
		} catch (krakedevException e) {

			e.printStackTrace();
			return Response.serverError().build();
		}

	}

	@Path("recuperar")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response recuperar() {

		ArrayList<TipoDato> tipoDocumentois = null;
		ProveedoresBDD provBDD = new ProveedoresBDD();
		try {
			tipoDocumentois = provBDD.recuperaraTipoDeDatos();
			return Response.ok(tipoDocumentois).build();
		} catch (krakedevException e) {

			e.printStackTrace();
			return Response.serverError().build();
		}

	}

	@Path("crear")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response crear(Proveedor proveedor) {
		ProveedoresBDD provBDD = new ProveedoresBDD();
		try {
			provBDD.insertar(proveedor);
			return Response.ok().build();
		} catch (krakedevException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}

	}

}

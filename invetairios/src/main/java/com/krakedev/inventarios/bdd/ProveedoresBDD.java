package com.krakedev.inventarios.bdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.krakedev.inventarios.entidades.Proveedor;
import com.krakedev.inventarios.entidades.TipoDato;
import com.krakedev.inventarios.utils.coneccionBDD;
import com.krakedev.iventarios.excepciones.krakedevException;

public class ProveedoresBDD {
	public ArrayList<Proveedor> buscar(String subcadena) throws krakedevException {
		ArrayList<Proveedor> proveedores = new ArrayList<Proveedor>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Proveedor p = null;
		try {
			con = coneccionBDD.obtenerConection();
			ps = con.prepareStatement("select identificador,tipo_documento,nombre,telefono,correo,direccion from proveedores "
					+ "where upper (nombre) like ?");
			ps.setString(1, "%"+subcadena.toUpperCase()+"%");
			rs = ps.executeQuery();

			while (rs.next()) {
				String identificador=rs.getString("identificador");
				String tipoDocumento=rs.getString("tipo_documento");
				String nombre=rs.getString("nombre");
				String telefono=rs.getString("telefono");
				String correo=rs.getString("correo");
				String direccion=rs.getString("direccion");
				
				
				p=new Proveedor(identificador,tipoDocumento,nombre,telefono,correo,direccion);
				proveedores.add(p);
			}
		} catch (krakedevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new krakedevException("error al consultar.detalle: " + e.getMessage());
		}

		return proveedores;
	}
	public ArrayList<TipoDato> recuperaraTipoDeDatos() throws krakedevException {
		ArrayList<TipoDato> tipoDattos = new ArrayList<TipoDato>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		TipoDato td = null;
		try {
			con = coneccionBDD.obtenerConection();
			ps = con.prepareStatement("select codigo,descripcion from tipo_documento");
			rs = ps.executeQuery();

			while (rs.next()) {
				String codigo=rs.getString("codigo");
				String descripcion=rs.getString("descripcion");

				
				td=new TipoDato(codigo,descripcion);
				tipoDattos.add(td);
			}
		} catch (krakedevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new krakedevException("error al consultar.detalle: " + e.getMessage());
		}

		return tipoDattos;
	}
}

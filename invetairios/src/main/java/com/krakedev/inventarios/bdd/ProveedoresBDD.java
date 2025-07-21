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
			ps = con.prepareStatement(
					"select p.identificador,p.tipo_documento,td.descripcion,p.nombre,p.telefono,p.correo,p.direccion from proveedores p, tipo_documento td  "
							+ "where p.tipo_documento=td.codigo  " + "and upper (nombre) like ?");
			ps.setString(1, "%" + subcadena.toUpperCase() + "%");
			rs = ps.executeQuery();

			while (rs.next()) {
				String identificador = rs.getString("identificador");
				String codigoTipoDocumento = rs.getString("tipo_documento");
				String descripcionTipoDocumento = rs.getString("descripcion");
				String nombre = rs.getString("nombre");
				String telefono = rs.getString("telefono");
				String correo = rs.getString("correo");
				String direccion = rs.getString("direccion");
				TipoDato td = new TipoDato(codigoTipoDocumento, descripcionTipoDocumento);

				p = new Proveedor(identificador, td, nombre, telefono, correo, direccion);
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
				String codigo = rs.getString("codigo");
				String descripcion = rs.getString("descripcion");

				td = new TipoDato(codigo, descripcion);
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

	public void insertar(Proveedor proveedor) throws krakedevException {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = coneccionBDD.obtenerConection();
			ps = con.prepareStatement("insert into proveedores (identificador,tipo_documento,nombre,telefono,correo,direccion)\r\n"
					+ "values (?,?,?,?,?,?)");
			ps.setString(1, proveedor.getIdentificador());
			ps.setString(2, proveedor.getTipoDocumento().getCodigo());
			ps.setString(3, proveedor.getNombre());
			ps.setString(4, proveedor.getTelefono());
			ps.setString(5, proveedor.getCorreo());
			ps.setString(6, proveedor.getDireccion());
			ps.executeUpdate();
		} catch (krakedevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new krakedevException("error al consultar.detalle: " + e.getMessage());
		}

	}
}

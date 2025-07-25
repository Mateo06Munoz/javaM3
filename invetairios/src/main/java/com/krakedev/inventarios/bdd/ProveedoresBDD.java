package com.krakedev.inventarios.bdd;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import com.krakedev.inventarios.entidades.CabeceraVentas;
import com.krakedev.inventarios.entidades.Categoria;
import com.krakedev.inventarios.entidades.DetallePedido;
import com.krakedev.inventarios.entidades.DetalleVentas;
import com.krakedev.inventarios.entidades.Pedido;
import com.krakedev.inventarios.entidades.Producto;
import com.krakedev.inventarios.entidades.Proveedor;
import com.krakedev.inventarios.entidades.TipoDato;
import com.krakedev.inventarios.entidades.UnidadesMedida;
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
	public ArrayList<Producto> buscarProductos(String subcadena) throws krakedevException {
		ArrayList<Producto> productos = new ArrayList<Producto>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Producto p = null;
		try {
			con = coneccionBDD.obtenerConection();
			ps = con.prepareStatement("select p.codigo_cat, p.nombre as nombre_producto, "
					+ "udm.nombre as nombre_udm, udm.descripcion as descripcion_udm, "
					+ "cast(p.precio_venta as decimal(6,2)), p.tiene_iva, "
					+ "cast(p.coste as decimal (5,4)), "
					+ "p.categoria, c.nombre as nombre_categoria, "
					+ "strock "
					+ "from  Productos p, Unidades_de_medida udm, categorias c "
					+ "where p.udm = udm.nombre "
					+ "and p.categoria = c.codigo_cat "
					+ "and upper (p.nombre) like ?");
			ps.setString(1,"%" + subcadena.toUpperCase() + "%" );
			rs = ps.executeQuery();

			while (rs.next()) {
				int codigoP = rs.getInt("codigo_cat");
				String nombreP = rs.getString("nombre_producto");
				String nombreUnidadMedida= rs.getString("nombre_udm");
				String descripcionUnidadMedida= rs.getString("descripcion_udm");
				BigDecimal precioventa =rs.getBigDecimal("precio_venta");
				boolean tieneIVA =rs.getBoolean("tiene_iva");
				BigDecimal costo =rs.getBigDecimal("coste");
				int codigoC = rs.getInt("categoria");
				String nombreC = rs.getString("nombre_categoria");
				int stock = rs.getInt("strock");
				
				UnidadesMedida udm =new UnidadesMedida();
				udm.setNombre(nombreUnidadMedida);
				udm.setDescripcion(descripcionUnidadMedida);
				
				Categoria c=new Categoria();
				c.setNombre(nombreC);
				c.setCodigo(codigoC);
				
				p=	new Producto(codigoP,nombreP,udm,precioventa,tieneIVA,costo,c,stock);

				productos.add(p);
			}
		} catch (krakedevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new krakedevException("error al consultar.detalle: " + e.getMessage());
		}

		return productos;
	}
	
	public void insertarproductos(Producto producto) throws krakedevException {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = coneccionBDD.obtenerConection();
			ps = con.prepareStatement("insert into Productos (nombre,udm,precio_venta,tiene_iva,coste,categoria,strock) "
					+ "values (?,?,?,?,?,?,?);");
			ps.setString(1, producto.getNombre());
			ps.setString(2, producto.getUnidadMedida().getNombre());
			ps.setBigDecimal(3, producto.getPrecioVenta());
			ps.setBoolean(4, producto.isTieneIVA());
			ps.setBigDecimal(5, producto.getCoste());
			ps.setInt(6, producto.getCategoria().getCodigo());
			ps.setInt(7, producto.getStock());
	
			ps.executeUpdate();
		} catch (krakedevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new krakedevException("error al consultar.detalle: " + e.getMessage());
		}

	}
	public void insertarPedido(Pedido pedido) throws krakedevException {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement psdet = null;
		ResultSet cleve =null;
		int codigoCabecera=0;
		
		Date fechaActual = new Date();
		java.sql.Date fechaSQL =new  java.sql.Date (fechaActual.getTime());
		
		try {
			con = coneccionBDD.obtenerConection();
			ps = con.prepareStatement("insert into cabecera_pedido(proveedores,fecha,estado) "
					+ "values (?,?,?);",Statement.RETURN_GENERATED_KEYS);	
			ps.setString(1, pedido.getProveedor().getIdentificador());
			ps.setDate(2, fechaSQL);
			ps.setString(3, "S");
			
			ps.executeUpdate();
			
			cleve=ps.getGeneratedKeys();
			
			if(cleve.next()) {
				codigoCabecera=cleve.getInt(1);
			}
			
			ArrayList<DetallePedido> detallesPedidos=pedido.getDetalles();
			DetallePedido dp=null;
			for(int i=0;i<detallesPedidos.size();i++) {
				dp=detallesPedidos.get(i);
				psdet=con.prepareStatement("insert into detalle_pedido (cabecera_pedido,producto,cantidad_solicitada,subtotal,cantidad_recibida) "
						+ "values (?,?,?,?,?);");
				psdet.setInt(1, codigoCabecera);
				psdet.setInt(2, dp.getProducto().getCodigo());
				psdet.setInt(3, dp.getCantidadSolicitada());
				psdet.setInt(5, 0) ;
				BigDecimal pv=dp.getProducto().getPrecioVenta();
				BigDecimal cantidad=new BigDecimal(dp.getCantidadSolicitada());
				BigDecimal subtotal=pv.multiply(cantidad);
				psdet.setBigDecimal(4, subtotal);
				
				psdet.executeUpdate();
							
			}
			
		} catch (krakedevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new krakedevException("error al consultar.detalle: " + e.getMessage());
		}

	}
	public void resivirPedido(Pedido pedido) throws krakedevException {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement psdet = null;
		ResultSet cleve =null;
		
		Date fechaActual = new Date();
		Timestamp fechaHoraActual=new Timestamp(fechaActual.getTime());
		
		try {
			con = coneccionBDD.obtenerConection();
			ps = con.prepareStatement("update cabecera_pedido  set estado='R'  where numero=?");	
			ps.setInt(1, pedido.getCodigo());

			ps.executeUpdate();
			
			ArrayList<DetallePedido> detallesPedidos=pedido.getDetalles();
			DetallePedido dp=null;
			for(int i=0;i<detallesPedidos.size();i++) {
				dp=detallesPedidos.get(i);
				psdet=con.prepareStatement("update detalle_pedido  "
						+ "set cantidad_recibida=?, subtotal=?  "
						+ "where codigo=?");
				psdet.setInt(1, dp.getCantidadRecivida());
				
				BigDecimal pv=dp.getProducto().getPrecioVenta();
				BigDecimal cantidad=new BigDecimal(dp.getCantidadRecivida());
				BigDecimal subtotal=pv.multiply(cantidad);
				psdet.setBigDecimal(2, subtotal);
				psdet.setInt(3, dp.getCodigo());
				
				psdet.executeUpdate();
				
				psdet=con.prepareStatement("insert into historia_Stock (fecha,referecia,producto,cantidad)"
						+ "values (?,?,?,?);");
				psdet.setTimestamp(1, fechaHoraActual);
				psdet.setString(2, "PEDIDO "+dp.getCodigo());
				psdet.setInt(3, dp.getProducto().getCodigo());
				psdet.setInt(4, dp.getCantidadRecivida());
				
				psdet.executeUpdate();
			}
			
		} catch (krakedevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new krakedevException("error al consultar.detalle: " + e.getMessage());
		}

	}
	
	
	
	
public void insertar(CabeceraVentas cabecera) throws krakedevException{
		
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement psT = null;
		PreparedStatement psA = null;
		PreparedStatement psS = null;
		BigDecimal cero=  new BigDecimal("0");
		BigDecimal totalSinIva=  new BigDecimal("0");
		BigDecimal ivaS=  new BigDecimal("0");
		BigDecimal total=  new BigDecimal("0");
		
		
		Date fechaActual = new Date();
		Timestamp fechaHoraActual = new Timestamp(fechaActual.getTime());
		
		ResultSet rsClave = null;
		int codigoCabecera =0;
		
		
		
		try {
			con = coneccionBDD.obtenerConection();
			ps = con.prepareStatement("insert into caberas_ventas (fecha,total_sin_iva,iva,total) "
					+ "values (?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
			
			ps.setTimestamp(1, fechaHoraActual);
			ps.setBigDecimal(2, cero);
			ps.setBigDecimal(3, cero);
			ps.setBigDecimal(4, cero);
			ps.executeUpdate();
			rsClave = ps.getGeneratedKeys();
			
			if(rsClave.next()) {
				codigoCabecera = rsClave.getInt(1);
			}
			ArrayList<DetalleVentas> ventas = cabecera.getDetalle();
			
			DetalleVentas det= null;
			
			for(int i =0;i<ventas.size();i++) {
				det = ventas.get(i);
				psT = con.prepareStatement("insert into detalle_ventas(cabecera_ventas,producto,cantidad,precio_venta,subtotal,subtotal_con_iva)"
						+ "values (?,?,?,?,?,?)");
				psT.setInt(1, codigoCabecera);
				psT.setInt(2, det.getProducto().getCodigo());
				psT.setInt(3, det.getCantidad());
				psT.setBigDecimal(4, det.getProducto().getPrecioVenta());
				
				BigDecimal precioVenta =det.getProducto().getPrecioVenta();
				BigDecimal cantidad  = new BigDecimal(det.getCantidad());
				BigDecimal subTotal =precioVenta.multiply(cantidad);
				totalSinIva = totalSinIva.add(subTotal);
				psT.setBigDecimal(5, subTotal);
				
				if(det.getProducto().isTieneIVA()) {
					BigDecimal iva  = new BigDecimal(0.15);
					BigDecimal TotalIva =subTotal.multiply(iva);
					BigDecimal subTotalIva = subTotal.add(TotalIva);
					ivaS = ivaS.add(TotalIva);
					total = total.add(subTotalIva);
					psT.setBigDecimal(6, subTotalIva);
				}else {
					psT.setBigDecimal(6, cero);
				}
				psT.executeUpdate();
				
				psS = con.prepareStatement("insert into historia_Stock (fecha,referecia,producto,cantidad) "
						+ "values (?,?,?,?)");
				psS.setTimestamp(1, fechaHoraActual);
				psS.setString(2, "venta " + codigoCabecera);
				psS.setInt(3, det.getProducto().getCodigo());
				psS.setInt(4, (-1)*det.getCantidad());
				psS.executeUpdate();
			}
			
			
			
			psA = con.prepareStatement("update caberas_ventas set total_sin_iva = ?,iva = ?,total=? "
					+ "where codigo	 = ?");
			psA.setBigDecimal(1, totalSinIva);
			psA.setBigDecimal(2, ivaS);
			psA.setBigDecimal(3, total);
			psA.setInt(4, codigoCabecera);
			psA.executeUpdate();
			
		} catch (krakedevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new krakedevException("error al consultar.detalle: " + e.getMessage());
		}
	}
	

}

package util;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JFrame;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class ConexionJasper<E> {
	
	public JDialog ventanaReporte = new JDialog(new JFrame(), "Visualizar Reporte", true);

	public void generarReporte(List<E> lista, Map<String, Object> parametros, String reporte) throws JRException {
		this.ventanaReporte.setSize(1080, 720);
		this.ventanaReporte.setModal(true);
		InputStream stream = ConexionJasper.class.getResourceAsStream("/jasper/"+reporte+".jrxml");
		JasperReport report = JasperCompileManager.compileReport(stream);
		JasperPrint print = JasperFillManager.fillReport(report, parametros, new JRBeanCollectionDataSource(lista));
		// JasperViewer es en sí mismo un JFrame; se lo usa solo para
		// reutilizar su toolbar (imprimir, exportar a PDF, zoom, buscar) y
		// se traslada ese contenido a nuestro JDialog modal, así el reporte
		// queda integrado a la ventana de la aplicación en vez de abrir una
		// ventana suelta aparte. Se oculta y descarta el frame original de
		// inmediato para que no quede una ventana vacía de fondo.
		JasperViewer viewer = new JasperViewer(print, false);
		viewer.setVisible(false);
		this.ventanaReporte.getContentPane().add(viewer.getContentPane());
		viewer.dispose();
	}
	
	
}

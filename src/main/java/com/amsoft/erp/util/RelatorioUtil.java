package com.amsoft.erp.util;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;



public class RelatorioUtil {
		
		
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void imprimeRelatorio(String nomeRelatorio, HashMap parametros, List lista) {
		try {
			
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(lista);
			
			FacesContext facesContext = FacesContext.getCurrentInstance();
			
			facesContext.responseComplete();
			
			ServletContext servletContext = 
					(ServletContext) facesContext.getExternalContext().getContext();
			
			  String path = servletContext.getRealPath("/relatorios/");
			  
	            parametros.put("SUBREPORT_DIR", path + File.separator);
	            JasperPrint jasperPrint = 
	            		JasperFillManager.fillReport(
	                    servletContext.getRealPath("/relatorios/")
	                    + nomeRelatorio + ".jasper", parametros, dataSource);
	            
	            HttpServletResponse res = 
	            		(HttpServletResponse) facesContext.getExternalContext().getResponse();
	            res.setContentType("application/pdf");
	            
	            int codigo = (int) (Math.random() * 1000);
	            
	            res.setHeader("Content-disposition","inline;filename=relatorio_" + codigo + ".pdf");
	            byte[] b = JasperExportManager.exportReportToPdf(jasperPrint);
	            
	            res.getOutputStream().write(b);
	            
	            res.getCharacterEncoding();
	            
	            facesContext.responseComplete();
	            
	        } catch (Exception e){
	            AmsoftUtils.error("Erro ao imprimir relatório: "+e.toString());
	            e.printStackTrace();
	        }
			
	
	}

	
}
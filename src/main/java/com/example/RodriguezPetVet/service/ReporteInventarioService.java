package com.example.RodriguezPetVet.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.RodriguezPetVet.model.Product;
import com.example.RodriguezPetVet.repository.ProductRepository;

@Service
public class ReporteInventarioService {

    @Autowired
    private ProductRepository productRepository;

    public ByteArrayInputStream generarReporteInventario() {
        String[] columnas = {"ID", "Nombre", "Precio", "Stock"};
        List<Product> productos = productRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet hoja = workbook.createSheet("Inventario");

            // Estilo del encabezado
            CellStyle estiloCabecera = workbook.createCellStyle();
            Font fuente = workbook.createFont();
            fuente.setBold(true);
            estiloCabecera.setFont(fuente);
            estiloCabecera.setAlignment(HorizontalAlignment.CENTER);

            // Crear fila de encabezado
            Row filaCabecera = hoja.createRow(0);
            for (int i = 0; i < columnas.length; i++) {
                Cell celda = filaCabecera.createCell(i);
                celda.setCellValue(columnas[i]);
                celda.setCellStyle(estiloCabecera);
            }

            // Llenar datos
            int filaNum = 1;
            for (Product producto : productos) {
                Row fila = hoja.createRow(filaNum++);
                fila.createCell(0).setCellValue(producto.getId());
                fila.createCell(1).setCellValue(producto.getName());
                fila.createCell(2).setCellValue(producto.getPrice());
                fila.createCell(3).setCellValue(producto.getQuantity());
            }

            // Ajustar tamaño automático de columnas
            for (int i = 0; i < columnas.length; i++) {
                hoja.autoSizeColumn(i);
            }

            workbook.write(out);

            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("❌ Error al generar el reporte de inventario", e);
        }
    }
}
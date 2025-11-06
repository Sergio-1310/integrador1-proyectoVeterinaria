package com.example.RodriguezPetVet.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;
import com.example.RodriguezPetVet.model.Product;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

@Service
public class ReporteVentaPdfService {

    public ByteArrayInputStream generarReporteVenta(Product producto, int cantidadVendida) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Encabezado
            Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLUE);
            Paragraph titulo = new Paragraph("Comprobante de Venta - Veterinaria Rodríguez", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);
            document.add(Chunk.NEWLINE);

            // Fecha y hora
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            Paragraph fecha = new Paragraph("Fecha de emisión: " + LocalDateTime.now().format(formatter));
            fecha.setAlignment(Element.ALIGN_RIGHT);
            document.add(fecha);
            document.add(Chunk.NEWLINE);

            // Datos del producto vendido
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new int[] { 3, 2, 1, 2 });

            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            PdfPCell hcell;

            hcell = new PdfPCell(new Phrase("Producto", headerFont));
            table.addCell(hcell);
            hcell = new PdfPCell(new Phrase("Precio Unitario", headerFont));
            table.addCell(hcell);
            hcell = new PdfPCell(new Phrase("Cantidad", headerFont));
            table.addCell(hcell);
            hcell = new PdfPCell(new Phrase("Total (S/)", headerFont));
            table.addCell(hcell);

            // Fila con datos de la venta
            table.addCell(producto.getName());
            table.addCell(String.format("%.2f", producto.getPrice()));
            table.addCell(String.valueOf(cantidadVendida));
            double total = producto.getPrice() * cantidadVendida;
            table.addCell(String.format("%.2f", total));

            document.add(table);

            document.add(Chunk.NEWLINE);
            Paragraph gracias = new Paragraph("¡Gracias por su compra!",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12));
            gracias.setAlignment(Element.ALIGN_CENTER);
            document.add(gracias);

            document.close();

        } catch (DocumentException e) {
            throw new RuntimeException("Error al generar el reporte de venta", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
package com.springboot.app.upload.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.app.upload.util.UploadUtil;

@Component
public class UploadService {

	public List<Map<String, String>> upload(MultipartFile file) throws IOException {

		Path tempDir = Files.createTempDirectory("");

		File tempFile = tempDir.resolve(file.getOriginalFilename()).toFile();

		file.transferTo(tempFile);

		Workbook workbook = WorkbookFactory.create(tempFile);

		Sheet sheet = workbook.getSheetAt(0);
		Supplier<Stream<Row>> rowStreamSupplier = UploadUtil.getRowStreamSupplier(sheet);

		Row headerRow = rowStreamSupplier.get().findFirst().get();
		

		List<String> headerCells = UploadUtil.getStream(headerRow).map(Cell::getStringCellValue).map(String::valueOf)
				.collect(Collectors.toList());
		System.out.println(headerCells);
		int colCount = headerCells.size();
		System.out.println(colCount);
		return rowStreamSupplier.get().skip(1).map(row->{
			List<String> cellValues = UploadUtil.getStream(row).map(Cell::getStringCellValue).collect(Collectors.toList());
			return UploadUtil.cellIteratorSupplier(colCount).get().collect(Collectors.toMap(headerCells::get, cellValues::get));
		}).collect(Collectors.toList());
		
	}
	
}

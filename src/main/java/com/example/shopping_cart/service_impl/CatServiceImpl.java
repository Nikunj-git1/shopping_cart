package com.example.shopping_cart.service_impl;

import com.example.shopping_cart.entity.CatEntity;
import com.example.shopping_cart.repository.CatRepository;
import com.example.shopping_cart.request_dto.CatDTO;
import com.example.shopping_cart.request_dto.CatDTOResponse;
import com.example.shopping_cart.request_dto.CatDTOUpdate;
import com.example.shopping_cart.request_dto.ExcelErrorDTOResponse;
import com.example.shopping_cart.service.CatService;
import com.example.shopping_cart.util.CustomizeDateFormat;
import com.example.shopping_cart.util.ExcelImportHelper;
import com.example.shopping_cart.util.PdfGeneratorHelper;
import com.itextpdf.text.BaseColor;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CatServiceImpl implements CatService {

    @Autowired
    private CatRepository catRepository;

    @Autowired
    private UserLoginServiceImpl userLoginService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PdfGeneratorHelper pdfGeneratorHelper;

    @Autowired
    private ExcelImportHelper excelImportHelper;


    @Override
    @CacheEvict(value = "catListCache", allEntries = true)
    public CatDTOResponse create(CatDTO catDTO, User user) {
        catRepository.findByCatNameIgnoreCase(catDTO.getCatName())
                .ifPresent(existing -> {
                    throw new DataIntegrityViolationException(
                            "This category '" + catDTO.getCatName() + "' is  already exists.");
                });

        Integer createdBy = userLoginService.getAdminId(user);

        CatEntity catEntity = modelMapper.map(catDTO, CatEntity.class);
        catEntity.setCreatedBy(createdBy);
        catEntity = catRepository.save(catEntity);

        CatDTOResponse catDTOResponse = modelMapper.map(catEntity, CatDTOResponse.class);
        String formattedDate = CustomizeDateFormat.formatTimestamp(catEntity.getCreatedAt());
        catDTOResponse.setCreatedAt(formattedDate);

        return catDTOResponse;
    }

    @Override
    @Transactional
    @CacheEvict(value = "catCacheList", allEntries = true)
    public Map<String, Object> createByImportExcel(MultipartFile excelFile, User user) {

        // Step 1: Parse and Validate Excel
        List<CatDTO> catDTOList = excelImportHelper.processExcelFile(excelFile, CatDTO.class);

//        // Step 2: Extract all catNames from DTO
//        List<String> excelCatNames = catDTOList.stream()
//                .map(CatDTO::getCatName)
//                .filter(Objects::nonNull)
//                .map(String::trim)
//                .map(String::toLowerCase)
//                .toList();
//
//        // Step 3: Fetch existing names from DB
//        List<String> existingCatNames = catRepository.findAllByCatNameIn(excelCatNames).stream()
//                .map(CatEntity::getCatName)
//                .filter(Objects::nonNull)
//                .map(String::trim)
//                .map(String::toLowerCase)
//                .toList();

        // Step 4: Initialize counters
        int updateCount = 0, insertCount = 0;

        // Step 5: Fetch creator info
        Integer createdBy = userLoginService.getAdminId(user);

        // Step 6: Iterate over each row with index (for rowNumber tracking)
        for (int i = 0; i < catDTOList.size(); i++) {
            CatDTO catDTO = catDTOList.get(i);
            int rowNumber = i + 2; // Assuming header is on row 1

            String catName = catDTO.getCatName();
            if (catName == null || catName.trim().isEmpty()) {
                excelImportHelper.getValidationErrors().add(new ExcelErrorDTOResponse(
                        rowNumber, "catName", "Category name is required", "."));
                continue;
            }

            String catNameLower = catName.trim().toLowerCase();

            try {
                Optional<CatEntity> existing = catRepository.findByCatNameIgnoreCase(catNameLower);

                // Insert new entity
                if (existing.isEmpty()) {
                    CatEntity entity = modelMapper.map(catDTO, CatEntity.class);
                    entity.setCreatedBy(createdBy);
                    catRepository.save(entity);
                    insertCount++;
                } else {
                    // Update existing entity
                    CatEntity catEntityUpdt = existing.get();
                    modelMapper.map(catDTO, catEntityUpdt);
                    catEntityUpdt.setCreatedBy(createdBy);
                    catRepository.save(catEntityUpdt);
                    updateCount++;
                }
            } catch (Exception e) {
                excelImportHelper.getValidationErrors().add(new ExcelErrorDTOResponse(
                        rowNumber, "catName", "Database error: " + e.getMessage(), catName));
            }
        }

        // Step 7: Return structured response
        return excelImportHelper.buildImportResponseDataOnly(
                catDTOList,
                excelImportHelper.getValidationErrors(),
                insertCount,
                updateCount
        );
    }


    @Override
    public void exportCategoryPdf() {

        // Step 1: Fetch data from DB
        List<CatEntity> catEntityList = catRepository.findAll();

        if (catEntityList.isEmpty()) {
            throw new RuntimeException("No categories found in database.");
        }

        // Step 2: Convert Entity to DTO
        List<CatDTO> dtoList = catEntityList.stream()
                .map(entity -> modelMapper.map(entity, CatDTO.class))
                .collect(Collectors.toList());

        // Step 3: Setup styling and export to PDF
        String fileSavingPath = "D:\\Git\\shopping_cart\\pdf-generated\\category_list.pdf";
        String bgImagePath = "D:\\Git\\shopping_cart\\pdf-generated\\pdf-background-image\\bg-1.jpg";

        try {
            pdfGeneratorHelper
                    .setBackgroundImagePath(bgImagePath, 0.3f)
                    .setBackgroundColor(BaseColor.LIGHT_GRAY)
                    .setWatermarkText("WATERMARK", 0.15f, 40, 45)
                    .setWatermarkTextColor(BaseColor.LIGHT_GRAY)
                    .exportToPdfWithStyling(dtoList, fileSavingPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Cacheable(value = "catListCache",
            key = "#status == null || #status.isEmpty() || #status.equalsIgnoreCase('All') ? 'ALL' : #status",
            unless = "#result == null || #result.isEmpty()")
    public List<CatDTOResponse> getList(String status) {
        System.out.println("****************");
        boolean isAllStatus = status == null || status.isEmpty() || status.equalsIgnoreCase("All");
        List<CatEntity> catEntityList = isAllStatus ? catRepository.findAll() : catRepository.findByStatus(status);

        return catEntityList.stream()
                .map(entity -> modelMapper.map(entity, CatDTOResponse.class))
                .collect(Collectors.toList());

    }

    @Override
    @CacheEvict(value = "catListCache", allEntries = true)
    public CatDTOResponse update(CatDTOUpdate catDTOUpdate) {
        CatEntity catEntity = catRepository.findById(catDTOUpdate.getCatId())
                .orElseThrow(() -> new NoSuchElementException(
                        "This category '" + catDTOUpdate.getCatId() + "' is not found."));

        if (catRepository.existsByCatNameIgnoreCaseAndCatIdNot(catDTOUpdate.getCatName(), catDTOUpdate.getCatId())) {
            throw new DataIntegrityViolationException(
                    "This category name '" + catDTOUpdate.getCatName() + "' is already exists.");
        }

        modelMapper.map(catDTOUpdate, catEntity);
        catEntity = catRepository.save(catEntity);

        return modelMapper.map(catEntity, CatDTOResponse.class);
    }

    @Override
    @CacheEvict(value = "catListCache", allEntries = true)
    public CatDTOResponse updateStatus(int catId, String status) {
        CatEntity catEntity = catRepository.findById(catId)
                .orElseThrow(() -> new NoSuchElementException("This category id '" + catId + "' is not found."));

        catEntity.setStatus(status);
        catEntity = catRepository.save(catEntity);

        return modelMapper.map(catEntity, CatDTOResponse.class);
    }

    @Override
    @CacheEvict(value = "catListCache", allEntries = true)
    public boolean delete(int catId) {
        Optional<CatEntity> catEntity = catRepository.findById(catId);
        if (catEntity.isEmpty()) {
            throw new NoSuchElementException("This category id '" + catId + "' is not found.");
        }

        catRepository.deleteById(catId);

        return true;
    }
}
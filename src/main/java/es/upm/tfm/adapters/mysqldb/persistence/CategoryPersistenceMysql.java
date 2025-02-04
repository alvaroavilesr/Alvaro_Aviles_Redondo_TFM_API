package es.upm.tfm.adapters.mysqldb.persistence;

import es.upm.tfm.adapters.mysqldb.dto.CategoryDTO;
import es.upm.tfm.adapters.mysqldb.entity.CategoryEntity;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoriesNotFoundException;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoryAlreadyAttachedToAnItem;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoryNameAlreadyExisting;
import es.upm.tfm.adapters.mysqldb.exception.category.CategoryNotFoundException;
import es.upm.tfm.adapters.mysqldb.response.CategoryResponse;
import es.upm.tfm.adapters.mysqldb.respository.CategoryRepository;
import es.upm.tfm.adapters.mysqldb.respository.ItemRepository;
import es.upm.tfm.domain.persistence_ports.CategoryPersistence;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Repository
public class CategoryPersistenceMysql implements CategoryPersistence {

    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public CategoryPersistenceMysql(CategoryRepository categoryRepository, ItemRepository itemRepository) {
        this.categoryRepository = categoryRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public CategoryResponse saveCategory(CategoryDTO categoryDTO) throws CategoryNameAlreadyExisting {
        boolean categoryAlreadyExisting = categoryRepository.findAll().stream()
                .noneMatch(category -> Objects.equals(category.getName(), categoryDTO.getName()));
        if(!categoryAlreadyExisting){
            throw new CategoryNameAlreadyExisting();
        }
        CategoryEntity category = modelMapper.map(categoryDTO, CategoryEntity.class);
        CategoryEntity savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryResponse.class);
    }

    public List<CategoryResponse> getCategories() throws CategoriesNotFoundException {
        List<CategoryEntity> categories = categoryRepository.findAll();

        if (categories.isEmpty()){
            throw new CategoriesNotFoundException();
        }else{
            return categories.stream()
                    .map(category -> modelMapper.map(category, CategoryResponse.class))
                    .toList();
        }
    }

    public CategoryResponse findById(Long id) throws CategoryNotFoundException {
        return categoryRepository.findById(id)
                .map(category -> modelMapper.map(category, CategoryResponse.class))
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    public CategoryResponse deleteById(Long id) throws CategoryNotFoundException, CategoryAlreadyAttachedToAnItem {
        boolean categoryAttachedToAnItem = itemRepository.findAll().stream()
                .noneMatch(item -> Objects.equals(item.getCategory().getCategoryId(), id));
        if(!categoryAttachedToAnItem){
            throw new CategoryAlreadyAttachedToAnItem(id);
        }
        CategoryResponse response = categoryRepository.findById(id)
                .map(category -> modelMapper.map(category, CategoryResponse.class))
                .orElseThrow(() -> new CategoryNotFoundException(id));
        categoryRepository.deleteById(id);
        return response;
    }

    public CategoryResponse updateCategory(Long id, CategoryDTO categoryDTO) throws CategoryNotFoundException, CategoryNameAlreadyExisting {
        boolean categoryAlreadyExisting = categoryRepository.findAll().stream()
                .noneMatch(category -> Objects.equals(category.getName(), categoryDTO.getName()));
        if(!categoryAlreadyExisting){
            throw new CategoryNameAlreadyExisting();
        }
        CategoryEntity category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
        category.setName(categoryDTO.getName());
        return modelMapper.map(categoryRepository.save(category), CategoryResponse.class);
    }
}

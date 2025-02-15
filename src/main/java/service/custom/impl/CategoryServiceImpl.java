package service.custom.impl;

import dto.Category;
import entity.CategoryEntity;
import org.modelmapper.ModelMapper;
import repository.DaoFactory;
import repository.custom.CategoryDao;
import service.custom.CategorySevice;
import util.DaoType;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryServiceImpl implements CategorySevice {

    private final CategoryDao categoryDao = DaoFactory.getInstance().getDaoType(DaoType.CATEGORY);
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<String> getAllCategories() {
        List<CategoryEntity> categoryEntities = categoryDao.getAll();
        return categoryEntities.stream()
                .map(CategoryEntity::getCategoryName) // Get only category names as a list of Strings
                .collect(Collectors.toList());
    }

//    @Override
//    public boolean addCategory(Category category) {
//        try {
//            CategoryEntity categoryEntity = modelMapper.map(category, CategoryEntity.class);
//            return categoryDao.save(categoryEntity);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    @Override
//    public boolean updateCategory(Category category) {
//        try {
//            CategoryEntity categoryEntity = modelMapper.map(category, CategoryEntity.class);
//            return categoryDao.update(categoryEntity);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    @Override
//    public boolean deleteCategory(String categoryName) {
//        return categoryDao.delete(categoryName);
//    }
}
